package com.example.filmlerbitirme.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.filmlerbitirme.data.entity.PaymentCard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class PaymentViewModel : ViewModel() {
    private val _savedCards = MutableStateFlow<List<PaymentCard>>(emptyList())
    val savedCards: StateFlow<List<PaymentCard>> = _savedCards

    fun addCard(card: PaymentCard) {
        _savedCards.value = _savedCards.value + card
    }
}
