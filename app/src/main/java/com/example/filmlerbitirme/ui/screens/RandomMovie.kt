package com.example.filmlerbitirme.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import coil.compose.rememberAsyncImagePainter
import com.example.filmlerbitirme.R
import com.example.filmlerbitirme.data.entity.Movies
import com.example.filmlerbitirme.data.repo.MovieDaoRepository
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RandomMovieScreen(
    navController: NavController,
    movieDaoRepository: MovieDaoRepository
) {
    val context = LocalContext.current
    var randomMovie by remember { mutableStateOf<Movies?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var imageUrl by remember { mutableStateOf<String?>("R.drawable.random") }

    Scaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        topBar = {
            Column(
                modifier = Modifier.background(MaterialTheme.colorScheme.background)
            ) {
                Spacer(modifier = Modifier.height(23.dp))
                TopAppBar(
                    title = {
                        Text(
                            "FlickPicker",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
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
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text("Hangi Filmi İzleyeceğiz? Kararı Birlikte Verelim!")
            // Film ya da varsayılan resim gösterimi
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .clickable {
                        randomMovie?.let { movie ->
                            val movieJson = Gson().toJson(movie)
                            navController.navigate("detaySayfa/$movieJson")
                        }
                    }
            ) {
                if (imageUrl == "R.drawable.random") {
                    Image(
                        painter = painterResource(id = R.drawable.random),
                        contentDescription = "Varsayılan Kapak Resmi",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = rememberAsyncImagePainter(model = imageUrl),
                        contentDescription = "Film Resmi",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Rastgele Film Butonu
            Button(
                onClick = {
                    isLoading = true
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val movies = movieDaoRepository.getAllMovies()
                            val random = movies.randomOrNull()
                            withContext(Dispatchers.Main) {
                                randomMovie = random
                                imageUrl = random?.image?.let {
                                    "http://kasimadalan.pe.hu/movies/images/$it"
                                } ?: "R.drawable.random"
                                isLoading = false
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                isLoading = false
                                Toast.makeText(context, "Film yüklenemedi!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("FlickPicker")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Seçilen Rastgele Film Detayları
            randomMovie?.let { movie ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val movieJson = Gson().toJson(movie)
                            navController.navigate("detaySayfa/$movieJson")
                        },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = movie.name,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}
