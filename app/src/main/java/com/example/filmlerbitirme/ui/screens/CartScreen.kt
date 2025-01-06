package com.example.filmlerbitirme.ui.screens


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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.example.filmlerbitirme.data.entity.CartMovie
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.filmlerbitirme.ui.viewmodel.CartViewModel
import com.example.filmlerbitirme.ui.viewmodel.DetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavController,
    viewModel: CartViewModel,
    detailViewModel: DetailViewModel,
    cartViewModel: CartViewModel
) {
    val cartMovies by detailViewModel.cartList.observeAsState(cartViewModel.cartList.value ?: emptyList())
    var totalPrice by remember { mutableStateOf(0) }


    LaunchedEffect(cartMovies) {

        totalPrice = cartMovies.sumOf { it.price * it.orderAmount }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cart") },
                navigationIcon = {
                    IconButton(onClick = {
                        try {
                            navController.navigateUp()
                        } catch (e: Exception) {
                            navController.navigate("anasayfa") {
                                popUpTo("anasayfa") { inclusive = true }
                            }
                        }
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total: $${totalPrice}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Button(
                        onClick = {

                        },
                        enabled = cartMovies.isNotEmpty()
                    ) {
                        Text("Checkout")
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
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Your cart is empty",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            navController.navigate("anasayfa") {
                                popUpTo("anasayfa") { inclusive = true }
                            }
                        }
                    ) {
                        Text("Continue Shopping")
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(cartMovies) { cartMovie ->
                        CartMovieItem(
                            cartMovie = cartMovie,
                            onDeleteClick = {
                                viewModel.deleteFromCart(cartMovie.cartId, cartMovie.userName)
                            },
                            onQuantityChange = { newQuantity ->
                                viewModel.updateQuantity(cartMovie, newQuantity)
                            }
                        )
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
            .padding(vertical = 4.dp)
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
                    .size(80.dp)
                    .clip(MaterialTheme.shapes.small)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = cartMovie.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "$${cartMovie.price}",
                    style = MaterialTheme.typography.bodyMedium
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

            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}
