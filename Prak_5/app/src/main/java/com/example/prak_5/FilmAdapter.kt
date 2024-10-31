package com.example.prak_5

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FilmAdapter(
    private var filmList: ArrayList<Film>
) : RecyclerView.Adapter<FilmAdapter.FilmViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FilmAdapter.FilmViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.film_item,
            parent, false
        )
        return FilmViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: FilmAdapter.FilmViewHolder, position: Int) {
        holder.filmTitle.text = filmList.get(position).title
        holder.filmDirector.text = filmList.get(position).director
        holder.filmYear.text = filmList.get(position).year.toString()
        holder.filmGenre.text = filmList.get(position).genre
        holder.filmRating.text = filmList.get(position).rating.toString()
    }

    override fun getItemCount(): Int {
        return filmList.size
    }

    class FilmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val filmTitle: TextView = itemView.findViewById(R.id.idTitle)
        val filmDirector: TextView = itemView.findViewById(R.id.idDirector)
        val filmYear: TextView = itemView.findViewById(R.id.idReleaseYear)
        val filmGenre: TextView = itemView.findViewById(R.id.idGenre)
        val filmRating: TextView = itemView.findViewById(R.id.idRating)
    }
}