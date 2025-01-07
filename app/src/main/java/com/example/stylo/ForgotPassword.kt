package com.example.stylo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stylo.ui.theme.miamaFontFamily

class ForgotPassword : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ForgotPasswordScreen()
        }
    }
}
val Miama = FontFamily(
    Font(R.font.miama) // Nama file miama.ttf di `res/font`
)

val TenorSansRegular = FontFamily(
    Font(R.font.tenorsans_regular) // Nama file tenorsans_regular.ttf di `res/font`
)

val backgroundColor = Color(0xB0A695).copy(alpha = 0.9f)

@Composable
fun ForgotPasswordScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.backround_login_register),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
        //aaaaaaaa
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo Text
            Text(
                text = "Stylo",
                fontFamily = miamaFontFamily,
                fontSize = 55.sp,
                color = Color.White,
                modifier = Modifier.padding(top = 16.dp)
            )

            // Forgot Password Text
            Text(
                text = "FORGOT\nPASSWORD",
                fontFamily = TenorSansRegular,
                fontSize = 40.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 30.dp, top = 150.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

             //Password Field
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Password", fontSize = 15.sp, fontFamily = TenorSansRegular) },
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth(0.9f)
                    .height(48.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color(0xB0A695).copy(alpha = 0.9f)),
            )


            Spacer(modifier = Modifier.height(16.dp))

            // Confirm Password Field
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Confirm Password", fontSize = 15.sp, fontFamily = TenorSansRegular) },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(48.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color(0xB0A695).copy(alpha = 0.9f)),
//
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Forgot Password Button
            Button(
                onClick = { /*TODO: Forgot Password action*/ },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFDD8560) // Warna tombol
                )
            ) {
                Text(
                    text = "Forgot Password",
                    fontSize = 18.sp,
                    fontFamily = TenorSansRegular,
                    color = Color.White
                )
            }
        }
    }

}
@Preview
@Composable
fun Previews1(){
    ForgotPasswordScreen()
}