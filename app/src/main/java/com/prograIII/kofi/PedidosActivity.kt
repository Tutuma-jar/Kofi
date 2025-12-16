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
import androidx.recyclerview.widget.LinearLayoutManager
import com.prograIII.kofi.adapters.PedidosAdapter
import com.prograIII.kofi.databinding.ActivityPedidosBinding
import com.prograIII.kofi.dataclasses.Pedido

class PedidosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPedidosBinding
    private lateinit var adapter: PedidosAdapter
    private val listaPedidos = mutableListOf<Pedido>()

    val context: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityPedidosBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
    }

    // ---------------- MODO OSCURO ----------------
    private fun configurarModoOscuro() {
        inicializarSwitchModoOscuro()

        binding.switchModoOscuro.setOnCheckedChangeListener { _, seleccionado ->
            if (seleccionado) {
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
            }
        }
    }

    private fun inicializarSwitchModoOscuro() {
        val nightModeFlags =
            resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        binding.switchModoOscuro.isChecked =
            nightModeFlags == Configuration.UI_MODE_NIGHT_YES
    }

    // ---------------- RECYCLER VIEW ----------------
    private fun configurarRecyclerView() {
        adapter = PedidosAdapter(
            pedidos = listaPedidos,
            onVerDetalles = { pedido ->
                // Aquí luego puedes abrir DetallePedidoActivity
            },
            onEstadoCambiado = { pedido, listo ->
                pedido.listo = listo
                // Aquí luego guardas en Room
            }
        )

        binding.rvPedidos.layoutManager = LinearLayoutManager(this)
        binding.rvPedidos.adapter = adapter
    }

    // ---------------- DATOS MOCK ----------------
    private fun cargarPedidosMock() {
        listaPedidos.addAll(
            listOf(
                Pedido(34, "Pérez", 5, 25.50, false),
                Pedido(35, "Gómez", 3, 18.00, true),
                Pedido(36, "Rojas", 7, 42.00, false)
            )
        )
        adapter.notifyDataSetChanged()
    }

    // ---------------- FILTROS ----------------
    private fun configurarFiltros() {

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
