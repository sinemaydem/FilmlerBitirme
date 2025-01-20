package com.example.filmlerbitirme.data.repo

import com.example.filmlerbitirme.data.datasource.MoviesDataSource
import com.example.filmlerbitirme.data.entity.CartMovie
import com.example.filmlerbitirme.data.entity.CartMoviesResponse
import com.example.filmlerbitirme.data.entity.Movies


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

