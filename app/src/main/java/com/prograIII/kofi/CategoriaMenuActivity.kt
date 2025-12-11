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
        enableEdgeToEdge()


        setContentView(R.layout.activity_categoria_menu)
        // RecyclerView que está en activity_categoria_menu.xml
        val recycler = findViewById<RecyclerView>(R.id.rvArticulosCategoria)
        recycler.layoutManager = LinearLayoutManager(this)

        val listaArticulos = listOf(
            Articulo(
                1,
                "Sandwich Club",
                "Sándwich de tres capas preparado con pan tostado ligeramente crujiente, pollo a la plancha, tiras de tocino dorado, lechuga fresca, tomate jugoso y una fina capa de mayonesa casera. Su combinación de texturas y sabores lo convierte en una opción abundante y satisfactoria, ideal para quienes buscan algo completo y delicioso.",
                40.0,
                R.drawable.food_1_svgrepo_com
            ),
            Articulo(
                2,
                "Sandwich Pollo",
                "Sándwich elaborado con pechuga de pollo a la plancha sazonada con hierbas, acompañada de hojas frescas de lechuga, rodajas de tomate y un toque ligero de salsa especial de la casa. Servido en pan suave ligeramente tostado. Una opción ligera, saludable y con muy buen sabor.",
                32.0,
                R.drawable.food_1_svgrepo_com
            ),
            Articulo(
                3,
                "Veggie Sandwich",
                "Sándwich vegetariano con una mezcla equilibrada de vegetales frescos: pepino, tomate, lechuga y pimientos, combinados con una capa de hummus cremoso y queso suave. Todo servido en pan integral recién tostado. Es una alternativa ligera, nutritiva y llena de sabor para quienes prefieren opciones sin carne.",
                28.0,
                R.drawable.food_1_svgrepo_com
            )
        )
        recycler.adapter = ArticulosCategoriaAdapter(listaArticulos)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
