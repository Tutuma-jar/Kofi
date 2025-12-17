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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.prograIII.kofi.LoginActivity.Companion.nombreDB
import com.prograIII.kofi.adapters.PedidosAdapter
import com.prograIII.kofi.data.AppDatabase
import com.prograIII.kofi.data.OrdenEntity
import com.prograIII.kofi.databinding.ActivityPrincipalBinding
import com.prograIII.kofi.dataclasses.Pedido
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PrincipalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPrincipalBinding
    lateinit var auth: FirebaseAuth
    private lateinit var db: AppDatabase
    private var listaCompletaDB: List<OrdenEntity> = emptyList()

    val context: Context = this


    override fun onCreate(savedInstanceState: Bundle?) {

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        super.onCreate(savedInstanceState)

        binding = ActivityPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            nombreDB
        ).build()

        val content = binding.mainContent

        binding.recyclerPedidosPendientes.layoutManager = LinearLayoutManager(this)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            nombreDB
        ).build()

        cargarOrdenes()

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
            finish()
        }
    }
    override fun onResume() {
        super.onResume()
        cargarOrdenes()
    }

    private fun cargarOrdenes() {
        GlobalScope.launch(Dispatchers.IO) {
            // Consultamos al DAO
            listaCompletaDB = db.ordenDao().obtenerOrdenesPorEstado(false)

            val pedidoUi = listaCompletaDB.map { p ->
                Pedido(
                    id = p.id,
                    cliente = p.cliente,
                    nit = p.nit,
                    comentario = p.comentario,
                    totalItems = p.totalItems,
                    totalMonto = p.totalMonto,
                    listo = p.listo
                )
            }
            runOnUiThread {
                // Llenamos el adapter con los datos reales
                binding.recyclerPedidosPendientes.adapter = PedidosAdapter(pedidoUi,
                    onVerDetalles = { item -> onVerDetalles(item.id)},
                    onEstadoCambiado = { item -> onEstadoCambiado(item)}
                )
            }
        }
    }

    private fun onVerDetalles(idOrden: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            val intent = Intent(context, FinalizarOrdenActivity::class.java)
            intent.putExtra("ID_ORDEN", idOrden) // IMPORTANTE: Pasamos el ID
            startActivity(intent)
        }
    }
    private fun onEstadoCambiado(item: Pedido) {
        GlobalScope.launch(Dispatchers.IO) {
            val ordenEntity = db.ordenDao().obtenerOrdenPorId(item.id)
            val ordenActualizada = ordenEntity.copy(listo = item.listo)
            db.ordenDao().actualizarOrden(ordenActualizada)

        }
    }
}
