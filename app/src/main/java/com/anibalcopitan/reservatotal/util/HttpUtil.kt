package com.anibalcopitan.reservatotal.util

import okhttp3.*
import java.io.IOException

object HttpUtil {
    private val client: OkHttpClient by lazy {
        OkHttpClient()
    }

    fun sendGetRequest(url: String, onResponse: (String) -> Unit, onFailure: (Exception) -> Unit) {
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onFailure(e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseData = response.body?.string() ?: ""
                    onResponse(responseData)
                } else {
                    onFailure(IOException("Request failed with code ${response.code}"))
                }
            }
        })
    }
}