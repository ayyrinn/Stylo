package com.example.stylo

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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stylo.ui.theme.cormorantFontFamily
import com.example.stylo.ui.theme.miamaFontFamily
import com.example.stylo.ui.theme.tenorFontFamily
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class MoreFootwearActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoreFootwearScreen()
        }
    }
}

@Composable
fun MoreFootwearScreen() {
    val clothingData = remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
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
            val footwears = retrieveClothingDataSuspend(it, "footwear")
            clothingData.value = footwears // Update the state with retrieved data
            println(clothingData.value)
        } ?: Log.e("MoreTopScreen", "User is not logged in")
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
                text = "FOOTWEAR",
                fontSize = 40.sp, // Ukuran font sesuai kebutuhan
                color = Color.White, // Warna font putih
                fontFamily = tenorFontFamily, // Font keluarga sesuai desain
                modifier = Modifier
                    .padding(vertical = 8.dp)
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
                            deleteFootwearItem(itemToDelete!!)
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

fun deleteFootwearItem(clothingItem: Map<String, Any>) {
    val db = Firebase.firestore
    val auth = FirebaseAuth.getInstance()
    val userId = auth.currentUser ?.uid ?: return // Get the current user's ID

    // Extract the document ID from the clothing item
    val itemId = clothingItem["id"] as? String ?: run {
        Log.e("MoreBottomActivity", "Item ID is null, cannot delete")
        return
    }

    Log.d("MoreBottomActivity", "Attempting to delete item with ID: $itemId") // Log the ID being deleted

    // Delete the document from Firestore
    db.collection("clothes")
        .document(itemId) // Use the document ID directly
        .delete()
        .addOnSuccessListener {
            Log.d("MoreBottomActivity", "DocumentSnapshot successfully deleted: $itemId") // Log success
        }
        .addOnFailureListener { e ->
            Log.w("MoreBottomActivity", "Error deleting document with ID: $itemId", e) // Log failure
        }
}

@Preview(showBackground = true)
@Composable
fun PreviewMoreFootwear() {
    MoreFootwearScreen()
}
