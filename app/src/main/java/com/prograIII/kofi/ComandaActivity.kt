package com.prograIII.kofi

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.prograIII.kofi.adapters.ProductoCategoriaComandaAdapter
import com.prograIII.kofi.databinding.ActivityComandaBinding

class ComandaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityComandaBinding
    val context: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityComandaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // ------ Prueba ------
//        val productos = listOf(
//            Producto(
//                id = 1,
//                nombre = "Capuccino",
//                descripcion = "Espresso con leche vaporizada y espuma suave.",
//                precio = 20.0,
//                imagenResId = R.drawable.ic_launcher_foreground
//            ),
//            Producto(
//                id = 2,
//                nombre = "Latte Vainilla",
//                descripcion = "Café suave con un toque de vainilla.",
//                precio = 22.0,
//                imagenResId = R.drawable.ic_launcher_foreground
//            ),
//            Producto(
//                id = 3,
//                nombre = "Mocca",
//                descripcion = "Café con chocolate y leche cremosa.",
//                precio = 25.0,
//                imagenResId = R.drawable.ic_launcher_foreground
//            ),
//            Producto(
//                id = 4,
//                nombre = "Frappe",
//                descripcion = "Café frío batido con hielo.",
//                precio = 24.0,
//                imagenResId = R.drawable.ic_launcher_foreground
//            )
//        )

        // ------ RecyclerView de productos ------
        binding.recyclerProductos.layoutManager = GridLayoutManager(context, 2)
//        binding.recyclerProductos.adapter = ProductoCategoriaComandaAdapter(productos)

        // ------ Botón de regreso ------
        binding.arrow.setOnClickListener {
            val intentCambioAPrincipal = Intent(context, PrincipalActivity::class.java)
            startActivity(intentCambioAPrincipal)
        }


        binding.finalizarOrden.setOnClickListener {
            val intentCambioAFinalizarOrden = Intent(context, FinalizarOrdenActivity::class.java)
            startActivity(intentCambioAFinalizarOrden)
        }
    }
}
