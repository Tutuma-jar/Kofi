package com.prograIII.kofi

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FinalizarOrdenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        setContentView(R.layout.activity_finalizar_orden)
        //Obtener referencia al EditText
        val etComentario = findViewById<EditText>(R.id.etComentario)
        //Activar scroll interno
        etComentario.movementMethod = ScrollingMovementMethod()


        setContentView(R.layout.activity_finalizar_orden)
        //Configurar RecyclerView
        val recycler = findViewById<RecyclerView>(R.id.rvArticulosPedido)
        recycler.layoutManager = LinearLayoutManager(this)
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
        recycler.adapter = ArticuloAdapter(articulos)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
