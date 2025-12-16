package com.prograIII.kofi

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.prograIII.kofi.databinding.ActivityPrincipalBinding

class PrincipalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPrincipalBinding
    lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences
    val context: Context = this

    companion object {
        val NOMBRE_SHARED = "Progra3II"
        val CLAVE_MODO_OSCURO = "ModoOscuro"
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        sharedPreferences = getSharedPreferences(
            NOMBRE_SHARED, MODE_PRIVATE
        )
        
        aplicarModoGuardado()

        super.onCreate(savedInstanceState)

        binding = ActivityPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // ---------------- BOTONES ----------------

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

        binding.btnCerrarSesion.setOnClickListener {
            auth.signOut()
            val intentCambioALogin = Intent(context, LoginActivity::class.java)
            startActivity(intentCambioALogin)
        }

        // ---------------- MODO OSCURO ----------------

        inicializarSwitchModoOscuro()

        binding.switchModoOscuro.setOnCheckedChangeListener { _, seleccionado ->
            guardarModoOscuro(seleccionado)

            if (seleccionado) {
                habilitarModoOscuro()
            } else {
                deshabilitarModoOscuro()
            }
        }
    }

    // ---------- MODO OSCURO ----------

    private fun inicializarSwitchModoOscuro() {
        val activado = sharedPreferences.getBoolean(
            CLAVE_MODO_OSCURO,
            false
        )
        binding.switchModoOscuro.isChecked = activado
    }

    private fun guardarModoOscuro(activado: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(CLAVE_MODO_OSCURO, activado)
        editor.apply()
    }

    private fun aplicarModoGuardado() {
        val activado = sharedPreferences.getBoolean(
            CLAVE_MODO_OSCURO,
            false
        )

        AppCompatDelegate.setDefaultNightMode(
            if (activado) MODE_NIGHT_YES else MODE_NIGHT_NO
        )
    }

    private fun habilitarModoOscuro() {
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
    }

    private fun deshabilitarModoOscuro() {
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
    }
}
