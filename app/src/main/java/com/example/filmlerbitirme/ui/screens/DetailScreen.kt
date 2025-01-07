package com.example.filmlerbitirme.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import com.example.filmlerbitirme.R
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
                        .size(400.dp, 300.dp),
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
