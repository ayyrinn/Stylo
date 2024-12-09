package com.example.stylo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import com.example.stylo.ui.theme.cormorantFontFamily
import com.example.stylo.ui.theme.tenorFontFamily
//import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.stylo.ui.theme.miamaFontFamily
//import com.android.volley.Header

class AboutUsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AboutUsPage()
        }
    }
}


@Composable
fun AboutUsPage(){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3EEEA))
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally // Center aligns all items horizontally
    ){
        // ABOUT US -- cormorantFontFamily (28)
        // Text di bawahnya -- tenorFontFamily (20)
        // REACH OUT -- cormorantFontFamily (22)
        // hitstyloteam@gmail.com Malang, Indonesia   -- tenorFontFamily (18)
        // Copyright © 2024 Stylo. All Rights Reserved. -- tenorFontFamily (16)
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

        Column(
            modifier = Modifier
                .size(width = 415.dp, height = 215.dp)
                .fillMaxWidth()
//                .padding(vertical = 16.dp)
                .height(215.dp)

        ) {
            Image(
                painter = painterResource(id = R.drawable.home_img_4), //ganti nama fotonya
                contentDescription = "About Us Image",
                modifier = Modifier
//                    .fillMaxSize()
                    .fillMaxWidth()

            )
        }

        //spacing 1,21, font 28
        Text(
            text = "ABOUT US",
            fontFamily = cormorantFontFamily, fontWeight = FontWeight.Normal, // Use your custom font here
            fontSize = 28.sp,
            color = Color.Black,
            textAlign = TextAlign.Center,
            letterSpacing = 1.21.sp,
            modifier = Modifier
                .padding(top = 20.dp)
        )
//        //font 20, Line height, 31, align kiri
        Text(
            text = "Welcome to Stylo, where style meets creativity. Founded in 2024, our platform is dedicated to the art of mix and match, empowering you to curate outfits that reflect your unique personality.\n" +
                    "\n" +
                    "With our intuitive features, planning your wardrobe becomes a joyful experience, allowing you to effortlessly blend fashion with sustainability. Embrace your style journey with us and let your creativity shine!",
            fontFamily = tenorFontFamily, fontWeight = FontWeight.Normal, // Use your custom font here
            fontSize = 20.sp,
            color = Color.Black,
            textAlign = TextAlign.Left,
            lineHeight = 31.sp,
            modifier = Modifier
                .padding(top = 20.dp)
        )
        //line height 35, spacing 1.21
        Text(
            text = "REACH OUT",
            fontFamily = cormorantFontFamily, fontWeight = FontWeight.Normal, // Use your custom font here
            fontSize = 22.sp,
            color = Color.Black,
            textAlign = TextAlign.Center,
            lineHeight = 31.sp,
            letterSpacing = 1.21.sp,
            modifier = Modifier
                .padding(top = 50.dp)
        )

        ContactInfo()
        CopyrightFooter()
    }
}

@Composable
fun ContactInfo(){
    Text(
        text = "hitstyloteam@gmail.com\nMalang, Indonesia",
        fontFamily = tenorFontFamily, fontWeight = FontWeight.Normal, // Use your custom font here
        fontSize = 18.sp,
        color = Color.Black,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .padding(top = 20.dp)
            .padding(bottom = 20.dp)
    )
}

@Composable
fun CopyrightFooter() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFB0A695))
            .padding(top = 10.dp)
            .padding(bottom = 10.dp)
    ){
        Text(
            text = "Copyright © 2024 Stylo. All Rights Reserved.",
            fontFamily = tenorFontFamily, fontWeight = FontWeight.Normal, // Use your custom font here
            fontSize = 16.sp,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 10.dp)
                .align(Alignment.Center)
                .padding(bottom = 10.dp)
        )
    }

}

@Preview
@Composable
fun PreviewAboutUs(){
    AboutUsPage()
}