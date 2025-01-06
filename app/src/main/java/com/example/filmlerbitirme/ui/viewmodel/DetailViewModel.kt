package com.example.filmlerbitirme.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.filmlerbitirme.data.repo.MovieDaoRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmlerbitirme.data.entity.CartMovie
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailViewModel @Inject constructor(
    var repository: MovieDaoRepository,
    private val sharedCartManager: SharedCartManager
) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _cartList = MutableLiveData<List<CartMovie>>()
    val cartList: LiveData<List<CartMovie>> = _cartList

    init {
        loadCartMovies("sinem")
    }

    private fun loadCartMovies(userName: String) {
        viewModelScope.launch {
            try {
                val movies = repository.getCartMovies(userName)
                _cartList.postValue(movies)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addToCart(
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
        viewModelScope.launch {
            try {
                _isLoading.postValue(true)

                val cartMovies = repository.getCartMovies(userName)
                val existingMovie = cartMovies.find { it.name == name }

                if (existingMovie != null) {
                    repository.deleteFromCart(existingMovie.cartId, userName)
                    repository.addToCart(
                        name,
                        image,
                        price,
                        category,
                        rating,
                        year,
                        director,
                        description,
                        existingMovie.orderAmount + orderAmount,
                        userName
                    )
                } else {
                    repository.addToCart(
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

                loadCartMovies(userName)
                // Notify cart update
                sharedCartManager.notifyCartUpdated()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}