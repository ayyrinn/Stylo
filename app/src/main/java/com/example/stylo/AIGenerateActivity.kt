package com.example.stylo

import android.content.Context
import android.os.Bundle
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
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
        setContent {
            StyloTheme {
                // Use Surface to define background color
                Surface(modifier = Modifier.fillMaxSize()) {
                    AIGeneratePhotos()
                }
            }
        }
    }
}



@Composable
fun AIGeneratePhotos() {
    val context = LocalContext.current
    var AIresponse = " "
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3EEEA)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){

        Image(
            painter = painterResource(id = R.drawable.foto_jas), // Ganti dengan ID gambar Anda
            contentDescription = "Jeans image",
            modifier = Modifier.size(362.dp, 529.dp)
        )

        Button(
            onClick = {AIresponse = visionModelCall(context)},
            modifier = Modifier
                .padding(top = 16.dp)
                .width(150.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFDD8560), // Orange color
                contentColor = Color.White // Text color
            )
        ) {
            Text(
                text = "GENERATE",
                fontSize = 16.sp,
                fontFamily = tenorFontFamily

            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        // Teks "Result:"
        Text(
            text = "RESULT:",
            fontSize = 20.sp,
            fontFamily = tenorFontFamily
        )

        // Teks hasil generate
        Text(
            text = AIresponse,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            fontFamily = tenorFontFamily,
            modifier = Modifier.padding(16.dp)
        )
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            color = Color.Black, // Sesuaikan warna garis
            thickness = 1.dp // Sesuaikan ketebalan garis
        )


    }
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
}
@Preview
@Composable
fun PreviewAIGenerate() {
    // You can pass a dummy context or remove context-related code in previews
    AIGeneratePhotos()
}

fun visionModelCall(context: Context): String{
    val bitmap = AppCompatResources.getDrawable(context, R.drawable.foto_jas)?.toBitmap()
    var imageData = bitmap!!
    var responseString = ""

    val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-pro",
        apiKey = "AIzaSyCKO7-qQBXsmPWHTn6f2aUzLRlX4-U6mnM"
    )
    println("vision model call3")

    val inputContent = content {
        image(imageData)
        text("Give a good description on what clothing this is, including the color as the clothing's title. Specify which category this is among casual/semi-formal/formal/business casual/smart casual/athleisure/evening wear/cocktail/loungewear/sportswear/other. And specify a tag according if this is a top/bottom/footwear/accessories. write your answer down as Clothing:... Category:... Tag:..., the answer for clothing should be a sentence while category and tag should be a few words")
    }
    MainScope().launch {
        val response = generativeModel.generateContent(inputContent)
        responseString = response.toString()
        println(responseString)
    }

    while(responseString == ""){
        Thread.sleep(1_000)
        if(responseString != ""){
            break
        }
    }
    return(responseString)
}
