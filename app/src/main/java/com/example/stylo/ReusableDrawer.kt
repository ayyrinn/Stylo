package com.example.stylo

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.stylo.ui.theme.miamaFontFamily


// Fungsi untuk Header Stylo dengan Burger Menu dan Teks
@Composable
fun StyloTopBar(onMenuClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF3EEEA)), // Menambahkan latar belakang dengan warna F3EEEA
        horizontalArrangement = Arrangement.Center
    ) {
        // Burger Icon
        Image(
            painter = painterResource(id = R.drawable.burger_icon),
            contentDescription = "Burger Icon",
            modifier = Modifier
                .size(50.dp)
                .fillMaxSize()
                .padding(top = 10.dp)
                .clickable { onMenuClick() }
        )

        Spacer(modifier = Modifier.weight(1f)) // Spacer untuk mendorong teks ke tengah

        // Teks "Stylo" di Tengah
        Text(
            text = "Stylo",
            fontSize = 45.sp,
            color = Color(0xFF776B5D),
            fontFamily = miamaFontFamily,
            modifier = Modifier
                .padding(end = 35.dp)
        )

        Spacer(modifier = Modifier.weight(1f)) // Spacer untuk keseimbangan kanan
    }
}

// Fungsi HamburgerMenu dengan Overlay Menu Samping
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReusableDrawer(navController: NavController) {
    var showMenu by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        // TopBar Tetap
        Column {
            StyloTopBar(onMenuClick = { showMenu = !showMenu })
        }

        // Overlay ketika menu aktif
        if (showMenu) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f)) // Warna hitam transparan
                    .clickable { showMenu = false } // Tutup menu jika overlay ditekan
            )

            // Menu Samping dengan Animasi
            AnimatedVisibility(
                visible = showMenu,
                enter = slideInHorizontally(initialOffsetX = { -it }) + fadeIn(),
                exit = slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(300.dp)
                        .background(Color(0xFFF3EEEA), RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Text(
                            "Profile", fontSize = 30.sp,
                            modifier = Modifier
                                .clickable { /* Tambahkan navigasi ke Profile */ }
                                .padding(8.dp)
                        )
                        Text(
                            "Gallery", fontSize = 30.sp,
                            modifier = Modifier
                                .clickable {
                                    navController.navigate("gallery") // Navigasi ke About Us
                                    showMenu = false
                                }
                                .padding(8.dp)
                        )
                        Text(
                            "Add Photo", fontSize = 30.sp,
                            modifier = Modifier
                                .clickable {
                                    navController.navigate("add photo") // Navigasi ke About Us
                                    showMenu = false
                                }
                                .padding(8.dp)
                        )
                        Text(
                            "About Us", fontSize = 30.sp,
                            modifier = Modifier
                                .clickable {
                                    navController.navigate("about") // Navigasi ke About Us
                                    showMenu = false
                                }
                                .padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}


// Fungsi Utama Aplikasi Stylo dengan Navigasi
@Composable
fun MainApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") { ReusableDrawer(navController) }
        composable("about") { AboutUsPage() }
        composable("gallery") { MoreTopScreen(navController) }
        composable("add photo") { PhotoActivity() }
        composable("homepage") {
            HomeScreen(
                context = LocalContext.current as HomeActivity, // Cast context menjadi HomeActivity
                navController = navController
            )
        }
    }
}

// Preview Utama
@Preview(showBackground = true)
@Composable
fun PreviewMainApp() {
    MainApp()
}
