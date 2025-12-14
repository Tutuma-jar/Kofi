package com.prograIII.kofi

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.prograIII.kofi.adapters.ProductoFinalizarOrdenAdapter
import com.prograIII.kofi.databinding.ActivityFinalizarOrdenBinding
import com.prograIII.kofi.dataclasses.Producto

class FinalizarOrdenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFinalizarOrdenBinding
    val context: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFinalizarOrdenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etComentario.movementMethod = ScrollingMovementMethod()
        binding.rvArticulosPedido.layoutManager = LinearLayoutManager(this)

        // Prueba
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

        // Conectar adapter
        binding.rvArticulosPedido.adapter = ProductoFinalizarOrdenAdapter(productos)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.arrow.setOnClickListener {
            val intentCambioAComanda = Intent(context, ComandaActivity::class.java)
            startActivity(intentCambioAComanda)
        }

        binding.btnConfirmarPedido.setOnClickListener {
            val intentCambioAPedidos = Intent(context, PedidosActivity::class.java)
            startActivity(intentCambioAPedidos)
        }
    }
}

