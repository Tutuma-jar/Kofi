package com.prograIII.kofi.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CategoriaEntity::class, ProductoEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoriaDao(): CategoriaDao
    abstract fun productoDao(): ProductoDao
}
