package com.example.stylo

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.RectangleShape
//import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stylo.ui.theme.cormorantFontFamily
import com.example.stylo.ui.theme.miamaFontFamily
import com.example.stylo.ui.theme.tenorFontFamily

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
//    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
//            .verticalScroll(state = scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

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


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .paint(
                    painter = painterResource(id = R.drawable.home_img_1),
                )
        ) {

            Column(
                modifier = Modifier
                    .padding(top = 220.dp)
            ) {
                Text(
                    text = "SAY WHO\nYOU ARE WITHOUT\nSPEAKING",
                    modifier = Modifier.padding(top = 100.dp),
                    fontSize = 30.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontFamily = cormorantFontFamily
                )
                Spacer(modifier = Modifier.height(100.dp))
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
            }
        }


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .paint(
                    painter = painterResource(id = R.drawable.home_img_2),
                )
        ){
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
                ){
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
                ){
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
                ){
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
                ){
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


            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
            ){
                 Row(modifier = Modifier.fillMaxWidth(),
                     horizontalArrangement = Arrangement.SpaceEvenly) {
                     Image(
                         painter = painterResource(id = R.drawable.card_img),
                         contentDescription = "Camera Left",
                         modifier = Modifier
                             .width(130.dp)
                             .height(160.dp)
                     )
                     Image(
                         painter = painterResource(id = R.drawable.card_img),
                         contentDescription = "Camera Left",
                         modifier = Modifier
                             .width(130.dp)
                             .height(160.dp)
                     )
                 }
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly) {
                    Image(
                        painter = painterResource(id = R.drawable.card_img),
                        contentDescription = "Camera Left",
                        modifier = Modifier
                            .width(130.dp)
                            .height(160.dp)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.card_img),
                        contentDescription = "Camera Left",
                        modifier = Modifier
                            .width(130.dp)
                            .height(160.dp)
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


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .height(800.dp)
                .paint(
                    painter = painterResource(id = R.drawable.home_img_5),
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    Row {
                        Text(
                            text = "plan your",
                            fontSize = 30.sp,
                            color = Color.White,
                            fontFamily = cormorantFontFamily
                        )
                        Text(
                            text = "Outfits",
                            fontSize = 50.sp,
                            color = Color.White,
                            fontFamily = miamaFontFamily
                        )
                    }
                }
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    Row {
                        Text(
                            text = "slay your",
                            fontSize = 30.sp,
                            color = Color.White,
                            fontFamily = cormorantFontFamily
                        )
                        Text(
                            text = "Day",
                            fontSize = 50.sp,
                            color = Color.White,
                            fontFamily = miamaFontFamily
                        )
                    }

                }
            }


            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ){
                    Image(
                        painter = painterResource(id = R.drawable.todays_pick_frame),
                        contentDescription = null,
                        modifier = Modifier
                            .width(325.dp)
                            .height(460.dp)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.pick_image),
                        contentDescription = "Today's pick",
                        modifier = Modifier
                            .width(300.dp)
                            .height(400.dp)
                            .padding(bottom = 45.dp)
                    )
                }

            }

            Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(85.dp)
                        .background(
                            color = Color.Transparent
                        ),
                ) {
                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 30.dp),
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.next_icon),
                            contentDescription = "go to calendar button",
                            modifier = Modifier
                                .width(50.dp)
                                .height(70.dp)
                        )

                        Text(
                            text = "GO TO OUTFIT CALENDAR",
                            color = Color.White,               // Adjust text color
                            fontSize = 15.sp,                  // Adjust text size
                            fontFamily = tenorFontFamily
                        )
                    }
                }


        }
    }

}

@Preview
@Composable
fun PreviewHome() {
    // You can pass a dummy context or remove context-related code in previews
    HomeScreen(context = HomeActivity()) // Just for preview, may not work directly
}
