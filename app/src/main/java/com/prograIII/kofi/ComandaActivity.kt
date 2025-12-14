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
import com.prograIII.kofi.dataclasses.Producto

class ComandaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityComandaBinding
    val context: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityComandaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // ------ Prueba ------
        val productos = listOf(
            Producto(
                id = 1,
                nombre = "Capuccino",
                descripcion = "Espresso con leche vaporizada y espuma suave.",
                precio = 20.0,
                imagenRes = R.drawable.ic_launcher_foreground,
                categoriaId = 2
            ),
            Producto(
                id = 2,
                nombre = "Latte Vainilla",
                descripcion = "Café suave con un toque de vainilla.",
                precio = 22.0,
                imagenRes = R.drawable.ic_launcher_foreground,
                categoriaId = 2
            )
        )

        // ------ RecyclerView de productos ------
        binding.recyclerProductos.layoutManager = GridLayoutManager(context, 2)
        binding.recyclerProductos.adapter = ProductoCategoriaComandaAdapter(productos)

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
