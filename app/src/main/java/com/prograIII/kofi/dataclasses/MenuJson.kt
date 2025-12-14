package com.prograIII.kofi.dataclasses

import kotlinx.serialization.Serializable

@Serializable
data class MenuJson(
    val categorias: List<CategoriaJson>
)

@Serializable
data class CategoriaJson(
    val codigo: String,
    val nombre: String,
    val productos: List<ProductoJson>
)

@Serializable
data class ProductoJson(
    val nombre: String,
    val descripcion: String,
    val precio: Double
)
