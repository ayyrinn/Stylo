package com.example.stylo

import android.content.Context

// Function to save the selected category to Shared Preferences
fun saveSelectedCategory(context: Context, category: String) {
    val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    with(sharedPreferences.edit()) {
        putString("selected_category", category)
        apply() // or commit() if you want to block until the changes are written
    }
}

// Function to retrieve the selected category from Shared Preferences
fun getSelectedCategory(context: Context): String? {
    val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    return sharedPreferences.getString("selected_category", null) // Returns null if not found
}