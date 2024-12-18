package com.example.stylo

//import AppNavigation
import ProfileScreen
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
//import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import android.widget.Button as WidgetButton
import com.example.stylo.ui.theme.StyloTheme
import com.google.firebase.auth.FirebaseAuth


class MainActivity : ComponentActivity() {
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //installSplashScreen()

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()

        // Check if user is already logged in
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            // If the user is logged in, navigate to HomeActivity and skip this screen
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish() // Close MainActivity so the user can't go back to it
        } else {
            // If the user is not logged in, show the main screen (login/register)
            setContentView(R.layout.activity_main)

            // Initialize the button and set the click listener
            val btnMain = findViewById<Button>(R.id.btn_main)
            btnMain.setOnClickListener {
                // Create an Intent to start RegisterActivity
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
    }


}


