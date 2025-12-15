package com.prograIII.kofi

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.prograIII.kofi.adapters.ProductoCategoriaMenuAdapter
import com.prograIII.kofi.data.AppDatabase
import com.prograIII.kofi.databinding.ActivityCategoriaBinding
import com.prograIII.kofi.dataclasses.Producto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.prograIII.kofi.LoginActivity.Companion.nombreDB

class CategoriaMenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoriaBinding
    private lateinit var db: AppDatabase
    private var codigoCategoria: String? = null
    val context: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Binding
        binding = ActivityCategoriaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Room
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            nombreDB
        ).build()

        // UI
        binding.rvArticulosCategoria.layoutManager = LinearLayoutManager(context)

        binding.arrow.setOnClickListener {
            val intentCambioAMenu = Intent(context, MenuActivity::class.java)
            startActivity(intentCambioAMenu)
        }

        // Obtener categoría actual
        codigoCategoria = intent.getStringExtra("codigoCategoria")

        // Botón +
        binding.nuevaReceta.setOnClickListener {
            val intentGuardarProducto =
                Intent(context, GuardarProductoActivity::class.java)

            intentGuardarProducto.putExtra("codigoCategoria", codigoCategoria)
            startActivity(intentGuardarProducto)
        }

        // Cargar productos de la categoría
        if (codigoCategoria != null) {
            cargarProductosPorCodigo(codigoCategoria!!)
        }

        // Insets
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun cargarProductosPorCodigo(codigo: String) {
        GlobalScope.launch(Dispatchers.IO) {

            val categoria = db.categoriaDao().obtenerCategoriaPorCodigo(codigo)

            if (categoria != null) {

                val productosDb = db.productoDao().obtenerPorCategoria(categoria.id)

                val productosUi = productosDb.map { p ->
                    val resId = resources.getIdentifier(p.imagen, "drawable", packageName)
                    val imagenFinal =
                        if (resId != 0) resId else R.drawable.food_1_svgrepo_com

                    Producto(
                        id = p.id,
                        nombre = p.nombre,
                        descripcion = p.descripcion,
                        precio = p.precio,
                        imagenRes = imagenFinal,
                        categoriaId = p.categoriaId
                    )
                }

                runOnUiThread {
                    binding.TituloCategoria.text = categoria.nombre
                    binding.rvArticulosCategoria.adapter =
                        ProductoCategoriaMenuAdapter(productosUi) { producto ->

                            val intent = Intent(context, ProductoIndividualActivity::class.java)
                            intent.putExtra(ProductoIndividualActivity.EXTRA_PRODUCTO_ID, producto.id)
                            startActivity(intent)
                        }


                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if (codigoCategoria != null) {
            cargarProductosPorCodigo(codigoCategoria!!)
        }
    }

}
