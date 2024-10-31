package com.example.prak_5

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    lateinit var filmRV: RecyclerView
    lateinit var loadingPB: ProgressBar
    lateinit var filmAdapter: FilmAdapter
    lateinit var filmList: ArrayList<Film>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        filmRV = findViewById(R.id.idRVFilms)
        loadingPB = findViewById(R.id.idPBLoading)

        filmList = ArrayList()

        getAllFilms()
    }

    private fun getAllFilms() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://jsonkeeper.com/b/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val retrofitAPI = retrofit.create(RetrofitAPI::class.java)

        val call: Call<ArrayList<Film>> = retrofitAPI.getAllFilms()

        call.enqueue(object : Callback<ArrayList<Film>> {
            override fun onResponse(
                call: Call<ArrayList<Film>>,
                response: Response<ArrayList<Film>>
            ) {
                if (response.isSuccessful) {
                    loadingPB.visibility = View.GONE
                    filmList = response.body()!!
                }

                filmAdapter = FilmAdapter(filmList)

                filmRV.adapter = filmAdapter
            }

            override fun onFailure(call: Call<ArrayList<Film>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Fail to get the data..", Toast.LENGTH_SHORT)
                    .show()
            }
        })

    }
}