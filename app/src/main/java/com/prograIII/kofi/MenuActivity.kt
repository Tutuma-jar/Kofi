package com.prograIII.kofi

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.prograIII.kofi.databinding.ActivityMenuBinding

class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding
    val context: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val content = binding.mainContent

        val pL = content.paddingLeft
        val pT = content.paddingTop
        val pR = content.paddingRight
        val pB = content.paddingBottom

        ViewCompat.setOnApplyWindowInsetsListener(binding.drawerLayout) { _, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            content.setPadding(
                pL + bars.left,
                pT + bars.top,
                pR + bars.right,
                pB + bars.bottom
            )
            insets
        }

        //CATEGORIAS
        binding.categoria1.setOnClickListener { abrirCategoria("BREAKFAST") }
        binding.categoria2.setOnClickListener { abrirCategoria("CAFES") }
        binding.categoria3.setOnClickListener { abrirCategoria("INFUSIONES") }
        binding.categoria4.setOnClickListener { abrirCategoria("BEBIDAS") }
        binding.categoria5.setOnClickListener { abrirCategoria("SANDWICHES") }
        binding.categoria6.setOnClickListener { abrirCategoria("PANADERIA") }
        binding.categoria7.setOnClickListener { abrirCategoria("PASTELES") }
        binding.categoria8.setOnClickListener { abrirCategoria("HELADOS") }

        // UI
        binding.arrow.setOnClickListener {
            startActivity(Intent(context, PrincipalActivity::class.java))
        }

        binding.menuButton.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.END)
        }

        binding.navBtnInicio.setOnClickListener {
            startActivity(Intent(context, PrincipalActivity::class.java))
        }

        binding.navBtnMenu.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.END)
        }

        binding.navBtnPedidos.setOnClickListener {
            startActivity(Intent(context, PedidosActivity::class.java))
        }

        binding.navBtnComanda.setOnClickListener {
            startActivity(Intent(context, ComandaActivity::class.java))
        }

        inicializarSwitchModoOscuro()

        binding.switchModoOscuro
            .setOnCheckedChangeListener { _, seleccionado ->
                if(seleccionado){
                    //seleccionado
                    habilitarModoOscuro()
                } else {
                    //no seleccionado
                    deshabilitarModoOscuro()
                }
            }
    }

    private fun abrirCategoria(codigo: String) {
        val intent = Intent(context, CategoriaMenuActivity::class.java)
        intent.putExtra("codigoCategoria", codigo)
        startActivity(intent)
    }

    private fun inicializarSwitchModoOscuro() {
        val nightModeFlags =
            resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        binding.switchModoOscuro.isChecked =
            nightModeFlags == Configuration.UI_MODE_NIGHT_YES
    }

    private fun habilitarModoOscuro(){
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)

    }

    private fun deshabilitarModoOscuro(){
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
    }
}
