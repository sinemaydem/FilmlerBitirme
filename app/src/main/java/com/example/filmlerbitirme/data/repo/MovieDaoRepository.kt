package com.example.filmlerbitirme.data.repo


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmlerbitirme.data.datasource.MoviesDataSource
import com.example.filmlerbitirme.data.entity.CartMovie
import com.example.filmlerbitirme.data.entity.CartMoviesResponse
import com.example.filmlerbitirme.data.entity.Movies
import com.example.filmlerbitirme.data.entity.MoviesResponse
import com.example.filmlerbitirme.retrofit.ApiUtils
import com.example.filmlerbitirme.retrofit.MoviesDaoInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


class MovieDaoRepository (var moviesDataSource: MoviesDataSource) {


    suspend fun getAllMovies(): List<Movies> = moviesDataSource.getAllMovies()


    suspend fun addToCart(
        name: String,
        image: String,
        price: Int,
        category: String,
        rating: Double,
        year: Int,
        director: String,
        description: String,
        orderAmount: Int,
        userName: String
    ) {
        moviesDataSource.addToCart(
            name,
            image,
            price,
            category,
            rating,
            year,
            director,
            description,
            orderAmount,
            userName
        )
    }

    suspend fun getCartMovies(userName: String): List<CartMovie> = moviesDataSource.getCartMovies(userName)

    suspend fun deleteFromCart(cartId: Int, userName: String) {
        moviesDataSource.deleteFromCart(cartId, userName)

    }


}

