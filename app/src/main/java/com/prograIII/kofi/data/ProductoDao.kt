package com.prograIII.kofi.data

import androidx.room.*

@Dao
interface ProductoDao {

    @Insert
    fun insertarProducto(producto: ProductoEntity): Long

    @Update
    fun actualizarProducto(producto: ProductoEntity): Int

    @Query("SELECT * FROM productos WHERE id = :id LIMIT 1")
    fun obtenerPorId(id: Int): ProductoEntity?

    @Query("SELECT * FROM productos")
    fun obtenerTodos(): List<ProductoEntity>

    @Query("SELECT * FROM productos WHERE categoriaId = :categoriaId")
    fun obtenerPorCategoria(categoriaId: Int): List<ProductoEntity>

    @Query("SELECT COUNT(*) FROM productos")
    fun contarProductos(): Int
}
