package com.prograIII.kofi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.prograIII.kofi.Articulo
import com.prograIII.kofi.R

class ArticuloAdapter(
    private val articulos: List<Articulo>
) : RecyclerView.Adapter<ArticuloAdapter.ArticuloViewHolder>() {

    inner class ArticuloViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Columna 1
        val imagen = itemView.findViewById<ImageView>(R.id.ivImagenArticulo)
        // Columna 2
        val nombre = itemView.findViewById<TextView>(R.id.tvNombreArticulo)
        // Columna 3
        val precio = itemView.findViewById<TextView>(R.id.tvPrecioArticulo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticuloViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.articulo_finalizarorden, parent, false)
        return ArticuloViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticuloViewHolder, position: Int) {
        val articulo = articulos[position]

        holder.nombre.text = articulo.nombre
        holder.precio.text = "${articulo.precio} Bs."
        holder.imagen.setImageResource(articulo.imagenResId)
    }

    override fun getItemCount(): Int = articulos.size
}

