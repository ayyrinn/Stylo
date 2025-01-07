package com.example.stylo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import com.example.stylo.ui.theme.cormorantFontFamily
import com.example.stylo.ui.theme.tenorFontFamily
//import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview


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
    var showMenu by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF3EEEA))
                .verticalScroll(rememberScrollState()),
            //.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally // Center aligns all items horizontally
        ) {
            // Top Bar
            StyloTopBar(onMenuClick = { showMenu = !showMenu })

            // Main Content
            Column(
                modifier = Modifier
                    //                .size(width = 00.dp, height = 215.dp)
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp) // Atur ukuran kotak
                        .padding(top = 15.dp)

                ) {
                    Image(
                        painter = painterResource(id = R.drawable.home_img_4), // Ganti dengan nama foto
                        contentDescription = "About Us Image",
                        contentScale = ContentScale.Crop, // Memastikan gambar memenuhi kotak
                        modifier = Modifier.fillMaxSize() // Gambar mengisi seluruh kotak
                    )
                }
            }


            //spacing 1,21, font 28
            Text(
                text = "ABOUT US",
                fontFamily = cormorantFontFamily,
                fontWeight = FontWeight.Normal, // Use your custom font here
                fontSize = 28.sp,
                color = Color.Black,
                textAlign = TextAlign.Center,
                letterSpacing = 1.21.sp,
                modifier = Modifier
                    .padding(top = 25.dp)
            )
            //        //font 20, Line height, 31, align kiri
            Text(
                text = "Welcome to Stylo, where style meets creativity. Founded in 2024, our platform is dedicated to the art of mix and match, empowering you to curate outfits that reflect your unique personality.\n" +
                        "\n" +
                        "With our intuitive features, planning your wardrobe becomes a joyful experience, allowing you to effortlessly blend fashion with sustainability. Embrace your style journey with us and let your creativity shine!",
                fontFamily = tenorFontFamily,
                fontWeight = FontWeight.Normal, // Use your custom font here
                fontSize = 20.sp,
                color = Color.Black,
                textAlign = TextAlign.Left,
                lineHeight = 31.sp,
                modifier = Modifier
                    .padding(top = 15.dp)
                    .padding(horizontal = 20.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "DEVELOPERS",
                fontFamily = cormorantFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 23.sp,
                color = Color.Black,
                textAlign = TextAlign.Center,
                letterSpacing = 1.21.sp,
                modifier = Modifier
                    .padding(top = 25.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Row untuk menampilkan 5 box foto developer
            Column(
                modifier = Modifier
                    .fillMaxWidth()  // Agar column mengambil lebar penuh
                    .padding(horizontal = 16.dp), // Menambahkan padding horizontal jika diperlukan
                verticalArrangement = Arrangement.spacedBy(16.dp) // Menambahkan jarak antar elemen secara vertikal
            ) {
                BoxItemWithLabel(R.drawable.devkezia, "Kezia Angeline Santoso\n2602097542")
                BoxItemWithLabel(R.drawable.devannisa, "Annisa'u Rahma\n2602181603")
                BoxItemWithLabel(R.drawable.devtania, "Kristania Yohana Tumilaar\n2602182436")
                BoxItemWithLabel(R.drawable.devnaomi, "Angelica Naomi\n2602079836")
                BoxItemWithLabel(R.drawable.devapril, "Aprillia Chandra Dewi\n2602181383")
            }


            Spacer(modifier = Modifier.height(26.dp))

            Text(
                text = "REACH OUT",
                fontFamily = tenorFontFamily,
                fontWeight = FontWeight.Normal, // Use your custom font here
                fontSize = 20.sp,
                color = Color.Black,
                textAlign = TextAlign.Left,
                lineHeight = 31.sp,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .padding(horizontal = 20.dp)
            )
            //line height 35, spacing 1.21


            ContactInfo()
            CopyrightFooter()
        }
        if (showMenu) {
            ReusableDrawer(context = context, onDismiss = { showMenu = false })
        }
    }
}




@Composable
fun BoxItemWithLabel(imageRes: Int, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, // Center secara horizontal
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()// Pastikan kolom ini tidak memaksa foto ke bawah
    ) {
        Box(
            modifier = Modifier
                .size(150.dp) // Ukuran kotak untuk gambar
                .clip(CircleShape) // Membuat gambar berbentuk lingkaran
                .background(Color.White) // Placeholder background
                .padding(4.dp) // Memberi jarak antar gambar
                .align(Alignment.CenterHorizontally) // Center gambar secara horizontal
        ) {
            Image(
                painter = painterResource(id = imageRes), // Menampilkan gambar
                contentDescription = label,
                modifier = Modifier
                    .fillMaxSize() // Gambar memenuhi ukuran box
                    .clip(CircleShape), // Bentuk gambar lingkaran
                contentScale = ContentScale.Crop // Menjaga rasio gambar
            )
        }
        Text(
            text = label,
            fontFamily = tenorFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 8.dp)
                .align(Alignment.CenterHorizontally)
        )
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
