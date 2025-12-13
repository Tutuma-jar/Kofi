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
//        val productos = listOf(
//            Producto(
//                1,
//                "Capuccino",
//                "Espresso combinado con leche vaporizada y una capa suave de espuma. Equilibrado, cremoso y aromático.",
//                20.0,
//                R.drawable.ic_launcher_foreground
//            ),
//            Producto(
//                2,
//                "Latte",
//                "Espresso con abundante leche caliente y una fina capa de espuma. Suave, ligero y perfecto para quienes prefieren sabores delicados.",
//                18.0,
//                R.drawable.ic_launcher_foreground
//            ),
//            Producto(
//                3,
//                "Mocca",
//                "Mezcla de espresso, leche vaporizada y chocolate. Dulce, intenso y con un toque de cacao que realza el aroma.",
//                22.0,
//                R.drawable.ic_launcher_foreground
//            ),
//            Producto(
//                4,
//                "Americano",
//                "Espresso diluido en agua caliente. Sabor limpio, ligero y con la intensidad justa del grano.",
//                15.0,
//                R.drawable.ic_launcher_foreground
//            ),
//            Producto(
//                5,
//                "Frappe",
//                "Café frío mezclado con hielo y leche hasta lograr una textura cremosa. Refrescante y ligeramente dulce.",
//                25.0,
//                R.drawable.ic_launcher_foreground
//            )
//        )

        // Conectar adapter
//        binding.rvArticulosPedido.adapter = ProductoFinalizarOrdenAdapter(productos)

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

