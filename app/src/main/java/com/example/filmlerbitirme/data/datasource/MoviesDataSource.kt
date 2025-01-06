package com.example.filmlerbitirme.data.datasource

import com.example.filmlerbitirme.data.entity.CartMovie
import com.example.filmlerbitirme.data.entity.Movies
import com.example.filmlerbitirme.retrofit.MoviesDaoInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class MoviesDataSource(val moviesDao : MoviesDaoInterface) {


    suspend fun getAllMovies(): List<Movies> = withContext(Dispatchers.IO) {
        return@withContext moviesDao.getAllMovies().movies
    }

    suspend fun addToCart(name: String,
                          image: String,
                          price: Int,
                          category: String,
                          rating: Double,
                          year: Int,
                          director: String,
                          description: String,
                          orderAmount: Int,
                          userName: String) {
        moviesDao.addToCart(name, image, price, category, rating, year, director, description, orderAmount, userName)

    }


    suspend fun getCartMovies(userName: String): List<CartMovie> = withContext(Dispatchers.IO) {
        return@withContext moviesDao.getCartMovies(userName).movie_cart
    }

    suspend fun deleteFromCart(cartId: Int, userName: String) {
        moviesDao.deleteFromCart(cartId, userName)



    }


}