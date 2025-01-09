package com.example.stylo

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.transition.Transition
import android.util.Log
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
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
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.target.CustomTarget
import com.example.stylo.ui.theme.StyloTheme
import com.example.stylo.ui.theme.miamaFontFamily
import com.example.stylo.ui.theme.tenorFontFamily
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonNull.content


class AIGenerateActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Retrieve the filename from SharedPreferences
        val imageUriString = intent.getStringExtra("imageUri")
        val imageUri = Uri.parse(imageUriString)

        Log.d("AIGenerateActivity", "Image URL: $imageUri")
        setContent {
            StyloTheme {
                // Use Surface to define background color
                Surface(modifier = Modifier.fillMaxSize()) {
                    AIGeneratePhotos(imageUri = imageUri.toString())
                }
            }
        }
    }
}



@Composable
fun AIGeneratePhotos(imageUri: String?) {
    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var response by remember { mutableStateOf("") }// State to store the response
    var displayResponse by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    var saveResponse by remember { mutableStateOf<List<String>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }

    if (response != ""){
        println("original string: " + response)

        saveResponse = response.split("split")

        println("desc: " + saveResponse[0])
        println("category: " + saveResponse[1])
        println("type: " + saveResponse[2])

        displayResponse = saveResponse[0] + "\n" + saveResponse[1] + "\n" + saveResponse[2]

    }


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

                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        Log.e("AIGeneratePhotos", "Image load failed: ${errorDrawable?.toString()}")
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
                modifier = Modifier.size(362.dp, 362.dp)
            )
        } ?: Text("No image to display", fontFamily = TenorSansRegular)

        Button(
            onClick = {
                isLoading = true
                coroutineScope.launch {
                    response =
                        bitmap?.let { visionModelCall(context, it).toString() }.toString() // Update the response state
                    isLoading = false
                }
            },
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
        if (isLoading) {
            // Show loading indicator
            CircularProgressIndicator(
                modifier = Modifier.size(64.dp),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        } else {
            // Show results if available
            if (response.isNotEmpty()) {
                Text(
                    text = "RESULT:",
                    fontSize = 20.sp,
                    fontFamily = tenorFontFamily,
                    color = Color.Black
                )

                // Process the response to display
                val saveResponse = response.split("split")
                displayResponse = saveResponse.joinToString("\n")

                Text(
                    text = displayResponse,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    fontFamily = tenorFontFamily,
                    color = Color.Black,
                    modifier = Modifier.padding(16.dp)
                )

                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    color = Color.Black,
                    thickness = 1.dp
                )



                Button(
                    onClick = {
                        // Check if the user is logged in
                        val auth = FirebaseAuth.getInstance()
                        if (auth.currentUser  == null) {
                            // User is not logged in, handle accordingly (e.g., show a login screen)
                            Log.e(TAG, "User  is not logged in")
                            // Optionally, you can show a message to the user
                        } else {
                            // Proceed with saving data
                            databaseSave(saveResponse[0], saveResponse[1], saveResponse[2], imageUri)

                            // Save the result to SharedPreferences
                            val sharedPreferences = context.getSharedPreferences("stylo_prefs", Context.MODE_PRIVATE)
                            sharedPreferences.edit().putString("saved_result", response).apply()

                            // Navigate to the next activity if needed
                             val intent = Intent(context, HomeActivity::class.java)
                             context.startActivity(intent)
                        }
                    },
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .width(150.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFDD8560),
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "SAVE",
                        fontSize = 16.sp,
                        fontFamily = tenorFontFamily
                    )
                }
            }
        }
    }
}
@Preview
@Composable
fun PreviewAIGenerate() {
    // You can pass a dummy context or remove context-related code in previews
    AIGeneratePhotos(imageUri = "https://firebasestorage.googleapis.com/v0/b/stylo-a5e01.appspot.com/o/images%2F22885c5c-608e-4594-9e94-c067215e6939.png?alt=media&token=ffb03c47-df08-47ab-bdd8-6443f5da36dc")
}

suspend fun visionModelCall(context: Context, imageData: Bitmap): String? {
//    val bitmap = AppCompatResources.getDrawable(context, R.drawable.foto_jas)?.toBitmap()
//    val imageData = bitmap!!

    val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-pro",
        apiKey = "AIzaSyCKO7-qQBXsmPWHTn6f2aUzLRlX4-U6mnM"
    )

    val inputContent = content {
        image(imageData)
        text("Give a somewhat detailed description on what clothing this is, including the color as the clothing's title. Specify which category this is among casual/semi-formal/formal/business casual/smart casual/athleisure/evening wear/cocktail/loungewear/sportswear/other. And specify a tag according if this is a top/bottom/footwear/accessories, make sure the tags are without spaces, and all in lowercase. write your answer down as (description)split(category)split(type). the answer for clothing should be a full sentence, while the answer for category and tag should be a few words, note that one clothing can be in multiple categories, seperate them with /")
    }

    return generativeModel.generateContent(inputContent).text
}

fun databaseSave(description: String, category: String, type: String, imageUri: String?) {
    val db = Firebase.firestore
    val auth = FirebaseAuth.getInstance()
    val userId = auth.currentUser ?.uid ?: "unknown_user"

    // Create a new thing ig
    val clothing = hashMapOf(
        "userID" to userId,
        "description" to description,
        "category" to category,
        "type" to type,
        "imageurl" to imageUri
    )

// Add a new document with a generated ID
    println("adding to database")
    db.collection("clothes")
        .add(clothing)
        .addOnSuccessListener { documentReference ->
            Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
        }
        .addOnFailureListener { e ->
            Log.w(TAG, "Error adding document", e)
        }
}

@GlideModule
class MyAppGlideModule : AppGlideModule() {
    // Leave this empty for now
}