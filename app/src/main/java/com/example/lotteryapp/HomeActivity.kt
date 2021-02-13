package com.example.lotteryapp

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.lotteryapp.databinding.ActivityHomeBinding
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class HomeActivity : AppCompatActivity() {
    private lateinit var layoutManager: LinearLayoutManager
    private var position: Int = 0
    private lateinit var binding: ActivityHomeBinding
    private lateinit var nameAdapter: NameAdapter
    private lateinit var timer: Timer
    private lateinit var timerTask: TimerTask

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /* binding.btnDeposit.setOnClickListener {
             val intent= Intent(this,DepositActivity::class.java)
             startActivity(intent)

         }*/


        layoutManager = LinearLayoutManager(applicationContext)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.recyclerView.layoutManager = layoutManager


        val nameList = ArrayList<String>()
        nameList.add("Aneri")
        nameList.add("Bhoomi")
        nameList.add("Chanda")
        nameList.add("Dipa")
        nameList.add("Emaly")
        nameList.add("Fateh")
        nameList.add("Gontu")

        nameAdapter = NameAdapter(nameList)
        binding.recyclerView.adapter = nameAdapter


        val time = 2000L // it's the delay time for sliding between items in recyclerview

        val linearSnapHelper = LinearSnapHelper()
        linearSnapHelper.attachToRecyclerView(binding.recyclerView)

        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                if (layoutManager.findLastCompletelyVisibleItemPosition() < nameAdapter.getItemCount() - 1) {
                    layoutManager.smoothScrollToPosition(
                        binding.recyclerView,
                        RecyclerView.State(),
                        layoutManager.findLastCompletelyVisibleItemPosition() + 1
                    )
                } else if (layoutManager.findLastCompletelyVisibleItemPosition() == nameAdapter.getItemCount() - 1) {
                    layoutManager.smoothScrollToPosition(
                        binding.recyclerView,
                        RecyclerView.State(),
                        0
                    )
                }
            }
        }, 0, time)


        /*binding.imageArrow.setOnClickListener {
            if (layoutManager.findFirstVisibleItemPosition() > 0) {
                binding.recyclerView.smoothScrollToPosition(layoutManager.findFirstVisibleItemPosition() - 1);
            } else {
                binding.recyclerView.smoothScrollToPosition(0);
            }
        }*/


        binding.btnDeposit.setOnClickListener {

        }

        //position = Int.MAX_VALUE / 2;
        //binding.recyclerview.scrollToPosition(position);

//        val snapHelper=LinearSnapHelper()
//        snapHelper.attachToRecyclerView(binding.recyclerview)
//        binding.recyclerview.smoothScrollBy(5, 0)

//        binding.recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//                if (newState == 1) {
//                    stopAutoScrollBanner();
//                } else if (newState == 0) {
//                    position = layoutManager.findFirstCompletelyVisibleItemPosition();
//                    runAutoScrollBanner();
//                }
//            }
//
//        })
//         autoScroll()
        /*binding.colorWheel.startAnimation(
            AnimationUtils.loadAnimation(
                applicationContext,
                R.anim.rotation
            )
        )*/
        val formatter = SimpleDateFormat("dd.MM.yyyy, HH:mm")
        val then = "20.02.2018, 14:00" //Timer date 2

        val calender: Calendar = Calendar.getInstance()
        calender.set(Calendar.HOUR_OF_DAY, 20)
        calender.set(Calendar.MINUTE, 0)
        calender.set(Calendar.SECOND, 0)


        val now = Date()
        val diff: Long = now.time - calender.time.time

        object : CountDownTimer(diff, 1000) {
            // adjust the milli seconds here
            override fun onTick(millisUntilFinished: Long) {

                val f: NumberFormat = DecimalFormat("00")

                val hour: Long = (millisUntilFinished / 3600000) % 24;

                val min: Long = (millisUntilFinished / 60000) % 60;

                val sec: Long = (millisUntilFinished / 1000) % 60;

                //binding.tvCountdown.text=f.format(hour)+" : "+f.format(min)+" : "+f.format(sec)
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


        /*binding.recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val firstItemVisible: Int = linearLayoutManager.findFirstVisibleItemPosition()
                if (firstItemVisible != 0 && firstItemVisible % nameList.size === 0) {
                    recyclerView.layoutManager!!.scrollToPosition(0)
                }
            }
        })*/
    }

    fun autoScroll() {
        val handler = Handler()
        val runnable: Runnable = object : Runnable {
            override fun run() {
                binding.recyclerView.scrollBy(2, 0)
                handler.postDelayed(this, 0)
            }
        }
        handler.postDelayed(runnable, 0)
    }

    private fun stopAutoScrollBanner() {
        if (timer != null && timerTask != null) {
            timerTask.cancel()
            timer.cancel()
            position = layoutManager.findFirstCompletelyVisibleItemPosition()
        }
    }

    private fun runAutoScrollBanner() {
        timer = Timer()
        timerTask = object : TimerTask() {
            override fun run() {
                if (position == Int.MAX_VALUE) {
                    position = Int.MAX_VALUE / 2
                    binding.recyclerView.scrollToPosition(position)
                    binding.recyclerView.smoothScrollBy(5, 0)
                } else {
                    position++
                    binding.recyclerView.smoothScrollToPosition(position)
                }
            }
        }
        timer.schedule(timerTask, 1000, 1000)
    }

    override fun onResume() {
        super.onResume()
        //runAutoScrollBanner()
    }

    override fun onPause() {
        super.onPause()
//        stopAutoScrollBanner()
    }
}
