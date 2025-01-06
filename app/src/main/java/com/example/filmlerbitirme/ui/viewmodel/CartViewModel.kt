package com.example.filmlerbitirme.ui.viewmodel

import android.util.Log
import com.example.filmlerbitirme.data.repo.MovieDaoRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmlerbitirme.data.entity.CartMovie
import com.example.filmlerbitirme.data.entity.Movies
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject



class CartViewModel @Inject constructor(private val repository: MovieDaoRepository): ViewModel() {
    val cartList = MutableLiveData<List<CartMovie>>()

    init {
        loadCartMovies()
    }

    private fun loadCartMovies() {
        CoroutineScope(Dispatchers.Main).launch {
            cartList.value = repository.getCartMovies("sinem")
        }
    }

    fun deleteFromCart(cartId: Int, userName: String) {
        CoroutineScope(Dispatchers.Main).launch {
            repository.deleteFromCart(cartId, userName)
            loadCartMovies()

        }
    }

    fun updateQuantity(cartMovie: CartMovie, newQuantity: Int) {
        if (newQuantity <= 0) {
            deleteFromCart(cartMovie.cartId, cartMovie.userName)
            return
        }

        CoroutineScope(Dispatchers.Main).launch {
            try {
                // First delete the existing item
                repository.deleteFromCart(cartMovie.cartId, cartMovie.userName)

                // Then add a new item with updated quantity
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

                // Refresh the cart
                loadCartMovies()
            } catch (e: Exception) {
                // Handle error
                e.printStackTrace()
                loadCartMovies() // Reload cart in case of error
            }
        }
    }
}