package com.example.filmlerbitirme.ui.viewmodel

import android.util.Log
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

    fun getCurrentUser(): String {
        // Since you're using "sinem" as a hardcoded username in loadCartMovies,
        // we'll return the same here for consistency
        return "sinem"
    }

    fun clearCart() {
        viewModelScope.launch {
            try {
                // Get current cart items
                val currentCart = _cartList.value ?: emptyList()

                // Delete each item from cart
                currentCart.forEach { cartMovie ->
                    repository.deleteFromCart(cartMovie.cartId, cartMovie.userName)
                }

                // Reload cart (should now be empty)
                loadCartMovies()

                // Notify that cart has been updated
                sharedCartManager.notifyCartUpdated()
            } catch (e: Exception) {
                Log.e("CartDebug", "Error clearing cart", e)
            }
        }
    }

    fun loadCartMovies() {
        viewModelScope.launch {
            try {
                val movies = repository.getCartMovies("sinem")
                Log.d("CartDebug", "Cart movies loaded: ${movies.size}")
                _cartList.postValue(movies.ifEmpty { emptyList() })
            } catch (e: Exception) {
                Log.e("CartDebug", "Error loading cart", e)
                _cartList.postValue(emptyList())
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