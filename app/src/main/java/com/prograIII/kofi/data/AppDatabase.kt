package com.prograIII.kofi.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        CategoriaEntity::class,
        ProductoEntity::class,
        OrdenEntity::class,
        DetalleOrdenEntity::class
    ],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoriaDao(): CategoriaDao
    abstract fun productoDao(): ProductoDao
    abstract fun ordenDao(): OrdenDao
}
