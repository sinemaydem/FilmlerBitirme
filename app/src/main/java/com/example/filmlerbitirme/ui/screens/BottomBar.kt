package com.example.filmlerbitirme.ui.screens


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.filmlerbitirme.R

@Composable
fun BottomBar(
    navController: NavController,
    onNavigateToCart: () -> Unit
) {
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        NavigationBarItem(
            selected = currentDestination?.route == "anasayfa",
            onClick = {
                navController.navigate("anasayfa") {
                    launchSingleTop = true
                    restoreState = true
                }
            },
            label = { Text(text = "Anasayfa") },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.anasayfa_resim),
                    contentDescription = "Anasayfa"
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                indicatorColor = MaterialTheme.colorScheme.surface
            )
        )

        NavigationBarItem(
            selected = currentDestination?.route == "randomMovie",
            onClick = {
                navController.navigate("randomMovie") {
                    launchSingleTop = true
                    restoreState = true
                }
            },
            label = { Text(text = "FlickPicker") },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.random_movie),
                    contentDescription = "Filmler"
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                indicatorColor = MaterialTheme.colorScheme.surface
            )
        )

        NavigationBarItem(
            selected = currentDestination?.route == "sepet",
            onClick = { onNavigateToCart() },
            label = { Text(text = "Sepet") },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.sepet_resim),
                    contentDescription = "Sepet"
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                indicatorColor = MaterialTheme.colorScheme.surface
            )
        )

        NavigationBarItem(
            selected = currentDestination?.route == "profil",
            onClick = {
                navController.navigate("profil") {
                    launchSingleTop = true
                    restoreState = true
                }
            },
            label = { Text(text = "Profil") },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.profil),
                    contentDescription = "Profil"
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                indicatorColor = MaterialTheme.colorScheme.surface
            )
        )

    }
}