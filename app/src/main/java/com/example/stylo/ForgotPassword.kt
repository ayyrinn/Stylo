package com.example.stylo

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stylo.ui.theme.miamaFontFamily
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class ForgotPassword : ComponentActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        firebaseAuth = FirebaseAuth.getInstance()

        setContent {
            ForgotPasswordScreen { email ->
                resetPassword(email)
            }
        }
    }

    private fun resetPassword(email: String) {
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Email sent.")
                    Toast.makeText(this, "Reset link sent to your email.", Toast.LENGTH_SHORT).show()
                    navigateToSignIn() // Navigate back to Sign-In activity
                } else {
                    Log.w(TAG, "sendPasswordResetEmail:failure", task.exception)
                    Toast.makeText(this, "Failed to send reset email: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun navigateToSignIn() {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }
}

val Miama = FontFamily(
    Font(R.font.miama)
)

val TenorSansRegular = FontFamily(
    Font(R.font.tenorsans_regular)
)

val backgroundColor = Color(0xB0A695).copy(alpha = 0.9f)

@Composable
fun ForgotPasswordScreen(onResetPasswordClick: (String) -> Unit) {
    var email by remember { mutableStateOf("") }
    var emailSent by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.backround_login_register),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        //aaaaaaaa
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo Text
            Text(
                text = "Stylo",
                fontFamily = miamaFontFamily,
                fontSize = 55.sp,
                color = Color.White,
                modifier = Modifier.padding(top = 16.dp)
            )

            // Forgot Password Text
            Text(
                text = "FORGOT\nPASSWORD",
                fontFamily = TenorSansRegular,
                fontSize = 40.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 30.dp, top = 150.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

             //Password Field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email", fontSize = 15.sp, fontFamily = TenorSansRegular) },
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth(0.9f)
                    .height(48.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color(0xB0A695).copy(alpha = 0.9f)),
            )

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Forgot Password Button
            Button(
                onClick = {
                    if (email.isNotEmpty()) {
                        onResetPasswordClick(email)
                    } else {
                        errorMessage = "Please enter your email."
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFDD8560) // Warna tombol
                )
            ) {
                Text(
                    text = "Reset Password",
                    fontSize = 18.sp,
                    fontFamily = TenorSansRegular,
                    color = Color.White
                )
            }
            if (emailSent) {
                Text(
                    text = "Reset link sent! Check your email.",
                    color = Color.Green,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }

}
@Preview
@Composable
fun PreviewForgotPassword(){
    ForgotPasswordScreen{}
}