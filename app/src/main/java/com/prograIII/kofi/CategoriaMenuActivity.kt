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

        // Recycler
        binding.rvArticulosCategoria.layoutManager = LinearLayoutManager(context)

        // Volver
        binding.arrow.setOnClickListener {
            startActivity(Intent(context, MenuActivity::class.java))
        }

        // Categoría actual
        codigoCategoria = intent.getStringExtra("codigoCategoria")

        // Botón +
        binding.nuevaReceta.setOnClickListener {
            val intent = Intent(context, GuardarProductoActivity::class.java)
            intent.putExtra("codigoCategoria", codigoCategoria)
            startActivity(intent)
        }

        // Cargar productos
        codigoCategoria?.let {
            cargarProductosPorCodigo(it)
        }

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


    }

    private fun cargarProductosPorCodigo(codigo: String) {
        GlobalScope.launch(Dispatchers.IO) {

            val categoria = db.categoriaDao().obtenerCategoriaPorCodigo(codigo)
            if (categoria == null) return@launch

            val productosDb = db.productoDao().obtenerPorCategoria(categoria.id)

            val productosUi = productosDb.map { p ->
                Producto(
                    id = p.id,
                    nombre = p.nombre,
                    descripcion = p.descripcion,
                    precio = p.precio,
                    categoriaId = p.categoriaId,
                    imagen = p.imagen       //Imagen URI
                )
            }

            runOnUiThread {
                binding.TituloCategoria.text = categoria.nombre

                binding.rvArticulosCategoria.adapter =
                    ProductoCategoriaMenuAdapter(productosUi) { producto ->
                        val intent = Intent(context, ProductoIndividualActivity::class.java)
                        intent.putExtra(
                            ProductoIndividualActivity.EXTRA_PRODUCTO_ID,
                            producto.id
                        )
                        startActivity(intent)
                    }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        codigoCategoria?.let {
            cargarProductosPorCodigo(it)
        }
    }
}
