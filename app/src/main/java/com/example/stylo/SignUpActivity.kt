package com.example.stylo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stylo.ui.theme.StyloTheme
import com.example.stylo.ui.theme.miamaFontFamily

class SignUpActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StyloTheme {
                SignUpScreen()
            }
        }
    }
}

@Composable
fun SignUpScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.backround_login_register),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            // Logo
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

                // Title
                Text(
                    text = "SIGN UP",
                    fontFamily = TenorSansRegular,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 30.dp, top = 150.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Email Field
                OutlinedTextField(
                    value = "",
                    onValueChange = { },
                    placeholder = {
                        Text(
                            "Email",
                            fontFamily = TenorSansRegular,
                            fontSize = 15.sp
                        )
                    },
                    textStyle = TextStyle(
                        fontFamily = TenorSansRegular, // Font sesuai aplikasi
                        fontSize = 15.sp
                    ),
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth(0.9f)
                        .height(48.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color(0xB0A695).copy(alpha = 0.9f)),
                )

                // Phone Number Field
                OutlinedTextField(
                    value = "",
                    onValueChange = { },
                    placeholder = {
                        Text(
                            "Phone Number",
                            fontFamily = TenorSansRegular,
                            fontSize = 15.sp
                        )
                    },
                    textStyle = TextStyle(
                        fontFamily = TenorSansRegular, // Font sesuai aplikasi
                        fontSize = 15.sp
                    ),
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth(0.9f)
                        .height(48.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color(0xB0A695).copy(alpha = 0.9f)),
                )

                var passwordVisible by remember { mutableStateOf(false) }
                // Password Field
                OutlinedTextField(
                    value = "",
                    onValueChange = { },
                    placeholder = {
                        Text(
                            "Password",
                            fontFamily = TenorSansRegular,
                            fontSize = 15.sp
                        )
                    },
                    textStyle = TextStyle(
                        fontFamily = TenorSansRegular, // Font sesuai aplikasi
                        fontSize = 15.sp
                    ),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            val image =
                                if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                            Icon(imageVector = image, contentDescription = null)
                        }
                    },
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth(0.9f)
                        .height(48.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color(0xB0A695).copy(alpha = 0.9f)),
                )

                // Confirm Password Field
                OutlinedTextField(
                    value = "",
                    onValueChange = { },
                    placeholder = {
                        Text(
                            "Confirm Password",
                            fontFamily = TenorSansRegular,
                            fontSize = 15.sp
                        )
                    },
                    textStyle = TextStyle(
                        fontFamily = TenorSansRegular, // Font sesuai aplikasi
                        fontSize = 15.sp
                    ),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            val image =
                                if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                            Icon(imageVector = image, contentDescription = null)
                        }
                    },
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth(0.9f)
                        .height(48.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color(0xB0A695).copy(alpha = 0.9f)),
                )

                Spacer(modifier = Modifier.height(30.dp))

                // Sign Up Button
                Button(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFDD8560) // Warna tombol
                    )
                ) {
                    Text(
                        text = "Sign Up",
                        color = Color.White,
                        fontFamily = TenorSansRegular,
                        fontSize = 18.sp
                    )
                }

                // OR Text
                Text(
                    text = "OR",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontFamily = TenorSansRegular,
                    modifier = Modifier
                        .padding(top = 15.dp, bottom = 15.dp)
                )

                // Sign Up with Google Button
                Button(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(48.dp),
//                        .padding(top = 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFDD8560) // Warna tombol
                    )
                ) {
                    Text(
                        text = "Sign Up With Google",
                        fontFamily = TenorSansRegular,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    StyloTheme {
        SignUpScreen()
    }
}