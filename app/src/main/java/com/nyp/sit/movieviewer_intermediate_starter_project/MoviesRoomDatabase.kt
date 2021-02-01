package com.nyp.sit.movieviewer_intermediate_starter_project

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nyp.sit.movieviewer_intermediate_starter_project.entity.MovieItem
import kotlinx.coroutines.CoroutineScope

@Database(entities = [MovieItem::class], version = 1, exportSchema = false)
abstract  class MoviesRoomDatabase: RoomDatabase() {
    abstract fun moviesDAO(): MovieItemDAO
    companion object{

        @Volatile
        private  var INSTANCE: MoviesRoomDatabase?= null

        fun getDatabase(context: Context, scope: CoroutineScope): MoviesRoomDatabase
        {

            return INSTANCE ?: synchronized(this)
            {
                val instance = Room.databaseBuilder(context,
                MoviesRoomDatabase::class.java, "movies_database").build()

                INSTANCE = instance

                instance

            }


        }
    }
}