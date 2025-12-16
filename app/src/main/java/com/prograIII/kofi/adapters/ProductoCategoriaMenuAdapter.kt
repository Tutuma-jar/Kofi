package com.prograIII.kofi.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.prograIII.kofi.R
import com.prograIII.kofi.dataclasses.Producto
import com.prograIII.kofi.databinding.ProductoCategoriaMenuBinding

class ProductoCategoriaMenuAdapter(
    private val productos: List<Producto>,
    private val onClick: (Producto) -> Unit
) : RecyclerView.Adapter<ProductoCategoriaMenuAdapter.ProductoViewHolder>() {

    inner class ProductoViewHolder(val binding: ProductoCategoriaMenuBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val binding = ProductoCategoriaMenuBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productos[position]

        holder.binding.apply {
            tvNombreArticulo.text = producto.nombre
            tvDescripcionArticulo.text = producto.descripcion
            tvPrecioArticulo.text = "${producto.precio} Bs."

            // ✅ IMAGEN: URI (galería) o drawable
            val img = producto.imagen
            if (img.startsWith("content://")) {
                ivArticulo.setImageURI(Uri.parse(img))
            } else {
                // Si viene nombre drawable, usamos imagenRes (ya trae fallback)
                if (producto.imagenRes != 0) {
                    ivArticulo.setImageResource(producto.imagenRes)
                } else {
                    // fallback extra
                    val resId = holder.itemView.context.resources.getIdentifier(
                        img, "drawable", holder.itemView.context.packageName
                    )
                    ivArticulo.setImageResource(if (resId != 0) resId else R.drawable.food_1_svgrepo_com)
                }
            }

            root.setOnClickListener { onClick(producto) }
        }
    }

    override fun getItemCount(): Int = productos.size
}
