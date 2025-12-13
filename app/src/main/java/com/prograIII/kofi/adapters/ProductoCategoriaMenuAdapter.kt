package com.prograIII.kofi.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.prograIII.kofi.Producto
import com.prograIII.kofi.R
import com.prograIII.kofi.databinding.ProductoCategoriaMenuBinding

class ProductoCategoriaMenuAdapter(
    private val productos: List<Producto>
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
        val context = holder.itemView.context

        holder.binding.apply {
            tvNombreArticulo.text = producto.nombre
            tvDescripcionArticulo.text = producto.descripcion
            tvPrecioArticulo.text = "${producto.precio} Bs."

            // --- LÓGICA INTELIGENTE DE IMÁGENES ---

            // 1. Intentamos buscar si el String coincide con un recurso en la carpeta 'drawable'
            // (Ejemplo: si el json dice "food_1", busca R.drawable.food_1)
            val resourceId = context.resources.getIdentifier(
                producto.imagenRuta,
                "drawable",
                context.packageName
            )

            if (resourceId != 0) {
                // CASO A: Es una imagen interna (del JSON inicial)
                ivArticulo.setImageResource(resourceId)
            } else {
                // CASO B: Es una imagen externa (URI) o una foto del usuario
                try {
                    val uri = Uri.parse(producto.imagenRuta)
                    ivArticulo.setImageURI(uri)

                    // Si después de intentar setear la URI la imagen sigue vacía (null drawable),
                    // ponemos una por defecto para que no se vea feo.
                    if (ivArticulo.drawable == null) {
                        ivArticulo.setImageResource(R.drawable.food_1_svgrepo_com)
                    }
                } catch (e: Exception) {
                    // CASO C: Error (texto corrupto o vacío), ponemos imagen default
                    ivArticulo.setImageResource(R.drawable.food_1_svgrepo_com)
                }
            }
        }
    }

    override fun getItemCount(): Int = productos.size
}
