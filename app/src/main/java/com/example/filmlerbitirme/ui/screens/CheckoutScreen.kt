package com.example.filmlerbitirme.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.filmlerbitirme.data.entity.Coupon
import com.example.filmlerbitirme.ui.viewmodel.CartViewModel
import com.example.filmlerbitirme.ui.viewmodel.OrdersViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    navController: NavController,
    totalPrice: Double,
    viewModel: CartViewModel,
    ordersViewModel: OrdersViewModel
) {
    var couponCode by remember { mutableStateOf("") }
    var discountedPrice by remember { mutableStateOf(totalPrice) }
    var appliedCoupon by remember { mutableStateOf<Coupon?>(null) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val cartMovies by viewModel.cartList.observeAsState(emptyList())

    val coupons = listOf(
        Coupon("INDIRIM25", 25, 25.0, false, "25$ üzeri alışverişlerde %25 indirim"),
        Coupon("WELCOME10", 10, null, true, "Tek kullanımlık %10 indirim"),
        Coupon("SUMMER15", 15, null, false, "Tüm filmlerde %15 indirim")
    )


    fun applyCoupon(code: String) {
        val coupon = coupons.find { it.code.equals(code, ignoreCase = true) }
        if (coupon != null) {
            if (coupon.minimumPurchase != null && totalPrice < coupon.minimumPurchase) {
                scope.launch {
                    snackbarHostState.showSnackbar("Bu kupon ${coupon.minimumPurchase}$ üzeri alışverişlerde geçerlidir")
                }
                return
            }

            appliedCoupon = coupon
            discountedPrice = totalPrice * (1 - coupon.discountPercentage / 100.0)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Ödeme") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Sipariş Özeti",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            // Ara Toplam
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Ara Toplam")
                Text("$${String.format("%.2f", totalPrice)}")
            }

            // Kupon Kodu Girişi
            OutlinedTextField(
                value = couponCode,
                onValueChange = { couponCode = it },
                label = { Text("Kupon Kodu") },
                placeholder = { Text("Kupon kodunuzu girin") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { applyCoupon(couponCode) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Kuponu Uygula")
            }

            // İndirim (eğer uygulanmışsa)
            if (appliedCoupon != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("İndirim (${appliedCoupon!!.discountPercentage}%)")
                    Text("-$${String.format("%.2f", totalPrice - discountedPrice)}")
                }
            }

            Divider()

            // Toplam
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Toplam",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "$${String.format("%.2f", discountedPrice)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Ödeme Yap Butonu
            Button(
                onClick = {
                    scope.launch {
                        ordersViewModel.addOrder(
                            cartItems = cartMovies,
                            totalPrice = discountedPrice,
                            userName = viewModel.getCurrentUser()
                        )
                        viewModel.clearCart()
                        snackbarHostState.showSnackbar("Siparişiniz başarıyla alındı!")
                        navController.navigate("anasayfa") {
                            popUpTo("cart_screen") { inclusive = true }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Ödeme Yap")
            }

            Button(
                onClick = { navController.navigate("coupons_screen") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text("Kuponlarımı Görüntüle")
            }
        }
    }
}