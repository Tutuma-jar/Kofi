package com.prograIII.kofi.dataclasses

import java.io.Serializable

data class ItemPedido(
    val productoId: Int,
    val nombre: String,
    val precio: Double,
    val imagen: String,
    var cantidad: Int
) : Serializable
