package com.example.filmlerbitirme.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    navController: NavController,
    totalPrice: Double
) {
    var couponCode by remember { mutableStateOf("") }
    var discountedPrice by remember { mutableStateOf(totalPrice) }
    val discountRate = 0.25

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Checkout") },
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
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Ödeme Ekranı",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "Toplam Fiyat: $${"%.2f".format(totalPrice)}",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = couponCode,
                onValueChange = { couponCode = it },
                label = { Text("Kupon Kodu") },
                placeholder = { Text("Kupon kodunuzu girin") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (couponCode.equals("INDIRIM25", ignoreCase = true)) {
                        discountedPrice = totalPrice * (1 - discountRate)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Kuponu Uygula")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "İndirimli Fiyat: $${"%.2f".format(discountedPrice)}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    // Ödeme işlemleri burada yapılacak
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ödeme Yap")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Kuponlar sayfasına gitmek için buton
            Button(onClick = { navController.navigate("coupons_screen") }, modifier = Modifier.fillMaxWidth()) {
                Text("Kuponlarımı Görüntüle")
            }
        }
    }
}
