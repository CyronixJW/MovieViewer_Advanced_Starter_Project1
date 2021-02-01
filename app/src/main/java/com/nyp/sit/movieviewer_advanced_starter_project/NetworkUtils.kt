package com.nyp.sit.movieviewer_advanced_starter_project

import android.net.Uri
import android.util.Log
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*




class NetworkUtils{

    companion object {

        private val TAG = NetworkUtils::class.java.simpleName
        private val MOVIE_DB_BASE_URL: String = "https://api.themoviedb.org/3/movie"

        private val MOVIE_DB_IMAGE_BASE_URL: String = "https://image.tmdb.org/t/p";


        private val format = "json"


         val API_KEY_PARAM = "api_key"
         val POPULAR_PARAM = "popular"
         val TOP_RATED_PARAM = "top_rated"
         val IMAGE_SIZE_PARAM = "w185"




        fun buildImageUrl(imagePath: String?): URL? {

            var url: URL? = null
            if (imagePath == null) {
                return url
            }

            val builtUri = Uri.parse(MOVIE_DB_IMAGE_BASE_URL).buildUpon()
                    .appendEncodedPath(IMAGE_SIZE_PARAM)
                    .appendEncodedPath(imagePath)
                    .build()


            try {
                url = URL(builtUri.toString())
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            }

            Log.v(TAG, "Built URI " + url!!)

            return url

        }

        fun buildUrl(queryType: String, apikey: String?): URL? {
            var url: URL? = null
            if (apikey == null) {
                return url
            }

            val builtUri = Uri.parse(MOVIE_DB_BASE_URL).buildUpon()
                    .appendEncodedPath(queryType)
                    .appendQueryParameter(API_KEY_PARAM, apikey)
                    .build()


            try {
                url = URL(builtUri.toString())
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            }

            Log.v(TAG, "Built URI " + url!!)

            return url
        }

        @Throws(IOException::class)
        fun getResponseFromHttpUrl(url: URL): String? {
            val urlConnection = url.openConnection() as HttpURLConnection
            try {
                val input = urlConnection.inputStream

                val scanner = Scanner(input)
                scanner.useDelimiter("\\A")

                val hasInput = scanner.hasNext()
                return if (hasInput) {
                    scanner.next()
                } else {
                    null
                }
            } finally {
                urlConnection.disconnect()
            }
        }
    }
}