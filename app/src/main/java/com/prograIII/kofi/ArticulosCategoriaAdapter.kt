package com.prograIII.kofi

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.prograIII.kofi.databinding.ArticuloCategoriaBinding

class ArticulosCategoriaAdapter(
    private val articulos: List<Articulo>
) : RecyclerView.Adapter<ArticulosCategoriaAdapter.ArticuloViewHolder>() {

    class ArticuloViewHolder(val binding: ArticuloCategoriaBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticuloViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ArticuloCategoriaBinding.inflate(inflater, parent, false)
        return ArticuloViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticuloViewHolder, position: Int) {
        val articulo = articulos[position]

        holder.binding.apply {
            tvNombreArticulo.text = articulo.nombre
            tvDescripcionArticulo.text = articulo.descripcion
            tvPrecioArticulo.text = "${articulo.precio} Bs."
            ivArticulo.setImageResource(articulo.imagenResId)
        }
    }

    override fun getItemCount(): Int = articulos.size
}
