package com.app.tutorialapp.retrofitconfig

import android.content.Context
import com.example.lotteryapp.R
import java.io.IOException

class NoConnectivityException(private val context: Context) :
    IOException() {
    override val message: String
        get() = context.resources.getString(R.string.toast_no_internet)

}