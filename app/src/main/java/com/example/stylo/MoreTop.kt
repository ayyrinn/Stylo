package com.example.stylo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.stylo.ui.theme.cormorantFontFamily
import com.example.stylo.ui.theme.miamaFontFamily
import com.example.stylo.ui.theme.tenorFontFamily
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MoreTopActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoreTopScreen()
        }
    }
}

@Composable
fun MoreTopScreen() {
    val clothingData = remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()
    val auth = FirebaseAuth.getInstance()
    val userId = auth.currentUser ?.uid // Get the current user's ID
    val context = LocalContext.current

    // Use LaunchedEffect to launch the coroutine
    LaunchedEffect(userId) {
        userId?.let {
            val retrievedData = retrieveClothingDataSuspend(it, "top")
            clothingData.value = retrievedData // Update the state with retrieved data
            println(clothingData.value)
        } ?: Log.e("MoreTopScreen", "User  is not logged in")
    }

    var showMenu by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar
            StyloTopBar(onMenuClick = { showMenu = !showMenu })

            // Main Content
            Text(
                text = "TOP",
                fontSize = 40.sp, // Ukuran font sesuai kebutuhan
                color = Color.White, // Warna font putih
                fontFamily = tenorFontFamily, // Font keluarga sesuai desain
                modifier = Modifier
                    .padding(vertical = 8.dp)
            )


            // Check if clothing data is empty
            if (clothingData.value.isEmpty()) {
                // Display message and button
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "You don't have this collection yet.",
                        color = Color.White,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            // Navigate to AddPhotoActivity
                            val intent = Intent(context, AddPhotoActivity::class.java)
                            context.startActivity(intent)
                        },
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text(text = "Add New Collection")
                    }
                }
            } else {
                // LazyColumn for scrollable content
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(10.dp)
                ) {
                    items(clothingData.value.size) { index ->
                        val clothingItem = clothingData.value[index]
                        val imageUrl = clothingItem["imageurl"] as? String // Assuming the image URL is stored under "imageurl"

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            // Display the image if the URL is not null
                            imageUrl?.let {
                                ImageCard(imageUrl = it, context)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ImageCard(imageUrl: String, context: Context) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(155.dp)
            .clickable { }
    ) {
        Image(
            painter = rememberImagePainter(imageUrl),
            contentDescription = "Clothing Item",
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    println("pressed top")
                    context.startActivity(
                        Intent(context, AIPhotosActivity::class.java)
                            .putExtra("top_image_url", imageUrl)
                    )
                }
                .height(185.dp)
                .background(Color.Gray, shape = RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
    }
}

suspend fun retrieveClothingDataSuspend(userId: String, type: String): List<Map<String, Any>> {
    val db = Firebase.firestore
    val clothingList = mutableListOf<Map<String, Any>>()

    return try {
        val result = db.collection("clothes")
            .whereEqualTo("userID", userId)
            .whereEqualTo("type", type)
            .get()
            .await() // Await the result using Kotlin Coroutines
        for (document in result) {
            clothingList.add(document.data)
        }
        clothingList
    } catch (e: Exception) {
        Log.w("FirebaseError", "Error retrieving documents", e)
        emptyList()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMoreTop() {
    MoreTopScreen()
}
