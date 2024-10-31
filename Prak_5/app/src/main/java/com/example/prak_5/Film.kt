package com.example.prak_5

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Film(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val director: String,
    @SerializedName("release_year") val year: Int,
    val genre: String,
    val rating: Double
)
