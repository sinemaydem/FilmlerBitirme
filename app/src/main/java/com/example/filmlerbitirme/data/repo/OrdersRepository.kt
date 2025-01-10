package com.example.filmlerbitirme.data.repo

import android.content.Context
import com.example.filmlerbitirme.data.entity.Order
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OrdersRepository @Inject constructor(
    private val context: Context
) {
    private val ORDERS_PREFERENCES = "orders_preferences"
    private val sharedPreferences = context.getSharedPreferences(ORDERS_PREFERENCES, Context.MODE_PRIVATE)
    private val gson = Gson()

    suspend fun addOrder(order: Order) {
        withContext(Dispatchers.IO) {
            val existingOrders = getOrders(order.userName)
            val updatedOrders = existingOrders + order

            val ordersJson = gson.toJson(updatedOrders)
            sharedPreferences.edit()
                .putString("${order.userName}_orders", ordersJson)
                .apply()
        }
    }

    suspend fun getOrders(userName: String): List<Order> {
        return withContext(Dispatchers.IO) {
            val ordersJson = sharedPreferences.getString("${userName}_orders", null)
            if (ordersJson != null) {
                try {
                    val type = object : TypeToken<List<Order>>() {}.type
                    gson.fromJson(ordersJson, type)
                } catch (e: Exception) {
                    emptyList()
                }
            } else {
                emptyList()
            }
        }
    }
}