package com.example.filmlerbitirme.retrofit

class ApiUtils {
    companion object {
        private const val BASE_URL = "http://kasimadalan.pe.hu/"

        fun getMoviesDaoInterface(): MoviesDaoInterface {
            return RetrofitClient.getClient(BASE_URL).create(MoviesDaoInterface::class.java)
        }
    }
}