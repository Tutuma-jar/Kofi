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

class CategoriaMenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoriaBinding
    private lateinit var db: AppDatabase
    val context: Context = this


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //Binding
        binding = ActivityCategoriaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Room
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "kofi-db"
        ).build()

        //UI
        binding.rvArticulosCategoria.layoutManager = LinearLayoutManager(context) //Recycler

        binding.arrow.setOnClickListener {
            val intentCambioAPrincipal = Intent(context, PrincipalActivity::class.java)
            startActivity(intentCambioAPrincipal)
        }

        binding.nuevaReceta.setOnClickListener {

        }

        //Datos
        val codigoCategoria = intent.getStringExtra("codigoCategoria")

        if (codigoCategoria != null) {
            cargarProductosPorCodigo(codigoCategoria)
        }

        //Insets
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun cargarProductosPorCodigo(codigo: String) {
        GlobalScope.launch(Dispatchers.IO) {


            val categoria = db.categoriaDao().obtenerCategoriaPorCodigo(codigo)
            println("CODIGO=$codigo  CATEGORIA=${categoria?.id} ${categoria?.nombre}")

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
                    binding.rvArticulosCategoria.adapter = ProductoCategoriaMenuAdapter(productosUi)
                }
            }
        }
    }
}
