package com.example.lotteryapp

import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import com.example.lotteryapp.databinding.ActivityHomeBinding
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val formatter = SimpleDateFormat("dd.MM.yyyy, HH:mm")
        val then = "20.02.2018, 14:00" //Timer date 2

        val calender:Calendar= Calendar.getInstance()
        calender.set(Calendar.HOUR_OF_DAY,20)
        calender.set(Calendar.MINUTE,0)
        calender.set(Calendar.SECOND,0)


        val now = Date()
        val diff: Long =  now.time -calender.time.time

        object : CountDownTimer(diff, 1000) {
            // adjust the milli seconds here
            override fun onTick(millisUntilFinished: Long) {

                val f: NumberFormat = DecimalFormat("00")

                val hour: Long = (millisUntilFinished / 3600000) % 24;

                val min: Long = (millisUntilFinished / 60000) % 60;

                val sec: Long = (millisUntilFinished / 1000) % 60;

                binding.tvCountdown.text=f.format(hour)+" : "+f.format(min)+" : "+f.format(sec)
                /*binding.tvCountdown.setText(
                    "" + String.format(
                        "%d - %d - %d",
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                            TimeUnit.MILLISECONDS.toHours(millisUntilFinished)
                        ),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                        )
                    )
                )*/
            }

            override fun onFinish() {

            }
        }.start()

    }
}