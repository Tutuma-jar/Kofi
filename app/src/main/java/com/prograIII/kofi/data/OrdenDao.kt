package com.prograIII.kofi.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete
import androidx.room.Update

@Dao
interface OrdenDao {
    @Insert
    fun insertarOrden(orden: OrdenEntity): Long

    @Query("SELECT * FROM tabla_ordenes WHERE id = :id")
    fun obtenerOrdenPorId(id: Int): OrdenEntity

    @Insert
    fun insertarDetalles(detalles: List<DetalleOrdenEntity>)

    @Query("SELECT * FROM tabla_ordenes ORDER BY id DESC")
    fun obtenerTodasLasOrdenes(): List<OrdenEntity>

    // --- NUEVO: FILTRO POR ESTADO (SQL) ---
    @Query("SELECT * FROM tabla_ordenes WHERE listo = :estaListo ORDER BY id DESC")
    fun obtenerOrdenesPorEstado(estaListo: Boolean): List<OrdenEntity>
    // --------------------------------------

    @Query("SELECT * FROM tabla_detalles WHERE ordenId = :ordenId")
    fun obtenerDetallesDeOrden(ordenId: Int): List<DetalleOrdenEntity>

    @Query("SELECT * FROM tabla_detalles WHERE id = :id LIMIT 1")
    fun obtenerDetallePorId(id: Int): DetalleOrdenEntity?

    @Delete
    fun eliminarProducto(detalle: DetalleOrdenEntity)

    @Update
    fun actualizarProducto(detalle: DetalleOrdenEntity)

    @Update
    fun actualizarOrden(orden: OrdenEntity)

    @Query("DELETE FROM tabla_detalles WHERE ordenId = :ordenId")
    fun eliminarDetallesPorOrdenId(ordenId: Int)

    @Query("DELETE FROM tabla_ordenes WHERE id = :ordenId")
    fun eliminarOrdenPorId(ordenId: Int)

    @Query("SELECT * FROM tabla_ordenes WHERE listo = :estaListo AND cliente != '' ORDER BY id DESC")
    fun obtenerOrdenesGuardadasPorEstado(estaListo: Boolean): List<OrdenEntity>
}