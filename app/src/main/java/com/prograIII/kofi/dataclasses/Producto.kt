package com.prograIII.kofi.dataclasses

data class Producto(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val imagenRes: Int,
    val categoriaId: Int
)
