package com.prograIII.kofi

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.prograIII.kofi.adapters.PedidosAdapter
import com.prograIII.kofi.databinding.ActivityPedidosBinding
import com.prograIII.kofi.dataclasses.Pedido

class PedidosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPedidosBinding
    private lateinit var auth: FirebaseAuth
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

        /* ---------------- INSETS (COMO ANTES) ---------------- */
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

        /* ---------------- DRAWER ---------------- */
        binding.arrow.setOnClickListener {
            val intentCambioAPrincipal = Intent(context, PrincipalActivity::class.java)
            startActivity(intentCambioAPrincipal)
        }

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
            finish()
        }

        /* ---------------- RECYCLER VIEW ---------------- */
        adapter = PedidosAdapter(
            pedidos = listaPedidos,
            onVerDetalles = { /* luego */ },
            onEstadoCambiado = { pedido, listo ->
                pedido.listo = listo
            }
        )

        binding.rvPedidos.layoutManager = LinearLayoutManager(this)
        binding.rvPedidos.adapter = adapter

        /* ---------------- DATOS MOCK ---------------- */
        listaPedidos.addAll(
            listOf(
                Pedido(34, "Pérez", 5, 25.50, false),
                Pedido(35, "Gómez", 3, 18.00, true),
                Pedido(36, "Rojas", 7, 42.00, false)
            )
        )
        adapter.notifyDataSetChanged()

        /* ---------------- FILTROS (DIRECTO, SIN MÉTODOS) ---------------- */
        binding.btnTodo.setOnClickListener {
            adapter.setLista(listaPedidos)
        }

        binding.btnListo.setOnClickListener {
            adapter.setLista(listaPedidos.filter { it.listo })
        }

        binding.btnPendiente.setOnClickListener {
            adapter.setLista(listaPedidos.filter { !it.listo })
        }
    }
}