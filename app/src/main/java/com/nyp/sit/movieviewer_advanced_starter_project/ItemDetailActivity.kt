package com.nyp.sit.movieviewer_advanced_starter_project

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nyp.sit.movieviewer_advanced_starter_project.entity.MovieItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_item_detail.*

class ItemDetailActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)


        val myIntents = Intent()
        var movie_array: ArrayList<MovieItem>
        movie_array= intent.getSerializableExtra("movie_array") as ArrayList<MovieItem>
        movie_array.map {
            movie_overview.text = it.overview.toString()
            movie_release_date.text = it.release_date.toString()
            movie_hasvideo.text = it.video.toString()
            movie_is_adult.text = it.adult.toString()
            movie_langauge.text = it.original_langauge.toString()
            movie_popularity.text = it.popularity.toString()
            movie_vote_avg.text = it.vote_average.toString()
            movie_vote_count.text = it.vote_count.toString()
            var imgstr = it.poster_path.toString()
            var network_imgurl = NetworkUtils.buildImageUrl(imgstr)
            Picasso.get().load(network_imgurl.toString()).into(posterIV)


        }




    }



}
