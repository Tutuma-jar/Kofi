package com.prograIII.kofi.dataclasses

data class Pedido(
    val id: Int,
    val nombreCliente: String,
    val productos: List<Producto>,
    val totalItems: Int,
    val totalMonto: Double,
    var listo: Boolean
)
