package com.example.stylo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    Surface(color = MaterialTheme.colorScheme.background, modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Image
            Image(
                painter = painterResource(id = R.drawable.img1), // Replace with your image resource
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) // Adjusts the image height
            )

            // Title
            Text(
                text = "Stylo",
                fontSize = 50.sp,
                color = Color.White,
                fontFamily = FontFamily.Default,
                modifier = Modifier.padding(top = 16.dp)
            )

            // Subtitle
            Text(
                text = "SAY WHO\nYOU ARE WITHOUT\nSPEAKING",
                fontSize = 30.sp,
                color = Color.White,
                fontFamily = FontFamily.Default,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.padding(top = 16.dp)
            )

            // Button
            Button(
                onClick = onButtonClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text(
                    text = "Discover Your Style",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontFamily = FontFamily.Default
                )
            }
        }
    }
}
