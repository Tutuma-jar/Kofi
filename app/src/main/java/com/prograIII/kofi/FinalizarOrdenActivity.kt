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
            val intentCambioAComanda = Intent(context, ComandaActivity::class.java)
            startActivity(intentCambioAComanda)
        }

        //Confirmar pedido
        binding.btnConfirmarPedido.setOnClickListener {
            val nombreClienteIngresado = binding.etNombreCliente.text.toString()

            if (nombreClienteIngresado.isEmpty()) {
                binding.etNombreCliente.error = "Ingresa un nombre"
                return@setOnClickListener
            }

            // Operación en base de datos (Background)
            GlobalScope.launch(Dispatchers.IO) {

                val ordenActual = db.ordenDao().obtenerOrdenPorId(ordenIdActual)

                val ordenActualizada = ordenActual.copy(cliente = nombreClienteIngresado)

                db.ordenDao().actualizarOrden(ordenActualizada)

                runOnUiThread {
                    val intent = Intent(context, PedidosActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
    //cargar db
    fun cargarProductosDeLaOrden(id: Int) {
        GlobalScope.launch(Dispatchers.IO) {

            val listaDetalles = db.ordenDao().obtenerDetallesDeOrden(id)

            val total = listaDetalles.sumOf { it.precio * it.cantidad }

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
                // Llenamos el adapter con los datos reales de la BD
                binding.rvArticulosPedido.adapter = ProductoFinalizarOrdenAdapter(detallesUi,
                    onSumar = { item -> modificarCantidad(item, 1) },
                    onRestar = { item -> modificarCantidad(item, -1) },
                    onEliminar = { item -> eliminarItem(item) }
                )
                binding.tvTotal.text = " $total Bs."
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

        if (nombreCliente.isEmpty()) {
            binding.etNombreCliente.error = "Ingresa nombre del cliente"
            return
        }

        GlobalScope.launch(Dispatchers.IO) {
            // Traemos la orden cabecera
            val orden = db.ordenDao().obtenerOrdenPorId(ordenIdActual)

            // Recalculamos totales finales por seguridad
            val detalles = db.ordenDao().obtenerDetallesDeOrden(ordenIdActual)
            val totalMontoFinal = detalles.sumOf { it.precio * it.cantidad }
            val totalItemsFinal = detalles.sumOf { it.cantidad }

            // Creamos copia actualizada
            val ordenFinal = orden.copy(
                cliente = nombreCliente,
                totalMonto = totalMontoFinal,
                totalItems = totalItemsFinal,
                listo = true // Marcamos como lista/confirmada si gustas
            )

            // Guardamos
            db.ordenDao().actualizarOrden(ordenFinal)

            runOnUiThread {
                Toast.makeText(context, "Pedido Confirmado", Toast.LENGTH_SHORT).show()
                val intent = Intent(context, PedidosActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}
