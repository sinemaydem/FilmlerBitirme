package com.example.filmlerbitirme.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import com.example.filmlerbitirme.R
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.example.filmlerbitirme.data.entity.Movies
import com.example.filmlerbitirme.ui.viewmodel.DetailViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    gelenFilm: Movies,
    detailViewModel: DetailViewModel,
    onBackClick: () -> Unit
) {
    val isLoading by detailViewModel.isLoading.observeAsState(initial = false)
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Image and overlay content
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(600.dp)
            ) {
                // Background image
                AsyncImage(
                    model = "http://kasimadalan.pe.hu/movies/images/${gelenFilm.image}",
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Back button
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .padding(16.dp)
                        .size(48.dp)
                        .background(Color.Black.copy(alpha = 0.4f), shape = CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                // Movie details overlay
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(Color.Black.copy(alpha = 0.5f))
                        .padding(8.dp)
                ) {
                    Text(
                        text = gelenFilm.name,
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color.Yellow
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "(${gelenFilm.year})",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Text(
                            text = "${gelenFilm.rating}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Yellow,
                            modifier = Modifier.padding(end = 8.dp)
                        )

                        Row {
                            val starRating = gelenFilm.rating / 2
                            val fullStars = starRating.toInt()
                            val hasHalfStar = starRating % 1 >= 0.5

                            repeat(5) { index ->
                                val icon = when {
                                    index < fullStars -> R.drawable.star_dolu
                                    index == fullStars && hasHalfStar -> R.drawable.star_yarim
                                    else -> R.drawable.star_bos
                                }

                                Icon(
                                    painter = painterResource(id = icon),
                                    contentDescription = null,
                                    tint = Color.Yellow,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Movie details section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Director: ${gelenFilm.director}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = gelenFilm.description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Add to cart button
            Button(
                onClick = {
                    detailViewModel.addToCart(
                        gelenFilm.name,
                        gelenFilm.image,
                        gelenFilm.price,
                        gelenFilm.category,
                        gelenFilm.rating,
                        gelenFilm.year,
                        gelenFilm.director,
                        gelenFilm.description,
                        1,
                        "sinem"
                    )
                    scope.launch {
                        snackbarHostState.showSnackbar("Added to cart")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(text = "$${gelenFilm.price}")
                }
            }
        }

        // Snackbar host
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    gelenFilm: Movies,
    detailViewModel: DetailViewModel
) {
    val isLoading by detailViewModel.isLoading.observeAsState(initial = false)
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = gelenFilm.name,
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth() // Yazıyı ortalamak için
                    )
                },
                modifier = Modifier.height(56.dp) // TopAppBar yüksekliği ayarlandı
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // Resim
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center // Resmi ortalamak için
            ) {
                AsyncImage(
                    model = "http://kasimadalan.pe.hu/movies/images/${gelenFilm.image}",
                    contentDescription = null,
                    modifier = Modifier
                        .size(400.dp, 500.dp),
                    contentScale = ContentScale.Fit // Resmin tamamen görünmesini sağlamak için
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Filmin adı ve rating
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = gelenFilm.name,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Rating: ${gelenFilm.rating}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Row {
                    val starRating = gelenFilm.rating / 2 // 10 üzerinden puanı 5'e dönüştür
                    val fullStars = starRating.toInt() // Tam dolu yıldız sayısı
                    val hasHalfStar = starRating % 1 >= 0.5 // Yarım yıldız var mı?

                    repeat(5) { index ->
                        val icon = when {
                            index < fullStars -> R.drawable.star_dolu // Tam dolu yıldız
                            index == fullStars && hasHalfStar -> R.drawable.star_yarim // Yarım yıldız
                            else -> R.drawable.star_bos // Boş yıldız
                        }

                        Icon(
                            painter = painterResource(id = icon),
                            contentDescription = null,
                            tint = Color.Yellow,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Director: ${gelenFilm.director}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Description: ${gelenFilm.description}")
            Spacer(modifier = Modifier.height(16.dp))

            // Sepete ekleme butonu
            Button(
                onClick = {
                    detailViewModel.addToCart(
                        gelenFilm.name,
                        gelenFilm.image,
                        gelenFilm.price,
                        gelenFilm.category,
                        gelenFilm.rating,
                        gelenFilm.year,
                        gelenFilm.director,
                        gelenFilm.description,
                        1,
                        "sinem"
                    )
                    scope.launch {
                        snackbarHostState.showSnackbar("Added to cart")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(text = "$${gelenFilm.price}")
                }
            }
        }

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}
*/