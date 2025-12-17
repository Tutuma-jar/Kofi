package com.prograIII.kofi.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.prograIII.kofi.R
import com.prograIII.kofi.databinding.ProductoCategoriaComandaBinding
import com.prograIII.kofi.dataclasses.Producto

class ProductoCategoriaComandaAdapter(
    private var productos: List<Producto>,
    private val onAddClick: (Producto) -> Unit
) : RecyclerView.Adapter<ProductoCategoriaComandaAdapter.ProductoViewHolder>() {

    inner class ProductoViewHolder(val binding: ProductoCategoriaComandaBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ProductoCategoriaComandaBinding.inflate(inflater, parent, false)
        return ProductoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productos[position]

        with(holder.binding) {
            nombreProducto.text = producto.nombre
            precioProducto.text = "${producto.precio} Bs."

            val img = producto.imagen
            if (img.startsWith("content://")) {
                imagenProducto.setImageURI(Uri.parse(img))
            } else {
                val resId = holder.itemView.context.resources.getIdentifier(
                    img,
                    "drawable",
                    holder.itemView.context.packageName
                )
                imagenProducto.setImageResource(
                    if (resId != 0) resId else R.drawable.food_1_svgrepo_com
                )
            }

            addButton.setOnClickListener {
                onAddClick(producto)
            }
        }
    }

    override fun getItemCount(): Int = productos.size

    fun actualizarLista(nuevaLista: List<Producto>) {
        productos = nuevaLista
        notifyDataSetChanged()
    }
}
