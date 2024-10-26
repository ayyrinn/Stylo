package com.example.stylo

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import java.io.ByteArrayOutputStream
import java.io.IOException

fun removeBackground(bitmap: Bitmap, apiKey: String, onResult: (Bitmap?) -> Unit) {
    val client = OkHttpClient()
    val url = "https://api.remove.bg/v1.0/removebg"

    // Convert bitmap to JPEG and then to ByteArray
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
    val byteArray = stream.toByteArray()

    // Create request body with toRequestBody extension function
    val requestBody = MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart(
            "image_file", "image.jpg",
            byteArray.toRequestBody("image/jpeg".toMediaTypeOrNull())
        )
        .build()

    // Request
    val request = Request.Builder()
        .url(url)
        .addHeader("X-Api-Key", apiKey)
        .post(requestBody)
        .build()

    client.newCall(request).enqueue(object : okhttp3.Callback {
        override fun onFailure(call: okhttp3.Call, e: IOException) {
            e.printStackTrace()
            onResult(null)
        }

        override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
            if (response.isSuccessful) {
                val responseBody: ResponseBody? = response.body
                val resultBitmap = responseBody?.byteStream()?.let { BitmapFactory.decodeStream(it) }
                onResult(resultBitmap)
            } else {
                onResult(null)
            }
        }
    })
}