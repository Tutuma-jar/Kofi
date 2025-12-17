package com.prograIII.kofi.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.prograIII.kofi.databinding.ProductoFinalizarOrdenBinding
import com.prograIII.kofi.dataclasses.ItemPedido

class ItemPedidoAdapter(
    private val context: Context,
    private val items: MutableList<ItemPedido>,
    private val onListaActualizada: (List<ItemPedido>) -> Unit
) : RecyclerView.Adapter<ItemPedidoAdapter.ItemPedidoViewHolder>() {

    private val itemsMostrados: MutableList<ItemPedido> = items.toMutableList()

    inner class ItemPedidoViewHolder(
        val binding: ProductoFinalizarOrdenBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemPedidoViewHolder {
        val binding = ProductoFinalizarOrdenBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ItemPedidoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemPedidoViewHolder, position: Int) {
        val item = itemsMostrados[position]

        with(holder.binding) {

            val resId = context.resources.getIdentifier(
                item.imagen,
                "drawable",
                context.packageName
            )
            if (resId != 0) {
                ivImagenArticulo.setImageResource(resId)
            }

            tvNombreArticulo.text = item.nombre
            tvPrecioArticulo.text = "%.2f Bs.".format(item.precio)
            tvCantidad.text = item.cantidad.toString()

            btnDisminuirCantidad.setOnClickListener {
                if (item.cantidad > 1) {
                    item.cantidad--
                    tvCantidad.text = item.cantidad.toString()
                    onListaActualizada(items)
                }
            }

            btnIncrementarCantidadCantidad.setOnClickListener {
                item.cantidad++
                tvCantidad.text = item.cantidad.toString()
                onListaActualizada(items)
            }

            ibEliminarArticulo.setOnClickListener {
                val pos = holder.adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    val itemAEliminar = itemsMostrados[pos]
                    items.remove(itemAEliminar)
                    itemsMostrados.removeAt(pos)
                    notifyItemRemoved(pos)
                    onListaActualizada(items)
                }
            }
        }
    }

    override fun getItemCount(): Int = itemsMostrados.size

    fun getItems(): List<ItemPedido> = items

    fun filtrar(texto: String) {
        val q = texto.trim().lowercase()
        itemsMostrados.clear()

        if (q.isEmpty()) {
            itemsMostrados.addAll(items)
        } else {
            itemsMostrados.addAll(
                items.filter { it.nombre.lowercase().contains(q) }
            )
        }
        notifyDataSetChanged()
    }
}
