package com.example.stylo

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
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
import com.example.stylo.ui.theme.tenorFontFamily


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
fun ReusableDrawer(context: Context, onDismiss : () -> Unit) { // Change NavController to Context
    var showMenu by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        // TopBar
        Column {
            StyloTopBar(onMenuClick = { showMenu = !showMenu })
        }

        // Overlay when the menu is active
        if (showMenu) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f))
                    .clickable {
                        showMenu = false
                        onDismiss()
                    }
            )

            // Side Menu with Animation
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
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            // Teks "Stylo" di Tengah
                            Text(
                                text = "Stylo",
                                fontSize = 45.sp,
                                color = Color(0xFF776B5D),
                                fontFamily = miamaFontFamily,
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            "Home", fontSize = 30.sp, fontFamily = tenorFontFamily,
                            modifier = Modifier
                                .clickable {
                                    val intent = Intent(context, HomeActivity::class.java) // Use Intent to navigate
                                    context.startActivity(intent)
                                    showMenu = false
                                    onDismiss()
                                }
                                .padding(8.dp)
                        )
                        Text(
                            "Profile", fontSize = 30.sp, fontFamily = tenorFontFamily,
                            modifier = Modifier
                                .clickable {
                                    val intent = Intent(context, UserProfileActivity::class.java) // Use Intent to navigate
                                    context.startActivity(intent)
                                    showMenu = false
                                    onDismiss()
                                }
                                .padding(8.dp)
                        )
                        Text(
                            "Generate Outfit", fontSize = 30.sp, fontFamily = tenorFontFamily,
                            modifier = Modifier
                                .clickable {
                                    val intent = Intent(context, SelectCategoriesActivity::class.java) // Use Intent to navigate
                                    context.startActivity(intent)
                                    showMenu = false
                                    onDismiss()
                                }
                                .padding(8.dp)
                        )
                        Text(
                            "Gallery", fontSize = 30.sp, fontFamily = tenorFontFamily,
                            modifier = Modifier
                                .clickable {
                                    val intent = Intent(context, SelectPhotosActivity::class.java) // Use Intent to navigate
                                    context.startActivity(intent)
                                    showMenu = false
                                    onDismiss()
                                }
                                .padding(8.dp)
                        )
                        Text(
                            "Add Photo", fontSize = 30.sp, fontFamily = tenorFontFamily,
                            modifier = Modifier
                                .clickable {
                                    val intent = Intent(context, AddPhotoActivity::class.java) // Use Intent to navigate
                                    context.startActivity(intent)
                                    showMenu = false
                                    onDismiss()
                                }
                                .padding(8.dp)
                        )
                        Text(
                            "About Us", fontSize = 30.sp, fontFamily = tenorFontFamily,
                            modifier = Modifier
                                .clickable {
                                    val intent = Intent(context, AboutUsActivity::class.java) // Use Intent to navigate
                                    context.startActivity(intent)
                                    showMenu = false
                                    onDismiss()
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
    val context = LocalContext.current // Get the current context

    Box(modifier = Modifier.fillMaxSize()) {
        ReusableDrawer(context = context, onDismiss = {}) // Pass the context to ReusableDrawer
    }
}

// Preview Utama
@Preview(showBackground = true)
@Composable
fun PreviewMainApp() {
    MainApp()
}
