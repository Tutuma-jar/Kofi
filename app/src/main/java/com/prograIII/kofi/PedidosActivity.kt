package com.prograIII.kofi

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
import com.prograIII.kofi.databinding.ActivityPedidosBinding
import com.prograIII.kofi.dataclasses.Pedido
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PedidosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPedidosBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: AppDatabase

    private lateinit var adapter: PedidosAdapter
    private var listaCompletaDB: List<OrdenEntity> = emptyList()

    val context: Context = this

    private var estado = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPedidosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            nombreDB
        ).build()

        binding.rvPedidos.layoutManager = LinearLayoutManager(this)


        //INSETS
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

        cargarOrdenes()

        //DRAWER
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

        binding.btnCerrarSesion.setOnClickListener {
            auth.signOut()
            startActivity(Intent(context, LoginActivity::class.java))
            finish()
        }

        binding.btnTodo.setOnClickListener {
            estado = 0
            cargarOrdenes()
        }

        binding.btnPendiente.setOnClickListener {
            estado = 1
            cargarOrdenes()
        }

        binding.btnListo.setOnClickListener {
            estado = 2
            cargarOrdenes()
        }
    }

    override fun onResume() {
        super.onResume()
        cargarOrdenes()
    }

    private fun cargarOrdenes() {
        GlobalScope.launch(Dispatchers.IO) {
            // Consultamos al DAO
            if (estado == 0){
                listaCompletaDB = db.ordenDao().obtenerTodasLasOrdenes()
            }else if (estado == 1){
                listaCompletaDB = db.ordenDao().obtenerOrdenesPorEstado(false)
            }else if (estado == 2) {
                listaCompletaDB = db.ordenDao().obtenerOrdenesPorEstado(true)
            }

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
                binding.rvPedidos.adapter = PedidosAdapter(pedidoUi,
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
