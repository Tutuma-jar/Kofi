package com.prograIII.kofi

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.prograIII.kofi.adapters.ProductoCategoriaMenuAdapter
import com.prograIII.kofi.databinding.ActivityCategoriaBinding
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.IOException

class CategoriaMenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoriaBinding
    private lateinit var sharedPreferences: SharedPreferences
    val context: Context = this
    private var productos: MutableList<Producto> = mutableListOf()
    private lateinit var adapter: ProductoCategoriaMenuAdapter

    companion object {
        const val FICHERO_SP = "COMIDA"
        const val KEY_SANDWICHES = "lista_sandwiches_data"
        const val sandwichJson = "sandwich.json"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityCategoriaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = context.getSharedPreferences(FICHERO_SP, MODE_PRIVATE)

        binding.rvArticulosCategoria.layoutManager = LinearLayoutManager(context)

        cargarDatos()

        binding.rvArticulosCategoria.adapter = ProductoCategoriaMenuAdapter(productos)



        binding.arrow.setOnClickListener {
            val intentCambioAPrincipal = Intent(context, PrincipalActivity::class.java)
            startActivity(intentCambioAPrincipal)
        }
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }

    private fun cargarDatos() {
        val jsonGuardado = sharedPreferences.getString(KEY_SANDWICHES, null)

        if (jsonGuardado != null) {
            productos = Json.decodeFromString<MutableList<Producto>>(jsonGuardado)
        } else {
            cargarAssets()
        }
    }

    // Lee el archivo sandwich.json desde assets
    private fun cargarAssets() {
        val jsonString = assets.open(sandwichJson).bufferedReader().use {
            it.readText()
        }
        productos = Json.decodeFromString<MutableList<Producto>>(jsonString)
        guardarEnMemoria()

    }

    private fun guardarEnMemoria() {
        val jsonString = Json.encodeToString(productos)
        val editor = sharedPreferences.edit()
        editor.putString(KEY_SANDWICHES, jsonString)
        editor.apply()
    }
}
