package com.prograIII.kofi

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.prograIII.kofi.databinding.ActivityMenuBinding

class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding
    val context: Context = this

    companion object{
        val categoria = "categoria"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.arrow.setOnClickListener {
            val intentCambioAPrincipal = Intent(context, PrincipalActivity::class.java)
            startActivity(intentCambioAPrincipal)
        }

        //Pasar dato de pantalla a categoria menu
        binding.breakfast.setOnClickListener {
            val cambio: Intent = Intent(context, CategoriaMenuActivity::class.java)
            cambio.apply {
                putExtra(categoria, "Breakfast")
            }
            startActivity(cambio)
        }
        binding.cafes.setOnClickListener {
            val cambio: Intent = Intent(context, CategoriaMenuActivity::class.java)
            cambio.apply {
                putExtra(categoria, "Cafe")
            }
            startActivity(cambio)
        }
        binding.infusiones.setOnClickListener {
            val cambio: Intent = Intent(context, CategoriaMenuActivity::class.java)
            cambio.apply {
                putExtra(categoria, "Infusiones")
            }
            startActivity(cambio)
        }
        binding.bebidas.setOnClickListener {
            val cambio: Intent = Intent(context, CategoriaMenuActivity::class.java)
            cambio.apply {
                putExtra(categoria, "Bebidas")
            }
            startActivity(cambio)
        }
        binding.sandwiches.setOnClickListener {
            val cambio: Intent = Intent(context, CategoriaMenuActivity::class.java)
            cambio.apply {
                putExtra(categoria, "Sandwiches")
            }
            startActivity(cambio)
        }
        binding.panaderia.setOnClickListener {
            val cambio: Intent = Intent(context, CategoriaMenuActivity::class.java)
            cambio.apply {
                putExtra(categoria, "Panaderia")
            }
            startActivity(cambio)
        }
        binding.pasteles.setOnClickListener {
            val cambio: Intent = Intent(context, CategoriaMenuActivity::class.java)
            cambio.apply {
                putExtra(categoria, "Pasteles")
            }
            startActivity(cambio)
        }
        binding.helados.setOnClickListener {
            val cambio: Intent = Intent(context, CategoriaMenuActivity::class.java)
            cambio.apply {
                putExtra(categoria, "Helados")
            }
            startActivity(cambio)
        }




        //Barra lateral
        binding.menuButton.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.END)
        }

        binding.navBtnInicio.setOnClickListener {
            val intent = Intent(context, PrincipalActivity::class.java)
            startActivity(intent)
        }

        binding.navBtnMenu.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.END)
        }

        binding.navBtnPedidos.setOnClickListener {
            val intent = Intent(context, PedidosActivity::class.java) // Asegúrate de crear esta Activity
            startActivity(intent)
        }

        binding.navBtnComanda.setOnClickListener {
            val intent = Intent(context, ComandaActivity::class.java)
            startActivity(intent)
        }
    }
}