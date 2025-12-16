package com.prograIII.kofi

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.room.Room
import com.prograIII.kofi.adapters.ProductoCategoriaComandaAdapter
import com.prograIII.kofi.data.AppDatabase
import com.prograIII.kofi.databinding.ActivityComandaBinding
import com.prograIII.kofi.dataclasses.Producto
import com.prograIII.kofi.dataclasses.Pedido
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.prograIII.kofi.LoginActivity.Companion.nombreDB

class ComandaActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var binding: ActivityComandaBinding
    val context: Context = this

    // Lista de pedidos seleccionados
    private val listaPedidos = mutableListOf<Pedido>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityComandaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            nombreDB
        ).build()

        binding.recyclerProductos.layoutManager = GridLayoutManager(context, 2)

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



        // Categoria inicial
        cargarProductosPorCodigo("CAFES")

        binding.categoria1.setOnClickListener { cargarProductosPorCodigo("CAFES") }
        binding.categoria2.setOnClickListener { cargarProductosPorCodigo("INFUSIONES") }
        binding.categoria3.setOnClickListener { cargarProductosPorCodigo("BREAKFAST") }
        binding.categoria4.setOnClickListener { cargarProductosPorCodigo("PANADERIA") }
        binding.categoria5.setOnClickListener { cargarProductosPorCodigo("PASTELES") }
        binding.categoria6.setOnClickListener { cargarProductosPorCodigo("SANDWICHES") }
        binding.categoria7.setOnClickListener { cargarProductosPorCodigo("HELADOS") }
        binding.categoria8.setOnClickListener { cargarProductosPorCodigo("BEBIDAS") }

        // Volver
        binding.arrow.setOnClickListener {
            startActivity(Intent(context, PrincipalActivity::class.java))
        }

        // Finalizar orden
        binding.finalizarOrden.setOnClickListener {
            val intentCambioAFinalizarOrden =
                Intent(context, FinalizarOrdenActivity::class.java)

            intentCambioAFinalizarOrden.putExtra(
                "LISTA_PEDIDOS",
                ArrayList(listaPedidos)
            )

            startActivity(intentCambioAFinalizarOrden)
        }
    }

    private fun cargarProductosPorCodigo(codigo: String) {
        GlobalScope.launch(Dispatchers.IO) {

            val categoria = db.categoriaDao().obtenerCategoriaPorCodigo(codigo)
                ?: return@launch

            val productosDb = db.productoDao().obtenerPorCategoria(categoria.id)

            val productosUi = productosDb.map { p ->
                Producto(
                    id = p.id,
                    nombre = p.nombre,
                    descripcion = p.descripcion,
                    precio = p.precio,
                    categoriaId = p.categoriaId,
                    imagen = p.imagen
                )
            }

            runOnUiThread {
                binding.recyclerProductos.adapter =
                    ProductoCategoriaComandaAdapter(productosUi) { productoSeleccionado ->

                        val nombre = productoSeleccionado.nombre
                        val existe = listaPedidos.any { it.nombre == nombre }

                        if (!existe) {
                            listaPedidos.add(
                                Pedido(nombre, productoSeleccionado.precio)
                            )
                            Toast.makeText(
                                context,
                                "Agregado: $nombre",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                context,
                                "El producto ya está en la orden",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
    }
}
