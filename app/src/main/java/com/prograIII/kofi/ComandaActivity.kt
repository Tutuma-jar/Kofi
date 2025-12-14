package com.prograIII.kofi

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.prograIII.kofi.adapters.ProductoCategoriaComandaAdapter
import com.prograIII.kofi.adapters.ProductoCategoriaMenuAdapter
import com.prograIII.kofi.data.AppDatabase
import com.prograIII.kofi.databinding.ActivityComandaBinding
import com.prograIII.kofi.dataclasses.Producto
import com.prograIII.kofi.dataclasses.Pedido
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ComandaActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var binding: ActivityComandaBinding
    val context: Context = this
    // 1. NUEVO: Lista para almacenar los pedidos seleccionados
    private val listaPedidos = mutableListOf<Pedido>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityComandaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "kofi-db"
        ).build()
        binding.recyclerProductos.layoutManager = GridLayoutManager(context,2)


        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        //nuevo código para cargar productos según circulito
        cargarProductosPorCodigo("CAFES")
        binding.categoria1.setOnClickListener { cargarProductosPorCodigo("CAFES") }
        binding.categoria2.setOnClickListener { cargarProductosPorCodigo("INFUSIONES") }
        binding.categoria3.setOnClickListener { cargarProductosPorCodigo("BREAKFAST") }
        binding.categoria4.setOnClickListener { cargarProductosPorCodigo("PANADERIA") }
        binding.categoria5.setOnClickListener { cargarProductosPorCodigo("PASTELES") }
        binding.categoria6.setOnClickListener { cargarProductosPorCodigo("SANDWICHES") }
        binding.categoria7.setOnClickListener { cargarProductosPorCodigo("HELADOS") }
        binding.categoria8.setOnClickListener { cargarProductosPorCodigo("BEBIDAS") }


        // ------ Botón de regreso ------
        binding.arrow.setOnClickListener {
            val intentCambioAPrincipal = Intent(context, PrincipalActivity::class.java)
            startActivity(intentCambioAPrincipal)
        }


        binding.finalizarOrden.setOnClickListener {
            //pasar lista
            val intentCambioAFinalizarOrden = Intent(context, FinalizarOrdenActivity::class.java)
            startActivity(intentCambioAFinalizarOrden)
        }
    }

    private fun cargarProductosPorCodigo(codigo: String) {
        GlobalScope.launch(Dispatchers.IO) {

            val categoria = db.categoriaDao().obtenerCategoriaPorCodigo(codigo)

            if (categoria != null) {

                val productosDb = db.productoDao().obtenerPorCategoria(categoria.id) //Traer productos con id

                val productosUi = productosDb.map { p -> //De clase a UI de producto
                    Producto(
                        id = p.id,
                        nombre = p.nombre,
                        descripcion = p.descripcion,
                        precio = p.precio,
                        imagenRes = R.drawable.food_1_svgrepo_com,
                        categoriaId = p.categoriaId
                    )
                }

                runOnUiThread {
                    binding.recyclerProductos.adapter = ProductoCategoriaComandaAdapter(productosUi){ productoSeleccionado ->

                        // LÓGICA PARA AGREGAR SIN REPETIR
                        val nombre = productoSeleccionado.nombre

                        // 'any' revisa si existe algún elemento que cumpla la condición
                        val existe = listaPedidos.any { it.nombre == nombre }

                        if (!existe) {
                            // Si no existe, lo agregamos
                            listaPedidos.add(Pedido(nombre, productoSeleccionado.precio))
                            Toast.makeText(context, "Agregado: $nombre", Toast.LENGTH_SHORT).show()
                            println("Lista actual: $listaPedidos") // Para ver en Logcat
                        } else {
                            // Si ya existe, avisamos
                            Toast.makeText(context, "El producto ya está en la orden", Toast.LENGTH_SHORT).show()
                        }
                    }

                }
            }
        }
    }
}
