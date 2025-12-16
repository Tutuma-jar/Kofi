package com.prograIII.kofi.dataclasses

data class Producto(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val categoriaId: Int,
    val imagen: String,   //URI o nombre drawable
    val imagenRes: Int = 0
)
