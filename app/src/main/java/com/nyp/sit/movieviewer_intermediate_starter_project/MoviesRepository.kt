package com.nyp.sit.movieviewer_intermediate_starter_project

import com.nyp.sit.movieviewer_intermediate_starter_project.entity.MovieItem

class MoviesRepository(private val moviesDao: MovieItemDAO) {

    val allMovies = moviesDao.retrieveAllMovies()


    suspend fun insert(movieItem: MovieItem)
    {
        moviesDao.insert(movieItem)
    }
    suspend fun delete(movieItem: MovieItem)
    {
     moviesDao.delete(movieItem)
    }
    suspend fun deleteAll()
    {
        moviesDao.deleteAll()
    }
}