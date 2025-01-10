package com.example.filmlerbitirme.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import com.example.filmlerbitirme.firebase.pages.LoginPage
import com.example.filmlerbitirme.firebase.pages.SignupPage
import com.example.filmlerbitirme.firebase.viewmodel.AuthState
import com.example.filmlerbitirme.firebase.viewmodel.AuthViewModel
import com.example.filmlerbitirme.ui.viewmodel.CartViewModel
import com.example.filmlerbitirme.ui.viewmodel.DetailViewModel

import com.example.filmlerbitirme.ui.viewmodel.HomeViewModel
import com.example.filmlerbitirme.ui.viewmodel.OrdersViewModel
import com.example.filmlerbitirme.ui.viewmodel.ThemeViewModel
import com.google.gson.Gson

@Composable
fun SayfaGecisleri(
    authViewModel: AuthViewModel,
    homeViewModel: HomeViewModel,
    detailViewModel: DetailViewModel,
    themeViewModel: ThemeViewModel,
    cartViewModel: CartViewModel,
    ordersViewModel: OrdersViewModel,
    movieDaoRepository: MovieDaoRepository,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val authState by authViewModel.authState.observeAsState()

    NavHost(
        navController = navController,
        startDestination = "login",
        modifier = modifier
    ) {
        composable("login") {
            when (authState) {
                is AuthState.Authenticated -> {
                    LaunchedEffect(Unit) {
                        navController.navigate("anasayfa") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                }
                else -> {
                    LoginPage(
                        modifier = modifier,
                        navController = navController,
                        authViewModel = authViewModel
                    )
                }
            }
        }

        composable("signup") {
            SignupPage(
                modifier = modifier,
                navController = navController,
                authViewModel = authViewModel
            )
        }

        composable("anasayfa") {
            when (authState) {
                is AuthState.Unauthenticated -> {
                    LaunchedEffect(Unit) {
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }
                else -> {
                    HomeScreen(
                        navController = navController,
                        viewModel = homeViewModel
                    )
                }
            }
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
                viewModel = cartViewModel
            )
        }

        composable("profil") {
            Profile(
                navController = navController,
                authViewModel = authViewModel,
                themeViewModel = themeViewModel
            )
        }

        composable("randomMovie") {
            RandomMovieScreen(
                navController = navController,
                movieDaoRepository = movieDaoRepository
            )
        }

        composable("change_password") {
            ChangePasswordScreen(
                authViewModel = authViewModel,
                navController = navController
            )
        }

        composable(
            route = "checkout_screen/{totalPrice}",
            arguments = listOf(navArgument("totalPrice") { type = NavType.StringType })
        ) { backStackEntry ->
            val totalPrice = backStackEntry.arguments?.getString("totalPrice")?.toDoubleOrNull() ?: 0.0
            CheckoutScreen(
                navController = navController,
                totalPrice = totalPrice,
                viewModel = cartViewModel,
                ordersViewModel = ordersViewModel
            )
        }

        composable("coupons_screen") {
            CouponsScreen(navController = navController)
        }

        composable("orders_screen") {
            OrdersScreen(
                navController = navController,
                viewModel = ordersViewModel
            )
        }
    }
}