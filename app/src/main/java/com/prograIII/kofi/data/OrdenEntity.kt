package com.prograIII.kofi.data

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "tabla_ordenes")
data class OrdenEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val cliente: String,
    val nit: Int,
    val comentario: String,
    val totalItems: Int,
    val totalMonto: Double,
    var listo: Boolean
)

@Entity(tableName = "tabla_detalles")
data class DetalleOrdenEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val ordenId: Int,
    val imagenProducto: String,
    val nombreProducto: String,
    val precio: Double,
    var cantidad:Int = 1
)
