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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.example.stylo.ui.theme.cormorantFontFamily
import com.example.stylo.ui.theme.miamaFontFamily
import com.example.stylo.ui.theme.tenorFontFamily
import com.example.stylo.ReusableDrawer
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomeScreen(context = this)
        }
    }
}

@Composable
fun HomeScreen(context: HomeActivity) {
    var showMenu by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val auth = FirebaseAuth.getInstance()
    val userId = auth.currentUser?.uid // Get the current user's ID

    var availableTop by remember { mutableStateOf<Bitmap?>(null) }
    var availableBottom by remember { mutableStateOf<Bitmap?>(null) }
    var availableAccessories by remember { mutableStateOf<Bitmap?>(null) }
    var availableFootwear by remember { mutableStateOf<Bitmap?>(null) }

    var temp by remember { mutableStateOf("") }

    LaunchedEffect(userId) {
        temp = userId?.let { retrieveImageSuspend("top", it) }.toString()
        availableTop = getBitmapFromUrl(context, temp)

        temp = userId?.let { retrieveImageSuspend("bottom", it) }.toString()
        availableBottom = getBitmapFromUrl(context, temp)

        temp = userId?.let { retrieveImageSuspend("accessories", it) }.toString()
        availableAccessories = getBitmapFromUrl(context, temp)

        temp = userId?.let { retrieveImageSuspend("footwear", it) }.toString()
        availableFootwear = getBitmapFromUrl(context, temp)
    }




//                if (typeData.equals("top", ignoreCase = true)){
//                    topData = retrieveDataSuspend(imageUrl) // Update the state with retrieved data
//                } else if (typeData.equals("bottom", ignoreCase = true)){
//                    bottomData = retrieveDataSuspend(imageUrl) // Update the state with retrieved data
//                }else if (typeData.equals("footwear", ignoreCase = true)){
//                    footwearData = retrieveDataSuspend(imageUrl) // Update the state with retrieved data
//                }else if (typeData.equals("accessories", ignoreCase = true)){
//                    accessoriesData = retrieveDataSuspend(imageUrl) // Update the state with retrieved data
//                }



    Box(modifier = Modifier.fillMaxSize()) {
        // Main content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center

        ) {
            // Top Bar
            StyloTopBar(onMenuClick = { showMenu = !showMenu })

            // Main Content
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .paint(
                        painter = painterResource(id = R.drawable.home_img_1),
                    )
            ) {
                Column(
                    modifier = Modifier
                        .padding(top = 200.dp)
                ) {
                    Text(
                        text = "SAY WHO\nYOU ARE WITHOUT\nSPEAKING",
                        modifier = Modifier
                            .padding(top = 100.dp)
                            .fillMaxWidth(), // Adjust this to align text horizontally to the center
                        fontSize = 30.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        fontFamily = cormorantFontFamily
                    )
                    Spacer(modifier = Modifier.height(100.dp))
                    Button(
                        onClick = {
                            val intent = Intent(context, AddPhotoActivity::class.java)
                            context.startActivity(intent)
                        },
                        modifier = Modifier
                            .padding(top = 50.dp) // Margin atas 50 dp
                            .width(300.dp)        // Lebar tombol
                            .height(50.dp)
                            .align(Alignment.CenterHorizontally),          // Tinggi tombol
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFB0A695) // Warna background tombol
                        )
                    ) {
                        Text(
                            text = "Discover Your Style",
                            color = Color.Black, // Warna teks tombol
                            fontSize = 16.sp,
                            fontFamily = TenorSansRegular
                        )
                    }

                }
            }


            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .paint(
                        painter = painterResource(id = R.drawable.home_img_2),
                    )
            ) {
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .height(70.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Absolute.Right
                    ) {
                        Text(
                            text = "Your ",
                            fontSize = 30.sp,
                            color = Color.White,
                            fontFamily = cormorantFontFamily
                        )
                        Text(
                            text = "clothes",
                            fontSize = 60.sp,
                            color = Color.White,
                            fontFamily = miamaFontFamily
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .height(35.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Absolute.Right
                    ) {
                        Text(
                            text = "Should",
                            fontSize = 30.sp,
                            color = Color.White,
                            fontFamily = cormorantFontFamily
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .height(35.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Absolute.Right
                    ) {
                        Text(
                            text = "be an extension",
                            fontSize = 30.sp,
                            color = Color.White,
                            fontFamily = cormorantFontFamily
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .height(75.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Absolute.Right
                    ) {
                        Text(
                            text = "of your ",
                            fontSize = 30.sp,
                            color = Color.White,
                            fontFamily = cormorantFontFamily
                        )
                        Text(
                            text = "personality",
                            fontSize = 50.sp,
                            color = Color.White,
                            fontFamily = miamaFontFamily
                        )
                    }


                }

                Spacer(modifier = Modifier.height(50.dp))


                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                            availableTop?.let {
                                Image(
                                    bitmap = it.asImageBitmap(),
                                    contentDescription = "Processed Image",
                                    modifier = Modifier
                                        .width(150.dp)
                                        .height(180.dp)
                                        .clickable {
                                            context.startActivity(
                                                Intent(context, MoreTopActivity::class.java)
                                            )
                                        }
                                        .background(Color.Gray, shape = RoundedCornerShape(30.dp)),

                                )
                            } ?: Image(
                                painter = painterResource(id = R.drawable.icon_card_img),
                                contentDescription = "Camera Left",
                                modifier = Modifier
                                    .width(150.dp)
                                    .height(180.dp)
                                    .clickable {
                                        context.startActivity(
                                            Intent(context, MoreTopActivity::class.java)
                                        )
                                    }
                            )


                        availableBottom?.let {
                            Image(
                                bitmap = it.asImageBitmap(),
                                contentDescription = "Processed Image",
                                modifier = Modifier
                                    .width(150.dp)
                                    .height(180.dp)

                                    .clickable {
                                        context.startActivity(
                                            Intent(context, MoreBottomActivity::class.java)
                                        )
                                    }
                                    .background(Color.Gray, shape = RoundedCornerShape(30.dp)),
                            )
                        } ?: Image(
                            painter = painterResource(id = R.drawable.icon_card_img),
                            contentDescription = "Camera Left",
                            modifier = Modifier
                                .width(150.dp)
                                .height(180.dp)
                                .clickable {
                                    context.startActivity(
                                        Intent(context, MoreBottomActivity::class.java)
                                    )
                                }
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                            availableAccessories?.let {
                                Image(
                                    bitmap = it.asImageBitmap(),
                                    contentDescription = "Processed Image",
                                    modifier = Modifier
                                        .width(150.dp)
                                        .height(180.dp)
                                        .clickable {
                                            context.startActivity(
                                                Intent(context, MoreAccessoriesActivity::class.java)
                                            )
                                        }
                                        .background(Color.Gray, shape = RoundedCornerShape(30.dp)),
                                )
                            } ?: Image(
                                painter = painterResource(id = R.drawable.icon_card_img),
                                contentDescription = "Camera Left",
                                modifier = Modifier
                                    .width(150.dp)
                                    .height(180.dp)
                                    .clickable {
                                        context.startActivity(
                                            Intent(context, MoreAccessoriesActivity::class.java)
                                        )
                                    }
                            )

                        availableFootwear?.let {
                            Image(
                                bitmap = it.asImageBitmap(),
                                contentDescription = "Processed Image",
                                modifier = Modifier
                                    .width(150.dp)
                                    .height(180.dp)
                                    .clickable {
                                        context.startActivity(
                                            Intent(context, MoreFootwearActivity::class.java)
                                        )
                                    }
                                    .background(Color.Gray, shape = RoundedCornerShape(30.dp)),

                            )
                        } ?: Image(
                            painter = painterResource(id = R.drawable.icon_card_img),
                            contentDescription = "Camera Left",
                            modifier = Modifier
                                .width(150.dp)
                                .height(180.dp)
                                .clickable {
                                    context.startActivity(
                                        Intent(context, MoreFootwearActivity::class.java)
                                    )
                                }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .width(75.dp)
                            .height(104.dp)
                            .background(
                                color = Color.Transparent
                            ),
                        contentAlignment = Alignment.Center
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.next_icon),
                                contentDescription = "more button",
                                modifier = Modifier
                                    .width(75.dp)
                                    .height(80.dp)
                                    .clickable {
                                        val intent = Intent(context, SelectPhotosActivity::class.java)
                                        context.startActivity(intent)
                                    }
                            )

                            Text(
                                text = "More",
                                color = Color.White,               // Adjust text color
                                fontSize = 24.sp,                  // Adjust text size
                                fontFamily = tenorFontFamily

                            )
                        }
                    }
                }
            }

//
//            Column(
//                horizontalAlignment = Alignment.CenterHorizontally,
//                modifier = Modifier
//                    .height(800.dp)
//                    .paint(
//                        painter = painterResource(id = R.drawable.home_img_5),
//                    )
//            ) {
//                Column(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(top = 50.dp)
//                ) {
//                    Column(
//                        horizontalAlignment = Alignment.Start,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(horizontal = 20.dp)
//                    ) {
//                        Row {
//                            Text(
//                                text = "plan your",
//                                fontSize = 30.sp,
//                                color = Color.White,
//                                fontFamily = cormorantFontFamily
//                            )
//                            Text(
//                                text = "Outfits",
//                                fontSize = 50.sp,
//                                color = Color.White,
//                                fontFamily = miamaFontFamily
//                            )
//                        }
//                    }
//                    Column(
//                        horizontalAlignment = Alignment.End,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(horizontal = 20.dp)
//                    ) {
//                        Row {
//                            Text(
//                                text = "slay your",
//                                fontSize = 30.sp,
//                                color = Color.White,
//                                fontFamily = cormorantFontFamily
//                            )
//                            Text(
//                                text = "Day",
//                                fontSize = 50.sp,
//                                color = Color.White,
//                                fontFamily = miamaFontFamily
//                            )
//                        }
//
//                    }
//                }
//
//
//                Column(
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.Center,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 5.dp)
//                ) {
//                    Box(
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Image(
//                            painter = painterResource(id = R.drawable.todays_pick_frame),
//                            contentDescription = null,
//                            modifier = Modifier
//                                .width(325.dp)
//                                .height(460.dp)
//                        )
//                        Image(
//                            painter = painterResource(id = R.drawable.pick_image),
//                            contentDescription = "Today's pick",
//                            modifier = Modifier
//                                .width(300.dp)
//                                .height(400.dp)
//                                .padding(bottom = 45.dp)
//                        )
//                    }
//
//                }
//
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(85.dp)
//                        .background(
//                            color = Color.Transparent
//                        ),
//                ) {
//                    Column(
//                        horizontalAlignment = Alignment.End,
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(horizontal = 30.dp),
//                    ) {
//                        Image(
//                            painter = painterResource(id = R.drawable.next_icon),
//                            contentDescription = "go to calendar button",
//                            modifier = Modifier
//                                .width(50.dp)
//                                .height(70.dp)
//                        )
//
//                        Text(
//                            text = "GO TO OUTFIT CALENDAR",
//                            color = Color.White,               // Adjust text color
//                            fontSize = 15.sp,                  // Adjust text size
//                            fontFamily = tenorFontFamily
//                        )
//                    }
//                }
//
//
//            }
        }
        if (showMenu) {
            ReusableDrawer(context = context, onDismiss = { showMenu = false })
        }
    }

}

@Preview
@Composable
fun PreviewHome() {
    // You can pass a dummy context or remove context-related code in previews
    HomeScreen(context = HomeActivity()) // Just for preview, may not work directly
}

private fun loadImageHome(imageUrl: String, context: Context, onImageLoaded: (Bitmap) -> Unit) {
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

//suspend fun retrieveImageSuspend(type: String, userID: String): Map<String, Any> {
//    val db = Firebase.firestore
//    var clothingImage = MutableMap<String, Any>()
//
//    return try {
//        val result = db.collection("clothes")
//            .whereEqualTo("type", type)
//            .whereEqualTo("userID", userID)
//            .limit(1)
//            .get()
//            .await() // Await the result using Kotlin Coroutines
//        for (document in result) {
//            clothingImage.add(document.data)
//        }
//        clothingImage
//
//
//    } catch (e: Exception) {
//        Log.w("FirebaseError", "Error retrieving documents", e)
//        emptyList()
//    }
//}

suspend fun retrieveImageSuspend(type: String, userID: String): String? {
    val db = Firebase.firestore

    return try {
        val result = db.collection("clothes")
            .whereEqualTo("type", type)
            .whereEqualTo("userID", userID)
            .limit(1) // Limit the query to only one document
            .get()
            .await() // Await the result using Kotlin Coroutines

        if (!result.isEmpty) {
            result.documents[0].getString("imageurl") // Return the first document's data
        } else {
            null // Return null if no document matches the query
        }
    } catch (e: Exception) {
        Log.w("FirebaseError", "Error retrieving documents", e)
        null // Return null in case of an exception
    }
}

fun loadImage(){
    suspend fun getBitmapFromUrl(context: Context, imageUrl: String): Bitmap? {
        return try {
            withContext(Dispatchers.IO) {
                Glide.with(context)
                    .asBitmap()
                    .load(imageUrl)
                    .submit()
                    .get()
            }
        } catch (e: Exception) {
            Log.e("BitmapError", "Error loading bitmap", e)
            null
        }
    }
}

suspend fun getBitmapFromUrl(context: Context, imageUrl: String): Bitmap? {
    return try {
        withContext(Dispatchers.IO) {
            Glide.with(context)
                .asBitmap()
                .load(imageUrl)
                .submit()
                .get()
        }
    } catch (e: Exception) {
        Log.e("BitmapError", "Error loading bitmap", e)
        null
    }
}
