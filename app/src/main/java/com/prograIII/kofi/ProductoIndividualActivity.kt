package com.prograIII.kofi

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.room.Room
import com.prograIII.kofi.data.AppDatabase
import com.prograIII.kofi.databinding.ActivityProductoIndividualBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.prograIII.kofi.LoginActivity.Companion.nombreDB

class ProductoIndividualActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_PRODUCTO_ID = "producto_id"
    }

    private lateinit var binding: ActivityProductoIndividualBinding
    private lateinit var db: AppDatabase
    private var productoId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityProductoIndividualBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            nombreDB
        ).build()

        productoId = intent.getIntExtra(EXTRA_PRODUCTO_ID, -1)
        if (productoId == -1) {
            Toast.makeText(this, "Producto inválido", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Mostrar datos
        cargarProducto(productoId)

        // Volver
        binding.ibRegresar.setOnClickListener { finish() }

        // Editar
        binding.btnEditar.setOnClickListener {
            val i = Intent(this, GuardarProductoActivity::class.java)
            i.putExtra(GuardarProductoActivity.EXTRA_PRODUCTO_ID, productoId)
            startActivity(i)
        }
    }

    private fun cargarProducto(id: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            val producto = db.productoDao().obtenerPorId(id)

            withContext(Dispatchers.Main) {
                if (producto == null) {
                    Toast.makeText(this@ProductoIndividualActivity, "Producto no encontrado", Toast.LENGTH_SHORT).show()
                    finish()
                    return@withContext
                }

                binding.tvNombreProducto.text = producto.nombre
                binding.tvPrecioProducto.text = "${producto.precio} Bs."
                binding.tvDescripcionProducto.text = producto.descripcion

                val resId = resources.getIdentifier(producto.imagen, "drawable", packageName)
                binding.ivProducto.setImageResource(
                    if (resId != 0) resId else R.drawable.food_1_svgrepo_com
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (productoId != -1) cargarProducto(productoId) // refresca si editaste
    }
}
