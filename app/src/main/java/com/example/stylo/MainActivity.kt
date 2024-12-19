package com.example.stylo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stylo.ui.theme.cormorantFontFamily
import com.example.stylo.ui.theme.miamaFontFamily
import com.example.stylo.ui.theme.tenorFontFamily
import com.google.firebase.auth.FirebaseAuth




class MainActivity : ComponentActivity() {
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()

        // Check if user is already logged in
        val currentUser  = firebaseAuth.currentUser
        Log.d("MainActivity", "Current user: $currentUser ")

        if (currentUser  != null) {
            // If the user is logged in, navigate to HomeActivity and skip this screen
            Log.d("MainActivity", "User  is logged in, navigating to HomeActivity.")
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish() // Close MainActivity so the user can't go back to it
        } else {
            // If the user is not logged in, show the main screen (login/register)
            Log.d("MainActivity", "User  is not logged in, showing MainScreen.")
            setContent {
                MainScreen {
                    // Navigate to RegisterActivity when button is clicked
                    val intent = Intent(this, RegisterActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}

@Composable
fun MainScreen(onButtonClick: () -> Unit) {
    val context = LocalContext.current
    Box(modifier = Modifier.fillMaxSize()) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.home_img_1),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize() // Ensures the image covers the full size of the screen
        )

        Column(
            modifier = Modifier
                .fillMaxSize() // Ensure Column covers the full size of the screen
                .wrapContentHeight(align = Alignment.CenterVertically) // Ensures the Column vertically aligns its content
                .wrapContentWidth(align = Alignment.CenterHorizontally) // Ensures the Column horizontally aligns its content
        ) {
            Text(
                text = "Stylo",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally), // Aligns the text horizontally to the center
                    //.padding(top = 100.dp), // Padding above the text for space
                fontSize = 60.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                fontFamily = miamaFontFamily,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "SAY WHO \n" +
                        "YOU ARE WITHOUT \n" +
                        "SPEAKING",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally) // Aligns the text horizontally to the center
                    .padding(top = 100.dp), // Padding above the text for space
                fontSize = 30.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                fontFamily = cormorantFontFamily,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = onButtonClick,
                modifier = Modifier
                    .padding(top = 150.dp) // Margin atas 50 dp
                    .width(300.dp)        // Lebar tombol
                    .height(50.dp)
                    .align(Alignment.CenterHorizontally),          // Tinggi tombol
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFB0A695) // Warna background tombol
                )
            ) {
                Text(
                    text = "Discover your Style",
                    color = Color.Black, // Warna teks tombol
                    fontSize = 16.sp,
                    fontFamily = TenorSansRegular
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    MainScreen(onButtonClick = { })
}