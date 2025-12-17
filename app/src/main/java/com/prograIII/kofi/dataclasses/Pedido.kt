package com.prograIII.kofi.dataclasses

data class Pedido(
    val id: Int,
    val cliente: String,
    val nit: Int,
    val comentario: String,
    val totalItems: Int,
    val totalMonto: Double,
    var listo: Boolean
)
