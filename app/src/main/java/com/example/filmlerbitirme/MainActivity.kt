package com.example.filmlerbitirme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.filmlerbitirme.data.datasource.MoviesDataSource
import com.example.filmlerbitirme.data.repo.MovieDaoRepository
import com.example.filmlerbitirme.firebase.viewmodel.AuthViewModel
import com.example.filmlerbitirme.retrofit.MoviesDaoInterface
import com.example.filmlerbitirme.ui.screens.BottomBar
import com.example.filmlerbitirme.ui.screens.HomeScreen
import com.example.filmlerbitirme.ui.screens.MainScreen
import com.example.filmlerbitirme.ui.screens.SayfaGecisleri
import com.example.filmlerbitirme.ui.theme.FilmlerBitirmeTheme
import com.example.filmlerbitirme.ui.viewmodel.CartViewModel
import com.example.filmlerbitirme.ui.viewmodel.DetailViewModel

import com.example.filmlerbitirme.ui.viewmodel.HomeViewModel
import com.example.filmlerbitirme.ui.viewmodel.OrdersViewModel
import com.example.filmlerbitirme.ui.viewmodel.PaymentViewModel
import com.example.filmlerbitirme.ui.viewmodel.ThemeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val themeViewModel: ThemeViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()
    private val cartViewModel: CartViewModel by viewModels()
    private val detailViewModel: DetailViewModel by viewModels()
    private val ordersViewModel: OrdersViewModel by viewModels()
    private val paymentViewModel: PaymentViewModel by viewModels()

    @Inject
    lateinit var movieDaoRepository: MovieDaoRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val isDarkModeActive by themeViewModel.isDarkMode.collectAsState()

            FilmlerBitirmeTheme(darkTheme = isDarkModeActive) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainScreen(
                        authViewModel = authViewModel,
                        homeViewModel = homeViewModel,
                        detailViewModel = detailViewModel,
                        cartViewModel = cartViewModel,
                        movieDaoRepository = movieDaoRepository,
                        themeViewModel = themeViewModel,
                        ordersViewModel = ordersViewModel,
                        paymentViewModel = paymentViewModel
                    )
                }
            }
        }
    }
}
