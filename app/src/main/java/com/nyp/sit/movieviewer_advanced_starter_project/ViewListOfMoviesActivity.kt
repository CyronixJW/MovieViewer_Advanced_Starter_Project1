package com.nyp.sit.movieviewer_advanced_starter_project

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.UserStateDetails
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedScanList
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.nyp.sit.movieviewer_advanced_starter_project.databinding.ActivityViewListOfMoviesBinding
import com.nyp.sit.movieviewer_advanced_starter_project.entity.MovieItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_item_detail.*
import kotlinx.android.synthetic.main.activity_view_list_of_movies.*
import kotlinx.coroutines.*


class ViewListOfMoviesActivity : AppCompatActivity() {

    val SHOW_BY_TOP_RATED = 1
    val SHOW_BY_POPULAR = 2
    var currentMovieDO: favMovieDO? = null
    var movie_array = favMovieDO().favMovie
    private lateinit var binding: ActivityViewListOfMoviesBinding
    var appCoroutineScope = CoroutineScope(Job() + Dispatchers.IO)
    var dynamoDBMapper : DynamoDBMapper?=null
    private var displayType = SHOW_BY_TOP_RATED

    private val moviesViewModel: MoviesViewModel by viewModels()
    {
        MoviesViewModelFactory((application as MyMovies).repo)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewListOfMoviesBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        appCoroutineScope = CoroutineScope(Job() + Dispatchers.IO)
        AWSMobileClient.getInstance().initialize(this, object : Callback<UserStateDetails> {

            override fun onResult(result: UserStateDetails?) {
                Log.d("CognitoLab", result?.userState?.name.toString())


            }

            override fun onError(e: java.lang.Exception?) {
                Log.d("CognitoLab", "There is an error - ${e.toString()}")
            }

        })


    }


    override fun onStart() {
        super.onStart()

        loadMovieData(displayType)
    }


    fun loadMovieData(viewType: Int) {
        moviesViewModel.deleteAll()

        var movielistAdapter :MovieListAdapter ?= null
        val arr_movie_title = ArrayList<MovieItem>()
        var arr_movie_image = ArrayList<String>()

        var showTypeStr: String? = null
        displayType = viewType

        when (viewType) {
            SHOW_BY_TOP_RATED -> showTypeStr = NetworkUtils.TOP_RATED_PARAM
            SHOW_BY_POPULAR -> showTypeStr = NetworkUtils.POPULAR_PARAM

        }

        if (showTypeStr != null) {

            var movieJob = CoroutineScope(Job() + Dispatchers.IO).async {

                var APIKEY = resources.getString(R.string.moviedb_api_key)

                var movie_url = NetworkUtils.buildUrl(showTypeStr, APIKEY)
                try {

                    val jsonMovieResponse = movie_url?.let { NetworkUtils.getResponseFromHttpUrl(it) }
                    var responseList = movieDBJsonUtils.getMovieDetailsFromJson(this@ViewListOfMoviesActivity, jsonMovieResponse!!)

                    responseList
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }
            }

            GlobalScope.launch(Dispatchers.Main)
            {
                val MovieData = movieJob.await()





                        withContext(Dispatchers.Main)
                        {

                            if (MovieData != null) {

                                MovieData.map {


                                    moviesViewModel.insert(it)
                                }
                            }
                            moviesViewModel.allMovies.observe(this@ViewListOfMoviesActivity, Observer {
                                var moviesConvertList = ArrayList<MovieItem>()

                                it.map {
                                    moviesConvertList.add(it)
                                }

                                movielistAdapter = MovieListAdapter(this@ViewListOfMoviesActivity, moviesConvertList as ArrayList<MovieItem>)
                                movielist.adapter = movielistAdapter

                                movielist.onItemClickListener = object : AdapterView.OnItemClickListener {

                                    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                        val posterurl = moviesConvertList[position].poster_path
                                        val overview = moviesConvertList[position].overview
                                        val release_date = moviesConvertList[position].release_date
                                        val popularity = moviesConvertList[position].popularity
                                        val vote_count = moviesConvertList[position].vote_count
                                        val vote_average = moviesConvertList[position].vote_average
                                        val backdrop_path = moviesConvertList[position].backdrop_path
                                        val language = moviesConvertList[position].original_langauge
                                        val original_title = moviesConvertList[position].original_title
                                        val title = moviesConvertList[position].title
                                        val genre_ids = moviesConvertList[position].genre_ids
                                        val id = moviesConvertList[position].id
                                        val movie_id = moviesConvertList[position].id
                                        val adult = moviesConvertList[position].adult
                                        val video = moviesConvertList[position].video
                                        var movie_array = ArrayList<MovieItem>()
                                        movie_array.add(MovieItem(posterurl, adult, overview, release_date, genre_ids, id, original_title, language, title, backdrop_path, popularity, vote_count, video, vote_average))
                                        var myIntents = Intent(this@ViewListOfMoviesActivity, ItemDetailActivity::class.java)
                                        myIntents.putExtra("movie_array", movie_array)
                                        startActivity(myIntents)


                                    }
                                }


                            })



                            }



                        }





                }


            }
















    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {

            R.id.sortPopular -> {

                loadMovieData(SHOW_BY_POPULAR)
                moviesViewModel.deleteAll()
            }
            R.id.sortTopRated -> {
                loadMovieData(SHOW_BY_TOP_RATED)
                moviesViewModel.deleteAll()
            }
            R.id.viewFav -> {
                var myIntent = Intent(this,Favourites::class.java)
                startActivity(myIntent)
            }

        }
        return super.onOptionsItemSelected(item)
    }

}
class MovieListAdapter(context: Context, data: ArrayList<MovieItem>): BaseAdapter()
{
    val sList = ArrayList<MovieItem>()
    val mInflator : LayoutInflater
    init {

        this.mInflator = LayoutInflater.from(context)
        sList.addAll(data)

    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val v: View
        v = this.mInflator.inflate(R.layout.row_listview, parent, false)
        val label: TextView = v.findViewById(R.id.label)
        val iv: ImageView = v.findViewById(R.id.image)
        var title_str = sList.get(position).title.toString()
        label.text = title_str

        var imgstr = sList.get(position).poster_path.toString()
        var network_imgurl = NetworkUtils.buildImageUrl(imgstr)
        Picasso.get().load(network_imgurl.toString()).into(iv)


        return  v
    }

    override fun getCount(): Int {
        return if(sList == null) 0 else sList?.size



    }

    override fun getItem(position: Int): Any {
        return sList?.get(position)
    }

    override fun getItemId(position: Int): Long {
        return 0
    }


}
