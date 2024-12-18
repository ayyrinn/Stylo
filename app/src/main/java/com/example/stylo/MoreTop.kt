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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
            val retrievedData = retrieveClothingDataSuspend(it)
            clothingData.value = retrievedData // Update the state with retrieved data
            println(clothingData.value)
        } ?: Log.e("MoreTopScreen", "User  is not logged in")
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Image(
                painter = painterResource(id = R.drawable.burger_icon),
                contentDescription = "Burger Icon",
                modifier = Modifier
                    .size(50.dp)
                    .fillMaxSize()
                    .clickable { }
                    .padding(top = 10.dp)
                // .padding(start = 160.dp, top = 16.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Stylo",
                fontSize = 45.sp,
                color = Color(0xFF776B5D),
                fontFamily = miamaFontFamily,
                //textAlign = TextAlign.Center,
                modifier = Modifier.padding(end = 35.dp),
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "TOP",
            fontSize = 40.sp, // Ukuran font sesuai kebutuhan
            color = Color.White, // Warna font putih
            fontFamily = tenorFontFamily, // Font keluarga sesuai desain
            modifier = Modifier
                .padding(vertical = 8.dp)
        )


        // LazyColumn untuk scrollable konten
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(10.dp)
        ) {
            // Duplikat 10x item gambar kanan kiri
            items(clothingData.value.size) { index ->
                val clothingItem = clothingData.value[index]
                val imageUrl = clothingItem["imageurl"] as? String // Assuming the image URL is stored under "imageurl"

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Display the image if the URL is not null
                    imageUrl?.let {
                        TopImageCard(imageUrl = it, context)
                    }
                }
            }
        }
    }
}

@Composable
fun TopImageCard(imageUrl: String, context: Context) {
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
                    val intent = Intent(context, AIPhotosActivity::class.java).apply {
                        putExtra("top_image_url", imageUrl) // Pass the image URL
                    }
                }
                .height(185.dp)
                .background(Color.Gray, shape = RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
    }
}

suspend fun retrieveClothingDataSuspend(userId: String): List<Map<String, Any>> {
    val db = Firebase.firestore
    val clothingList = mutableListOf<Map<String, Any>>()

    return try {
        val result = db.collection("clothes")
            .whereEqualTo("userID", userId)
            .whereEqualTo("type", "top")
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
