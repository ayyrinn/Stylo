package com.example.stylo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stylo.ui.theme.StyloTheme
import com.example.stylo.ui.theme.cormorantFontFamily
import com.example.stylo.ui.theme.tenorFontFamily

class SelectCategoriesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StyloTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Pass a function as the argument
                    SelectCategories(onCategoryClick = ::handleCategoryClick)
                }
            }
        }
    }

    private fun handleCategoryClick(selectedCategory: String) {
        // Save the selected category to Shared Preferences
        saveSelectedCategory(this, selectedCategory)

        // Navigate to the next activity
        val intent = Intent(this, AIPhotosActivity::class.java)
        startActivity(intent)
    }

    private fun saveSelectedCategory(context: Context, category: String) {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("selected_category", category)
            apply()
        }
    }
}

// Assuming your category data is stored in a list of strings
val categories = listOf("CASUAL", "SEMI-FORMAL", "FORMAL", "BUSINESS CASUAL", "SMART CASUAL", "ATHLEISURE", "EVENING WEAR", "COCKTAIL", "LOUNGEWEAR", "SPORTSWEAR", "OTHER")

@Composable
fun SelectCategories(
    onCategoryClick: (String) -> Unit // Callback function for handling category selection
) {
    val context = LocalContext.current
    var selectedCategory by remember { mutableStateOf("") }
    var customCategory by remember { mutableStateOf("") }
    var showCustomInput by remember { mutableStateOf(false) }

    var showMenu by remember { mutableStateOf(false) }
    if (showCustomInput) {
        AlertDialog(
            onDismissRequest = { showCustomInput = false },
            title = { Text("Enter Your Category") },
            text = {
                TextField(
                    value = customCategory,
                    onValueChange = { customCategory = it },
                    label = { Text("Custom Category") }
                )
            },
            confirmButton = {
                Button(onClick = {
                    showCustomInput = false
                }) {
                    Text("OK")
                }
            }
        )
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar
            StyloTopBar(onMenuClick = { showMenu = !showMenu })

            // Main Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF3EEEA))
                    .padding(top = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "SELECT CATEGORIES",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontFamily = cormorantFontFamily,
                    textAlign = TextAlign.Center,
                    textDecoration = TextDecoration.Underline,
                )
                Text(
                    text = "You can skip this step and click next.",
                    fontSize = 15.sp,
                    color = Color.Gray,
                    fontFamily = tenorFontFamily,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Use LazyColumn for better performance with many categories
                LazyColumn {
                    items(categories) { category ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                                .clickable {
                                    selectedCategory = category
                                    Log.d("SelectCategoriesActivity", "Selected category: $selectedCategory")
                                    if (category == "OTHER") {
                                        showCustomInput = true
                                    }
                                }
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = category,
                                fontSize = 20.sp,
                                fontFamily = cormorantFontFamily,
                                textAlign = TextAlign.Center,
                                color = if (selectedCategory == category) Color(0xFFDD8560) else Color.Black
                            )
                        }
                    }
                }

                // Next Button
                Button(
                    onClick = {
                        // Determine the final category to use
                        val finalCategory = if (selectedCategory == "OTHER") customCategory else if (selectedCategory.isEmpty()) "Not specified" else selectedCategory

                        // Check if a category is selected or if a custom category is provided
//                        if (finalCategory.isNotEmpty()) {
//
//                        } else {
//
//
//                            // Save the selected category to Shared Preferences
//                            saveSelectedCategory(context, "Not specified")
//                        }
                        // sebug message
                        Log.d("SelectCategoriesActivity", selectedCategory)

                        // Save the selected category to Shared Preferences
                        saveSelectedCategory(context, finalCategory)

                        // Navigate to AIPhotosActivity with the selected category
                        val intent = Intent(context, AIPhotosActivity::class.java).apply {
                            putExtra("selected_category", "Not specified")
                        }
                        context.startActivity(intent)

                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDD8560)),
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .align(Alignment.End)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Next", color = Color.White, fontFamily = cormorantFontFamily)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(imageVector = Icons.Filled.ArrowForward, contentDescription = "Next", tint = Color.White)
                    }
                }
            }
        }
        if (showMenu) {
            ReusableDrawer(context = context, onDismiss = { showMenu = false })
        }
    }
}


@Preview
@Composable
fun PreviewsCategories() {
    SelectCategories { /* Handle category click here */ }
}