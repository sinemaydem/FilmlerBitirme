package com.example.filmlerbitirme.ui.viewmodel

import com.example.filmlerbitirme.data.repo.MovieDaoRepository
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.filmlerbitirme.data.entity.Movies
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(var repository: MovieDaoRepository) : ViewModel() {
    val moviesList = MutableLiveData<List<Movies>>()

    init {
        getAllMovies()
    }

    fun getAllMovies() {
        CoroutineScope(Dispatchers.Main).launch {
            moviesList.value = repository.getAllMovies()
        }
    }
}
