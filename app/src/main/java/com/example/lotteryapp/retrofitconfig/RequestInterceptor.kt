package com.app.tutorialapp.retrofitconfig

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import com.example.lotteryapp.Utility
import okhttp3.Interceptor
import okhttp3.Response

import java.io.IOException

class RequestInterceptor(//this is called when every request is sent
    private val context: Context
) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isConnected()) {
            throw NoConnectivityException(context)

        }
        val preferences =
            context.getSharedPreferences(Utility.PREF_KEY_TOKENS, Context.MODE_PRIVATE)
        val tokenvalue = preferences.getString(Utility.PREF_TOKEN, "")
        Log.d("Request", chain.request().url().encodedPath())

        // rewrite the request
        val newRequest = chain.request().newBuilder()
            .header("Authorization", "Bearer $tokenvalue")
            .build()
        return chain.proceed(newRequest)


    }

    private fun isConnected(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = connectivityManager.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }


}