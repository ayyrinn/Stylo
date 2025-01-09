package com.example.stylo

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.material.ChipDefaults
import coil.compose.rememberImagePainter
import com.example.stylo.ui.theme.cormorantFontFamily
import com.example.stylo.ui.theme.miamaFontFamily
import com.example.stylo.ui.theme.tenorFontFamily
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MoreTopActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoreTopScreen()
        }
    }
}

@Composable
fun MoreTopScreen() {
    val clothingData = remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()
    val auth = FirebaseAuth.getInstance()
    val userId = auth.currentUser?.uid // Get the current user's ID
    val context = LocalContext.current

    var showInfoDialog by remember { mutableStateOf(false) }
    var selectedItemInfo by remember { mutableStateOf<Map<String, Any>?>(null) }
    var showConfirmationDialog by remember { mutableStateOf(false) }
    var itemToDelete by remember { mutableStateOf<Map<String, Any>?>(null) }

    // Use LaunchedEffect to launch the coroutine
    LaunchedEffect(userId) {
        userId?.let {
            val retrievedData = retrieveClothingDataSuspend(it, "top")
            clothingData.value = retrievedData // Update the state with retrieved data
            println(clothingData.value)
        } ?: Log.e("MoreTopScreen", "User  is not logged in")
    }

    var showMenu by remember { mutableStateOf(false) }
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
            Text(
                text = "TOP",
                fontSize = 40.sp, // Font size
                color = Color.White, // Font color
                fontFamily = tenorFontFamily, // Font family
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .align(Alignment.CenterHorizontally)
            )

            // Check if clothing data is empty
            if (clothingData.value.isEmpty()) {
                // Display message and button
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "You don't have this collection yet.",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontFamily = tenorFontFamily,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            // Navigate to AddPhotoActivity
                            val intent = Intent(context, AddPhotoActivity::class.java)
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFDD8560) // Warna tombol
                        ),
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text(
                            text = "Add New Collection",
                            fontFamily = tenorFontFamily
                        )
                    }
                }
            } else {
                // LazyVerticalGrid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2), // Two columns
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(clothingData.value) { clothingItem ->
                        val imageUrl = clothingItem["imageurl"] as? String
                        val itemId = clothingItem["id"] as? String
                        imageUrl?.let {
                            ImageCard(
                                imageUrl = it,
                                context = context,
                                onInfoClick = {
                                    selectedItemInfo = clothingItem
                                    showInfoDialog = true
                                },
                                onDeleteClick = {
                                    itemToDelete = clothingItem
                                    showConfirmationDialog = true
                                }
                            )
                        }
                    }
                }
            }
        }
        if (showMenu) {
            ReusableDrawer(context = context, onDismiss = { showMenu = false })
        }

        // Info Dialog
        if (showInfoDialog && selectedItemInfo != null) {
            InfoDialog(
                item = selectedItemInfo!!,
                onDismiss = { showInfoDialog = false }
            )
        }

        // Confirmation Dialog
        if (showConfirmationDialog && itemToDelete != null) {
            AlertDialog(
                onDismissRequest = { showConfirmationDialog = false },
                title = { Text(text = "Confirm Deletion", fontFamily = tenorFontFamily) },
                text = { Text(text = "Are you sure you want to delete this item?", fontFamily = tenorFontFamily) },
                confirmButton = {
                    Button(
                        onClick = {
                            deleteClothingItem(itemToDelete!!)
                            clothingData.value = clothingData.value.filter { it["id"] != itemToDelete!!["id"] }
                            showConfirmationDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White
                        ),
                        border = BorderStroke(1.dp, Color(0xFFDD8560)),
                    ) {
                        Text(text = "Delete", fontFamily = tenorFontFamily, color = Color(0xFFDD8560))
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showConfirmationDialog = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFDD8560)
                        ),
                    ) {
                        Text(text = "Cancel", fontFamily = tenorFontFamily)
                    }
                }
            )
        }
    }
}

@Composable
fun ImageCard(imageUrl: String, context: Context, onInfoClick: () -> Unit, onDeleteClick: () -> Unit) {

    Box(
        modifier = Modifier
            .width(155.dp)
            .clickable { }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 170.dp) // Add some space above the box
                .background(Color.DarkGray, shape = RoundedCornerShape(8.dp)) // Rounded corners
                .height(50.dp) // Set a fixed height for the box
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp, bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onInfoClick) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = "Info",
                        tint = Color.White
                    )
                }
                IconButton(onClick = {
                    onDeleteClick()
                    Log.d("ImageCard", "Delete button clicked")
                }) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete",
                        tint = Color.Red
                    )
                }
            }
        }

        Image(
            painter = rememberImagePainter(imageUrl),
            contentDescription = "Clothing Item",
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    context.startActivity(
                        Intent(context, AIPhotosActivity::class.java)
                            .putExtra("image_url", imageUrl)
                    )
                }
                .height(185.dp)
                .background(Color.Gray, shape = RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )


    }
}


@Composable
fun InfoDialog(item: Map<String, Any>, onDismiss: () -> Unit) {
    // Create a custom dialog layout using a Box
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable(onClick = onDismiss)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        ElevatedCard(
            modifier = Modifier
                .padding(16.dp)
                .background(Color(0xFFF3EEEA))
                .fillMaxWidth()
                .wrapContentHeight(),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "ITEM INFORMATION", fontFamily = tenorFontFamily, color = Color.Black, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                val imageUrl = item["imageurl"] as? String
                imageUrl?.let {
                    Image(
                        painter = rememberImagePainter(it),
                        contentDescription = "Item Image",
                        modifier = Modifier
                            .aspectRatio(1f)
                            .height(250.dp)
                            .background(Color.Gray, shape = RoundedCornerShape(8.dp))
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                val categories = (item["category"] as? String)?.split("/") ?: emptyList()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    for (category in categories) {
                        ElevatedAssistChip(
                            onClick = { },
                            label = { Text(text = category.trim().uppercase(), fontFamily = TenorSansRegular, color = Color.Black) },
                            modifier = Modifier.padding(end = 4.dp),
                            colors = AssistChipDefaults.elevatedAssistChipColors(
                                containerColor = Color.White
                            ),
                            border = BorderStroke(1.dp, Color(0xFFDD8560)),
                            shape = RoundedCornerShape(50),
                        )
                    }
                }
                Text(
                    text = "${item["description"]}",
                    fontFamily = tenorFontFamily,
                    color = Color.Black,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFDD8560)
                    ),
                ) {
                    Text(text = "Close",  fontFamily = tenorFontFamily)
                }
            }
        }
    }
}

suspend fun retrieveClothingDataSuspend(userId: String, type: String): List<Map<String, Any>> {
    val db = Firebase.firestore
    val clothingList = mutableListOf<Map<String, Any>>()

    return try {
        val result = db.collection("clothes")
            .whereEqualTo("userID", userId)
            .whereEqualTo("type", type)
            .get()
            .await() // Await the result using Kotlin Coroutines
        for (document in result) {
            val dataWithId = document.data.toMutableMap()
            dataWithId["id"] = document.id
            clothingList.add(dataWithId)
        }
        Log.d("result", clothingList.toString());
        clothingList
    } catch (e: Exception) {
        Log.w("FirebaseError", "Error retrieving documents", e)
        emptyList()
    }
}

fun deleteClothingItem(clothingItem: Map<String, Any>) {
    val db = Firebase.firestore
    val auth = FirebaseAuth.getInstance()
    val userId = auth.currentUser ?.uid ?: return // Get the current user's ID

    // Extract the document ID from the clothing item
    val itemId = clothingItem["id"] as? String ?: run {
        Log.e("MoreTopActivity", "Item ID is null, cannot delete")
        return
    }

    Log.d("MoreTopActivity", "Attempting to delete item with ID: $itemId") // Log the ID being deleted

    // Delete the document from Firestore
    db.collection("clothes")
        .document(itemId) // Use the document ID directly
        .delete()
        .addOnSuccessListener {
            Log.d("MoreTopActivity", "DocumentSnapshot successfully deleted: $itemId") // Log success
        }
        .addOnFailureListener { e ->
            Log.w("MoreTopActivity", "Error deleting document with ID: $itemId", e) // Log failure
        }
}

@Preview(showBackground = true)
@Composable
fun PreviewMoreTop() {
    MoreTopScreen()
}
