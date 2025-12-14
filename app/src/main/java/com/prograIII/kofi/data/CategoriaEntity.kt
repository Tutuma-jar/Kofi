package com.prograIII.kofi.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categorias")
data class CategoriaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val codigo: String,
    val nombre: String
)
