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
            Articulo("Capuccino", 20.0, R.drawable.ic_launcher_foreground),
            Articulo("Latte", 18.0, R.drawable.ic_launcher_foreground),
            Articulo("Mocca", 22.0, R.drawable.ic_launcher_foreground),
            Articulo("Americano", 15.0, R.drawable.ic_launcher_foreground),
            Articulo("Frappe", 25.0, R.drawable.ic_launcher_foreground)
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
