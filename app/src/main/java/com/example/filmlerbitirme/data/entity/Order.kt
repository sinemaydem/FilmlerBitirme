package com.example.filmlerbitirme.data.entity

import java.util.UUID

data class Order(
    val orderId: String = UUID.randomUUID().toString(),
    val items: List<CartMovie>,
    val totalPrice: Double,
    val orderDate: Long = System.currentTimeMillis(),
    val userName: String
)