package com.prograIII.kofi.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.prograIII.kofi.databinding.ItemLayoutBinding
import com.prograIII.kofi.dataclasses.Pedido

class PedidosAdapter(
    private var pedidos: List<Pedido>,
    private val onVerDetalles: (Pedido) -> Unit,
    private val onEstadoCambiado: (Pedido) -> Unit
) : RecyclerView.Adapter<PedidosAdapter.PedidoViewHolder>() {

    inner class PedidoViewHolder(val binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoViewHolder {
        val binding = ItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PedidoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PedidoViewHolder, position: Int) {
        val pedido = pedidos[position]

        with(holder.binding) {
            numeroPedido.text = "PEDIDO ${pedido.id}"
            nombrePedido.text = "Cliente: ${pedido.cliente}"
            textItems.text = "Total de Items: ${pedido.totalItems}"
            textTotal.text = "Total: %.2f Bs.".format(pedido.totalMonto)

            switchStatus.setOnCheckedChangeListener(null)
            switchStatus.isChecked = pedido.listo

            switchStatus.setOnCheckedChangeListener { _, isChecked ->
                pedido.listo = isChecked
                onEstadoCambiado(pedido)
            }

            btnVerDetalles.setOnClickListener {
                onVerDetalles(pedido)
            }
        }
    }

    override fun getItemCount(): Int = pedidos.size

    fun actualizarLista(nuevaLista: List<Pedido>) {
        pedidos = nuevaLista
        notifyDataSetChanged()
    }
}
