package com.example.stylo

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.example.stylo.ui.theme.miamaFontFamily

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

    // Function to handle category selection
    private fun handleCategoryClick(selectedCategory: String) {
        // Implement your logic here based on the selected category
        println("Selected category: $selectedCategory")
    }
}

// Assuming your category data is stored in a list of strings
val categories = listOf("CASUAL", "SEMI-FORMAL", "FORMAL", "BUSINESS CASUAL", "SMART CASUAL", "ATHLEISURE", "EVENING WEAR", "COCKTAIL", "LOUNGEWEAR", "SPORTSWEAR", "OTHER")

@Composable
fun SelectCategories(
    onCategoryClick: (String) -> Unit // Callback function for handling category selection
) {
    var showMenu by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3EEEA)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        StyloTopBar(onMenuClick = { showMenu = !showMenu })


        Text(
            text = "SELECT CATEGORIES",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = cormorantFontFamily,
            textAlign = TextAlign.Center,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .padding(top = 40.dp)
        )

        // Use LazyColumn for better performance with many categories
        LazyColumn {
            items(categories) { category ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .align(Alignment.CenterHorizontally),
                ) {
                    Text(
                        text = category,
                        fontSize = 20.sp,
                        fontFamily = cormorantFontFamily,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
//                            .weight(1f)
                            .align(Alignment.Center)
                            .padding(top = 5.dp, bottom = 5.dp)
                    )
                }
            }
        }
    }
    if (showMenu) {
        ReusableDrawer(context = context, onDismiss = { showMenu = false })
    }

//    Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
//        Image(
//            painter = painterResource(id = R.drawable.burger_icon),
//            contentDescription = "Burger Icon",
//            modifier = Modifier
//                .size(50.dp)
//                .fillMaxSize()
//                .clickable {  }
//                .padding(top = 10.dp),
//
//            // .padding(start = 160.dp, top = 16.dp)
//        )
//        Spacer(modifier = Modifier.weight(1f))
//        Text(text = "Stylo",
//            fontSize = 45.sp,
//            color = Color(0xFF776B5D),
//            fontFamily = miamaFontFamily,
//            modifier = Modifier
//                .padding(end = 35.dp)
//        )
//        Spacer(modifier = Modifier.weight(1f))
//    }
}

@Preview
@Composable
fun PreviewsCategories() {
    SelectCategories { /* Handle category click here */ }
}