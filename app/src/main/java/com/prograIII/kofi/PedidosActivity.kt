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
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.prograIII.kofi.databinding.ActivityPedidosBinding

class PedidosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPedidosBinding
    lateinit var auth: FirebaseAuth
    val context: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        super.onCreate(savedInstanceState)

        binding = ActivityPedidosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.arrow.setOnClickListener {
            val intentCambioAPrincipal = Intent(context, PrincipalActivity::class.java)
            startActivity(intentCambioAPrincipal)
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
            val intent = Intent(context, MenuActivity::class.java) // Asegúrate de crear esta Activity
            startActivity(intent)
        }

        binding.navBtnPedidos.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.END)
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
    }
}