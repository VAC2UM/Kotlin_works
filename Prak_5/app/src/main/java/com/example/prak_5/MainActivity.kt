package com.example.prak_5

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
    lateinit var db: AppDatabase
    lateinit var buttonNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        filmRV = findViewById(R.id.idRVFilms)
        loadingPB = findViewById(R.id.idPBLoading)
        buttonNext = findViewById(R.id.buttonNext)
        db = AppDatabase.getDatabase(this)

        filmList = ArrayList()

        clearDatabase()
        getAllFilms()

        buttonNext.setOnClickListener {
            val intent = Intent(this, DatabaseActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getAllFilms() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://dummyjson.com/c/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val retrofitAPI = retrofit.create(RetrofitAPI::class.java)

        val call: Call<ArrayList<Film>> = retrofitAPI.getAllFilms()

        // Отправка асинхронного запроса к API.
        call.enqueue(object : Callback<ArrayList<Film>> {
            override fun onResponse(
                call: Call<ArrayList<Film>>,
                response: Response<ArrayList<Film>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    loadingPB.visibility = View.GONE
                    filmList = response.body()!!

                    lifecycleScope.launch(Dispatchers.IO) {
                        db.filmDao().insertFilms(filmList)
                    }

                    filmAdapter = FilmAdapter(filmList)
                    filmRV.adapter = filmAdapter
                } else {
                    Toast.makeText(this@MainActivity, "No data found", Toast.LENGTH_SHORT).show()
                }
            }


            override fun onFailure(call: Call<ArrayList<Film>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Fail to get the data..", Toast.LENGTH_SHORT)
                    .show()
                t.printStackTrace()
            }
        })

    }

    private fun clearDatabase() {
        lifecycleScope.launch(Dispatchers.IO) {
            db.filmDao().deleteAllFilms() // Удаляем все фильмы
        }
    }
}