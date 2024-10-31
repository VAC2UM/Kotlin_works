package com.example.prak_5

import retrofit2.Call
import retrofit2.http.GET

interface RetrofitAPI {
    @GET("d286-4953-47fe-830d")
    fun getAllFilms(): Call<ArrayList<Film>>
}