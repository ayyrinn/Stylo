package com.example.stylo

import android.content.Context
import android.content.SharedPreferences

object SavedPreference {
    const val EMAIL = "email"
    const val USERNAME = "username"

    private fun getSharedPreferences(ctx: Context): SharedPreferences {
        return ctx.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    }

    fun setEmail(ctx: Context, email: String) {
        val editor = getSharedPreferences(ctx).edit()
        editor.putString(EMAIL, email)
        editor.apply()
    }

    fun getEmail(ctx: Context): String? {
        return getSharedPreferences(ctx).getString(EMAIL, "")
    }

    fun setUsername(ctx: Context, username: String) {
        val editor = getSharedPreferences(ctx).edit()
        editor.putString(USERNAME, username)
        editor.apply()
    }

    fun getUsername(ctx: Context): String? {
        return getSharedPreferences(ctx).getString(USERNAME, "")
    }
}
