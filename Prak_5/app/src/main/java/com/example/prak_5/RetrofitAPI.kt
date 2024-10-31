package com.example.prak_5

import retrofit2.Call
import retrofit2.http.GET

interface RetrofitAPI {
    @GET("W344")
    fun getAllFilms(): Call<ArrayList<Film>>
}