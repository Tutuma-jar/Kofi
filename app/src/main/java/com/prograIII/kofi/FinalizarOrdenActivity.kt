package com.prograIII.kofi

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
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

        // Comentarios con scroll
        binding.etComentario.movementMethod = ScrollingMovementMethod()

        // RecyclerView
        binding.rvArticulosPedido.layoutManager = LinearLayoutManager(this)

        // Inicia vacío; luego recibirás los productos reales
        val productos: List<Producto> = emptyList()

        binding.rvArticulosPedido.adapter =
            ProductoFinalizarOrdenAdapter(productos)

        // Insets
        val pL = binding.main.paddingLeft
        val pT = binding.main.paddingTop
        val pR = binding.main.paddingRight
        val pB = binding.main.paddingBottom

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                pL + systemBars.left,
                pT + systemBars.top,
                pR + systemBars.right,
                pB + systemBars.bottom
            )
            insets
        }

        // Volver a Comanda
        binding.arrow.setOnClickListener {
            val intentCambioAComanda = Intent(context, ComandaActivity::class.java)
            startActivity(intentCambioAComanda)
        }

        // Confirmar pedido
        binding.btnConfirmarPedido.setOnClickListener {
            val intentCambioAPedidos = Intent(context, PedidosActivity::class.java)
            startActivity(intentCambioAPedidos)
        }
    }
}
