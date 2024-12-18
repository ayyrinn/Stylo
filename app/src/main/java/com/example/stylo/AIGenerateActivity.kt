package com.example.stylo

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.transition.Transition
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.example.stylo.ui.theme.StyloTheme
import com.example.stylo.ui.theme.miamaFontFamily
import com.example.stylo.ui.theme.tenorFontFamily
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonNull.content


class AIGenerateActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Retrieve the filename from SharedPreferences
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val imageFileName = sharedPreferences.getString("imageFileName", null)

        // Construct the Firebase Storage URL
        val imageUri = imageFileName?.let {
            "https://firebasestorage.googleapis.com/v0/b/YOUR_PROJECT_ID.appspot.com/o/$it?alt=media"
        }
        setContent {
            StyloTheme {
                // Use Surface to define background color
                Surface(modifier = Modifier.fillMaxSize()) {
                    AIGeneratePhotos(imageUri = imageUri)
                }
            }
        }
    }
}



@Composable
fun AIGeneratePhotos(imageUri: String?) {
    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var response by remember { mutableStateOf("") } // State to store the response
    val coroutineScope = rememberCoroutineScope()


    // Load the image using Glide
    LaunchedEffect(imageUri) {
        imageUri?.let {
            Glide.with(context)
                .asBitmap()
                .load(it)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?) {
                        bitmap = resource
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        // Handle cleanup if needed
                    }
                })
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3EEEA)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        bitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "Processed Image",
                modifier = Modifier.size(362.dp, 529.dp)
            )
        } ?: Text("No image to display")

        Button(
            onClick = { coroutineScope.launch {
                response = visionModelCall(context).toString() // Update the response state
            } },
            modifier = Modifier
                .padding(top = 16.dp)
                .width(150.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFDD8560),
                contentColor = Color.White
            )
        ) {
            Text(
                text = "GENERATE",
                fontSize = 16.sp,
                fontFamily = tenorFontFamily
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "RESULT:",
            fontSize = 20.sp,
            fontFamily = tenorFontFamily
        )

        Text(
            text = response,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            fontFamily = tenorFontFamily,
            modifier = Modifier.padding(16.dp)
        )
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            color = Color.Black,
            thickness = 1.dp
        )
    }
}
@Preview
@Composable
fun PreviewAIGenerate() {
    // You can pass a dummy context or remove context-related code in previews
    AIGeneratePhotos(imageUri = null)
}

suspend fun visionModelCall(context: Context): String? {
    val bitmap = AppCompatResources.getDrawable(context, R.drawable.foto_jas)?.toBitmap()
    val imageData = bitmap!!

    val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-pro",
        apiKey = "AIzaSyCKO7-qQBXsmPWHTn6f2aUzLRlX4-U6mnM"
    )

    val inputContent = content {
        image(imageData)
        text("Give a somewhat detailed description on what clothing this is, including the color as the clothing's title. Specify which category this is among casual/semi-formal/formal/business casual/smart casual/athleisure/evening wear/cocktail/loungewear/sportswear/other. And specify a tag according if this is a top/bottom/footwear/accessories. write your answer down as (description)split(category)split(type). the answer for clothing should be a full sentence, while the answer for category and tag should be a few words, note that one clothing can be in multiple categories, seperate them with /")
    }

    return generativeModel.generateContent(inputContent).text
}
