package com.prograIII.kofi

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CategoriaMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.articulo_categoria)
        val recycler = findViewById<RecyclerView>(R.id.rvArticulosCategoria)
        recycler.layoutManager = LinearLayoutManager(this)
        val listaArticulos = listOf(
            Articulo(1, "Sandwich Club", "Sándwich de tres capas...", 40.0, R.drawable.food_1_svgrepo_com),
            Articulo(2, "Sandwich Pollo", "Pollo a la plancha...", 32.0, R.drawable.food_1_svgrepo_com),
            Articulo(3, "Veggie Sandwich", "Opciones frescas...", 28.0, R.drawable.food_1_svgrepo_com)
        )
        recycler.adapter = ArticulosCategoriaAdapter(listaArticulos)


        enableEdgeToEdge()
        setContentView(R.layout.activity_categoria_menu)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}