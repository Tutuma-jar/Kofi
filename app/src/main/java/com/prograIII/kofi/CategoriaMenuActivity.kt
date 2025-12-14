package com.prograIII.kofi

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.prograIII.kofi.adapters.ProductoCategoriaMenuAdapter
import com.prograIII.kofi.databinding.ActivityCategoriaBinding
import kotlinx.serialization.json.Json
import com.prograIII.kofi.MenuActivity.Companion.categoria

class CategoriaMenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoriaBinding
    private lateinit var sharedPreferences: SharedPreferences
    val context: Context = this
    private var productos: MutableList<Producto> = mutableListOf()
    private lateinit var adapter: ProductoCategoriaMenuAdapter
    private var listaCategoria: String = ""
    private var archivoJson: String = ""

    companion object {
        const val FICHERO_SP = "COMIDA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityCategoriaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Creamos pantalla dependiendo lo que apreto usuario:)
        val categoriaRecibida: String? = intent.getStringExtra(categoria)
        binding.TituloCategoria.text = categoriaRecibida
        configurarCategoria(categoriaRecibida)


        sharedPreferences = context.getSharedPreferences(FICHERO_SP, MODE_PRIVATE)

        binding.rvArticulosCategoria.layoutManager = LinearLayoutManager(context)

        cargarDatos()

        binding.rvArticulosCategoria.adapter = ProductoCategoriaMenuAdapter(productos)

        binding.arrow.setOnClickListener {
            val intentCambioAPrincipal = Intent(context, PrincipalActivity::class.java)
            startActivity(intentCambioAPrincipal)
            finish()
        }

        binding.nuevaReceta.setOnClickListener {
            val intentCambioAComanda = Intent(context, ComandaActivity::class.java)
            startActivity(intentCambioAComanda)
        }
    }

    fun configurarCategoria(nombreCategoria: String?) {
        if (nombreCategoria=="Breakfast"){
            listaCategoria = "data_sandwiches"
            archivoJson = "sandwiches.json"
        }else if(nombreCategoria=="Cafe"){
            listaCategoria = "data_cafes"
            archivoJson = "cafes.json"
        }else if (nombreCategoria=="Infusiones"){
            listaCategoria = "data_tes"
            archivoJson = "tes.json"
        }else if (nombreCategoria=="Bebidas"){
            listaCategoria = "data_bebidas"
            archivoJson = "bebidas.json"
        }else if (nombreCategoria=="Sandwiches"){
            listaCategoria = "data_tes"
            archivoJson = "tes.json"
        }else if (nombreCategoria=="Panaderia"){
            listaCategoria = "data_tes"
            archivoJson = "tes.json"
        }else if (nombreCategoria=="Pasteles"){
            listaCategoria = "data_tes"
            archivoJson = "tes.json"
        }else if (nombreCategoria=="Helados"){
            listaCategoria = "data_tes"
            archivoJson = "tes.json"
        }
    }
    fun cargarDatos() {
        val jsonGuardado = sharedPreferences.getString(listaCategoria, null)
        if (jsonGuardado != null) {
            productos = Json.decodeFromString<MutableList<Producto>>(jsonGuardado)
        } else {
            cargarAssets()
        }
    }

    // Lee el archivo sandwich.json desde assets
    fun cargarAssets() {
        val jsonString = assets.open(archivoJson).bufferedReader().use {
            it.readText()
        }
        productos = Json.decodeFromString<MutableList<Producto>>(jsonString)
        guardarEnMemoria()
    }

    fun guardarEnMemoria() {
        val jsonString = Json.encodeToString(productos)
        val editor = sharedPreferences.edit()
        editor.putString(listaCategoria, jsonString)
        editor.apply()
    }
}
