package com.prograIII.kofi.dataclasses

data class Pedido(
    val nombre: String,
    val precio: Double
)

data class ordenes(
    val id: Int,
    val cliente: String,
    val totalItems: Int,
    val totalMonto: Double,
    var listo: Boolean
)
