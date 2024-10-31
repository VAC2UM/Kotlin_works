package com.example.prak_5

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

// Определяет класс, который содержит методы преобразования.
// Эти методы будут использоваться Room для преобразования данных, которые не могут быть напрямую сохранены в базе данных.
class Converters {
    // Аннотация, указывающая, что этот метод будет использоваться для преобразования типа данных, который Room не может сохранить непосредственно.
    @TypeConverter
    fun fromStringList(value: List<String>?): String {
        // Создает новый экземпляр Gson, который будет использоваться для сериализации.
        val gson = Gson()
        // Определяет тип для списка строк с использованием TypeToken. Это позволяет Gson корректно обрабатывать коллекцию.
        val type: Type = object : TypeToken<List<String>>() {}.type
        // Сериализует список строк в JSON-формат и возвращает его как строку.
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toStringList(value: String): List<String>? {
        val gson = Gson()
        val type: Type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, type)
    }
}
