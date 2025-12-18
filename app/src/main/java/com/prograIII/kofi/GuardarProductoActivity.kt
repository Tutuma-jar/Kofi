package com.prograIII.kofi

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.room.Room
import com.prograIII.kofi.data.AppDatabase
import com.prograIII.kofi.data.ProductoEntity
import com.prograIII.kofi.databinding.ActivityGuardarProductoBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.core.net.toUri

class GuardarProductoActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_PRODUCTO_ID = "producto_id"
    }

    private lateinit var binding: ActivityGuardarProductoBinding
    private lateinit var db: AppDatabase

    private var productoId: Int = -1
    private var categoriaId: Int = -1
    private var imagenActual: String = ""

    // Selector de imagen
    private val seleccionarImagen =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
            uri?.let {
                contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                imagenActual = it.toString()
                binding.ivProducto.setImageURI(it)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityGuardarProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val root = binding.root

        val pL = root.paddingLeft
        val pT = root.paddingTop
        val pR = root.paddingRight
        val pB = root.paddingBottom

        ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                pL + bars.left,
                pT + bars.top,
                pR + bars.right,
                pB + bars.bottom
            )
            insets
        }



        // Room
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "kofi-db"
        ).build()
        productoId = intent.getIntExtra(EXTRA_PRODUCTO_ID, -1)
        val esEdicion = productoId != -1

        val codigoCategoria = intent.getStringExtra("codigoCategoria")

        if (esEdicion) {
            binding.tvDetallesProducto.text = "EDITAR PRODUCTO"
            binding.btnGuardar.text = "GUARDAR CAMBIOS"
            cargarProducto(productoId)
        } else {
            binding.tvDetallesProducto.text = "NUEVO PRODUCTO"
            binding.btnGuardar.text = "AÑADIR"

            if (codigoCategoria == null) {
                Toast.makeText(this, "Falta la categoría", Toast.LENGTH_SHORT).show()
                finish()
                return
            }

            GlobalScope.launch(Dispatchers.IO) {
                val categoria = db.categoriaDao().obtenerCategoriaPorCodigo(codigoCategoria)
                withContext(Dispatchers.Main) {
                    if (categoria == null) {
                        Toast.makeText(
                            this@GuardarProductoActivity,
                            "Categoría no encontrada",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    } else {
                        categoriaId = categoria.id
                    }
                }
            }
        }

        binding.ibRegresar.setOnClickListener { finish() }

        // Abrir galería
        binding.ivProducto.setOnClickListener {
            seleccionarImagen.launch(arrayOf("image/*"))
        }

        binding.btnGuardar.setOnClickListener {
            if (esEdicion) guardarCambios()
            else agregarNuevo()
        }
    }

    private fun cargarProducto(id: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            val producto = db.productoDao().obtenerPorId(id)

            withContext(Dispatchers.Main) {
                if (producto == null) {
                    Toast.makeText(
                        this@GuardarProductoActivity,
                        "Producto no encontrado",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                    return@withContext
                }

                binding.etNombreProducto.setText(producto.nombre)
                binding.etPrecioProducto.setText(producto.precio.toString())
                binding.etDescripcionProducto.setText(producto.descripcion)

                imagenActual = producto.imagen
                categoriaId = producto.categoriaId

                val img = imagenActual
                if (img.startsWith("content://")) {
                    binding.ivProducto.setImageURI(img.toUri())
                } else {
                    val resId = resources.getIdentifier(img, "drawable", packageName)
                    binding.ivProducto.setImageResource(
                        if (resId != 0) resId else R.drawable.food_1_svgrepo_com
                    )
                }

            }
        }
    }

    private fun leerCampos(): Triple<String, Double?, String> {
        val nombre = binding.etNombreProducto.text.toString().trim()
        val precioTxt = binding.etPrecioProducto.text.toString().trim()
        val descripcion = binding.etDescripcionProducto.text.toString().trim()

        val precio = precioTxt.toDoubleOrNull()
        return Triple(nombre, precio, descripcion)
    }

    private fun agregarNuevo() {
        val (nombre, precio, descripcion) = leerCampos()

        if (nombre.isEmpty()) {
            binding.etNombreProducto.error = "Requerido"
            return
        }
        if (precio == null) {
            binding.etPrecioProducto.error = "Precio inválido"
            return
        }
        if (categoriaId == -1) {
            Toast.makeText(this, "Categoría no cargada aún", Toast.LENGTH_SHORT).show()
            return
        }

        val nuevo = ProductoEntity(
            nombre = nombre,
            descripcion = descripcion,
            precio = precio,
            categoriaId = categoriaId,
            imagen = imagenActual
        )

        GlobalScope.launch(Dispatchers.IO) {
            db.productoDao().insertarProducto(nuevo)
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    this@GuardarProductoActivity,
                    "Producto añadido",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun guardarCambios() {
        val (nombre, precio, descripcion) = leerCampos()

        if (nombre.isEmpty()) {
            binding.etNombreProducto.error = "Requerido"
            return
        }
        if (precio == null) {
            binding.etPrecioProducto.error = "Precio inválido"
            return
        }

        val actualizado = ProductoEntity(
            id = productoId,
            nombre = nombre,
            descripcion = descripcion,
            precio = precio,
            categoriaId = categoriaId,
            imagen = imagenActual
        )

        GlobalScope.launch(Dispatchers.IO) {
            db.productoDao().actualizarProducto(actualizado)
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    this@GuardarProductoActivity,
                    "Cambios guardados",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }
}
