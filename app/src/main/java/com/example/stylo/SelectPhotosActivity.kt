package com.example.stylo
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.bumptech.glide.Glide
import com.example.stylo.R
import com.example.stylo.ui.theme.cormorantFontFamily
import com.example.stylo.ui.theme.miamaFontFamily
import com.example.stylo.ui.theme.tenorFontFamily
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SelectPhotosActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SelectPhotos()
        }
    }
}

@Composable
fun SelectPhotos() {
    val context = LocalContext.current
    var showMenu by remember { mutableStateOf(false) }

    // Fetch user clothing data
    val auth = FirebaseAuth.getInstance()
    val userId = auth.currentUser ?.uid

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

    //header stylo
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        StyloTopBar(onMenuClick = { showMenu = !showMenu })

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "GALLERY",
            color = Color.White,
            fontSize = 28.sp,
            fontFamily = tenorFontFamily,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(start = 10.dp)
        )
        Spacer(modifier = Modifier.height(25.dp))


//        Row(modifier = Modifier
//            .fillMaxWidth()
//            .padding(top = 1.dp),
//            horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally), // Centers and spaces items
//            verticalAlignment = Alignment.CenterVertically){
//            // Left Button
//            Box(
//                modifier = Modifier
//                    .clickable {
//                        val intent = Intent(context, MoreTopActivity::class.java)
//                        context.startActivity(intent)
//                    }
//                    .width(155.dp)
//                    .height(215.dp)
//                    .background(Color.Transparent, shape = RectangleShape)
//            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.foto_jas),
//                    contentDescription = "Camera Left",
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(185.dp)
//                        .clip(RoundedCornerShape(40.dp))
//                        .align(Alignment.Center)
//                )
//                Text(
//                    text = "TOP",
//                    color = Color(0xFF776B5D),
//                    fontSize = 20.sp,
//                    fontFamily = tenorFontFamily,
//                    textAlign = TextAlign.Center,
//                    modifier = Modifier
//                        .align(Alignment.Center)
//                        .padding(top = 10.dp)
//                )
//            }
//
//            Box(
//                modifier = Modifier
//                    .clickable {
//                        val intent = Intent(context, MoreBottomActivity::class.java)
//                        context.startActivity(intent)
//                    }
//                    .width(155.dp)
//                    .height(215.dp)
//                    .background(Color.Transparent, shape = RectangleShape)
//            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.foto_jas),
//                    contentDescription = "Camera Left",
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(185.dp)
//                        .clip(RoundedCornerShape(40.dp))
//                        .align(Alignment.Center)
//                )
//                Text(
//                    text = "BOTTOM",
//                    color = Color(0xFF776B5D),
//                    fontSize = 20.sp,
//                    fontFamily = tenorFontFamily,
//                    textAlign = TextAlign.Center,
//                    modifier = Modifier
//                        .align(Alignment.Center)
//                        .padding(top = 10.dp)
//                )
//            }
//        }
//        Row(modifier = Modifier
//            .fillMaxWidth()
//            .padding(top = 1.dp),
//            horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally), // Centers and spaces items
//            verticalAlignment = Alignment.CenterVertically){
//            // Left Button
//            Box(
//                modifier = Modifier
//                    .clickable {
//                        val intent = Intent(context, MoreFootwearActivity::class.java)
//                        context.startActivity(intent)
//                    }
//                    .width(155.dp)
//                    .height(215.dp)
//                    .background(Color.Transparent, shape = RectangleShape)
//            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.foto_jas),
//                    contentDescription = "Camera Left",
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(185.dp)
//                        .clip(RoundedCornerShape(40.dp))
//                        .align(Alignment.Center)
//                )
//                Text(
//                    text = "FOOTWEAR",
//                    color = Color(0xFF776B5D),
//                    fontSize = 20.sp,
//                    fontFamily = tenorFontFamily,
//                    textAlign = TextAlign.Center,
//                    modifier = Modifier
//                        .align(Alignment.Center)
//                        .padding(top = 10.dp)
//                )
//            }
//
//
//            Box(
//                modifier = Modifier
//                    .clickable {
//                        val intent = Intent(context, MoreAccessoriesActivity::class.java)
//                        context.startActivity(intent)
//                    }
//                    .width(155.dp)
//                    .height(215.dp)
//                    .background(Color.Transparent, shape = RectangleShape)
//            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.foto_jas),
//                    contentDescription = "Camera Left",
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(185.dp)
//                        .clip(RoundedCornerShape(40.dp))
//                        .align(Alignment.Center)
//                )
//                Text(
//                    text = "ACCESSORIES",
//                    color = Color(0xFF776B5D),
//                    fontSize = 20.sp,
//                    fontFamily = tenorFontFamily,
//                    textAlign = TextAlign.Center,
//                    modifier = Modifier
//                        .align(Alignment.Center)
//                        .padding(top = 10.dp)
//                )
//            }
//        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(0.dp) // No spacing between rows
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), // Fill half of the available height
                horizontalArrangement = Arrangement.spacedBy(0.dp) // No spacing between columns
            ) {
                // Top Image
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight() // Fill height
                        .clickable {
                            val intent = Intent(context, MoreTopActivity::class.java)
                            context.startActivity(intent)
                        }
                        .background(Color.Transparent)
                ) {
                    availableTop?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = "Processed Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable {
                                    context.startActivity(
                                        Intent(context, MoreTopActivity::class.java)
                                    )
                                }
                                .background(Color.DarkGray),

                            )
                    } ?: Image(
                        painter = painterResource(id = R.drawable.foto_jas),
                        contentDescription = "TOPS",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                context.startActivity(
                                    Intent(context, MoreTopActivity::class.java)
                                )
                            }
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(Color.Black.copy(alpha = 0.5f)) // Semi-transparent black overlay
                    )
                    Text(
                        text = "TOPS",
                        color = Color.White,
                        fontFamily = tenorFontFamily,
                        fontSize = 30.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                // Bottom Image
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight() // Fill height
                        .clickable {
                            val intent = Intent(context, MoreBottomActivity::class.java)
                            context.startActivity(intent)
                        }
                        .background(Color.Transparent)
                ) {
                    availableBottom?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = "Processed Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable {
                                    context.startActivity(
                                        Intent(context, MoreBottomActivity::class.java)
                                    )
                                }
                                .background(Color(0xFF776B5D)),
                        )
                    } ?: Image(
                        painter = painterResource(id = R.drawable.foto_jas),
                        contentDescription = "BOTTOMS",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                context.startActivity(
                                    Intent(context, MoreBottomActivity::class.java)
                                )
                            }
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(Color.Black.copy(alpha = 0.5f)) // Semi-transparent black overlay
                    )
                    Text(
                        text = "BOTTOMS",
                        color = Color.White,
                        fontFamily = tenorFontFamily,
                        fontSize = 30.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), // Fill half of the available height
                horizontalArrangement = Arrangement.spacedBy(0.dp) // No spacing between columns
            ) {
                // Footwear Image
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight() // Fill height
                        .clickable {
                            val intent = Intent(context, MoreFootwearActivity::class.java)
                            context.startActivity(intent)
                        }
                        .background(Color.Transparent)
                ) {
                    availableFootwear?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = "Processed Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable {
                                    context.startActivity(
                                        Intent(context, MoreFootwearActivity::class.java)
                                    )
                                }
                                .background(Color(0xFF776B5D)),

                            )
                    } ?: Image(
                        painter = painterResource(id = R.drawable.foto_jas),
                        contentDescription = "FOOTWEARS",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                context.startActivity(
                                    Intent(context, MoreFootwearActivity::class.java)
                                )
                            }
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(Color.Black.copy(alpha = 0.5f)) // Semi-transparent black overlay
                    )
                    Text(
                        text = "FOOTWEARS",
                        color = Color.White,
                        fontFamily = tenorFontFamily,
                        fontSize = 25.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                // Accessories Image
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight() // Fill height
                        .clickable {
                            val intent = Intent(context, MoreAccessoriesActivity::class.java)
                            context.startActivity(intent)
                        }
                        .background(Color.Transparent)
                ) {
                    availableAccessories?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = "Processed Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable {
                                    context.startActivity(
                                        Intent(context, MoreAccessoriesActivity::class.java)
                                    )
                                }
                                .background(Color.DarkGray),
                        )
                    } ?: Image(
                        painter = painterResource(id = R.drawable.foto_jas),
                        contentDescription = "ACCESSORIES",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                context.startActivity(
                                    Intent(context, MoreAccessoriesActivity::class.java)
                                )
                            }
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(Color.Black.copy(alpha = 0.5f)) // Semi-transparent black overlay
                    )
                    Text(
                        text = "ACCESSORIES",
                        color = Color.White,
                        fontFamily = tenorFontFamily,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
    if (showMenu) {
        ReusableDrawer(context = context, onDismiss = { showMenu = false })
    }
}

@Preview(showBackground = true)
@Composable
fun ReviewPhotos() {
    SelectPhotos()
}
