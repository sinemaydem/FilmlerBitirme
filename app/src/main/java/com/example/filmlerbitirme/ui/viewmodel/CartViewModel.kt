package com.example.filmlerbitirme.ui.viewmodel

import com.example.filmlerbitirme.data.repo.MovieDaoRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmlerbitirme.data.entity.CartMovie
import kotlinx.coroutines.launch
import javax.inject.Inject

class CartViewModel @Inject constructor(
    private val repository: MovieDaoRepository,
    private val sharedCartManager: SharedCartManager
) : ViewModel() {
    private val _cartList = MutableLiveData<List<CartMovie>>()
    val cartList: LiveData<List<CartMovie>> = _cartList

    init {
        loadCartMovies()
        viewModelScope.launch {
            // Listen for cart updates
            sharedCartManager.cartUpdated.collect {
                loadCartMovies()
            }
        }
    }

    fun loadCartMovies() {
        viewModelScope.launch {
            try {
                val movies = repository.getCartMovies("sinem")
                _cartList.postValue(movies.ifEmpty { emptyList() })
            } catch (e: Exception) {
                e.printStackTrace()
                _cartList.postValue(emptyList()) // Boş listeyle güncelle
            }
        }
    }


    fun deleteFromCart(cartId: Int, userName: String) {
        viewModelScope.launch {
            try {
                repository.deleteFromCart(cartId, userName)
                loadCartMovies() // Listeyi güncelle
                sharedCartManager.notifyCartUpdated() // Sepet güncellemesini bildir
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun updateQuantity(cartMovie: CartMovie, newQuantity: Int) {
        viewModelScope.launch {
            try {
                if (newQuantity <= 0) {
                    deleteFromCart(cartMovie.cartId, cartMovie.userName)
                    return@launch
                }

                repository.deleteFromCart(cartMovie.cartId, cartMovie.userName)
                repository.addToCart(
                    cartMovie.name,
                    cartMovie.image,
                    cartMovie.price,
                    cartMovie.category,
                    cartMovie.rating,
                    cartMovie.year,
                    cartMovie.director,
                    cartMovie.description,
                    newQuantity,
                    cartMovie.userName
                )

                loadCartMovies()
                sharedCartManager.notifyCartUpdated()
            } catch (e: Exception) {
                e.printStackTrace()
                loadCartMovies()
            }
        }
    }
}