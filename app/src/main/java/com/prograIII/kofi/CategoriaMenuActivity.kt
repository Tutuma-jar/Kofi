package com.prograIII.kofi

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.prograIII.kofi.adapters.ProductoCategoriaMenuAdapter
import com.prograIII.kofi.databinding.ActivityCategoriaBinding

class CategoriaMenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoriaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityCategoriaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar Recycler
        binding.rvArticulosCategoria.layoutManager = LinearLayoutManager(this)

        val productos = listOf(
            Producto(
                1,
                "Sandwich Club",
                "Sándwich de tres capas preparado con pan tostado, pollo, tocino, lechuga, tomate y mayonesa casera.",
                40.0,
                R.drawable.food_1_svgrepo_com
            ),
            Producto(
                2,
                "Sandwich Pollo",
                "Pechuga de pollo a la plancha con tomate fresco, lechuga y salsa especial.",
                32.0,
                R.drawable.food_1_svgrepo_com
            ),
            Producto(
                3,
                "Veggie Sandwich",
                "Hummus, vegetales frescos y pan integral tostado. Ligero y nutritivo.",
                28.0,
                R.drawable.food_1_svgrepo_com
            )
        )

        binding.rvArticulosCategoria.adapter = ProductoCategoriaMenuAdapter(productos)

        // Insets
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
