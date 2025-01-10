package com.example.filmlerbitirme.data.entity

data class PaymentCard(
    val cardNumber: String,
    val cardHolderName: String,
    val expiryDate: String,
    val cvv: String,
    val isDefault: Boolean = false
)
