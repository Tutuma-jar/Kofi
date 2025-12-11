package com.prograIII.kofi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ArticulosCategoriaAdapter(
    private val articulos: List<Articulo>
) : RecyclerView.Adapter<ArticulosCategoriaAdapter.ArticuloViewHolder>() {

    class ArticuloViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombre = itemView.findViewById<TextView>(R.id.tvNombreArticulo)
        val tvDescripcion = itemView.findViewById<TextView>(R.id.tvDescripcionArticulo)
        val tvPrecio = itemView.findViewById<TextView>(R.id.tvPrecioArticulo)
        val ivImagen = itemView.findViewById<ImageView>(R.id.ivImagenArticulo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticuloViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.articulo_categoria, parent, false)
        return ArticuloViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticuloViewHolder, position: Int) {
        val articulo = articulos[position]

        holder.tvNombre.text = articulo.nombre
        holder.tvDescripcion.text = articulo.descripcion
        holder.tvPrecio.text = "${articulo.precio} Bs."
        holder.ivImagen.setImageResource(articulo.imagenResId)
    }

    override fun getItemCount(): Int = articulos.size
}

