package com.example.filmlerbitirme.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmlerbitirme.data.entity.CartMovie
import com.example.filmlerbitirme.data.entity.Order
import com.example.filmlerbitirme.data.repo.OrdersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val ordersRepository: OrdersRepository
) : ViewModel() {
    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders = _orders.asStateFlow()

    fun loadOrders(userName: String) {
        viewModelScope.launch {
            _orders.value = ordersRepository.getOrders(userName)
        }
    }

    fun addOrder(cartItems: List<CartMovie>, totalPrice: Double, userName: String) {
        viewModelScope.launch {
            val order = Order(
                items = cartItems,
                totalPrice = totalPrice,
                userName = userName
            )
            ordersRepository.addOrder(order)
            loadOrders(userName)
        }
    }
}