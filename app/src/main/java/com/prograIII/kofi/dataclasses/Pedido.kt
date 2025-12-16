package com.prograIII.kofi.dataclasses

data class Pedido(
    val id: Int,
    val nombreCliente: String,
    val totalItems: Int,
    val total: Double,
    var listo: Boolean
)
