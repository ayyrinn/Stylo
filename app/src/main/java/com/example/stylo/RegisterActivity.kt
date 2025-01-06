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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stylo.ui.theme.miamaFontFamily
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
import com.google.firebase.firestore.FirebaseFirestore

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
            RegisterScreen(
                onGoogleSignInClick = { signInGoogle() },
                onLoginClick = { email, password -> loginUser (email, password) }
            )
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
//                    SavedPreference.setEmail(this, user.email.toString())
//                    SavedPreference.setUsername(this, user.displayName.toString())
                    saveUserProfile(user.uid, user.displayName ?: "User ", user.email ?: "user@example.com")
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

    private fun loginUser(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Login successful
                    val user = firebaseAuth.currentUser
                    if (user != null) {
                        // Check if user profile exists in Firestore
                        checkUserProfile(user.uid, user.displayName ?: "User ", user.email ?: "user@example.com")
                    }
                } else {
                    // Login failed
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(this, "Authentication Failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun checkUserProfile(userId: String, username: String, email: String) {
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // User profile exists, proceed to HomeActivity
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // User profile does not exist, create it
                    saveUserProfile(userId, username, email)
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error checking user profile: ${e.message}")
            }
    }


    private fun saveUserProfile(userId: String, username: String, email: String) {
        val firestore = FirebaseFirestore.getInstance()

        val userProfileData = hashMapOf(
            "username" to username,
            "email" to email,
            "gender" to "", // Default value or leave empty
            "birthdate" to "", // Default value or leave empty
            "height" to "", // Default value or leave empty
            "weight" to "", // Default value or leave empty
            "profileImageUri" to "" // Default value or leave empty
        )

        firestore.collection("users").document(userId) // Use userId as the document ID
            .set(userProfileData)
            .addOnSuccessListener {
                // Successfully saved user profile data
                Log.d("Firestore", "User  profile created for $username")
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                // Handle the error
                Log.e("Firestore", "Error saving user profile: ${e.message}")
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
fun RegisterScreen(onGoogleSignInClick: () -> Unit, onLoginClick: (String, String) -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Box(modifier = Modifier.fillMaxSize()) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.backround_login_register),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            // Logo
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
                    text = "SIGN IN",
                    fontFamily = TenorSansRegular,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 30.dp, top = 150.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Email Field
                TextField(
                    value = "",
                    onValueChange = {},
                    placeholder = { Text("Email") },
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth(0.9f)
                        .height(48.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color(0xB0A695).copy(alpha = 0.9f)),

                )


                // Password Field
                TextField(
                    value = "",
                    onValueChange = {},
                    placeholder = { Text("Password") },
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth(0.9f)
                        .height(48.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color(0xB0A695).copy(alpha = 0.9f)),
                )

                // Remember Me Checkbox
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 16.dp, top = 1.dp)
                ) {
                    Text(
                    text = "Forgot Password?",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontFamily = TenorSansRegular,
                    modifier = Modifier.padding(top = 1.dp)
                    )
                    Spacer(modifier = Modifier.width(45.dp))
                    Checkbox(
                        checked = false,
                        onCheckedChange = {},
                        colors = CheckboxDefaults.colors(checkedColor = Color.White)
                    )

                    Text(
                        text = "Remember me",
                        color = Color.White,
                        fontFamily = TenorSansRegular,
                        fontSize = 15.sp
                    )
                }
                Spacer(modifier = Modifier.height(30.dp))

                // Log In Button
                Button(
                    onClick = { onLoginClick(email, password) },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFDD8560) // Warna tombol
                    )
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
                    fontFamily = TenorSansRegular, // Terapkan font Tenor Sans
                    modifier = Modifier
                        .padding(top = 15.dp, bottom = 15.dp)
                )

                // Sign In with Google Button
                Button(
                    onClick = { onGoogleSignInClick() },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(48.dp),
//                        .padding(top = 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFDD8560) // Warna tombol
                    )
                ){
                    Text("Sign in with Google")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRegisterScreen() {
    RegisterScreen(
        onGoogleSignInClick = {},
        onLoginClick = { email, password ->
            // Simulate login action
            println("Email: $email, Password: $password")
        }
    )
}