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
import com.prograIII.kofi.databinding.ActivityPrincipalBinding

class PrincipalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPrincipalBinding
    val context: Context = this


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.buttonNuevaComanda.setOnClickListener {
            val intentCambioAComandas = Intent(context, ComandaActivity::class.java)
            startActivity(intentCambioAComandas)
        }

        binding.buttonMenu.setOnClickListener {
            val intentCambioAMenu = Intent(context, MenuActivity::class.java)
            startActivity(intentCambioAMenu)
        }

        //Barra lateral
        binding.menuButton.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.END)
        }

        binding.navBtnInicio.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.END)
        }

        binding.navBtnMenu.setOnClickListener {
            val intent = Intent(context, MenuActivity::class.java)
            startActivity(intent)
        }

        binding.navBtnPedidos.setOnClickListener {
            val intent = Intent(context, PedidosActivity::class.java)
            startActivity(intent)
        }

        binding.navBtnComanda.setOnClickListener {
            val intent = Intent(context, ComandaActivity::class.java)
            startActivity(intent)
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

    private fun inicializarSwitchModoOscuro() {
        val nightModeFlags =
            resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        binding.switchModoOscuro.isChecked =
            nightModeFlags == Configuration.UI_MODE_NIGHT_YES
    }

    private fun habilitarModoOscuro(){
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
//        delegate.applyDayNight()

    }

    private fun deshabilitarModoOscuro(){
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
//        delegate.applyDayNight()
    }

}