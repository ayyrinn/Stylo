package com.example.stylo
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stylo.ui.theme.miamaFontFamily
import com.example.stylo.ui.theme.tenorFontFamily
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
import java.util.*


class RemoveBackgroundActivity : ComponentActivity() {
    private lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storageReference = FirebaseStorage.getInstance().reference

        val imageUriString = intent.getStringExtra("imageUri")
        val imageUri = Uri.parse(imageUriString)
        val bitmap: Bitmap? = imageUri?.let { uri ->
            MediaStore.Images.Media.getBitmap(contentResolver, uri)
        }

        setContent {
            RemoveBackground(
                bitmap = bitmap,
                onRetakeClick = { finish() },
                onUseClick = { uploadImage(bitmap) }
            )
        }
    }

    private fun uploadImage(bitmap: Bitmap?) {
        if (bitmap == null) {
            Log.e("RemoveBackgroundActivity", "Bitmap is null, cannot upload.")
            return
        }

        // Convert bitmap to byte array
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val data = baos.toByteArray()

        // Create a unique filename
        val fileName = "images/${UUID.randomUUID()}.png"
        val imageRef = storageReference.child(fileName)

        // Upload the image
        imageRef.putBytes(data)
            .addOnSuccessListener {
                Log.d("RemoveBackgroundActivity", "Image uploaded successfully.")
                // Get the download URL
                imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    // Save the filename or download URL to SharedPreferences
                    val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                    with(sharedPreferences.edit()) {
                        putString("imageDownloadUrl", downloadUri.toString()) // Save the filename
                        apply()
                    }
                    navigateToAIGeneratePhotos(downloadUri)
                }
            }
            .addOnFailureListener { exception ->
                Log.e("RemoveBackgroundActivity", "Upload failed: ${exception.message}")
            }
    }

    private fun navigateToAIGeneratePhotos(downloadUri: Uri) {
        val intent = Intent(this, AIGenerateActivity::class.java)
        intent.putExtra("imageUri", downloadUri.toString())
        startActivity(intent)
    }
}

@Composable
fun RemoveBackground(
    bitmap: Bitmap?,
    onUseClick: () -> Unit,
    onRetakeClick: () -> Unit
) {

    //header stylo
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(0xFFF3EEEA)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Image(
                painter = painterResource(id = R.drawable.burger_icon),
                contentDescription = "Burger Icon",
                modifier = Modifier
                    .size(50.dp)
                    .fillMaxSize()
                    .clickable { }
                    .padding(top = 10.dp),

                )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Stylo",
                fontSize = 45.sp,
                color = Color(0xFF776B5D),
                fontFamily = miamaFontFamily,
                modifier = Modifier
                    .padding(end = 35.dp)
            )
            Spacer(modifier = Modifier.weight(1f))

        }


        // box remove background
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent), // Background for the inner Column
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(50.dp))

            Text(
                text = "Background\nRemoved!",
                color = Color.Black,
                fontSize = 28.sp,
                fontFamily = tenorFontFamily,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = 15.dp)
            )

            Spacer(modifier = Modifier.height(50.dp))
            bitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "Image without background",
                    modifier = Modifier
                        .size(width = 350.dp, height = 400.dp)
                )
            } ?: Text("No image to display")

            //submit button
            Spacer(modifier = Modifier.height(40.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp), // Optional: Adds horizontal padding
                horizontalArrangement = Arrangement.Center, // Space between Text and Image
                verticalAlignment = Alignment.CenterVertically,


            ){
                // retake button
                Box(
                    modifier = Modifier
                        .width(110.dp)
                        .height(50.dp)
                        .background(
                            color = Color.Transparent,
                            shape = RoundedCornerShape(45.dp)
                        )
                        .border(
                            width = 2.dp,  // Outline thickness
                            color = Color(0xFFDD8560),  // Orange color for the outline
                            shape = RoundedCornerShape(45.dp) // Matches the background's shape
                        )
                        .padding(16.dp)
                        .clickable { onRetakeClick() },
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = "Retake",
                        color = Color.Black,               // Adjust text color
                        fontSize = 14.sp,                  // Adjust text size
                        fontFamily = tenorFontFamily
                    )
                }
                Spacer(modifier = Modifier.width(40.dp))
                // use button
                Box(
                    modifier = Modifier
                        .width(110.dp)
                        .height(50.dp)
                        .background(
                            color = Color(0xFFDD8560),
                            shape = RoundedCornerShape(45.dp)
                        )
                        .padding(16.dp)
                        .clickable { onUseClick() },
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = "Use",
                        color = Color.White,               // Adjust text color
                        fontSize = 14.sp,                  // Adjust text size
                        fontFamily = tenorFontFamily
                    )
                }
            }

        }

    }
}

@Preview
@Composable
fun PreviewsRemoveBG(){
//    RemoveBackground()
}
