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
import com.prograIII.kofi.dataclasses.Pedido

class PedidosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPedidosBinding
    lateinit var auth: FirebaseAuth
    private lateinit var adapter: PedidosAdapter
    private val listaPedidos = mutableListOf<Pedido>()

    val context: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityPedidosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        aplicarInsets()
        configurarDrawer()
        configurarModoOscuro()
        configurarRecyclerView()
        cargarPedidosMock()
        configurarFiltros()
    }

    // ---------------- INSETS ----------------
    private fun aplicarInsets() {
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
    }

    // ---------------- DRAWER ----------------
    private fun configurarDrawer() {

        binding.arrow.setOnClickListener {
            startActivity(Intent(context, PrincipalActivity::class.java))
        }


        //Barra lateral
        binding.menuButton.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.END)
        }

        binding.navBtnInicio.setOnClickListener {
            startActivity(Intent(context, PrincipalActivity::class.java))
        }

        binding.navBtnMenu.setOnClickListener {
            startActivity(Intent(context, MenuActivity::class.java))
        }

        binding.navBtnPedidos.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.END)
        }

        binding.navBtnComanda.setOnClickListener {
            startActivity(Intent(context, ComandaActivity::class.java))
        }

        binding.btnCerrarSesion.setOnClickListener {
            auth.signOut()
            val intentCambioALogin = Intent(context, LoginActivity::class.java)
            startActivity(intentCambioALogin)
        }
    }
}