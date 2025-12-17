package com.prograIII.kofi

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.prograIII.kofi.adapters.ProductoFinalizarOrdenAdapter
import com.prograIII.kofi.data.AppDatabase
import com.prograIII.kofi.databinding.ActivityFinalizarOrdenBinding
import com.prograIII.kofi.LoginActivity.Companion.nombreDB
import com.prograIII.kofi.dataclasses.ItemPedido
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class FinalizarOrdenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFinalizarOrdenBinding
    private lateinit var db: AppDatabase
    val context: Context = this
    private var ordenIdActual: Int = 0

    var totalDescuento: Double = 1.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFinalizarOrdenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            nombreDB
        ).build()

        //scroll
        binding.etComentario.movementMethod = ScrollingMovementMethod()

        // RecyclerView
        binding.rvArticulosPedido.layoutManager = LinearLayoutManager(this)

        // Insets
        val root = binding.root

        val pL = root.paddingLeft
        val pT = root.paddingTop
        val pR = root.paddingRight
        val pB = root.paddingBottom

        ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                pL + bars.left,
                pT + bars.top,
                pR + bars.right,
                pB + bars.bottom
            )
            insets
        }

        //Obtenemos el ID
        ordenIdActual = intent.getIntExtra("ID_ORDEN", 0)

        cargarProductosDeLaOrden(ordenIdActual)

        //Volver a Comanda
        binding.arrow.setOnClickListener {
            finish()
        }

        //Confirmar pedido
        binding.btnConfirmarPedido.setOnClickListener {
            confirmarPedidoFinal()
        }
        binding.rgMetodoPago.setOnCheckedChangeListener { _, _ ->

            val code = binding.tvPromocion.text.toString()

            val montoOriginal = totalDescuento

            if (code == "PROMOCION" || code == "OREO") {
                val totalConDescuento = montoOriginal * 0.90
                binding.tvTotal.text = " %.2f Bs.".format(totalConDescuento)

                Toast.makeText(context, "¡Descuento aplicado!", Toast.LENGTH_SHORT).show()
            } else {
                binding.tvTotal.text = " $montoOriginal Bs."
            }
        }
    }
    //cargar db
    fun cargarProductosDeLaOrden(id: Int) {
        GlobalScope.launch(Dispatchers.IO) {

            val ordenHeader = db.ordenDao().obtenerOrdenPorId(id)

            val listaDetalles = db.ordenDao().obtenerDetallesDeOrden(id)

            val total = listaDetalles.sumOf { it.precio * it.cantidad }

            totalDescuento = total


            val detallesUi = listaDetalles.map { p ->
                ItemPedido(
                    productoId = p.id,
                    nombre = p.nombreProducto,
                    precio = p.precio,
                    imagen = p.imagenProducto,
                    cantidad = p.cantidad,
                )
            }
            runOnUiThread {
                binding.etNombreCliente.setText(ordenHeader.cliente)
                binding.etComentario.setText(ordenHeader.comentario)
                binding.etNitCliente.setText(ordenHeader.nit.toString())
                binding.tvTotal.text = " $total Bs."


                // Llenamos el adapter con los datos reales de la BD
                binding.rvArticulosPedido.adapter = ProductoFinalizarOrdenAdapter(detallesUi,
                    onSumar = { item -> modificarCantidad(item, 1) },
                    onRestar = { item -> modificarCantidad(item, -1) },
                    onEliminar = { item -> eliminarItem(item) }
                )
            }
        }
    }
    private fun modificarCantidad(item: ItemPedido, cambio: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            // Buscamos el detalle específico por su ID único
            val detalle = db.ordenDao().obtenerDetallePorId(item.productoId)

            if (detalle != null) {
                val nuevaCantidad = detalle.cantidad + cambio

                if (nuevaCantidad > 0) {
                    // Actualizamos en BD
                    detalle.cantidad = nuevaCantidad
                    db.ordenDao().actualizarProducto(detalle)

                    // Recargamos la lista para ver cambios y nuevo precio total
                    cargarProductosDeLaOrden(ordenIdActual)
                } else {
                    // Si la cantidad llega a 0, borramos el producto
                    eliminarItem(item)
                }
            }
        }
    }

    // --- LÓGICA DE ELIMINAR (Basurero) ---
    private fun eliminarItem(item: ItemPedido) {
        GlobalScope.launch(Dispatchers.IO) {
            val detalle = db.ordenDao().obtenerDetallePorId(item.productoId)

            if (detalle != null) {
                db.ordenDao().eliminarProducto(detalle)

                // Recargamos la UI
                cargarProductosDeLaOrden(ordenIdActual)

                runOnUiThread {
                    Toast.makeText(context, "${item.nombre} eliminado", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // --- CONFIRMAR PEDIDO (Guardar nombre cliente) ---
    private fun confirmarPedidoFinal() {
        val nombreCliente = binding.etNombreCliente.text.toString()
        val comentario = binding.etComentario.text.toString()
        val nit = binding.etNitCliente.text.toString().toInt()

        if (nombreCliente.isEmpty()) {
            binding.etNombreCliente.error = "Ingresa nombre del cliente"
            return
        }

        GlobalScope.launch(Dispatchers.IO) {
            // 1. Traer la orden vieja
            val ordenVieja = db.ordenDao().obtenerOrdenPorId(ordenIdActual)
            val detalles = db.ordenDao().obtenerDetallesDeOrden(ordenIdActual)

            val totalMontoFinal = detalles.sumOf { it.precio * it.cantidad }
            val totalItemsFinal = detalles.sumOf { it.cantidad }

            // 2. Crear copia con TODOS los datos nuevos
            val ordenActualizada = ordenVieja.copy(
                cliente = nombreCliente,
                nit = nit,
                comentario = comentario,
                totalMonto = totalMontoFinal,
                totalItems = totalItemsFinal,
                listo = false
            )

            // 3. Guardar en BD
            db.ordenDao().actualizarOrden(ordenActualizada)

            runOnUiThread {
                Toast.makeText(context, "Pedido Guardado", Toast.LENGTH_SHORT).show()
                val intent = Intent(context, PedidosActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}
