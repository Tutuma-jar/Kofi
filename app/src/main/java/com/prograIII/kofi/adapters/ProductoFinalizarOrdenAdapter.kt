package com.prograIII.kofi.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.prograIII.kofi.R
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

            //URI o drawable
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

    override fun getItemCount(): Int = productos.size
}
