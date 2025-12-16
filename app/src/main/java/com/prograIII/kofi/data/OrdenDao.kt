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

    @Query("SELECT * FROM tabla_detalles WHERE ordenId = :ordenId")
    fun obtenerDetallesDeOrden(ordenId: Int): List<DetalleOrdenEntity>

    @Query("SELECT * FROM tabla_detalles WHERE id = :id LIMIT 1")
    fun obtenerDetallePorId(id: Int): DetalleOrdenEntity?
    //ELIMINAR
    @Delete
    fun eliminarProducto(detalle: DetalleOrdenEntity)

    //MODIFICAR
    @Update
    fun actualizarProducto(detalle: DetalleOrdenEntity)

    // 3. Opcional: Para actualizar el total de la cabecera de la orden
    @Update
    fun actualizarOrden(orden: OrdenEntity)
}