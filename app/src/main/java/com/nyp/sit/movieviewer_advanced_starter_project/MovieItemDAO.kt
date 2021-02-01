package com.nyp.sit.movieviewer_advanced_starter_project

import androidx.room.*
import com.nyp.sit.movieviewer_advanced_starter_project.entity.MovieItem

import kotlinx.coroutines.flow.Flow

@Dao
interface MovieItemDAO {


        @Query("Select * from MovieItem_table")
        fun retrieveAllMovies() : Flow<List<MovieItem>>
        @Insert(onConflict = OnConflictStrategy.ABORT)
        fun insert(newMovies: MovieItem)
        @Delete
        fun delete(delMovie: MovieItem)
        @Query("DELETE FROM MovieItem_table")
        fun deleteAll()


}