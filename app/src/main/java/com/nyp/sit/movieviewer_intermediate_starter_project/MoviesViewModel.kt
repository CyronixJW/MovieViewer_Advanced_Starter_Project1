package com.nyp.sit.movieviewer_intermediate_starter_project

import androidx.lifecycle.*
import com.nyp.sit.movieviewer_intermediate_starter_project.entity.MovieItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class MoviesViewModel(val repo: MoviesRepository): ViewModel() {

    val allMovies: LiveData<List<MovieItem>> = repo.allMovies.asLiveData()

    fun insert(movieItem: MovieItem) = viewModelScope.launch(Dispatchers.IO)
    {
        repo.insert(movieItem)
    }
    fun remove(movieItem: MovieItem) = viewModelScope.launch(Dispatchers.IO)
    {
        repo.delete(movieItem)
    }
    fun deleteAll() = viewModelScope.launch(Dispatchers.IO)
    {
        repo.deleteAll()
    }

}

class  MoviesViewModelFactory(private val repo: MoviesRepository): ViewModelProvider.Factory
{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MoviesViewModel::class.java))
        {
            return  MoviesViewModel(repo) as T
        }
        throw IllegalArgumentException("unknown viewmodel class")
    }
}