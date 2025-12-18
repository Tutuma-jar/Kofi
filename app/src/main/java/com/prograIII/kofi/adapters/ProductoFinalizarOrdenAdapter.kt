package com.prograIII.kofi.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.prograIII.kofi.R
import com.prograIII.kofi.data.DetalleOrdenEntity
import com.prograIII.kofi.databinding.ProductoFinalizarOrdenBinding
import com.prograIII.kofi.dataclasses.ItemPedido

class ProductoFinalizarOrdenAdapter(
    private val detallesOrden: List<ItemPedido>,
    private val onSumar: (ItemPedido) -> Unit,
    private val onRestar: (ItemPedido) -> Unit,
    private val onEliminar: (ItemPedido) -> Unit

) : RecyclerView.Adapter<ProductoFinalizarOrdenAdapter.ProductoViewHolder>() {

    inner class ProductoViewHolder(val binding: ProductoFinalizarOrdenBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ProductoFinalizarOrdenBinding.inflate(inflater, parent, false)
        return ProductoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = detallesOrden[position]

        holder.binding.apply {
            // Datos traídos de la BD
            tvNombreArticulo.text = producto.nombre

            // Mostramos precio y cantidad
            val precioTotalItem = producto.precio * producto.cantidad
            tvPrecioArticulo.text = "$precioTotalItem Bs."

            // Cantidad en el medio de los botones
            tvCantidad.text = producto.cantidad.toString()

            // --- LISTENERS DE BOTONES
            btnIncrementarCantidadCantidad.setOnClickListener { onSumar(producto) }
            btnDisminuirCantidad.setOnClickListener { onRestar(producto) }
            ibEliminarArticulo.setOnClickListener { onEliminar(producto) }


            // IMAGEN: Usamos el recurso por defecto ya que la Entity no guarda la ruta de imagen.
            val img = producto.imagen
            if (img.startsWith("content://")) {
                ivImagenArticulo.setImageURI(Uri.parse(img))
            } else {
                val resId = holder.itemView.context.resources.getIdentifier(
                    img,
                    "drawable",
                    holder.itemView.context.packageName
                )
                ivImagenArticulo.setImageResource(
                    if (resId != 0) resId else R.drawable.food_1_svgrepo_com
                )
            }
        }
    }

    override fun getItemCount(): Int = detallesOrden.size
}
