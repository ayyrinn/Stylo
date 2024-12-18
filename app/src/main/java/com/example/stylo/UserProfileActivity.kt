package com.example.stylo

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.stylo.PhotoActivity
import com.example.stylo.R
import com.example.stylo.TenorSansRegular
import com.example.stylo.ui.theme.miamaFontFamily
import com.google.firebase.auth.FirebaseAuth

class UserProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProfileScreens()
        }
    }
}

@Composable
fun ProfileScreens() {
    val auth = FirebaseAuth.getInstance()
    val currentUser  = auth.currentUser
    var userName by remember { mutableStateOf(currentUser ?.displayName ?: "User ") } // Fetch display name
    var email by remember { mutableStateOf(currentUser ?.email ?: "user@example.com") } // Fetch email
    var profileImageUri by remember { mutableStateOf<Uri?>(null) } // State for profile image URI

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        profileImageUri = uri // Update URI when user selects a photo
    }
    var showMenu by remember { mutableStateOf(false) }
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
            // Teks "Hi," dan nama pengguna
            // Greeting Text
            Column(
                modifier = Modifier.fillMaxWidth().padding(end = 50.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "Hi,",
                    fontSize = 40.sp,
                    fontFamily = TenorSansRegular,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    textAlign = TextAlign.End
                )
                Text(
                    text = userName,
                    fontSize = 50.sp,
                    fontFamily = TenorSansRegular,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.End
                )
            }

            Spacer(modifier = Modifier.height(100.dp))
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
                            color = Color.Gray, // Label color
                            modifier = Modifier.padding(bottom = 8.dp) // Spacing below the label
                        )

                        // Input for User Name
                        BasicTextField(
                            value = userName,
                            onValueChange = { userName = it },
                            textStyle = TextStyle(
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Medium,
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
                                .padding(horizontal = 16.dp)
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
                            color = Color.Gray,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Text(
                            text = "Email",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Gray,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        // Input for Email
                        BasicTextField(
                            value = email,
                            onValueChange = { email = it },
                            textStyle = TextStyle(
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                textAlign = TextAlign.Center
                            ),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    // Action when "Done" is pressed
                                }
                            ),
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .padding(horizontal = 16.dp)
                                .background(Color.White)
                                .focusable(true)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(150.dp))
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween, // Memberikan jarak antara elemen atas dan bawah
                horizontalAlignment = Alignment.CenterHorizontally // Menyelaraskan elemen ke tengah secara horizontal
            ) {
                // Spacer untuk membuat bagian atas kosong
                Spacer(modifier = Modifier.weight(1f)) // Elemen pertama mengambil ruang kosong di atas

                // Tombol Log Out
                Button(
                    onClick = { auth.signOut() },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFDD8560) // Warna tombol
                    )
                ) {
                    Text(
                        text = "Log Out",
                        fontSize = 20.sp, // Ubah ukuran teks tombol
                        fontFamily = TenorSansRegular,
                        color = Color.White
                    )
                }

            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreens()
}
