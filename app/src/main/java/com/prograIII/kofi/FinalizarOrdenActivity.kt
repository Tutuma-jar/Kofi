package com.prograIII.kofi

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.prograIII.kofi.databinding.ActivityFinalizarOrdenBinding

class FinalizarOrdenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFinalizarOrdenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //Inflar binding
        binding = ActivityFinalizarOrdenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Activar scroll del EditText
        binding.etComentario.movementMethod = ScrollingMovementMethod()
        //Configurar RecyclerView
        binding.rvArticulosPedido.layoutManager = LinearLayoutManager(this)

        //Datos de prueba
        val articulos = listOf(
            Articulo(
                1, "Capuccino",
                "Espresso combinado con leche vaporizada y una capa suave de espuma. Equilibrado, cremoso y aromático.",
                20.0, R.drawable.ic_launcher_foreground
            ),
            Articulo(
                2, "Latte",
                "Espresso con abundante leche caliente y una fina capa de espuma. Suave, ligero y perfecto para quienes prefieren sabores delicados.",
                18.0, R.drawable.ic_launcher_foreground
            ),
            Articulo(
                3, "Mocca",
                "Mezcla de espresso, leche vaporizada y chocolate. Dulce, intenso y con un toque de cacao que realza el aroma.",
                22.0, R.drawable.ic_launcher_foreground
            ),
            Articulo(
                4, "Americano",
                "Espresso diluido en agua caliente. Sabor limpio, ligero y con la intensidad justa del grano.",
                15.0, R.drawable.ic_launcher_foreground
            ),
            Articulo(
                5, "Frappe",
                "Café frío mezclado con hielo y leche hasta lograr una textura cremosa. Refrescante y ligeramente dulce.",
                25.0, R.drawable.ic_launcher_foreground
            )
        )

        //Conectar adapter
        binding.rvArticulosPedido.adapter = ArticuloAdapter(articulos)

        //Window Insets
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
