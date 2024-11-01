package com.example.prak_5

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class DatabaseActivity : AppCompatActivity() {
    lateinit var filmRV: RecyclerView
    lateinit var filmAdapter: FilmAdapter
    @Inject
    lateinit var db: AppDatabase
    lateinit var filmList: ArrayList<Film>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_database)

        filmRV = findViewById(R.id.idRVFilms)
//        db = AppDatabase.getDatabase(this)

        filmList = ArrayList()
        loadFilmsFromDatabase()
    }

    private fun loadFilmsFromDatabase() {
        lifecycleScope.launch(Dispatchers.IO) {
            val filmsFromDb = db.filmDao().getAllFilms()
            filmList.addAll(filmsFromDb)

            withContext(Dispatchers.Main) {
                filmAdapter = FilmAdapter(filmList)
                filmRV.adapter = filmAdapter
            }
        }
    }
}
