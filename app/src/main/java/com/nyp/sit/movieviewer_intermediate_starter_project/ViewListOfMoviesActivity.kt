package com.nyp.sit.movieviewer_intermediate_starter_project

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
import com.nyp.sit.movieviewer_intermediate_starter_project.entity.MovieItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_item_detail.*
import kotlinx.android.synthetic.main.activity_view_list_of_movies.*
import kotlinx.coroutines.*

class ViewListOfMoviesActivity : AppCompatActivity() {
    private val STATE_ID = "id"
    private val STATE_ITEMS = "items"
    private val mId: Long = 0

    val SHOW_BY_TOP_RATED = 1
    val SHOW_BY_POPULAR = 2


    private var displayType = SHOW_BY_TOP_RATED
    var moviesConvertList = ArrayList<MovieItem>()
    private val moviesViewModel: MoviesViewModel by viewModels()
    {
        MoviesViewModelFactory((application as MyMovies).repo)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_list_of_movies)



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
                                        val movie_id = moviesConvertList[position].id
                                        val adult = moviesConvertList[position].adult
                                        val video = moviesConvertList[position].video
                                        var movie_array = ArrayList<MovieItem>()
                                        movie_array.add(MovieItem(posterurl, adult, overview, release_date, genre_ids, movie_id, original_title, language, title, backdrop_path, popularity, vote_count, video, vote_average))
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
            }
            R.id.sortTopRated -> {
                loadMovieData(SHOW_BY_TOP_RATED)
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
        Log.i("test", sList[position].title.toString())
        var imgstr = sList.get(position).poster_path.toString()
        var network_imgurl = NetworkUtils.buildImageUrl(imgstr)
        Picasso.get().load(network_imgurl.toString()).into(iv)
        Log.i("test2", imgstr)

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
