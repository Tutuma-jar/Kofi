package com.prograIII.kofi.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProductoDao {

    @Insert
    fun insertarProducto(producto: ProductoEntity): Long

    @Query("SELECT * FROM productos")
    fun obtenerTodos(): List<ProductoEntity>

    @Query("SELECT * FROM productos WHERE categoriaId = :categoriaId")
    fun obtenerPorCategoria(categoriaId: Int): List<ProductoEntity>

    @Query("SELECT COUNT(*) FROM productos")
    fun contarProductos(): Int
}
