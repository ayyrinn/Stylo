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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stylo.ui.theme.StyloTheme
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
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivity : ComponentActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val Req_Code: Int = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        // Configure Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        firebaseAuth = FirebaseAuth.getInstance()

        setContent {
            StyloTheme {
                SignUpScreen(
                    onSignUpClick = { name, email, password -> registerUser (name, email, password) },
                    onGoogleSignInClick = { signInGoogle() },
                    onSignInClick = { navigateToSignIn() }
                )
            }
        }
    }

    private fun registerUser (name: String, email: String, password: String) {
        // Check if the email is already in use
        firebaseAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val signInMethods = task.result?.signInMethods
                if (signInMethods != null && signInMethods.isNotEmpty()) {
                    // Email is already in use
                    Toast.makeText(this, "Email is already taken.", Toast.LENGTH_SHORT).show()
                } else {
                    // Proceed with registration
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Registration successful
                            val user = firebaseAuth.currentUser
                            if (user != null) {
                                // Save user profile in Firestore
                                saveUserProfile(user.uid, name, email, "")
                            }
                        } else {
                            // Registration failed
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(this, "Registration Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Log.e(TAG, "Error checking email: ${task.exception?.message}")
            }
        }
    }

    private fun saveUserProfile(userId: String, username: String, email: String, profilePictureUrl: String) {
        val firestore = FirebaseFirestore.getInstance()

        val userProfileData = hashMapOf(
            "username" to username,
            "email" to email,
            "gender" to "", // Default value or leave empty
            "birthdate" to "", // Default value or leave empty
            "height" to "", // Default value or leave empty
            "weight" to "", // Default value or leave empty
            "profileImageUri" to profilePictureUrl,
        )

        firestore.collection("users").document(userId) // Use userId as the document ID
            .set(userProfileData)
            .addOnSuccessListener {
                // Successfully saved user profile data
                Log.d("Firestore", "User profile created for $username")
                navigateToHome()
            }
            .addOnFailureListener { e ->
                // Handle the error
                Log.e("Firestore", "Error saving user profile: ${e.message}")
            }
    }

    private fun signInGoogle() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, Req_Code)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Req_Code) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleGoogleSignInResult(task)
        }
    }

    private fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            if (account != null) {
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign-in success
                        val user = firebaseAuth.currentUser
                        if (user != null) {
                            val firestore = FirebaseFirestore.getInstance()
                            firestore.collection("users").document(user.uid).get()
                                .addOnSuccessListener { document ->
                                    if (document.exists()) {
                                        // User already exists, navigate to home
                                        navigateToHome()
                                    } else {
                                        // New user, save profile
                                        val profilePictureUrl = account.photoUrl.toString()
                                        saveUserProfile(user.uid, account.displayName ?: "User ", account.email ?: "user@example.com", profilePictureUrl)
                                    }
                                }
                        }
                    } else {
                        // Sign-in failed
                        Toast.makeText(this, "Authentication Failed.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } catch (e: ApiException) {
            Toast.makeText(this, "Sign-in failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToSignIn() {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
    }
}

@Composable
fun SignUpScreen(onSignUpClick: (String, String, String) -> Unit, onGoogleSignInClick: () -> Unit, onSignInClick: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var passwordMismatch by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.backround_login_register),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
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

                // Title
                Text(
                    text = "SIGN UP",
                    fontFamily = TenorSansRegular,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 30.dp, top = 80.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Name Field
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = {
                        Text(
                            "Name",
                            fontFamily = TenorSansRegular,
                            fontSize = 15.sp
                        )
                    },
                    textStyle = TextStyle(
                        fontFamily = TenorSansRegular, // Font sesuai aplikasi
                        fontSize = 15.sp
                    ),
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth(0.9f)
                        .height(48.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color(0xB0A695).copy(alpha = 0.9f)),
                )

                // Email Field
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = {
                        Text(
                            "Email",
                            fontFamily = TenorSansRegular,
                            fontSize = 15.sp
                        )
                    },
                    textStyle = TextStyle(
                        fontFamily = TenorSansRegular, // Font sesuai aplikasi
                        fontSize = 15.sp
                    ),
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth(0.9f)
                        .height(48.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color(0xB0A695).copy(alpha = 0.9f)),
                )

                // Password Field
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = {
                        Text(
                            "Password",
                            fontFamily = TenorSansRegular,
                            fontSize = 15.sp
                        )
                    },
                    textStyle = TextStyle(
                        fontFamily = TenorSansRegular, // Font sesuai aplikasi
                        fontSize = 15.sp
                    ),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        Text(
                            text = if (passwordVisible) "Hide" else "Show",
                            fontFamily = TenorSansRegular,
                            color = Color.Gray,
                            modifier = Modifier
                                .clickable { passwordVisible = !passwordVisible }
                                .padding(end = 8.dp)
                        )
                    },
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth(0.9f)
                        .height(48.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color(0xB0A695).copy(alpha = 0.9f)),
                )

                // Confirm Password Field
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    placeholder = {
                        Text(
                            "Confirm Password",
                            fontFamily = TenorSansRegular,
                            fontSize = 15.sp
                        )
                    },
                    textStyle = TextStyle(
                        fontFamily = TenorSansRegular, // Font sesuai aplikasi
                        fontSize = 15.sp
                    ),
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        Text(
                            text = if (confirmPasswordVisible) "Hide" else "Show",
                            fontFamily = TenorSansRegular,
                            color = Color.Gray,
                            modifier = Modifier
                                .clickable { confirmPasswordVisible = !confirmPasswordVisible }
                                .padding(end = 8.dp)
                        )
                    },
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth(0.9f)
                        .height(48.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color(0xB0A695).copy(alpha = 0.9f)),
                )

                if (passwordMismatch) {
                    Text(
                        text = "Passwords do not match",
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))

                // Sign Up Button
                Button(
                    onClick = {
                        if (password == confirmPassword) {
                            onSignUpClick(name, email, password)
                        } else {
                            passwordMismatch = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFDD8560) // Button color
                    )
                ) {
                    Text(
                        text = "Sign Up",
                        color = Color.White,
                        fontFamily = TenorSansRegular,
                        fontSize = 18.sp
                    )
                }

                // OR Text
                Text(
                    text = "OR",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontFamily = TenorSansRegular,
                    modifier = Modifier
                        .padding(top = 15.dp, bottom = 15.dp)
                )

                // Sign Up with Google Button
                Button(
                    onClick = { onGoogleSignInClick() },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(48.dp),
//                        .padding(top = 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White // Warna tombol
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start // Align items to the start
                    ) {
                        // Google Logo
                        Image(
                            painter = painterResource(id = R.drawable.googlelogo), // Replace with your Google logo resource
                            contentDescription = "Google Logo",
                            modifier = Modifier
                                .size(30.dp) // Adjust size as needed
                                .padding(end = 8.dp) // Space between logo and text
                        )
                        // Text
                        Text(
                            text = "Sign Up With Google",
                            fontFamily = TenorSansRegular,
                            fontSize = 15.sp,
                            color = Color.Black // Set text color to white
                        )
                    }
                }

                // Sign Up Button
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Already have an account? Sign in here!",
                    color = Color.LightGray,
                    fontFamily = TenorSansRegular,
                    fontSize = 15.sp,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier
                        .clickable { onSignInClick() }
                        .padding(top = 10.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    StyloTheme {
        SignUpScreen(onSignUpClick = { _, _, _ -> }, onGoogleSignInClick = {}, onSignInClick = {})
    }
}