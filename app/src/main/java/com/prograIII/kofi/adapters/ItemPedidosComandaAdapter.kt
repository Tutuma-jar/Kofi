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
        val item = items[position]

        with(holder.binding) {

            // ===== Imagen =====
            val resId = context.resources.getIdentifier(
                item.imagen,
                "drawable",
                context.packageName
            )
            if (resId != 0) {
                ivImagenArticulo.setImageResource(resId)
            }

            // ===== Textos =====
            tvNombreArticulo.text = item.nombre
            tvPrecioArticulo.text = "%.2f Bs.".format(item.precio)
            tvCantidad.text = item.cantidad.toString()

            // ===== Cantidad - =====
            btnDisminuirCantidad.setOnClickListener {
                if (item.cantidad > 1) {
                    item.cantidad--
                    tvCantidad.text = item.cantidad.toString()
                    onListaActualizada(items)
                }
            }

            // ===== Cantidad + =====
            btnIncrementarCantidadCantidad.setOnClickListener {
                item.cantidad++
                tvCantidad.text = item.cantidad.toString()
                onListaActualizada(items)
            }

            // ===== Eliminar =====
            ibEliminarArticulo.setOnClickListener {
                val pos = holder.adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    items.removeAt(pos)
                    notifyItemRemoved(pos)
                    onListaActualizada(items)
                }
            }
        }
    }

    override fun getItemCount(): Int = items.size

    fun getItems(): List<ItemPedido> = items
}
