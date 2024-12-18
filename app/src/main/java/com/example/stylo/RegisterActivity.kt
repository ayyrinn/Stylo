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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.android.material.button.MaterialButton

class RegisterActivity : ComponentActivity() {
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val Req_Code: Int = 123
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        firebaseAuth = FirebaseAuth.getInstance()

        setContent {
            RegisterScreen { signInGoogle() }
        }
    }

    private fun signInGoogle() {
        Log.d("RegisterActivity", "Starting Google Sign-In")
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, Req_Code)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Req_Code) {
            Log.d("RegisterActivity", "Received sign-in result")
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
    }

    private fun handleResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            if (account != null) {
                Log.d("RegisterActivity", "Sign-in successful: ${account.email}")
                updateUI(account)
            }
        } catch (e: ApiException) {
            Log.w("RegisterActivity", "Sign-in failed: ${e.statusCode}")
            Toast.makeText(this, "Sign-in failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Sign-in success
                val user = firebaseAuth.currentUser
                if (user != null) {
                    SavedPreference.setEmail(this, user.email.toString())
                    SavedPreference.setUsername(this, user.displayName.toString())
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            } else {
                // Sign-in failed
                Log.w(TAG, "signInWithCredential:failure", task.exception)
                Toast.makeText(this, "Authentication Failed.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (GoogleSignIn.getLastSignedInAccount(this) != null) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }
}

@Composable
fun RegisterScreen(onGoogleSignInClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Background Image
            Image(
                painter = painterResource(id = R.drawable.backround_login_register), // Replace with your image resource
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            )

            // Logo
            Text(
                text = "Stylo",
                fontSize = 55.sp,
                color = Color.White,
                fontFamily = FontFamily.Default,
                modifier = Modifier.padding(top = 32.dp)
            )

            // Title
            Text(
                text = "SIGN IN",
                fontSize = 50.sp,
                color = Color.White,
                fontFamily = FontFamily.Default,
                modifier = Modifier.padding(top = 16.dp)
            )

            // Email Field
            TextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Email") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.LightGray,
                    unfocusedContainerColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            // Password Field
            TextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Password") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.LightGray,
                    unfocusedContainerColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            // Forgot Password
            Text(
                text = "Forgot Password?",
                color = Color.White,
                fontSize = 18.sp,
                modifier = Modifier.padding(top = 8.dp)
            )

            // Remember Me Checkbox
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Checkbox(
                    checked = false,
                    onCheckedChange = {},
                    colors = CheckboxDefaults.colors(checkedColor = Color.White)
                )
                Text(
                    text = "Remember Me",
                    color = Color.White,
                    fontSize = 18.sp
                )
            }

            // Log In Button
            Button(
                onClick = { /* Handle Log In */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            ) {
                Text(
                    text = "Log In",
                    color = Color.White,
                    fontSize = 20.sp
                )
            }

            // OR Text
            Text(
                text = "OR",
                color = Color.White,
                fontSize = 20.sp,
                modifier = Modifier.padding(top = 32.dp)
            )

            // Sign In with Google Button
            Button(
                onClick = onGoogleSignInClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text("Sign in with Google")
            }
        }
    }
}
