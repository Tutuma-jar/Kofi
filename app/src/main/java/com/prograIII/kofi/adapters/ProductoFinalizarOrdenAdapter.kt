package com.prograIII.kofi.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.prograIII.kofi.dataclasses.Producto
import com.prograIII.kofi.databinding.ProductoFinalizarOrdenBinding

class ProductoFinalizarOrdenAdapter(
    private val productos: List<Producto>
) : RecyclerView.Adapter<ProductoFinalizarOrdenAdapter.ProductoViewHolder>() {

    inner class ProductoViewHolder(val binding: ProductoFinalizarOrdenBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ProductoFinalizarOrdenBinding.inflate(inflater, parent, false)
        return ProductoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productos[position]

        holder.binding.apply {
            tvNombreArticulo.text = producto.nombre
            tvPrecioArticulo.text = "${producto.precio} Bs."
            ivImagenArticulo.setImageResource(producto.imagenRes)
        }
    }

    override fun getItemCount(): Int = productos.size
}
