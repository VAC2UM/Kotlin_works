package com.example.prak_5

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Film::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun filmDao(): FilmDao

    companion object {
        // @Volatile указывает, что поле INSTANCE может изменяться в разных потоках
        @Volatile
        // Объявляние переменной INSTANCE, которая будет хранить единственный экземпляр базы данных.
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // Проверка, существует ли уже экземпляр базы данных.
            // Если нет, используется синхронизация, чтобы избежать создания нескольких экземпляров базы данных в многопоточной среде.
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "film_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
