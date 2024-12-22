package com.example.stylo
import android.content.Intent
import android.os.Bundle
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.stylo.R
import com.example.stylo.ui.theme.cormorantFontFamily
import com.example.stylo.ui.theme.miamaFontFamily
import com.example.stylo.ui.theme.tenorFontFamily

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

    //header stylo
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .background(color = Color(0xFFF3EEEA)),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        StyloTopBar(onMenuClick = { showMenu = !showMenu })





        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "GALLERY",
            color = Color.Black,
            fontSize = 28.sp,
            fontFamily = tenorFontFamily,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(start = 15.dp)
        )
        Spacer(modifier = Modifier.height(25.dp))


        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 1.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally), // Centers and spaces items
            verticalAlignment = Alignment.CenterVertically){
            // Left Button
            Box(
                modifier = Modifier
                    .clickable {
                        val intent = Intent(context, MoreTopActivity::class.java)
                        context.startActivity(intent)
                    }
                    .width(155.dp)
                    .height(215.dp)
                    .background(Color.Transparent, shape = RectangleShape)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.foto_jas),
                    contentDescription = "Camera Left",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(185.dp)
                        .clip(RoundedCornerShape(40.dp))
                        .align(Alignment.Center)
                )
                Text(
                    text = "TOP",
                    color = Color(0xFF776B5D),
                    fontSize = 20.sp,
                    fontFamily = tenorFontFamily,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(top = 10.dp)
                )
            }

            Box(
                modifier = Modifier
                    .clickable {
                        val intent = Intent(context, MoreBottomActivity::class.java)
                        context.startActivity(intent)
                    }
                    .width(155.dp)
                    .height(215.dp)
                    .background(Color.Transparent, shape = RectangleShape)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.foto_jas),
                    contentDescription = "Camera Left",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(185.dp)
                        .clip(RoundedCornerShape(40.dp))
                        .align(Alignment.Center)
                )
                Text(
                    text = "BOTTOM",
                    color = Color(0xFF776B5D),
                    fontSize = 20.sp,
                    fontFamily = tenorFontFamily,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(top = 10.dp)
                )
            }
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 1.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally), // Centers and spaces items
            verticalAlignment = Alignment.CenterVertically){
            // Left Button
            Box(
                modifier = Modifier
                    .clickable {
                        val intent = Intent(context, MoreFootwearActivity::class.java)
                        context.startActivity(intent)
                    }
                    .width(155.dp)
                    .height(215.dp)
                    .background(Color.Transparent, shape = RectangleShape)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.foto_jas),
                    contentDescription = "Camera Left",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(185.dp)
                        .clip(RoundedCornerShape(40.dp))
                        .align(Alignment.Center)
                )
                Text(
                    text = "FOOTWEAR",
                    color = Color(0xFF776B5D),
                    fontSize = 20.sp,
                    fontFamily = tenorFontFamily,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(top = 10.dp)
                )
            }


            Box(
                modifier = Modifier
                    .clickable {
                        val intent = Intent(context, MoreAccessoriesActivity::class.java)
                        context.startActivity(intent)
                    }
                    .width(155.dp)
                    .height(215.dp)
                    .background(Color.Transparent, shape = RectangleShape)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.foto_jas),
                    contentDescription = "Camera Left",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(185.dp)
                        .clip(RoundedCornerShape(40.dp))
                        .align(Alignment.Center)
                )
                Text(
                    text = "ACCESSORIES",
                    color = Color(0xFF776B5D),
                    fontSize = 20.sp,
                    fontFamily = tenorFontFamily,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(top = 10.dp)
                )
            }
        }
    }
    if (showMenu) {
        ReusableDrawer(context = context, onDismiss = { showMenu = false })
    }
}

@Preview
@Composable
fun ReviewPhotos(){
    SelectPhotos()
}
