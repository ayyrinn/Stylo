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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation.width
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AIPhotosActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val imageUrl: String


        if (intent.getStringExtra("image_url").toString() != "" || intent.getStringExtra("image_url").toString() != null){
            imageUrl = intent.getStringExtra("image_url").toString()
        }else{
            imageUrl = ""
        }

        setContent {
            AIPhotosScreen(imageUrl)
        }
    }
}

@Composable
fun AIPhotosScreen(imageUrl: String) {
    var top by remember { mutableStateOf<Bitmap?>(null) } //ini buat gambar yg ditunjukkin di screennya
    var bottom by remember { mutableStateOf<Bitmap?>(null) }
    var footwear by remember { mutableStateOf<Bitmap?>(null) }
    var accessories by remember { mutableStateOf<Bitmap?>(null) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var topData by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }//buat deskripsi baju" yg dipilih user
    var bottomData by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var footwearData by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var accessoriesData by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }

    val clothingData = remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var recsResponse by remember { mutableStateOf("") } // State to store the response

    var typeData by remember { mutableStateOf("") } // State to store the response


    if(imageUrl != ""){
        LaunchedEffect(imageUrl) {
            imageUrl?.let {
                if (typeData.equals("top", ignoreCase = true)){
                    topData = retrieveDataSuspend(it, imageUrl) // Update the state with retrieved data
                }

                if (typeData.equals("bottom", ignoreCase = true)){
                    bottomData = retrieveDataSuspend(it, imageUrl) // Update the state with retrieved data
                }

                if (typeData.equals("footwear", ignoreCase = true)){
                    footwearData = retrieveDataSuspend(it, imageUrl) // Update the state with retrieved data
                }

                if (typeData.equals("accessories", ignoreCase = true)){
                    accessoriesData = retrieveDataSuspend(it, imageUrl) // Update the state with retrieved data
                }

            } ?: Log.e("MoreTopScreen", "User  is not logged in")


            imageUrl?.let {
                Glide.with(context)
                    .asBitmap()
                    .load(it)
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                        ) {
                            if (typeData.equals("top", ignoreCase = true)){
                                top = resource
                            }

                            if (typeData.equals("bottom", ignoreCase = true)){
                                bottom = resource
                            }

                            if (typeData.equals("footwear", ignoreCase = true)){
                                footwear = resource
                            }

                            if (typeData.equals("accessories", ignoreCase = true)){
                                accessories = resource
                            }
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {
                            // Handle cleanup if needed
                        }

                        override fun onLoadFailed(errorDrawable: Drawable?) {
                            Log.e(
                                "AIGeneratePhotos",
                                "Image load failed: ${errorDrawable?.toString()}"
                            )
                        }
                    })
            }
        }
    }



//    if(bottomImageUrl != "") {
//        println("should be filled: " + bottomImageUrl)
//        LaunchedEffect(bottomImageUrl) {
//            bottomImageUrl?.let {
//                val retrievedData = retrieveDataSuspend(it, bottomImageUrl)
//                topData = retrievedData // Update the state with retrieved data
//                println(clothingData.value)
//            } ?: Log.e("MoreTopScreen", "User  is not logged in")
//        }
//
//        LaunchedEffect(bottomImageUrl) {
//            bottomImageUrl?.let {
//                Glide.with(context)
//                    .asBitmap()
//                    .load(it)
//                    .into(object : CustomTarget<Bitmap>() {
//                        override fun onResourceReady(
//                            resource: Bitmap,
//                            transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
//                        ) {
//                            top = resource
//                        }
//
//                        override fun onLoadCleared(placeholder: Drawable?) {
//                            // Handle cleanup if needed
//                        }
//
//                        override fun onLoadFailed(errorDrawable: Drawable?) {
//                            Log.e(
//                                "AIGeneratePhotos",
//                                "Image load failed: ${errorDrawable?.toString()}"
//                            )
//                        }
//                    })
//            }
//        }
//    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(color = Color(0xFFF3EEEA)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Image(
                painter = painterResource(id = R.drawable.burger_icon),
                contentDescription = "Burger Icon",
                modifier = Modifier
                    .size(50.dp)
                    .fillMaxSize()
                    .clickable {  }
                    .padding(top = 10.dp),

                   // .padding(start = 160.dp, top = 16.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "Stylo",
                fontSize = 45.sp,
                color = Color(0xFF776B5D),
                fontFamily = miamaFontFamily,
                modifier = Modifier
                    .padding(end = 35.dp)
                )
            Spacer(modifier = Modifier.weight(1f))
        }
        
        Spacer(modifier = Modifier.height(16.dp))


        // Selected Outfit Image
        Image(
            painter = painterResource(id = R.drawable.selected_outfit),
            contentDescription = "Selected Outfit",
            modifier = Modifier
                .width(370.dp) // Adjust width as needed
                .height(37.dp) // Adjust height as needed
                .padding(bottom = 16.dp)
        )

        // Row for buttons at the top
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 1.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally), // Centers and spaces items
            verticalAlignment = Alignment.CenterVertically){
            // Left Button
            Box(
                modifier = Modifier
                    .clickable {  }
                    .width(155.dp)
                    .height(215.dp)
                    .background(Color.Transparent, shape = RectangleShape) // Adjust shape if needed
            ) {
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                ){
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
                    ){
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 10.dp), // Optional: Adds horizontal padding
                            horizontalArrangement = Arrangement.SpaceBetween, // Space between Text and Image
                            verticalAlignment = Alignment.CenterVertically
                        ){
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
                    .clickable {  }
                    .width(155.dp)
                    .height(215.dp)
                    .background(Color.Transparent, shape = RectangleShape) // Adjust shape if needed
            ) {
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    bottom?.let {
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
                    ){
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 10.dp), // Optional: Adds horizontal padding
                            horizontalArrangement = Arrangement.SpaceBetween, // Space between Text and Image
                            verticalAlignment = Alignment.CenterVertically
                        ){
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
        Spacer(modifier = Modifier.height(10.dp)) // Adjust this value for top padding

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 1.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally), // Centers and spaces items
            verticalAlignment = Alignment.CenterVertically){
            // Left Button
            Box(
                modifier = Modifier
                    .clickable {  }
                    .width(155.dp)
                    .height(215.dp)
                    .background(Color.Transparent, shape = RectangleShape) // Adjust shape if needed
            ) {
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    footwear?.let {
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
                                    Intent(context, MoreFootwearActivity::class.java)
                                )
                            }
                            .height(185.dp)
                    )
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFFCDFD2))
                            .fillMaxWidth()
                            .height(30.dp)
                    ){
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 10.dp), // Optional: Adds horizontal padding
                            horizontalArrangement = Arrangement.SpaceBetween, // Space between Text and Image
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Text(
                                text = "Footwear",
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
                    .clickable {  }
                    .width(155.dp)
                    .height(215.dp)
                    .background(Color.Transparent, shape = RectangleShape) // Adjust shape if needed
            ) {
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    accessories?.let {
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
                                    Intent(context, MoreAccessoriesActivity::class.java)
                                )
                            }
                            .height(185.dp)
                    )
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFFCDFD2))
                            .fillMaxWidth()
                            .height(30.dp)
                    ){
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 10.dp), // Optional: Adds horizontal padding
                            horizontalArrangement = Arrangement.SpaceBetween, // Space between Text and Image
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Text(
                                text = "Accessories",
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


        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFFF3EEEA)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center // Centers content vertically
        ){
            Spacer(modifier = Modifier.height(10.dp)) // Adjust this value for top padding

            Box(
                modifier = Modifier
                    .width(370.dp)
                    .height(200.dp)
                    .background(Color.Transparent)
                    .verticalScroll(rememberScrollState())
                    .border(
                        width = 1.dp,              // Border thickness
                        color = Color(0xFFDD8560), // Border color (orange)
                        shape = RoundedCornerShape(45.dp)  // Adjust corner radius here
                    ) // Thin orange outline
                    .padding(16.dp)
            ){
                Text(
                    text = recsResponse,
                    color = Color.Black,               // Adjust text color
                    fontSize = 14.sp,                  // Adjust text size
                    fontFamily = tenorFontFamily      // Optional: Change to a specific font family
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp), // Optional: Adds horizontal padding
                horizontalArrangement = Arrangement.SpaceBetween, // Space between Text and Image
                verticalAlignment = Alignment.CenterVertically
            ){
                Box(
                    modifier = Modifier
                        .width(250.dp)
                        .height(50.dp)
                        .background(Color.Transparent)
                        .border(
                            width = 1.dp,              // Border thickness
                            color = Color(0xFFDD8560), // Border color (orange)
                            shape = RoundedCornerShape(45.dp)  // Adjust corner radius here
                        ) // Thin orange outline
                        .padding(16.dp)
                ){
                    Text(
                        text = "Send a message",
                        color = Color.Gray,               // Adjust text color
                        fontSize = 14.sp,                  // Adjust text size
                        fontFamily = tenorFontFamily      // Optional: Change to a specific font family
                    )
                }
                Box(
                    modifier = Modifier
                        .width(110.dp)
                        .height(50.dp)
                        .clickable {
                            coroutineScope.launch {

                                clothingData.value = topData + bottomData + footwearData + accessoriesData // Update the state with retrieved data

                                println("Retrieved Data: $clothingData")

                                recsResponse = generateRecs(context, clothingData.toString()).toString() // Update the response state
                            }
                        }
                        .background(
                            color = Color(0xFFDD8560),
                            shape = RoundedCornerShape(45.dp)
                        )
                        .padding(16.dp),
                            contentAlignment = Alignment.Center
                ){
                    Text(
                        text = "submit",
                        color = Color.White,               // Adjust text color
                        fontSize = 14.sp,                  // Adjust text size
                        fontFamily = tenorFontFamily)
                }
            }
        }

    }
}

@Preview
@Composable
fun Previews(){
    AIPhotosScreen("")

}

suspend fun retrieveDataSuspend(userId: String, image_url: String): List<Map<String, Any>> {
    val db = Firebase.firestore
    val clothingList = mutableListOf<Map<String, Any>>()

    return try {
        val result = db.collection("clothes")
            .whereEqualTo("type", "top")
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

suspend fun retrieveTypeSuspend(userId: String, imageUrl: String): String {
    val db = Firebase.firestore

    return try {
        val result = db.collection("clothes")
            .whereEqualTo("userID", userId)
            .whereEqualTo("imageurl", imageUrl)
            .get()
            .await() // Await the result using Kotlin Coroutines

        // Get the "type" field from the first matching document
        result.documents.firstOrNull()?.getString("type") ?: "No type found"
    } catch (e: Exception) {
        Log.w("FirebaseError", "Error retrieving documents", e)
        "Error retrieving type"
    }
}

suspend fun generateRecs(context: Context, clothingData: String): String? {
    println("generatee")
    val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-pro",
        apiKey = "AIzaSyCKO7-qQBXsmPWHTn6f2aUzLRlX4-U6mnM"
    )

    val inputContent = content {
        text("I'm giong to give you some outfit(s), ignore the userID, 'MutableState', and 'value'. Give a recomendation on what I should style it with, or how I should style this. If I gave more than one outfit, then give me suggestions on how to style them together. The outfits are as follows: " + clothingData)
    }

    return generativeModel.generateContent(inputContent).text
}


@GlideModule
class AIPhotosGlideModule : AppGlideModule() {
    // Leave this empty for now
    //what is this for?
}