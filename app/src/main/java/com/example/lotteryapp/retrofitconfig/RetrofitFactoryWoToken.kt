package com.app.tutorialapp.retrofitconfig

import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

//creatation of retrofit object without token interceptor
object RetrofitFactoryWoToken {
    private const val BASE_URL =
        "http://192.168.0.113:8000/"

    fun getRetrofitInstance(): LotteryAPI {
        val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(JacksonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
        return retrofit.create<LotteryAPI>(LotteryAPI::class.java)
    }
}