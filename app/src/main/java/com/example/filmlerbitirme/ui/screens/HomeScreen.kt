package com.example.filmlerbitirme.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType.Companion.Uri
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.example.filmlerbitirme.R
import com.example.filmlerbitirme.data.entity.Movies
import com.example.filmlerbitirme.ui.viewmodel.HomeViewModel
import com.google.gson.Gson
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel
) {
    val movies by viewModel.moviesList.observeAsState(emptyList())
    val categories = listOf("All", "Science Fiction", "Action", "Drama", "Fantastic")

    var selectedCategory by remember { mutableStateOf("All") }
    var searchText by remember { mutableStateOf("") }
    var sortByRating by remember { mutableStateOf(false) }

    val filteredMovies = movies
        .filter { it.category == selectedCategory || selectedCategory == "All" }
        .filter { it.name.contains(searchText, ignoreCase = true) }
        .let { if (sortByRating) it.sortedByDescending { movie -> movie.rating } else it }

    LaunchedEffect(true) {
        viewModel.getAllMovies()
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("Movies") },
                    actions = {
                        IconButton(onClick = { navController.navigate("profil") }) {
                            Icon(
                                painter = painterResource(id = R.drawable.profil),
                                contentDescription = "Profil"
                            )
                        }
                    }
                )
                TextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    label = { Text("Search movies...") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Search Icon")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                TabRow(selectedTabIndex = categories.indexOf(selectedCategory)) {
                    categories.forEachIndexed { index, category ->
                        Tab(
                            selected = selectedCategory == category,
                            onClick = { selectedCategory = category },
                            text = { Text(category) }
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Sort by rating")
                    Switch(
                        checked = sortByRating,
                        onCheckedChange = { sortByRating = it }
                    )
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(filteredMovies) { movie ->
                MovieCard(
                    movie = movie,
                    onMovieClick = {
                        val filmJson = Gson().toJson(movie)
                        navController.navigate("detaySayfa/${filmJson}")
                    }
                )
            }
        }
    }
}

@Composable
fun MovieCard(
    movie: Movies,
    onMovieClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onMovieClick),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = "http://kasimadalan.pe.hu/movies/images/${movie.image}",
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Column(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1f)
            ) {
                Text(
                    text = movie.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Director: ${movie.director}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "$${movie.price}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

