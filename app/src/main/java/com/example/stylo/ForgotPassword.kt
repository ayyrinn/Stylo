package com.example.stylo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class ForgotPassword : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ForgotPasswordScreen()
        }
    }
}

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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo Text
            Text(
                text = "Stylo",
                fontFamily = FontFamily.Cursive,
                fontSize = 55.sp,
                color = Color.White,
                modifier = Modifier.padding(top = 16.dp)
            )

            // Forgot Password Text
            Text(
                text = "FORGOT\nPASSWORD",
                fontFamily = FontFamily.Serif,
                fontSize = 50.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 30.dp, top = 180.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Password Field
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Password") },
                modifier = Modifier
                    .padding(top = 30.dp)
                    .fillMaxWidth(0.9f)
                    .height(58.dp),
//                colors = TextFieldDefaults.outlinedTextFieldColors(
//                    containerColor = Color.White,
//                    focusedBorderColor = colorResource(id = R.color.black),
//                    unfocusedBorderColor = colorResource(id = R.color.black)

            )

            Spacer(modifier = Modifier.height(16.dp))

            // Confirm Password Field
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Confirm Password") },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(58.dp),
//                colors = TextFieldDefaults.outlinedTextFieldColors(
//                    containerColor = Color.White,
//                    focusedBorderColor = colorResource(id = R.color.black),
//                    unfocusedBorderColor = colorResource(id = R.color.black)
//                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Forgot Password Button
            Button(
                onClick = { /*TODO: Forgot Password action*/ },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
//                    containerColor = colorResource(id = R.color.button_color)
                )
            ) {
                Text(
                    text = "Forgot Password",
                    fontSize = 20.sp,
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