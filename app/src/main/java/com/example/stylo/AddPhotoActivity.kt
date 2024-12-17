package com.example.stylo

//import RemoveBackgroundActivity
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stylo.ui.theme.miamaFontFamily
import com.example.stylo.ui.theme.tenorFontFamily
import java.io.File
import java.io.FileOutputStream

class AddPhotoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PhotoActivity()
        }
    }
}

@Composable
fun PhotoActivity() {

    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var resultBitmap by remember { mutableStateOf<Bitmap?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            bitmap = if (Build.VERSION.SDK_INT >= 30) {
                val source = ImageDecoder.createSource(context.contentResolver, it)
                ImageDecoder.decodeBitmap(source)
            } else {
                @Suppress("DEPRECATION")
                MediaStore.Images.Media.getBitmap(context.contentResolver, it)
            }
        }
    }

    val cLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { imageBitmap ->
        bitmap = imageBitmap
    }


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3EEEA))
            .padding(top = 90.dp)
    ) {

        Text(
            text = "ADD YOUR\nCOLLECTIONS",
            color = Color.Black,
            fontSize = 28.sp,
            fontFamily = tenorFontFamily,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(start = 15.dp)
        )
        Spacer(modifier = Modifier.height(15.dp))
        Box(
            modifier = Modifier
                .width(370.dp)
                .background(Color.Transparent)
                .border(
                    width = 1.dp,              // Border thickness
                    color = Color(0xFF776B5D), // Border color (orange)
                    shape = RoundedCornerShape(45.dp)  // Adjust corner radius here
                ) // Thin orange outline
                .padding(16.dp)
        ){
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(10.dp)
            ){
                Text(
                    text = "Take a photo of your item",
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontFamily = tenorFontFamily,
                    textAlign = TextAlign.Left,
                    modifier = Modifier
                        .fillMaxWidth(),
                    //modifier = Modifier.padding(start = 15.dp)
                )

                Spacer(modifier = Modifier.height(25.dp))

                Text(
                    text = "We recommend capturing the item in a flat-lay position, taken directly from above.  ",
                    color = Color.Black,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(end = 30.dp),
                    fontFamily = tenorFontFamily,
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "The background will be automatically removed for a cleaner look.",
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontFamily = tenorFontFamily,
                    modifier = Modifier.padding(end = 20.dp),
                )
                Spacer(modifier = Modifier.height(20.dp))
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                        .background(Color.White, shape = RoundedCornerShape(10.dp))
                        .clickable { showDialog = true } // Enable click on the whole box
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        if (bitmap != null) {
                            Image(
                                bitmap = bitmap!!.asImageBitmap(),
                                contentDescription = null,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.LightGray),
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.baseline_add_a_photo_24),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(50.dp) // Reduced camera icon size
                            )
                            Spacer(modifier = Modifier.height(8.dp)) // Space between icon and text
                            Text(
                                text = "Choose image",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
                Image(
                    painter = painterResource(id = R.drawable.baseline_arrow_forward_24),
                    contentDescription = null,
                    modifier = Modifier
                        .size(70.dp)
                        .padding(top = 20.dp)
                        .align(Alignment.End)
                        .clickable {
                            bitmap?.let { image ->
                                removeBackground(image, "kYBubGppg1G3uoM7gxKBrSJQ") { result ->
                                    if (result != null) {
                                        resultBitmap = result
                                        val imageUri = saveBitmapToFile(context, result)
                                        // Navigasi ke halaman hasil
                                        context.startActivity(
                                            Intent(context, RemoveBackgroundActivity::class.java)
                                                .putExtra("imageUri", imageUri.toString())
                                        )
                                    } else {
                                        (context as? Activity)?.runOnUiThread {
                                            Toast.makeText(
                                                context,
                                                "Failed to remove background",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }
                            } ?: run {
                                // Show a toast on the main thread
                                (context as? Activity)?.runOnUiThread {
                                    Toast.makeText(
                                        context,
                                        "Please select an image",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                )
            }
        }



    }

    Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
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
        Text(text = "Stylo",
            fontSize = 45.sp,
            color = Color(0xFF776B5D),
            fontFamily = miamaFontFamily,
            //textAlign = TextAlign.Center,
            modifier = Modifier.padding(end = 35.dp),

        )
        Spacer(modifier = Modifier.weight(1f))
    }

    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 10.dp)
    ) {
        if (showDialog) {
            Box(
                contentAlignment = Alignment.TopEnd,
                modifier = Modifier
                    .width(300.dp)
                    .height(150.dp)
                    .background(Color.LightGray, shape = RoundedCornerShape(10.dp))
                    .padding(16.dp)
            ) {
                // Close button in the top-right corner
                Text(
                    text = "X",
                    color = Color.Black,
                    fontSize = 24.sp,
                    modifier = Modifier
                        .clickable {
                            showDialog = false
                        }
                )

                // Camera and Gallery options centered within the box
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(id = R.drawable.baseline_add_a_photo_24),
                            contentDescription = null,
                            modifier = Modifier
                                .size(50.dp)
                                .clickable {
                                    cLauncher.launch()
                                    showDialog = false
                                }
                        )
                        Text(
                            text = "Camera",
                            color = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.width(80.dp))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(id = R.drawable.baseline_add_photo_alternate_24),
                            contentDescription = null,
                            modifier = Modifier
                                .size(50.dp)
                                .clickable {
                                    launcher.launch("image/*")
                                    showDialog = false
                                }
                        )
                        Text(
                            text = "Gallery",
                            color = Color.Black
                        )
                    }
                }
            }
        }
        // firebase
//        val isUploading = remember { mutableStateOf(false) }
//        Column (
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center,
//            modifier = Modifier
//                .height(450.dp)
//                .fillMaxWidth()
//        ){
//            if (isUploading.value){
//                CircularProgressIndicator(
//                    modifier = Modifier
//                        .padding(16.dp),
//                    color = Color.White
//                )
//            }
//        }
    }
}
private fun saveBitmapToFile(context: Context, bitmap: Bitmap): Uri {
    val file = File(context.cacheDir, "image_${System.currentTimeMillis()}.png")
    FileOutputStream(file).use { out ->
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
    }
    return Uri.fromFile(file)
}

@Preview
@Composable
fun PreviewAddPhoto(){
    PhotoActivity()
}