package com.nyp.sit.movieviewer_advanced_starter_project

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.nyp.sit.movieviewer_advanced_starter_project.databinding.ActivityFavouritesBinding
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_favourites.*
import kotlinx.android.synthetic.main.activity_view_list_of_movies.*
import kotlinx.coroutines.*

class Favourites : AppCompatActivity() {
    private lateinit var binding: ActivityFavouritesBinding

    var dynamoDBMapper: DynamoDBMapper? = null
    var currentMovieDO: favMovieDO? = null

    var movie_array = favMovieDO().favMovie
    var activityCoroutineScope = CoroutineScope(Job() + Dispatchers.IO)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavouritesBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
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

            } catch (ex: Exception) {
                Log.d("DynamoDBLab", "Exception ${ex.message}")
            }

        }
        loadMovie()


    }
    fun loadMovie() {

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
                if(itemList != null && itemList?.size !=0 ) {
                    for (i in itemList!!.iterator()) {
                        currentMovieDO = i

                        viewItem.id = i.id.toString()
                        viewItem.favMovie = i.favMovie
                        viewItem.itemname = i.itemname


                    }



                    for (movie in currentMovieDO!!.favMovie!!.iterator()) {


                        Log.i("movie", movie.overview.toString())

                    }
                    runOnUiThread {
                        // Stuff that updates the UI
                        var x = favMovieDO()
                        x.favMovie = currentMovieDO?.favMovie

                        var movielistAdapter = MovieListAdapterDetails(this@Favourites, x.favMovie as ArrayList<favMovieDO.MovieItem>)
                        lv_favourite.adapter = movielistAdapter
                    }





                }

                else {
                    Log.i("currentMovie", "test fail")
                    currentMovieDO = favMovieDO()
                    currentMovieDO?.apply {
                        id = AWSMobileClient.getInstance().username
                        favMovie = mutableListOf<favMovieDO.MovieItem>()
                        itemname = favMovieDO.MovieItem()

                    }
                    Log.i("currentMovie", currentMovieDO?.id.toString())
                    Log.i("currentMovie", currentMovieDO?.itemname?.title.toString())
                    currentMovieDO?.favMovie?.map {
                        Log.i("currentMovie", it.overview.toString())
                    }




                    // Stuff that updates the UI

                }


            } catch (ex: java.lang.Exception) {
                Log.d("DynamoDBLab", "Exception ${ex.message}")
            }




        }
    }
}





class MovieListAdapterDetails(context: Context, data: ArrayList<favMovieDO.MovieItem>): BaseAdapter()
{
    var sList = data
    val mInflator : LayoutInflater
    init {

        this.mInflator = LayoutInflater.from(context)




    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val v: View
        v = this.mInflator.inflate(R.layout.row_listview, parent, false)
        val label: TextView = v.findViewById(R.id.label)
        val iv: ImageView = v.findViewById(R.id.image)
        var title_str = sList?.get(position)?.title.toString()
        label.text = title_str
        Log.i("test", sList?.get(position)?.title.toString())
        var imgstr = sList?.get(position)?.poster_path.toString()
        var network_imgurl = NetworkUtils.buildImageUrl(imgstr)
        Picasso.get().load(network_imgurl.toString()).into(iv)
        Log.i("test2", imgstr)

        return  v
    }

    override fun getCount(): Int {
        return if(sList == null) 0 else sList?.size!!



    }

    override fun getItem(position: Int): favMovieDO.MovieItem? {
        return sList?.get(position)
    }

    override fun getItemId(position: Int): Long {
        return 0
    }


}