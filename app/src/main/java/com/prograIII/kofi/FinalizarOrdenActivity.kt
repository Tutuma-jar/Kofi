package com.prograIII.kofi

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.prograIII.kofi.adapters.ProductoFinalizarOrdenAdapter
import com.prograIII.kofi.data.AppDatabase
import com.prograIII.kofi.databinding.ActivityFinalizarOrdenBinding
import com.prograIII.kofi.LoginActivity.Companion.nombreDB
import com.prograIII.kofi.dataclasses.Pedido
import com.prograIII.kofi.dataclasses.Producto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class FinalizarOrdenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFinalizarOrdenBinding
    private lateinit var db: AppDatabase
    val context: Context = this

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
        val idOrden = intent.getIntExtra("ID_ORDEN", 0)

        cargarProductosDeLaOrden(idOrden)

        //Volver a Comanda
        binding.arrow.setOnClickListener {
            val intentCambioAComanda = Intent(context, ComandaActivity::class.java)
            startActivity(intentCambioAComanda)
        }

        //Confirmar pedido
        binding.btnConfirmarPedido.setOnClickListener {
            val intentCambioAPedidos = Intent(context, PedidosActivity::class.java)
            startActivity(intentCambioAPedidos)
        }
        binding.btnConfirmarPedido.setOnClickListener {
            val nombreClienteIngresado = binding.etNombreCliente.text.toString()

            if (nombreClienteIngresado.isEmpty()) {
                binding.etNombreCliente.error = "Ingresa un nombre"
                return@setOnClickListener
            }

            // Operación en base de datos (Background)
            GlobalScope.launch(Dispatchers.IO) {

                val ordenActual = db.ordenDao().obtenerOrdenPorId(idOrden)

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
            // Consultamos al DAO
            val listaDetalles = db.ordenDao().obtenerDetallesDeOrden(id)
            val total = listaDetalles.sumOf { it.precio * it.cantidad }
            val detallesUi = listaDetalles.map { p ->
                Pedido(
                    ordenId = p.id,
                    imagenProducto = p.imagenProducto,
                    nombreProducto = p.nombreProducto,
                    precio = p.precio,
                    cantidad = p.cantidad,
                )
            }
            runOnUiThread {
                // Llenamos el adapter con los datos reales de la BD
                binding.rvArticulosPedido.adapter = ProductoFinalizarOrdenAdapter(listaDetalles)
                binding.tvTotal.text = " $total Bs."
            }
        }
    }
}
