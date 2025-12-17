package com.prograIII.kofi.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete
import androidx.room.Update
import androidx.room.Transaction


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

    // 1. Elimina solo la cabecera (la orden)
    @Delete
    fun eliminarOrden(orden: OrdenEntity)

    // 2. Elimina todos los detalles que pertenezcan a esa orden
    @Query("DELETE FROM tabla_detalles WHERE ordenId = :ordenId")
    fun eliminarDetallesPorOrdenId(ordenId: Int)

    // 3. TRANSACCIÓN MAESTRA: Ejecuta ambos pasos automáticamente
    // Usa ESTE método desde tu Activity
    @Transaction
    fun eliminarOrdenCompleta(orden: OrdenEntity) {
        eliminarDetallesPorOrdenId(orden.id) // Primero borra los hijos
        eliminarOrden(orden)                 // Luego borra el padre
    }
}