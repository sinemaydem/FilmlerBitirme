package com.example.filmlerbitirme.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.filmlerbitirme.data.repo.MovieDaoRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmlerbitirme.data.entity.CartMovie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DetailViewModel @Inject constructor(var repository: MovieDaoRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // Sepetteki ürünlerin listesini tutmak için LiveData
    private val _cartList = MutableLiveData<List<CartMovie>>()
    val cartList: LiveData<List<CartMovie>> = _cartList

    private fun loadCartMovies(userName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val movies = repository.getCartMovies(userName)
                withContext(Dispatchers.Main) {
                    _cartList.value = movies
                }
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
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isLoading.postValue(true)

                // Mevcut sepetteki ürünleri kontrol et
                val cartMovies = repository.getCartMovies(userName)
                val existingMovie = cartMovies.find { it.name == name }

                if (existingMovie != null) {
                    // Ürün varsa, miktarı güncelle
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
                    // Yeni bir ürün ekle
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

                // Sepeti yeniden yükle
                loadCartMovies(userName)

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}
