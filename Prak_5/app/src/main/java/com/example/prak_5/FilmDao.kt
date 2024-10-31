package com.example.prak_5

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

// Шаблон проектирования, который используется в приложениях для абстрагирования и инкапсуляции доступа к данным
@Dao
interface FilmDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFilms(films: List<Film>)

    @Query("SELECT * FROM film")
    fun getAllFilms(): List<Film>

    @Query("DELETE FROM film")
    fun deleteAllFilms()
}