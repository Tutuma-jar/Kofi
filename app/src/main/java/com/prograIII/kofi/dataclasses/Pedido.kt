package com.prograIII.kofi.dataclasses

import kotlinx.serialization.Serializable

@Serializable
data class Pedido(
    val nombre: String,
    val imagen: String,
    val precio: Double
)

@Serializable
data class Orden(
    val id: Int = 0,
    val cliente: String,
    val totalItems: Int,
    val totalMonto: Double,
    var listo: Boolean
)
