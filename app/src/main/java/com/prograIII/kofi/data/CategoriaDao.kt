package com.prograIII.kofi.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CategoriaDao {

    @Insert
    fun insertarCategoria(categoria: CategoriaEntity): Long

    @Query("SELECT * FROM categorias")
    fun obtenerCategorias(): List<CategoriaEntity>

    @Query("SELECT COUNT(*) FROM categorias")
    fun contarCategorias(): Int

    @Query("SELECT * FROM categorias WHERE codigo = :codigo LIMIT 1")
    fun obtenerCategoriaPorCodigo(codigo: String): CategoriaEntity?
}
