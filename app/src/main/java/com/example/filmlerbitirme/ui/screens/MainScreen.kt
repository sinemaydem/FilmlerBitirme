package com.example.filmlerbitirme.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.filmlerbitirme.data.repo.MovieDaoRepository
import com.example.filmlerbitirme.ui.viewmodel.CartViewModel
import com.example.filmlerbitirme.ui.viewmodel.DetailViewModel
import com.example.filmlerbitirme.ui.viewmodel.HomeViewModel


@Composable
fun MainScreen(
    homeViewModel: HomeViewModel,
    detailViewModel: DetailViewModel,
    cartViewModel: CartViewModel,
    movieDaoRepository: MovieDaoRepository
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomBar(
                navController = navController,
                onNavigateToCart = {
                    navController.navigate("sepet") {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { innerPadding ->
        SayfaGecisleri(
            homeViewModel = homeViewModel,
            detailViewModel = detailViewModel,
            cartViewModel = cartViewModel,
            movieDaoRepository = movieDaoRepository,
            modifier = Modifier.padding(innerPadding),
            navController = navController
        )
    }
}
