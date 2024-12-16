package com.example.stylo
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
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



class RemoveBackgroundActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val bitmap: Bitmap? = intent.getParcelableExtra<Bitmap>("bitmap")
            RemoveBackground(bitmap, onRetakeClick = { finish()}, onUseClick = { navigateToAIGeneratePhotos()})
        }
    }
    private fun navigateToAIGeneratePhotos() {
        val intent = Intent(this, AIGenerateActivity::class.java)
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
