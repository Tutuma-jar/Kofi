package com.prograIII.kofi

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
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
import com.prograIII.kofi.databinding.ActivityMenuBinding

class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding
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

        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // ---------------- CATEGORÍAS ----------------

        binding.categoria1.setOnClickListener { abrirCategoria("BREAKFAST") }
        binding.categoria2.setOnClickListener { abrirCategoria("CAFES") }
        binding.categoria3.setOnClickListener { abrirCategoria("INFUSIONES") }
        binding.categoria4.setOnClickListener { abrirCategoria("BEBIDAS") }
        binding.categoria5.setOnClickListener { abrirCategoria("SANDWICHES") }
        binding.categoria6.setOnClickListener { abrirCategoria("PANADERIA") }
        binding.categoria7.setOnClickListener { abrirCategoria("PASTELES") }
        binding.categoria8.setOnClickListener { abrirCategoria("HELADOS") }

        // ---------------- UI ----------------

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

        Log.v("Ejemplo", "Se cambió el modo")
    }

    private fun abrirCategoria(codigo: String) {
        val intent = Intent(context, CategoriaMenuActivity::class.java)
        intent.putExtra("codigoCategoria", codigo)
        startActivity(intent)
    }

    // ---------- MODO OSCURO ----------

    private fun aplicarModoGuardado() {
        val activado = sharedPreferences.getBoolean(
            CLAVE_MODO_OSCURO, false
        )

        AppCompatDelegate.setDefaultNightMode(
            if (activado) MODE_NIGHT_YES else MODE_NIGHT_NO
        )
    }

    private fun inicializarSwitchModoOscuro() {
        binding.switchModoOscuro.isChecked =
            sharedPreferences.getBoolean(CLAVE_MODO_OSCURO, false)
    }

    private fun guardarModoOscuro(activado: Boolean) {
        val editor= sharedPreferences.edit()
        editor.putBoolean(CLAVE_MODO_OSCURO, activado)
        editor.apply()
    }

    private fun habilitarModoOscuro() {
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
    }

    private fun deshabilitarModoOscuro() {
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
    }
}
