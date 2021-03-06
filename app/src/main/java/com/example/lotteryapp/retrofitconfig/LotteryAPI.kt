package com.app.tutorialapp.retrofitconfig

import com.app.tutorialapp.model.*
import retrofit2.Call
import retrofit2.http.*

interface LotteryAPI {

    @POST("/api/login")
    fun login(@Body loginReq: LoginReq):Call<Map<String,String>>

    @POST("/api/signup")
    fun signup(@Body signupReq: SignupReq):Call<Map<String,String>>

    @GET("/api/users")
    fun lotteryUsers():Call<List<String>>

    //@GET("/home")


}

