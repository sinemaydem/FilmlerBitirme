package com.example.filmlerbitirme.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.example.filmlerbitirme.data.entity.CartMovie
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.filmlerbitirme.ui.theme.jersey
import com.example.filmlerbitirme.ui.viewmodel.CartViewModel
import com.example.filmlerbitirme.ui.viewmodel.DetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavController,
    viewModel: CartViewModel
) {
    val cartMovies by viewModel.cartList.observeAsState(emptyList())
    val totalPrice = cartMovies.sumOf { it.price * it.orderAmount }

    LaunchedEffect(Unit) {
        Log.d("CartDebug", "CartScreen composed")
        viewModel.loadCartMovies()
    }

    Scaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        topBar = {
            Column(
                modifier = Modifier.background(MaterialTheme.colorScheme.background)
            ) {
                Spacer(modifier = Modifier.height(23.dp))
                TopAppBar(
                    title = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 56.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Cart",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.titleMedium,
                                fontFamily = jersey,
                                fontSize = 30.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )
            }
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.height(100.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Total:",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.secondary,
                            fontSize = 23.sp
                        )
                        Text(
                            text = "$$totalPrice",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            fontSize = 23.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            navController.navigate("checkout_screen/$totalPrice")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(70.dp),
                        shape = RoundedCornerShape(20.dp),
                    ) {
                        Text(
                            "Checkout",
                            fontSize = 20.sp
                        )
                    }

                }
            }
        }


    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (cartMovies.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Your cart is empty",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { navController.navigate("anasayfa") }) {
                        Text("Continue Shopping")
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(cartMovies) { cartMovie ->
                        Column {
                            CartMovieItem(
                                cartMovie = cartMovie,
                                onDeleteClick = {
                                    viewModel.deleteFromCart(
                                        cartMovie.cartId,
                                        cartMovie.userName
                                    )
                                },
                                onQuantityChange = { newQuantity ->
                                    viewModel.updateQuantity(cartMovie, newQuantity)
                                }
                            )
                            Divider(
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f),
                                thickness = 1.dp,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CartMovieItem(
    cartMovie: CartMovie,
    onDeleteClick: () -> Unit,
    onQuantityChange: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = "http://kasimadalan.pe.hu/movies/images/${cartMovie.image}",
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .clip(MaterialTheme.shapes.small)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = cartMovie.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$${cartMovie.price}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 16.sp
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = {
                            if (cartMovie.orderAmount > 1) {
                                onQuantityChange(cartMovie.orderAmount - 1)
                            }
                        }
                    ) {
                        Text("-")
                    }
                    Text(cartMovie.orderAmount.toString())
                    IconButton(
                        onClick = { onQuantityChange(cartMovie.orderAmount + 1) }
                    ) {
                        Text("+")
                    }
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "$${cartMovie.price * cartMovie.orderAmount}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                IconButton(
                    onClick = onDeleteClick,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = Color.Red
                    )
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            }
        }
    }
}
