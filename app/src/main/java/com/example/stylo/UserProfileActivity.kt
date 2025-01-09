package com.example.stylo

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.platform.LocalContext
import coil.compose.rememberImagePainter
import com.example.stylo.PhotoActivity
import com.example.stylo.R
import com.example.stylo.TenorSansRegular
import com.example.stylo.ui.theme.cormorantFontFamily
import com.example.stylo.ui.theme.miamaFontFamily
import com.example.stylo.ui.theme.tenorFontFamily
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class UserProfileActivity : ComponentActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProfileScreens()
        }

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()

        // Configure Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }
}

@Composable
fun ProfileScreens() {
    val auth = FirebaseAuth.getInstance()
    val currentUser  = auth.currentUser
    var userName by remember { mutableStateOf(currentUser ?.displayName ?: "User ") } // Fetch display name
    var email by remember { mutableStateOf(currentUser ?.email ?: "user@example.com") } // Fetch email
    var profileImageUri by remember { mutableStateOf<Uri?>(null) } // State for profile image URI
    var gender by remember { mutableStateOf("") }
    var birthdate by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var isDateValid by remember { mutableStateOf(true) }

    var showGenderDialog by remember { mutableStateOf(false) }
    var showSuccessPopup by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        profileImageUri = uri // Update URI when user selects a photo
    }
    var showMenu by remember { mutableStateOf(false) }

    fun fetchUserData() {
        val firestore = FirebaseFirestore.getInstance()
        val userId = auth.currentUser ?.uid
        userId?.let {
            firestore.collection("users").document(it).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        userName = document.getString("username") ?: "User  "
                        email = document.getString("email") ?: "user@example.com"
                        gender = document.getString("gender") ?: ""
                        birthdate = document.getString("birthdate") ?: ""
                        height = document.getString("height") ?: ""
                        weight = document.getString("weight") ?: ""
                        profileImageUri = Uri.parse(document.getString("profileImageUri"))
                    }
                }
                .addOnFailureListener { e ->
                    // Handle the error
                }
        }
    }

    LaunchedEffect(true) {
        fetchUserData()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF3EEEA))
                .verticalScroll(rememberScrollState()),
            //.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally // Center aligns all items horizontally
        ) {
            // Top Bar
            StyloTopBar(onMenuClick = { showMenu = !showMenu })


            // Main Content
            // Greeting Text with Profile Picture
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start // Align to the end
            ) {
                // Profile Image
                if (profileImageUri != null && profileImageUri.toString().isNotEmpty()) {
                    Image(
                        painter = rememberImagePainter(profileImageUri),
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(RoundedCornerShape(50.dp))
                            .clickable { launcher.launch("image/*") } // Launch image picker
                    )
                } else {
                    // Use Material Icon as default profile picture
                    Icon(
                        imageVector = Icons.Filled.Person, // Use the Person icon
                        contentDescription = "Default Profile Icon",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(RoundedCornerShape(50.dp))
                            .clickable { launcher.launch("image/*") } // Launch image picker
                            .background(Color.LightGray, shape = RoundedCornerShape(50.dp)) // Optional: background color
                    )
                }
                Spacer(modifier = Modifier.width(16.dp)) // Space between image and text

                // Greeting Text
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Hi,",
                        fontSize = 30.sp,
                        fontFamily = tenorFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black,
                        textAlign = TextAlign.End
                    )
                    Text(
                        text = userName,
                        fontSize = 40.sp,
                        fontFamily = tenorFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        textAlign = TextAlign.End,
                        modifier = Modifier.widthIn(max = 200.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
            // Background Container
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF3EEEA)), // Background color F3EEEA
                contentAlignment = Alignment.Center
            ) {
                // Card for the Profile
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .wrapContentHeight(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(10.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Label for the Username
                        Text(
                            text = "Username",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = tenorFontFamily,
                            color = Color.Gray, // Label color
                            modifier = Modifier.padding(bottom = 8.dp) // Spacing below the label
                        )

                        // Input for User Name
                        BasicTextField(
                            value = userName,
                            onValueChange = { userName = it },
                            textStyle = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = tenorFontFamily,
                                color = Color.Black,
                                textAlign = TextAlign.Center
                            ),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    // Hide keyboard or perform any other action on Done
                                },
                            ),
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            // Background Container
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF3EEEA)), // Background color F3EEEA
                contentAlignment = Alignment.Center
            ) {
                // Card for the Profile
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .wrapContentHeight(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(10.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        // Label for Email
                        Text(
                            text = "Email",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = tenorFontFamily,
                            color = Color.Gray,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        // Display Email
                        Text(
                            text = email,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = tenorFontFamily,
                            color = Color.Black,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            // Background Container
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF3EEEA)), // Background color F3EEEA
                contentAlignment = Alignment.Center
            ) {
                // Card for the Profile
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .wrapContentHeight(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(10.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Label for the Gender
                        Text(
                            text = "Gender",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = tenorFontFamily,
                            color = Color.Gray,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Box(modifier = Modifier.fillMaxWidth(0.8f)) {
                            Text(
                                text = if (gender.isEmpty()) "Select Gender" else gender,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { showGenderDialog = true },
                                textAlign = TextAlign.Center,
                                fontFamily = tenorFontFamily,
                                fontSize = 20.sp,
                            )
                            if (showGenderDialog) {
                                AlertDialog(
                                    onDismissRequest = { showGenderDialog = false },
                                    title = { Text("Select Gender", fontFamily = TenorSansRegular) },
                                    text = {
                                        Column {
                                            listOf("Male", "Female").forEach { option ->
                                                Text(
                                                    text = option,
                                                    fontFamily = tenorFontFamily,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .clickable {
                                                            gender = option
                                                            showGenderDialog = false
                                                        }
                                                        .padding(16.dp)
                                                )
                                            }
                                        }
                                    },
                                    confirmButton = {
                                        Button(
                                            onClick = { showGenderDialog = false },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color(0xFFDD8560) // Warna tombol
                                            )
                                        ) {
                                            Text(
                                                "Close",
                                                fontFamily = tenorFontFamily
                                            )
                                        }
                                    }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Background Container
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF3EEEA)), // Background color F3EEEA
                contentAlignment = Alignment.Center
            ) {
                // Card for the Profile
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .wrapContentHeight(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(10.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Label for the Birthdate
                        Text(
                            text = "Birthdate",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = tenorFontFamily,
                            color = Color.Gray,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        // Input for Birthdate
                        BasicTextField(
                            value = birthdate,
                            onValueChange = {
                                birthdate = it
                                isDateValid = validateDate(it)
                            },
                            textStyle = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = tenorFontFamily,
                                color = Color.Black,
                                textAlign = TextAlign.Center
                            ),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    // Hide keyboard or perform any other action on Done
                                },
                            ),
                            modifier = Modifier
                                .fillMaxWidth(0.8f),
                        )

                        if (!isDateValid) {
                            Text(
                                text = "Invalid date format. Please use DD/MM/YYYY.",
                                color = Color.Red,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF3EEEA)), // Background color F3EEEA
                contentAlignment = Alignment.Center
            ) {
                // Card for the Profile
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .wrapContentHeight(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(10.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Label for the Weight
                        Text(
                            text = "Weight (kg)",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = tenorFontFamily,
                            color = Color.Gray, // Label color
                            modifier = Modifier.padding(bottom = 8.dp) // Spacing below the label
                        )

                        // Input for Weight
                        BasicTextField(
                            value = weight,
                            onValueChange = { weight = it },
                            textStyle = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = tenorFontFamily,
                                color = Color.Black,
                                textAlign = TextAlign.Center
                            ),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    // Hide keyboard or perform any other action on Done
                                },
                            ),
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF3EEEA)), // Background color F3EEEA
                contentAlignment = Alignment.Center
            ) {
                // Card for the Profile
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .wrapContentHeight(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(10.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Label for the height
                        Text(
                            text = "Height (cm)",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = tenorFontFamily,
                            color = Color.Gray, // Label color
                            modifier = Modifier.padding(bottom = 8.dp) // Spacing below the label
                        )

                        // Input for Height
                        BasicTextField(
                            value = height,
                            onValueChange = { height = it },
                            textStyle = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = tenorFontFamily,
                                color = Color.Black,
                                textAlign = TextAlign.Center
                            ),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    // Hide keyboard or perform any other action on Done
                                },
                            ),
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))


//            Spacer(modifier = Modifier.height(150.dp))
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween, // Memberikan jarak antara elemen atas dan bawah
                horizontalAlignment = Alignment.CenterHorizontally // Menyelaraskan elemen ke tengah secara horizontal
            ) {
                // Tombol Save
                Button(
                    onClick = {
                        saveUserProfile(userName, email, gender, birthdate, height, weight, profileImageUri)
                        showSuccessPopup = true // Show success popup after saving
                        fetchUserData() // Fetch updated data from Firestore
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFDD8560) // Warna tombol
                    )
                ) {
                    Text(
                        text = "Save",
                        fontSize = 20.sp, // Ubah ukuran teks tombol
                        fontFamily = TenorSansRegular,
                        color = Color.White
                    )
                }

                if (showSuccessPopup) {
                    AlertDialog(
                        onDismissRequest = { showSuccessPopup = false },
                        title = { Text("Success", fontFamily = TenorSansRegular) },
                        text = { Text("Profile updated successfully!", fontFamily = TenorSansRegular) },
                        confirmButton = {
                            Button(
                                onClick = { showSuccessPopup = false },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFDD8560) // Warna tombol
                                )
                            ) {
                                Text("OK")
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Tombol Log Out
                Button(
                    onClick = {
                        // Sign out from Firebase
                        auth.signOut()

                        // Sign out from Google
                        val googleSignInClient = GoogleSignIn.getClient(context, GoogleSignInOptions.DEFAULT_SIGN_IN)
                        googleSignInClient.signOut().addOnCompleteListener {
                            // Optionally, navigate to the main activity or show a message
                            context.startActivity(Intent(context, MainActivity::class.java))
                            // Optionally finish the current activity (context as? Activity)?.finish()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent // Warna tombol
                    ),
                    border = BorderStroke(2.dp, Color(0xFFFFA500))
                ) {
                    Text(
                        text = "Log Out",
                        fontSize = 20.sp, // Ubah ukuran teks tombol
                        fontFamily = TenorSansRegular,
                        color = Color(0xFFFFA500)
                    )
                }

            }

        }
        if (showMenu) {
        ReusableDrawer(context = context, onDismiss = { showMenu = false })

    }
    }
}

fun validateDate(date: String): Boolean {
    val datePattern = Regex("^\\d{2}/\\d{2}/\\d{4}$") // Format DD/MM/YYYY
    if (!date.matches(datePattern)) return false

    val parts = date.split("/")
    val day = parts[0].toIntOrNull()
    val month = parts[1].toIntOrNull()
    val year = parts[2].toIntOrNull()

    // Validasi hari, bulan, dan tahun
    if (day == null || month == null || year == null) return false
    if (day !in 1..31 || month !in 1..12 || year < 1900 || year > Calendar.getInstance().get(Calendar.YEAR)) return false

    return true
}

private fun saveUserProfile(userName: String, email: String, gender: String, birthdate: String, height: String, weight: String, profileImageUri: Uri?) {
    val firestore = FirebaseFirestore.getInstance()
    val userId = FirebaseAuth.getInstance().currentUser ?.uid

    val userProfileData = hashMapOf(
        "username" to userName,
        "email" to email,
        "gender" to gender,
        "birthdate" to birthdate,
        "height" to height,
        "weight" to weight,
        "profileImageUri" to profileImageUri.toString()
    )

    userId?.let {
        firestore.collection("users").document(it)
            .set(userProfileData)
            .addOnSuccessListener {
                // Successfully saved user profile data
            }
            .addOnFailureListener { e ->
                // Handle the error
            }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreens()
}
