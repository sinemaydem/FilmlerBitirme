package com.example.filmlerbitirme.retrofit

import com.example.filmlerbitirme.data.entity.CartMoviesResponse
import com.example.filmlerbitirme.data.entity.MoviesResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST


interface MoviesDaoInterface {

    @GET("movies/getAllMovies.php")
    suspend fun getAllMovies() : MoviesResponse

    @POST("movies/insertMovie.php")
    @FormUrlEncoded
    suspend fun addToCart(
        @Field("name") name: String,
        @Field("image") image: String,
        @Field("price") price: Int,
        @Field("category") category: String,
        @Field("rating") rating: Double,
        @Field("year") year: Int,
        @Field("director") director: String,
        @Field("description") description: String,
        @Field("orderAmount") orderAmount: Int,
        @Field("userName") userName: String
    ): CartMoviesResponse

    @POST("movies/getMovieCart.php")
    @FormUrlEncoded
    suspend fun getCartMovies(@Field("userName") userName: String): CartMoviesResponse

    @POST("movies/deleteMovie.php")
    @FormUrlEncoded
    suspend fun deleteFromCart(
        @Field("cartId") cartId: Int,
        @Field("userName") userName: String
    ): CartMoviesResponse
}
