package com.nyp.sit.movieviewer_advanced_starter_project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.nyp.sit.movieviewer_advanced_starter_project.databinding.ActivityItemDetailBinding
import com.nyp.sit.movieviewer_advanced_starter_project.databinding.ActivityLoginBinding
import com.nyp.sit.movieviewer_advanced_starter_project.entity.MovieItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_item_detail.*
import kotlinx.coroutines.*
import java.lang.Exception

class ItemDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityItemDetailBinding

    var activityCoroutineScope = CoroutineScope(Job() + Dispatchers.IO)
    var currentMovieDO = favMovieDO()
    var dynamoDBMapper : DynamoDBMapper?=null
    var movie_array =  ArrayList<MovieItem>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        val myIntents = Intent()

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
            posterIV.setTag(imgstr)

        }
        var activityCoroutineScope = CoroutineScope(Job() + Dispatchers.IO)
        activityCoroutineScope.launch()
        {
            try {
                var credentials: AWSCredentials = AWSMobileClient.getInstance().awsCredentials
                var dynamoDBClient= AmazonDynamoDBClient(credentials)
                dynamoDBMapper = DynamoDBMapper.builder()
                        .dynamoDBClient(dynamoDBClient)
                        .awsConfiguration(
                                AWSMobileClient.getInstance().configuration
                        )
                        .build()
            } catch (ex: Exception) {
                Log.d("DynamoDBLab", "Exception ${ex.message}")
            }
        }





    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.details, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {

            R.id.addtofav -> {


            addMovie()



            }

        }
        return super.onOptionsItemSelected(item)
    }
    fun addMovie()
    {

        movie_array= intent.getSerializableExtra("movie_array") as ArrayList<MovieItem>
        var newMovie = favMovieDO.MovieItem()
        var x = mutableListOf<favMovieDO.MovieItem>()
        movie_array.map {
            newMovie?.adult = it.adult
            newMovie?.backdrop_path = it.backdrop_path
            newMovie?.genre_ids = it.genre_ids
            newMovie?.id = it.id.toString()
            newMovie?.original_language = it.original_langauge
            newMovie?.original_title = it.original_title
            newMovie?.overview = it.overview
            newMovie?.popularity = it.popularity
            newMovie?.poster_path = it.poster_path
            newMovie?.title = it.title
            newMovie?.release_date = it.release_date
            newMovie?.video = it.video
            newMovie?.vote_average = it.vote_average
            newMovie?.vote_count = it.vote_count




        }


        currentMovieDO?.itemname = newMovie
        x.add(currentMovieDO.itemname!!)
        currentMovieDO?.favMovie = x




        currentMovieDO?.id = AWSMobileClient.getInstance().username
        Log.i("MovieDO", currentMovieDO?.id.toString())


        currentMovieDO?.favMovie!!.map {
            Log.i("DynamoSave", it.overview.toString())
        }
        Log.i("DynamoSave", currentMovieDO.itemname!!.title.toString())



        activityCoroutineScope?.launch()
        {
            Log.i("DynamoSave","Movie Saved")

            dynamoDBMapper?.save(currentMovieDO)



        }



    }


}
