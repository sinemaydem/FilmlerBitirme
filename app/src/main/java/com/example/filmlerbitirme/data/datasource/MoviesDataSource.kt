package com.example.filmlerbitirme.data.datasource

import android.util.Log
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
        try {
            val response = moviesDao.addToCart(name, image, price, category, rating, year, director, description, orderAmount, userName)
            Log.d("CartDebug", "Add to cart response: $response")
        } catch (e: Exception) {
            Log.e("CartDebug", "Add to cart error", e)
        }
    }


    suspend fun getCartMovies(userName: String): List<CartMovie> = withContext(Dispatchers.IO) {
        try {
            val response = moviesDao.getCartMovies(userName)
            Log.d("CartDebug", "Get cart response: ${response.movie_cart}")
            return@withContext response.movie_cart
        } catch (e: Exception) {
            Log.e("CartDebug", "Get cart error", e)
            return@withContext emptyList()
        }
    }

    suspend fun deleteFromCart(cartId: Int, userName: String) {
        moviesDao.deleteFromCart(cartId, userName)



    }


}