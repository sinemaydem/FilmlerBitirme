package com.example.filmlerbitirme.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.filmlerbitirme.data.entity.Movies
import com.example.filmlerbitirme.data.repo.MovieDaoRepository
import com.example.filmlerbitirme.ui.viewmodel.CartViewModel
import com.example.filmlerbitirme.ui.viewmodel.DetailViewModel

import com.example.filmlerbitirme.ui.viewmodel.HomeViewModel
import com.google.gson.Gson

@Composable
fun SayfaGecisleri(
    homeViewModel: HomeViewModel,
    detailViewModel: DetailViewModel,
    cartViewModel: CartViewModel,
    movieDaoRepository: MovieDaoRepository,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "anasayfa",
        modifier = modifier
    ) {
        composable("anasayfa") {
            HomeScreen(navController = navController, viewModel = homeViewModel)
        }
        composable(
            "detaySayfa/{film}",
            arguments = listOf(navArgument("film") { type = NavType.StringType })
        ) {
            val movieJson = it.arguments?.getString("film")
            val nesne = Gson().fromJson(movieJson, Movies::class.java)
            DetailScreen(
                gelenFilm = nesne,
                detailViewModel = detailViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
        composable("sepet") {
            CartScreen(
                navController = navController,
                viewModel = cartViewModel,
            )
            //cartViewModel.loadCartMovies()
        }
        composable("profil") {
            Profile(navController = navController)
        }
        composable("randomMovie") {
            RandomMovieScreen(navController = navController, movieDaoRepository = movieDaoRepository)
        }
    }
}
