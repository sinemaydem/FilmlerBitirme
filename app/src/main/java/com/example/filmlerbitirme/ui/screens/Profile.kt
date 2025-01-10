package com.example.filmlerbitirme.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.filmlerbitirme.R
import com.example.filmlerbitirme.firebase.viewmodel.AuthViewModel
import com.example.filmlerbitirme.ui.theme.FilmlerBitirmeTheme
import com.example.filmlerbitirme.ui.theme.jersey
import com.example.filmlerbitirme.ui.viewmodel.ThemeViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(
    navController: NavController,
    authViewModel: AuthViewModel,
    themeViewModel: ThemeViewModel
) {
    val isDarkModeActive by themeViewModel.isDarkMode.collectAsState()

    Scaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        topBar = {
            Column(
                modifier = Modifier.background(MaterialTheme.colorScheme.background)
            ) {
                Spacer(modifier = Modifier.height(23.dp))
                TopAppBar(
                    title = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 56.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Profil",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.titleMedium,
                                fontFamily = jersey,
                                fontSize = 30.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
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
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Profil Resmi",
                modifier = Modifier
                    .size(120.dp)
                    .padding(8.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Text(
                text = authViewModel.auth.currentUser?.email ?: "E-posta bulunamadı",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 16.dp)
            )


            Text(
                text = "Hesap",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )


            // Siparişlerim Kısmı

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .clickable(onClick = {
                        navController.navigate("orders_screen")

                    })
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    painter = painterResource(id = R.drawable.siparislerim),
                    contentDescription = "Siparişlerim",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )

                Text(
                    text = "Siparişlerim",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp)
                )

                Icon(
                    painter = painterResource(id = R.drawable.git),
                    contentDescription = "Git",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(24.dp)
                )
            }


            //Ödeme Yöntemleri Kısmı
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .clickable(onClick = {
                        navController.navigate("saved_cards")
                    })
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    painter = painterResource(id = R.drawable.odeme),
                    contentDescription = "Ödeme Yöntemlerim",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )

                Text(
                    text = "Ödeme Yöntemlerim",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp)
                )

                Icon(
                    painter = painterResource(id = R.drawable.git),
                    contentDescription = "Git",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(24.dp)
                )
            }


            // Kuponlarım Kısmı
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .clickable(onClick = {
                        navController.navigate("coupons_screen")

                    })
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    painter = painterResource(id = R.drawable.ticket),
                    contentDescription = "Kuponlarım",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )

                Text(
                    text = "Kuponlarım",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp)
                )

                Icon(
                    painter = painterResource(id = R.drawable.git),
                    contentDescription = "Git",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(24.dp)
                )
            }


            // Ayarlar Kısmı
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Ayarlar",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )


            // Karanlık Mod
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .clickable(onClick = { /* Tıklama işlemi istenirse eklenebilir */ })
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(
                        id = if (isDarkModeActive) R.drawable.sun else R.drawable.moon
                    ),
                    contentDescription = if (isDarkModeActive) "Aydınlık Mod" else "Karanlık Mod",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )

                Text(
                    text = "Karanlık Mod",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp)
                )

                Switch(
                    checked = isDarkModeActive,
                    onCheckedChange = { isChecked ->
                        themeViewModel.setDarkMode(isChecked)
                    }
                )
            }


            // Dil Seçenekleri
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .clickable(onClick = { /* Dil seçeneklerini aç */ })
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    painter = painterResource(id = R.drawable.language),
                    contentDescription = "Dil Seçenekleri",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )


                Text(
                    text = "Dil Seçenekleri (TR/EN)",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp)
                )


                Icon(
                    painter = painterResource(id = R.drawable.git),
                    contentDescription = "Git",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Şifre Güncelle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .clickable(onClick = { navController.navigate("change_password") })
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    painter = painterResource(id = R.drawable.sifre),
                    contentDescription = "Şifre Güncelle",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )


                Text(
                    text = "Şifre Güncelle",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp)
                )


                Icon(
                    painter = painterResource(id = R.drawable.git),
                    contentDescription = "Git",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    authViewModel.signout()
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                )
            ) {
                Text("Çıkış Yap")
            }

        }
    }
}