package com.prograIII.kofi.dataclasses

import kotlinx.serialization.Serializable

@Serializable
data class Pedido(
    val nombre: String,
    val precio: Double
)

@Serializable
data class ordenes(
    val id: Int,
    val cliente: String,
    val totalItems: Int,
    val totalMonto: Double,
    var listo: Boolean
)
