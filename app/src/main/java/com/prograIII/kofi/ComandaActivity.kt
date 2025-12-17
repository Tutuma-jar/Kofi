package com.prograIII.kofi

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.room.Room
import com.prograIII.kofi.LoginActivity.Companion.nombreDB
import com.prograIII.kofi.adapters.ProductoCategoriaComandaAdapter
import com.prograIII.kofi.data.AppDatabase
import com.prograIII.kofi.data.DetalleOrdenEntity
import com.prograIII.kofi.data.OrdenEntity
import com.prograIII.kofi.databinding.ActivityComandaBinding
import com.prograIII.kofi.dataclasses.ItemPedido
import com.prograIII.kofi.dataclasses.Producto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ComandaActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var binding: ActivityComandaBinding
    private val context: Context = this

    private val listaPedidos = mutableListOf<ItemPedido>()
    private var productosActuales: List<Producto> = emptyList()
    private var productosMenuCompleto: List<Producto> = emptyList()

    private var ordenIdActual: Int? = null

    private lateinit var adapterProductos: ProductoCategoriaComandaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityComandaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            nombreDB
        ).build()

        binding.recyclerProductos.layoutManager = GridLayoutManager(context, 2)

        adapterProductos = ProductoCategoriaComandaAdapter(emptyList()) { productoSeleccionado ->
            agregarProductoAOrden(productoSeleccionado)
        }
        binding.recyclerProductos.adapter = adapterProductos

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

        // Cargar todo el menu
        GlobalScope.launch(Dispatchers.IO) {
            val productosDb = db.productoDao().obtenerTodos()
            val productosUi = productosDb.map { p ->
                Producto(
                    id = p.id,
                    nombre = p.nombre,
                    descripcion = p.descripcion,
                    precio = p.precio,
                    categoriaId = p.categoriaId,
                    imagen = p.imagen
                )
            }
            runOnUiThread {
                productosMenuCompleto = productosUi
            }
        }

        cargarProductosPorCodigo("CAFES")
        binding.categoria1.setOnClickListener { cargarProductosPorCodigo("CAFES") }
        binding.categoria2.setOnClickListener { cargarProductosPorCodigo("INFUSIONES") }
        binding.categoria3.setOnClickListener { cargarProductosPorCodigo("BREAKFAST") }
        binding.categoria4.setOnClickListener { cargarProductosPorCodigo("PANADERIA") }
        binding.categoria5.setOnClickListener { cargarProductosPorCodigo("PASTELES") }
        binding.categoria6.setOnClickListener { cargarProductosPorCodigo("SANDWICHES") }
        binding.categoria7.setOnClickListener { cargarProductosPorCodigo("HELADOS") }
        binding.categoria8.setOnClickListener { cargarProductosPorCodigo("BEBIDAS") }

        binding.etBuscar.addTextChangedListener { texto ->
            val q = texto.toString().trim().lowercase()

            if (q.isEmpty()) {
                adapterProductos.actualizarLista(productosActuales)
            } else {
                val filtrados = productosMenuCompleto.filter {
                    it.nombre.lowercase().contains(q)
                }
                adapterProductos.actualizarLista(filtrados)
            }
        }

        binding.arrow.setOnClickListener {
            startActivity(Intent(context, PrincipalActivity::class.java))
        }

        // ✅ ÚNICO CAMBIO: ahora finaliza usando la BD (ordenIdActual + detalles)
        binding.finalizarOrden.setOnClickListener {

            if (ordenIdActual == null) {
                Toast.makeText(context, "La orden está vacía, agrega productos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            GlobalScope.launch(Dispatchers.IO) {

                val detalles = db.ordenDao().obtenerDetallesDeOrden(ordenIdActual!!)

                if (detalles.isEmpty()) {
                    runOnUiThread {
                        Toast.makeText(context, "La orden está vacía, agrega productos", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }

                runOnUiThread {
                    val intent = Intent(context, FinalizarOrdenActivity::class.java)
                    intent.putExtra("ID_ORDEN", ordenIdActual!!)
                    startActivity(intent)
                }
            }
        }
    }

    private fun cargarProductosPorCodigo(codigo: String) {
        GlobalScope.launch(Dispatchers.IO) {

            val categoria = db.categoriaDao().obtenerCategoriaPorCodigo(codigo)
                ?: return@launch

            val productosDb = db.productoDao().obtenerPorCategoria(categoria.id)

            val productosUi = productosDb.map { p ->
                Producto(
                    id = p.id,
                    nombre = p.nombre,
                    descripcion = p.descripcion,
                    precio = p.precio,
                    categoriaId = p.categoriaId,
                    imagen = p.imagen
                )
            }

            runOnUiThread {
                productosActuales = productosUi
                binding.etBuscar.setText("")
                adapterProductos.actualizarLista(productosActuales)
            }
        }
    }

    private fun agregarProductoAOrden(productoSeleccionado: Producto) {

        GlobalScope.launch(Dispatchers.IO) {

            // Si no hay orden aún, la creamos
            if (ordenIdActual == null) {
                val nuevaOrden = OrdenEntity(
                    cliente = "",
                    nit = 0,
                    comentario = "",
                    totalItems = 0,
                    totalMonto = 0.0,
                    listo = false
                )
                ordenIdActual = db.ordenDao().insertarOrden(nuevaOrden).toInt()
            }

            // Insertamos el producto directamente en BD
            val detalle = DetalleOrdenEntity(
                ordenId = ordenIdActual!!,
                imagenProducto = productoSeleccionado.imagen,
                nombreProducto = productoSeleccionado.nombre,
                precio = productoSeleccionado.precio,
                cantidad = 1
            )

            db.ordenDao().insertarDetalles(listOf(detalle))

            runOnUiThread {
                Toast.makeText(context, "Agregado: ${productoSeleccionado.nombre}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
