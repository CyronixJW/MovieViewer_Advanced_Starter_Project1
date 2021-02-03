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
import kotlinx.android.synthetic.main.activity_favourites.*
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
        var x = favMovieDO()

        activityCoroutineScope.launch()
        {
            try {
                var credentials: AWSCredentials = AWSMobileClient.getInstance().awsCredentials
                var dynamoDBClient = AmazonDynamoDBClient(credentials)
                dynamoDBMapper = DynamoDBMapper.builder()
                        .dynamoDBClient(dynamoDBClient)
                        .awsConfiguration(
                                AWSMobileClient.getInstance().configuration
                        )
                        .build()


                var eav = HashMap<String, AttributeValue>()
                eav.put(":val1", AttributeValue().withS(AWSMobileClient.getInstance().username))
                var queryExpression =
                        DynamoDBScanExpression().withFilterExpression("id = :val1")
                                .withExpressionAttributeValues(eav)

                var itemList = dynamoDBMapper?.scan(favMovieDO::class.java, queryExpression)

                val viewItem = favMovieDO()
                if (itemList != null && itemList?.size != 0) {
                    for (i in itemList!!.iterator()) {
                        currentMovieDO = i

                        viewItem.id = i.id.toString()
                        viewItem.favMovie = i.favMovie
                        viewItem.itemname = i.itemname
                        x.id = i.id.toString()


                    }



                    for (movie in currentMovieDO!!.favMovie!!.iterator()) {


                        Log.i("movie", movie.overview.toString())

                    }

                    x.itemname = currentMovieDO?.itemname

                    x.favMovie = currentMovieDO?.favMovie
                    // Stuff that updates the UI



                } else {
                    Log.i("currentMovie", "test fail")
                    currentMovieDO = favMovieDO()
                    currentMovieDO?.apply {
                        id = AWSMobileClient.getInstance().username
                        favMovie = mutableListOf<favMovieDO.MovieItem>()
                        itemname = favMovieDO.MovieItem()

                    }
                    Log.i("currentMovie", currentMovieDO?.id.toString())
                    x.id = ""
                    x.id = currentMovieDO?.id.toString()
                    Log.i("currentMovie", currentMovieDO?.itemname?.title.toString())
                    currentMovieDO?.favMovie?.map {
                        Log.i("currentMovie", it.overview.toString())
                    }
                    x.favMovie = currentMovieDO?.favMovie
                    x.itemname = currentMovieDO?.itemname



                    // Stuff that updates the UI

                }


            } catch (ex: java.lang.Exception) {
                Log.d("DynamoDBLab", "Exception ${ex.message}")
            }










            movie_array = intent.getSerializableExtra("movie_array") as ArrayList<MovieItem>
            var add_movie_item = favMovieDO.MovieItem()
            movie_array.map {
                add_movie_item?.adult = it.adult
                add_movie_item?.backdrop_path = it.backdrop_path
                add_movie_item?.genre_ids = it.genre_ids
                add_movie_item?.id = it.id.toString()
                add_movie_item?.original_language = it.original_langauge
                add_movie_item?.original_title = it.original_title
                add_movie_item?.overview = it.overview
                add_movie_item?.popularity = it.popularity
                add_movie_item?.poster_path = it.poster_path
                add_movie_item?.title = it.title
                add_movie_item?.release_date = it.release_date
                add_movie_item?.video = it.video
                add_movie_item?.vote_average = it.vote_average
                add_movie_item?.vote_count = it.vote_count


            }
            var list_item_id = ArrayList<String>()
            currentMovieDO?.id = x?.id
            currentMovieDO?.itemname = x?.itemname
            currentMovieDO?.favMovie = x?.favMovie
            currentMovieDO?.favMovie?.map {

                list_item_id.add(it.id)

            }

            if(list_item_id.contains(add_movie_item.id))
            {
                Log.i("MovieItem","Movie exists in faourites already!")
            }
            else
            {
                currentMovieDO.favMovie?.add(add_movie_item)
            }




            currentMovieDO?.id = AWSMobileClient.getInstance().username
            Log.i("MovieDO", currentMovieDO?.id.toString())


            currentMovieDO?.favMovie!!.map {
                Log.i("DynamoSave", it.overview.toString())
            }
            Log.i("DynamoSave", currentMovieDO.itemname!!.title.toString())



            activityCoroutineScope?.launch()
            {
                Log.i("DynamoSave", "Movie Saved")

                dynamoDBMapper?.save(currentMovieDO)


            }


        }
    }


}
