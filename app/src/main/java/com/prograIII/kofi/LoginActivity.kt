package com.prograIII.kofi

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.prograIII.kofi.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

import androidx.room.Room
import com.prograIII.kofi.data.AppDatabase
import com.prograIII.kofi.data.CategoriaEntity
import com.prograIII.kofi.data.ProductoEntity

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import kotlinx.serialization.json.Json
import com.prograIII.kofi.dataclasses.MenuJson

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: AppDatabase

    val context: Context = this
    companion object {
        val nombreDB = "kofi-db"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            nombreDB
        ).build()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val currentUser = auth.currentUser
        if (currentUser != null) {
            llenarBaseSiEstaVacia()
            val intentUsuarioLogueado = Intent(context, PrincipalActivity::class.java)
            startActivity(intentUsuarioLogueado)
        }

        binding.signIn.setOnClickListener {
            val correo = binding.correo.text.toString()
            val password = binding.pass.text.toString()
            loginUsuario(correo, password)
        }

        binding.crearCuenta.setOnClickListener {
            val correo = binding.correo.text.toString()
            val password = binding.pass.text.toString()
            crearUsuario(correo, password)
        }
    }

    fun loginUsuario(correo: String, password: String) {
        auth.signInWithEmailAndPassword(correo, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    llenarBaseSiEstaVacia()

                    // Nuestro usuario se logeo correctamente
                    val intentLogueado = Intent(context, PrincipalActivity::class.java)
                    startActivity(intentLogueado)
                } else {
                    Toast.makeText(
                        baseContext,
                        "No pudo loguearse",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    fun crearUsuario(
        correo: String,
        password: String
    ) {
        auth.createUserWithEmailAndPassword(correo, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // mi usuario se creó correctamente
                } else {
                    Toast.makeText(
                        baseContext,
                        "No se pudo crear usuario",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun llenarBaseSiEstaVacia() {
        GlobalScope.launch(Dispatchers.IO) {

            val categoriasCount = db.categoriaDao().contarCategorias()
            val productosCount = db.productoDao().contarProductos()

            if (categoriasCount == 0 && productosCount == 0) {

                val jsonString = leerJsonDesdeAssets("menu.json")
                val menu = Json.decodeFromString<MenuJson>(jsonString)

                menu.categorias.forEach { categoriaJson ->

                    db.categoriaDao().insertarCategoria(
                        CategoriaEntity(
                            codigo = categoriaJson.codigo,
                            nombre = categoriaJson.nombre
                        )
                    )

                    val categoriaEntity =
                        db.categoriaDao().obtenerCategoriaPorCodigo(categoriaJson.codigo)
                            ?: return@forEach

                    categoriaJson.productos.forEach { productoJson ->
                        db.productoDao().insertarProducto(
                            ProductoEntity(
                                nombre = productoJson.nombre,
                                descripcion = productoJson.descripcion,
                                precio = productoJson.precio,
                                categoriaId = categoriaEntity.id,
                                imagen = productoJson.imagen
                            )
                        )
                    }
                }
            }
        }
    }

    private fun leerJsonDesdeAssets(nombreArchivo: String): String {
        return assets.open(nombreArchivo)
            .bufferedReader()
            .use { it.readText() }
    }
}
