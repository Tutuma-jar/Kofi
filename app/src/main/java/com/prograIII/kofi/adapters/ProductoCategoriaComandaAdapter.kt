package com.prograIII.kofi.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.prograIII.kofi.databinding.ProductoCategoriaComandaBinding
import com.prograIII.kofi.dataclasses.Producto

class ProductoCategoriaComandaAdapter(
    private val productos: List<Producto>,
    // 1. NUEVO: Agregamos la función que avisa el click
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
            imagenProducto.setImageResource(producto.imagenRes)

            // 2. NUEVO: Detectamos el click en el botón "addButton"
            addButton.setOnClickListener {
                onAddClick(producto)
            }
        }
    }

    override fun getItemCount(): Int = productos.size

}
