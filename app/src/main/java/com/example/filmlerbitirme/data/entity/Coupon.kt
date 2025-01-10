package com.example.filmlerbitirme.data.entity

data class Coupon(
    val code: String,
    val discountPercentage: Int,
    val minimumPurchase: Double? = null,
    val isOneTime: Boolean = false,
    val description: String
)