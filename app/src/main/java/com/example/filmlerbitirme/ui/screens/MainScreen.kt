package com.example.filmlerbitirme.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.filmlerbitirme.data.repo.MovieDaoRepository
import com.example.filmlerbitirme.firebase.viewmodel.AuthViewModel
import com.example.filmlerbitirme.ui.viewmodel.CartViewModel
import com.example.filmlerbitirme.ui.viewmodel.DetailViewModel
import com.example.filmlerbitirme.ui.viewmodel.HomeViewModel
import com.example.filmlerbitirme.ui.viewmodel.OrdersViewModel
import com.example.filmlerbitirme.ui.viewmodel.PaymentViewModel
import com.example.filmlerbitirme.ui.viewmodel.ThemeViewModel

@Composable
fun MainScreen(
    homeViewModel: HomeViewModel,
    detailViewModel: DetailViewModel,
    cartViewModel: CartViewModel,
    ordersViewModel: OrdersViewModel,
    movieDaoRepository: MovieDaoRepository,
    paymentViewModel: PaymentViewModel,
    authViewModel: AuthViewModel,
    themeViewModel: ThemeViewModel
) {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    val showBottomBar = when (currentRoute) {
        "detaySayfa/{film}", "login", "signup", "change_password",
        "checkout_screen/{totalPrice}", "orders_screen" -> false  // Hide bottom bar on checkout and orders screens
        else -> true
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
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
        }
    ) { innerPadding ->
        SayfaGecisleri(
            authViewModel = authViewModel,
            homeViewModel = homeViewModel,
            detailViewModel = detailViewModel,
            cartViewModel = cartViewModel,
            ordersViewModel = ordersViewModel,  // Add ordersViewModel
            movieDaoRepository = movieDaoRepository,
            themeViewModel = themeViewModel,
            modifier = Modifier.padding(
                bottom = if (showBottomBar) innerPadding.calculateBottomPadding() else 0.dp,
                top = innerPadding.calculateTopPadding()
            ),
            navController = navController,
            paymentViewModel = paymentViewModel

        )
    }
}