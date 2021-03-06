package com.app.tutorialapp.retrofitconfig

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

//creatation of retrofit object with token interceptor
object RetrofitFactory {
    private const val BASE_URL =
        "http://192.168.0.113:8000/"

    fun getRetrofitInstance(context: Context?): LotteryAPI {
        //context needed for shared prefrences
        val okHttpClient=OkHttpClient.Builder()
            .addInterceptor(RequestInterceptor(context!!))
            .build()

        val retrofit: Retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(JacksonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
        return retrofit.create<LotteryAPI>(LotteryAPI::class.java)
    }
}