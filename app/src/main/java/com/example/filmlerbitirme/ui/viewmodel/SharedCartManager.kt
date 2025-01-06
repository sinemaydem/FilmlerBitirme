package com.example.filmlerbitirme.ui.viewmodel

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedCartManager @Inject constructor() {
    private val _cartUpdated = MutableSharedFlow<Unit>()
    val cartUpdated = _cartUpdated.asSharedFlow()

    suspend fun notifyCartUpdated() {
        _cartUpdated.emit(Unit)
    }
}
