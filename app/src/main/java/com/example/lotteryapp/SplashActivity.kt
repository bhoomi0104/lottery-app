package com.example.lotteryapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.tutorialapp.login.LoginActivity
import com.example.lotteryapp.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySplashBinding.inflate(layoutInflater)
setContentView(binding.root)

        val thread: Thread = object : Thread() {
            override fun run() {
                sleep(500);
                nextMove()
            }
        }
        thread.start();
    }

    private fun nextMove() {
        val sharedPreferences = getSharedPreferences(Utility.PREF_KEY_MYPREFRENCES, Context.MODE_PRIVATE);


        //if user have completed onboarding
            //check if user is logged in
            val isUserLoggedin = sharedPreferences.getBoolean(Utility.PREF_LOGGEDIN, false)
            if (isUserLoggedin) {
//                if user is logged in go to home screen
                val intent = Intent(this@SplashActivity, HomeActivity::class.java);
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                startActivity(intent)
                finish()
            } else {
                //if user is not logged in go to login screen
                val intent = Intent(this@SplashActivity, LoginActivity::class.java);
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                startActivity(intent)
                finish()
            }

    }
}