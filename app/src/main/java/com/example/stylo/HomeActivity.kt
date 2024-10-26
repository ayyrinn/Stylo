package com.example.stylo

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stylo.ui.theme.cormorantFontFamily
import com.example.stylo.ui.theme.miamaFontFamily

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomeScreen(context = this) // Pass the context to HomeScreen
        }
    }
}

@Composable
fun HomeScreen(context: HomeActivity) { // Accept the context as a parameter
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Stylo",
            fontSize = 45.sp,
            color = Color(0xFF776B5D),
            fontFamily = miamaFontFamily,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(),
        )

        Text(
            text = "SAY WHO\nYOU ARE WITHOUT\nSPEAKING",
            modifier = Modifier.padding(top = 100.dp),
            fontSize = 30.sp,
            color = Color.White,
            textAlign = TextAlign.Center,
            fontFamily = cormorantFontFamily
        )

        Button(
            onClick = {
                val intent = Intent(context, AddPhotoActivity::class.java) // Use the context
                context.startActivity(intent) // Use context to start activity
            },
            modifier = Modifier
                .padding(top = 50.dp)
                .width(300.dp)
                .height(50.dp)
        ) {
            Text(text = "Discover Your Style")
        }
//        Image(
//            painter = painterResource(id = R.drawable.img1), contentDescription = null
//        )
    }
    Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Image(
            painter = painterResource(id = R.drawable.burger_icon),
            contentDescription = "Burger Icon",
            modifier = Modifier
                .size(50.dp)
                .fillMaxSize()
                .clickable {  }
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
fun PreviewHome() {
    // You can pass a dummy context or remove context-related code in previews
    HomeScreen(context = HomeActivity()) // Just for preview, may not work directly
}
