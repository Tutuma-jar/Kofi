package com.prograIII.kofi

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.prograIII.kofi.databinding.ArticuloFinalizarordenBinding

class ArticuloAdapter(
    private val articulos: List<Articulo>
) : RecyclerView.Adapter<ArticuloAdapter.ArticuloViewHolder>() {

    inner class ArticuloViewHolder(val binding: ArticuloFinalizarordenBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticuloViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ArticuloFinalizarordenBinding.inflate(inflater, parent, false)
        return ArticuloViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticuloViewHolder, position: Int) {
        val articulo = articulos[position]

        with(holder.binding) {
            tvNombreArticulo.text = articulo.nombre
            tvPrecioArticulo.text = "${articulo.precio} Bs."
            ivImagenArticulo.setImageResource(articulo.imagenResId)
        }
    }

    override fun getItemCount(): Int = articulos.size
}
