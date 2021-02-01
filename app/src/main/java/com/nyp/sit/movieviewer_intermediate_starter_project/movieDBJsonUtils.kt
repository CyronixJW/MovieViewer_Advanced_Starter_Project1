package com.nyp.sit.movieviewer_intermediate_starter_project

import android.content.Context
import com.nyp.sit.movieviewer_intermediate_starter_project.entity.MovieItem
import org.json.JSONArray

import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

class movieDBJsonUtils() {


    companion object {


        @Throws(JSONException::class)
        fun getMovieDetailsFromJson(context: Context, movieDetailsJsonStr: String): ArrayList<MovieItem>? {
            val Movie_poster_path = "poster_path"
            val Movie_adult = "adult"
            val  Movie_overview= "overview"
            val  Movie_release_date= "release_date"
            val Movie_genre_ids = "genre_ids"
            val Movie_id = "id"
            val Movie_original_title = "original_title"
            val Movie_original_language = "original_language"
            val Movie_title = "title"
            val Movie_backdrop_path = "backdrop_path"
            val Movie_popularity = "popularity"
            val Movie_vote_count = "vote_count"
            val Movie_video = "video"
            val Movie_vote_average = "vote_average"
            val parsedMovieData = ArrayList<MovieItem>()

            val MovieJSON = JSONObject(movieDetailsJsonStr)
            val Movie_array = MovieJSON.getJSONArray("results")

            for(i in 0 until Movie_array.length())
            {
                val movie_json = Movie_array.getJSONObject(i)

                val id : Int
                val poster_path: String
                val adult: Boolean
                val overview: String
                val release_date: String
                val genre_ids: JSONArray
                val original_title: String
                val original_language: String
                val backdrop_path: String
                val popularity: Double
                val vote_count: Int
                val video: Boolean
                val vote_average: Double
                val title: String
                var str_genre_ids: String
                poster_path = movie_json.getString(Movie_poster_path).toString()
                adult = movie_json.getBoolean(Movie_adult)
                overview = movie_json.getString(Movie_overview).toString()
                release_date = movie_json.getString(Movie_release_date).toString()
                genre_ids = movie_json.getJSONArray(Movie_genre_ids)
                original_title = movie_json.getString(Movie_original_title).toString()
                original_language = movie_json.getString(Movie_original_language).toString()
                backdrop_path = movie_json.getString(Movie_backdrop_path).toString()
                popularity = movie_json.getDouble(Movie_popularity)
                vote_count = movie_json.getInt(Movie_vote_count)
                video = movie_json.getBoolean(Movie_video)
                vote_average = movie_json.getDouble(Movie_vote_average)
                title = movie_json.getString(Movie_title).toString()
                id = movie_json.getInt(Movie_id)


                str_genre_ids = "["
                var genre_list = ArrayList<String>()
                for (i in 0 until genre_ids.length()) {
                    val item = genre_ids.getInt(i)
                    if (i < genre_ids.length() - 1) {
                        str_genre_ids = str_genre_ids + item.toString() + ","
                    }
                    else
                    {
                        str_genre_ids = str_genre_ids + item.toString()
                    }

                    // Your code here
                }
                str_genre_ids = str_genre_ids+"]"




                parsedMovieData.add(MovieItem(poster_path,adult,overview,release_date, str_genre_ids,id,original_title,original_language, title,backdrop_path,popularity,vote_count, video,vote_average))

            }





            return parsedMovieData
        }



    }

}