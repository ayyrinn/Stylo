package com.example.stylo

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation.width
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.target.CustomTarget
import com.example.stylo.ui.theme.cormorantFontFamily
import com.example.stylo.ui.theme.miamaFontFamily
import com.example.stylo.ui.theme.tenorFontFamily
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class AIPhotosActivity : ComponentActivity() {
    private var selectedCategory: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val imageUrl: String = intent.getStringExtra("image_url") ?: ""
        selectedCategory = getSelectedCategory(this)

        Log.d("AIPhotosActivity", "Received selected category: $selectedCategory")

        setContent {
            AIPhotosScreen(imageUrl, selectedCategory)
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save the selected category
        outState.putString("selected_category", selectedCategory)
    }
}

@Composable
fun AIPhotosScreen(imageUrl: String, selectedCategory: String?) {
    Log.d("check", "here")
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val auth = FirebaseAuth.getInstance()
    val userId = auth.currentUser?.uid // Get the current user's ID

    var top by remember { mutableStateOf<Bitmap?>(null) } //ini buat gambar yg ditunjukkin di screennya
    var bottom by remember { mutableStateOf<Bitmap?>(null) }
    var footwear by remember { mutableStateOf<Bitmap?>(null) }
    var accessories by remember { mutableStateOf<Bitmap?>(null) }

    var topData by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }//buat deskripsi baju" yg dipilih user
    var bottomData by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var footwearData by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var accessoriesData by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }

    var recsResponse by remember { mutableStateOf("") } // State to store the response
    var loading by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }

    var typeData by remember { mutableStateOf("") } // State to store the response

    var clothingData by remember { mutableStateOf("") }

    var wardrobeData by remember { mutableStateOf("") }

    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }

    var showMenu by remember { mutableStateOf(false) }

    LaunchedEffect(userId) {
        userId?.let {
            fetchUserData(it) { userHeight, userWeight, userGender ->
                height = userHeight
                weight = userWeight
                gender = userGender
            }
        }
    }

    if(imageUrl != ""){
        LaunchedEffect(imageUrl) {
            if (imageUrl.isNotEmpty()) {
                typeData = retrieveTypeSuspend(imageUrl)
                loadImage(imageUrl, context) { bitmap ->
                    when (typeData.lowercase()) {
                        "top" -> top = bitmap
                        "bottom" -> bottom = bitmap
                        "footwear" -> footwear = bitmap
                        "accessories" -> accessories = bitmap
                    }
                }

                if (typeData.equals("top", ignoreCase = true)){
                    topData = retrieveDataSuspend(imageUrl) // Update the state with retrieved data
                } else if (typeData.equals("bottom", ignoreCase = true)){
                    bottomData = retrieveDataSuspend(imageUrl) // Update the state with retrieved data
                }else if (typeData.equals("footwear", ignoreCase = true)){
                    footwearData = retrieveDataSuspend(imageUrl) // Update the state with retrieved data
                }else if (typeData.equals("accessories", ignoreCase = true)){
                    accessoriesData = retrieveDataSuspend(imageUrl) // Update the state with retrieved data
                }

            }
        }


    }

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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color(0xFFF3EEEA)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {

                // Selected Outfit
                Text(
                    text = "SELECTED OUTFIT",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontFamily = cormorantFontFamily,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 10.dp)
                )

                // Row for buttons at the top
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 1.dp),
                    horizontalArrangement = Arrangement.spacedBy(
                        20.dp,
                        Alignment.CenterHorizontally
                    ), // Centers and spaces items
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left Button
                    Box(
                        modifier = Modifier
                            .clickable { }
                            .width(155.dp)
                            .height(215.dp)
                            .background(
                                Color.Transparent,
                                shape = RectangleShape
                            ) // Adjust shape if needed
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            top?.let {
                                Image(
                                    bitmap = it.asImageBitmap(),
                                    contentDescription = "Processed Image",
                                    modifier = Modifier
                                        .size(362.dp, 362.dp)
                                        .clickable {
                                            context.startActivity(
                                                Intent(context, MoreTopActivity::class.java)
                                            )
                                        }
                                )
                            } ?: Image(
                                painter = painterResource(id = R.drawable.foto_jas),
                                contentDescription = "Camera Left",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        context.startActivity(
                                            Intent(context, MoreTopActivity::class.java)
                                        )
                                    }
                                    .height(185.dp)
                            )
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFFFCDFD2))
                                    .fillMaxWidth()
                                    .height(30.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 10.dp), // Optional: Adds horizontal padding
                                    horizontalArrangement = Arrangement.SpaceBetween, // Space between Text and Image
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Top",
                                        color = Color.Black,
                                        fontFamily = cormorantFontFamily,
                                        modifier = Modifier.align(Alignment.CenterVertically)
                                    )
                                    Image(
                                        painter = painterResource(id = R.drawable.outline_photo_library_24),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .height(30.dp)
                                            .align(Alignment.CenterVertically)
                                    )
                                }
                            }
                        }

                    }


                    Box(
                        modifier = Modifier
                            .clickable { }
                            .width(155.dp)
                            .height(215.dp)
                            .background(
                                Color.Transparent,
                                shape = RectangleShape
                            ) // Adjust shape if needed
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            bottom?.let {
                                Image(
                                    bitmap = it.asImageBitmap(),
                                    contentDescription = "Processed Image",
                                    modifier = Modifier
                                        .size(362.dp, 362.dp)
                                        .clickable {
                                            context.startActivity(
                                                Intent(context, MoreBottomActivity::class.java)
                                            )
                                        }
                                )
                            } ?: Image(
                                painter = painterResource(id = R.drawable.foto_jas),
                                contentDescription = "Camera Left",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        context.startActivity(
                                            Intent(context, MoreBottomActivity::class.java)
                                        )
                                    }
                                    .height(185.dp)
                            )
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFFFCDFD2))
                                    .fillMaxWidth()
                                    .height(30.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 10.dp), // Optional: Adds horizontal padding
                                    horizontalArrangement = Arrangement.SpaceBetween, // Space between Text and Image
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Bottom",
                                        color = Color.Black,
                                        fontFamily = cormorantFontFamily,
                                        modifier = Modifier.align(Alignment.CenterVertically)
                                    )
                                    Image(
                                        painter = painterResource(id = R.drawable.outline_photo_library_24),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .height(30.dp)
                                            .align(Alignment.CenterVertically)
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(5.dp)) // Adjust this value for top padding

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color(0xFFF3EEEA)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center // Centers content vertically
                ) {
                    Spacer(modifier = Modifier.height(10.dp)) // Adjust this value for top padding
                    // Response Box
                    Box(
                        modifier = Modifier
                            .width(370.dp)
                            .height(380.dp)
                            .background(Color.Transparent)
                            .border(
                                width = 1.dp,              // Border thickness
                                color = Color(0xFFDD8560), // Border color (orange)
                                shape = RoundedCornerShape(45.dp)  // Adjust corner radius here
                            ) // Thin orange outline
                            .padding(16.dp),
                        contentAlignment = Alignment.Center // Center content within the Box
                    ) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(), // Fill the available space
                            content = {
                                if (loading) {
                                    item {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            CircularProgressIndicator(
                                                modifier = Modifier.size(64.dp),
                                                color = MaterialTheme.colorScheme.secondary,
                                                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                                            )
                                        }
                                    }
                                } else if (recsResponse.isNotEmpty()) {
                                    Log.d("AIPhotosActivity", "Response received: $recsResponse")
                                    Log.d("AIPhotosActivity", "Starting to parse the AI response")
                                    val clothingItems = parseAIResponse(recsResponse)
                                    items(clothingItems) { item ->
                                        OutfitCard(item, context) // Assuming you have an OutfitCard composable
                                    }
                                } else {
                                    item {
                                        // Center the message if the response is empty
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center // Center the message
                                        ) {
                                            Text(
                                                text = "Choose one clothing item to start generating outfit.",
                                                color = Color.Gray,
                                                fontFamily = tenorFontFamily,
                                                fontSize = 14.sp,
                                                modifier = Modifier.padding(8.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp), // Optional: Adds horizontal padding
                        horizontalArrangement = Arrangement.SpaceBetween, // Space between Text and Image
                        verticalAlignment = Alignment.CenterVertically
                    ) {
//                        // Send a Message Box
//                        Box(
//                            modifier = Modifier
//                                .width(250.dp)
//                                .height(50.dp)
//                                .background(Color.Transparent)
//                                .border(
//                                    width = 1.dp,              // Border thickness
//                                    color = Color(0xFFDD8560), // Border color (orange)
//                                    shape = RoundedCornerShape(45.dp)  // Adjust corner radius here
//                                ) // Thin orange outline
//                                .padding(16.dp)
//                        ) {
//                            Text(
//                                text = "Send a message",
//                                color = Color.Gray,               // Adjust text color
//                                fontSize = 14.sp,                  // Adjust text size
//                                fontFamily = tenorFontFamily      // Optional: Change to a specific font family
//                            )
//                        }

                        // Submit Button
                        Button(
                            onClick = {
                                // Check if any clothing items are selected
                                if (topData.isEmpty() && bottomData.isEmpty()) {
                                    showErrorDialog = true
                                } else {
                                    loading = true // Show loading indicator
                                    coroutineScope.launch {
                                        // Combine clothing data for recommendations
                                        val clothingData = (topData + bottomData + footwearData + accessoriesData).toString()
                                        println("clothing data: $clothingData")
                                        // Get all wardrobe data
                                        val wardrobeData = userId?.let { retrieveWardrobeSuspend(it) }.toString()
                                        println("wardrobe data: $wardrobeData")

                                        // Get response
                                        recsResponse = generateRecs(context, clothingData, wardrobeData, selectedCategory, height, weight, gender)
                                            ?: "No recommendations available."
                                        loading = false // Hide loading indicator after response
                                    }
                                }
                            },
                            modifier = Modifier
                                .width(370.dp)
                                .height(50.dp)
                                .background(Color(0xFFDD8560), shape = RoundedCornerShape(45.dp))
                                .padding(4.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDD8560)), // Set button color
                        ) {
                            Text(
                                text = "Generate",
                                color = Color.White,               // Adjust text color
                                fontSize = 14.sp,                  // Adjust text size
                                fontFamily = tenorFontFamily
                            )
                        }
                    }
                }
            }
        }
    }
    // Error Dialog
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text("Error") },
            text = { Text ("Please select at least one clothing item (Top or Bottom).") },
            confirmButton = {
                Button(
                    onClick = { showErrorDialog = false }
                ) {
                    Text("OK")
                }
            }
        )
    }
}

@Preview
@Composable
fun Previews(){
    AIPhotosScreen("","")

}

private fun loadImage(imageUrl: String, context: Context, onImageLoaded: (Bitmap) -> Unit) {
    Glide.with(context)
        .asBitmap()
        .load(imageUrl)
        .into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?) {
                onImageLoaded(resource)
            }

            override fun onLoadCleared(placeholder: Drawable?) {
                // Handle cleanup if needed
            }

            override fun onLoadFailed(errorDrawable: Drawable?) {
                Log.e("AIPhotosActivity", "Image load failed: ${errorDrawable?.toString()}")
            }
        })
}

suspend fun retrieveDataSuspend(image_url: String): List<Map<String, Any>> {
    val db = Firebase.firestore
    val clothingList = mutableListOf<Map<String, Any>>()

    return try {
        val result = db.collection("clothes")
            .whereEqualTo("imageurl", image_url)

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

suspend fun retrieveWardrobeSuspend(userId: String): List<Map<String, Any>> {
    val db = Firebase.firestore
    val clothingList = mutableListOf<Map<String, Any>>()

    return try {
        val result = db.collection("clothes")
            .whereEqualTo("userID", userId)

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

//suspend fun retrieveTypeSuspend(userId: String, imageUrl: String): Any {
//    val db = Firebase.firestore
//    return try {
//        val result = db.collection("clothes")
//            .whereEqualTo("userID", userId)
//            .whereEqualTo("imageurl", imageUrl)
//            .get()
//            .await() // Await the result using Kotlin Coroutines
//
//        // Get the "type" field from the first matching document
//        result.get("type") as String ?: "No type found"
//    } catch (e: Exception) {
//        Log.w("FirebaseError", "Error retrieving documents", e)
//        "Error retrieving type"
//    }
//}
suspend fun retrieveTypeSuspend(imageUrl: String): String {
    val db = FirebaseFirestore.getInstance()
    return try {
        val result = db.collection("clothes")
            .whereEqualTo("imageurl", imageUrl)
            .get()
            .await()

        result.documents.firstOrNull()?.getString("type") ?: "No type found"
    } catch (e: Exception) {
        Log.w("FirebaseError", "Error retrieving type", e)
        "Error retrieving type"
    }
}

suspend fun generateRecs(context: Context, clothingData: String, wardrobeData: String, selectedCategory: String?, height: String, weight: String, gender: String): String? {
    // Convert the clothing data to a string representation for logging
//    val clothingDataString = clothingData.joinToString(", ") { it?.toString() ?: "null" }

    // Log the clothing data being sent
    Log.d("AIPhotosActivity", "Clothing Data: $clothingData")
    Log.d("AIPhotosScreen", "Selected category in AIPhotosScreen: $selectedCategory")

// temp in case i need it again: ${selectedCategory ?: "No category selected"}

    // Construct the prompt
    val prompt = """
        I want you to help me style some clothing items. 
        Here are the clothing items I want to wear: $clothingData.
        Additionally, here are the items in my wardrobe: $wardrobeData.
        The selected category is: $selectedCategory.
        Here are some additional information: height is $height cm, weight is $weight kg, and gender is $gender.
        
        Please provide recommendations on how to style the clothing items I want to wear, 
        including suggestions from my wardrobe. 

        If I provided more than one clothing item, please give me suggestions on how to style them together. 
        Format your response as a numbered list of outfits, where each outfit contains:
        - A description of the outfit
        - The clothing items included in the outfit
        - Each outfit should be numbered (e.g., "Outfit 1:", "Outfit 2:", etc.)
        
        Each clothing item should include:
        - "type": The type of clothing (e.g., "Top", "Bottom", "Footwear", "Accessory")
        - "imageurl": The URL of the clothing item image
        - "description": A brief description of the clothing item
        
        Example response:
        [
            {
                "outfit_number": 1,
                "description": "A casual look perfect for a day out.",
                "items": [
                    {
                        "type": "Top",
                        "imageurl": "https://example.com/white-shirt.png",
                        "description": "A classic white shirt."
                    },
                    {
                        "type": "Bottom",
                        "imageurl": "https://example.com/blue-jeans.png",
                        "description": "Comfortable blue jeans."
                    },
                    {
                        "type": "Footwear",
                        "imageurl": "https://example.com/sneakers.png",
                        "description": "White sneakers."
                    },
                    {
                        "type": "Accessory",
                        "imageurl": "https://example.com/leather-belt.png",
                        "description": "A brown leather belt."
                    }
                ]
            },
            {
                "outfit_number": 2,
                "description": "A smart casual outfit for a dinner.",
                "items": [
                    {
                        "type": "Top",
                        "imageurl": "https://example.com/blue-blazer.png",
                        "description": "A stylish blue blazer."
                    },
                    {
                        "type": "Top",
                        "imageurl": "https://example.com/white-shirt.png",
                        "description": "A classic white shirt."
                    },
                    {
                        "type": "Bottom",
                        "imageurl": "https://example.com/chinos.png",
                        "description": "Beige chinos."
                    },
                    {
                        "type": "Footwear",
                        "imageurl": "https://example.com/loafers.png",
                        "description": "Brown loafers."
                    }
                ]
            }
        ]
    """.trimIndent()


//    "If I gave more than one clothing, then give me suggestions on how to style them together. " +
    // Log the prompt being sent to the AI
    Log.d("AIPhotosActivity", "Prompt sent to AI: $prompt")

    val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-pro",
        apiKey = "AIzaSyCKO7-qQBXsmPWHTn6f2aUzLRlX4-U6mnM"
    )

    val inputContent = content {
        text(prompt)
    }

    return generativeModel.generateContent(inputContent).text
}

private suspend fun fetchUserData(userId: String, onDataFetched: (String, String, String) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    try {
        val document = db.collection("users").document(userId).get().await()
        if (document.exists()) {
            val userHeight = document.getString("height") ?: "Null"
            val userWeight = document.getString("weight") ?: "Null"
            val userGender = document.getString("gender") ?: "Not specified"
            onDataFetched(userHeight, userWeight, userGender)
        }
    } catch (e: Exception) {
        Log.e("FirestoreError", "Error fetching user data: ${e.message}")
    }
}

@Composable
fun OutfitCard(outfit: Outfit, context: Context) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "Outfit " + outfit.outfit_number, fontSize = 20.sp, color = Color.Black, fontWeight = FontWeight.Bold, fontFamily = cormorantFontFamily)
        Text(text = outfit.description, fontSize = 16.sp, color = Color.Black, fontFamily = tenorFontFamily)
        outfit.items.forEach { item ->
            if (item.imageurl.isNotEmpty()) {
                val bitmapState = remember { mutableStateOf<Bitmap?>(null) }
                loadImage(item.imageurl, context) { bitmap ->
                    bitmapState.value = bitmap
                }
                bitmapState.value?.let { bitmap ->
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = item.description,
                        modifier = Modifier
                            .size(100.dp)
                            .padding(4.dp)
                    )
                }
            }
        }
    }
}

@Serializable
data class Outfit(
    val outfit_number: Int,
    val description: String,
    val items: List<ClothingItem>
)

@Serializable
data class ClothingItem(
    val type: String,
    val imageurl: String,
    val description: String
)

//fun parseAIResponse(response: String): List<OutfitItem> {
//    return try {
//        val trimmedResponse = response.trim()
//        if (trimmedResponse.isBlank()) {
//            Log.e("ParseError", "Received empty response")
//            return emptyList()
//        }
//
//        // Log the raw response before parsing
//        Log.d("AIPhotosActivity", "Raw AI Response: $trimmedResponse")
//
//        // Deserialize the JSON response directly into a List of Outfit
//        return Json.decodeFromString<List<Outfit>>(trimmedResponse) // Expecting a list
//
//        // Log the number of outfits parsed
//        Log.d("AIPhotosActivity", "Successfully parsed ${outfits.size} outfits.")
//
//        // Map the outfits to OutfitItem
//        val outfitItems = outfits.flatMap { outfit ->
//            outfit.items.map { item ->
//                OutfitItem(
//                    type = item.type,
//                    imageUrl = item.imageurl,
//                    description = item.description
//                )
//            }
//        }
//
//        // Log the details of the parsed outfit items
//        Log.d("AIPhotosActivity", "Parsed Outfit Items: $outfitItems")
//
//        outfitItems
//    } catch (e: Exception) {
//        Log.e("ParseError", "Error parsing AI response: ${e.message}", e)
//        emptyList()
//    }
//}

fun parseAIResponse(response: String): List<Outfit> {
    val trimmedResponse = response.trim()
    return Json.decodeFromString<List<Outfit>>(trimmedResponse)
}