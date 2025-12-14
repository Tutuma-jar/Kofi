package com.prograIII.kofi

import kotlinx.serialization.Serializable

@Serializable
data class Producto(
    val id: Int, // ID único automático basado en el tiempo
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val imagenRuta: String // Cambiado a String para soportar URIs de cámara y recursos
)
