package com.example.stylo
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stylo.R
import com.example.stylo.ui.theme.cormorantFontFamily
import com.example.stylo.ui.theme.miamaFontFamily
import com.example.stylo.ui.theme.tenorFontFamily

@Composable
fun SelectPhotos() {

    //header stylo
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
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

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "CHOOSE YOUR\nCOLLECTIONS",
            color = Color.Black,
            fontSize = 28.sp,
            fontFamily = tenorFontFamily,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(start = 15.dp)
        )
        Spacer(modifier = Modifier.height(50.dp))

        Text(
            text = "TOP",
            color = Color(0xFF776B5D),
            fontSize = 20.sp,
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
                    .clickable {  }
                    .width(155.dp)
                    .height(215.dp)
                    .background(Color.Transparent, shape = RectangleShape) // Adjust shape if needed
            ) {
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    Image(
                        painter = painterResource(id = R.drawable.foto_jas),
                        contentDescription = "Camera Left",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(185.dp)
                            .clip(RoundedCornerShape(40.dp))
                    )

                }

            }


            Box(
                modifier = Modifier
                    .clickable {  }
                    .width(155.dp)
                    .height(215.dp)
                    .background(Color.Transparent, shape = RectangleShape) // Adjust shape if needed
            ) {
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    Image(
                        painter = painterResource(id = R.drawable.foto_jas),
                        contentDescription = "Camera Right",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(185.dp)
                            .clip(RoundedCornerShape(40.dp))
                    )

                }
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
                    .clickable {  }
                    .width(155.dp)
                    .height(215.dp)
                    .background(Color.Transparent, shape = RectangleShape) // Adjust shape if needed
            ) {
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    Image(
                        painter = painterResource(id = R.drawable.foto_jas),
                        contentDescription = "Camera Left",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(185.dp)
                            .clip(RoundedCornerShape(40.dp))
                    )

                }

            }


            Box(
                modifier = Modifier
                    .clickable {  }
                    .width(155.dp)
                    .height(215.dp)
                    .background(Color.Transparent, shape = RectangleShape) // Adjust shape if needed
            ) {
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    Image(
                        painter = painterResource(id = R.drawable.foto_jas),
                        contentDescription = "Camera Right",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(185.dp)
                            .clip(RoundedCornerShape(40.dp))
                    )

                }
            }
        }

        Spacer(modifier = Modifier.height(50.dp))

        Text(
            text = "BOTTOM",
            color = Color(0xFF776B5D),
            fontSize = 20.sp,
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
                    .clickable {  }
                    .width(155.dp)
                    .height(215.dp)
                    .background(Color.Transparent, shape = RectangleShape) // Adjust shape if needed
            ) {
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    Image(
                        painter = painterResource(id = R.drawable.foto_jas),
                        contentDescription = "Camera Left",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(185.dp)
                            .clip(RoundedCornerShape(40.dp))
                    )

                }

            }


            Box(
                modifier = Modifier
                    .clickable {  }
                    .width(155.dp)
                    .height(215.dp)
                    .background(Color.Transparent, shape = RectangleShape) // Adjust shape if needed
            ) {
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    Image(
                        painter = painterResource(id = R.drawable.foto_jas),
                        contentDescription = "Camera Right",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(185.dp)
                    )

                }
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
                    .clickable {  }
                    .width(155.dp)
                    .height(215.dp)
                    .background(Color.Transparent, shape = RectangleShape) // Adjust shape if needed
            ) {
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    Image(
                        painter = painterResource(id = R.drawable.foto_jas),
                        contentDescription = "Camera Left",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(185.dp)
                            .clip(RoundedCornerShape(40.dp))
                    )

                }

            }


            Box(
                modifier = Modifier
                    .clickable {  }
                    .width(155.dp)
                    .height(215.dp)
                    .background(Color.Transparent, shape = RectangleShape) // Adjust shape if needed
            ) {
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    Image(
                        painter = painterResource(id = R.drawable.foto_jas),
                        contentDescription = "Camera Right",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(185.dp)
                            .clip(RoundedCornerShape(40.dp))
                    )

                }
            }
        }
        Spacer(modifier = Modifier.height(50.dp))

        Text(
            text = "ACCESSORIES",
            color = Color(0xFF776B5D),
            fontSize = 20.sp,
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
                    .clickable {  }
                    .width(155.dp)
                    .height(215.dp)
                    .background(Color.Transparent, shape = RectangleShape) // Adjust shape if needed
            ) {
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    Image(
                        painter = painterResource(id = R.drawable.foto_jas),
                        contentDescription = "Camera Left",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(185.dp)
                            .clip(RoundedCornerShape(40.dp))
                    )

                }

            }


            Box(
                modifier = Modifier
                    .clickable {  }
                    .width(155.dp)
                    .height(215.dp)
                    .background(Color.Transparent, shape = RectangleShape) // Adjust shape if needed
            ) {
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    Image(
                        painter = painterResource(id = R.drawable.foto_jas),
                        contentDescription = "Camera Right",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(185.dp)
                            .clip(RoundedCornerShape(40.dp))
                    )

                }
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
                    .clickable {  }
                    .width(155.dp)
                    .height(215.dp)
                    .background(Color.Transparent, shape = RectangleShape) // Adjust shape if needed
            ) {
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    Image(
                        painter = painterResource(id = R.drawable.foto_jas),
                        contentDescription = "Camera Left",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(185.dp)
                            .clip(RoundedCornerShape(40.dp))
                    )

                }

            }


            Box(
                modifier = Modifier
                    .clickable {  }
                    .width(155.dp)
                    .height(215.dp)
                    .background(Color.Transparent, shape = RectangleShape) // Adjust shape if needed
            ) {
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    Image(
                        painter = painterResource(id = R.drawable.foto_jas),
                        contentDescription = "Camera Right",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(185.dp)
                            .clip(RoundedCornerShape(40.dp))
                    )

                }
            }
        }
        Spacer(modifier = Modifier.height(50.dp))

        Text(
            text = "FOOTWEAR",
            color = Color(0xFF776B5D),
            fontSize = 20.sp,
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
                    .clickable {  }
                    .width(155.dp)
                    .height(215.dp)
                    .background(Color.Transparent, shape = RectangleShape) // Adjust shape if needed
            ) {
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    Image(
                        painter = painterResource(id = R.drawable.foto_jas),
                        contentDescription = "Camera Left",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(185.dp)
                            .clip(RoundedCornerShape(40.dp))
                    )

                }

            }


            Box(
                modifier = Modifier
                    .clickable {  }
                    .width(155.dp)
                    .height(215.dp)
                    .background(Color.Transparent, shape = RectangleShape) // Adjust shape if needed
            ) {
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    Image(
                        painter = painterResource(id = R.drawable.foto_jas),
                        contentDescription = "Camera Right",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(185.dp)
                            .clip(RoundedCornerShape(40.dp))
                    )

                }
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
                    .clickable {  }
                    .width(155.dp)
                    .height(215.dp)
                    .background(Color.Transparent, shape = RectangleShape) // Adjust shape if needed
            ) {
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    Image(
                        painter = painterResource(id = R.drawable.foto_jas),
                        contentDescription = "Camera Left",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(185.dp)
                            .clip(RoundedCornerShape(40.dp))
                    )

                }

            }


            Box(
                modifier = Modifier
                    .clickable {  }
                    .width(155.dp)
                    .height(215.dp)
                    .background(Color.Transparent, shape = RectangleShape) // Adjust shape if needed
            ) {
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    Image(
                        painter = painterResource(id = R.drawable.foto_jas),
                        contentDescription = "Camera Right",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(185.dp)
                            .clip(RoundedCornerShape(40.dp))
                    )

                }
            }
        }


    }
}

@Preview
@Composable
fun ReviewPhotos(){
    SelectPhotos()
}
