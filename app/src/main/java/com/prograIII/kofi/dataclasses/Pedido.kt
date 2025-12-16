package com.prograIII.kofi.dataclasses


data class Pedido(
    val ordenId: Int,
    val imagenProducto: String,
    val nombreProducto: String,
    val precio: Double,
    var cantidad:Int
)
