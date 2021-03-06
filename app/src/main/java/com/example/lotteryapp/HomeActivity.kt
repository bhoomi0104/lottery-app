package com.example.lotteryapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.app.tutorialapp.retrofitconfig.NoConnectivityException
import com.app.tutorialapp.retrofitconfig.RetrofitFactory
import com.example.lotteryapp.databinding.ActivityHomeBinding
import com.paypal.android.sdk.payments.*
import retrofit2.Call
import retrofit2.Response
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class HomeActivity : AppCompatActivity() {
    private var diff: Long=0
    private lateinit var layoutManager: LinearLayoutManager
    private var position: Int = 0
    private lateinit var binding: ActivityHomeBinding
    private lateinit var nameAdapter: NameAdapter
    private lateinit var timer: Timer
    private lateinit var timerTask: TimerTask
    private val PAYPAL_REQUEST_CODE=7777

    private val TAG="Home"
    private val config = PayPalConfiguration()
        .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
        .clientId(Utility.PAYPAL_CLIENT_ID)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent=Intent(this,PayPalService::class.java)
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config)
        startService(intent)

         binding.btnDeposit.setOnClickListener {
                paypalPaymentMethod()
         }

        setRecyclerView(arrayListOf())

        showProgressBar()

        getNamesCall()


        val nameList = ArrayList<String>()
        nameList.add("Aneri")
        nameList.add("Bhoomi")
        nameList.add("Chanda")
        nameList.add("Dipa")
        nameList.add("Emaly")
        nameList.add("Fateh")
        nameList.add("Gontu")

//        setRecyclerView(nameList)
        val time = 2000L // it's the delay time for sliding between items in recyclerview

        val linearSnapHelper = LinearSnapHelper()
        linearSnapHelper.attachToRecyclerView(binding.recyclerView)

        layoutManager=LinearLayoutManager(applicationContext)
        binding.recyclerView.layoutManager=layoutManager
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
        binding.colorWheel.startAnimation(
            AnimationUtils.loadAnimation(
                applicationContext,
                R.anim.rotation
            )
        )
        val formatter = SimpleDateFormat("dd.MM.yyyy, HH:mm")
        val then = "20.02.2018, 14:00" //Timer date 2

        val calenderForTwenty: Calendar = Calendar.getInstance()
        calenderForTwenty.set(Calendar.HOUR_OF_DAY, 20)
        calenderForTwenty.set(Calendar.MINUTE, 0)
        calenderForTwenty.set(Calendar.SECOND, 0)


        val rightNow = Calendar.getInstance()

        Log.d(TAG, "before "+calenderForTwenty.time.toString() +" ------ "+ rightNow.time.toString())
        if(rightNow.compareTo(calenderForTwenty)<0){
            Log.d(TAG,"1")

        }else if(rightNow.compareTo(calenderForTwenty)>0){
            Log.d(TAG,"2")
            calenderForTwenty.add(Calendar.DAY_OF_YEAR,1)

        }
        diff =calenderForTwenty.timeInMillis-rightNow.timeInMillis

        Log.d(TAG, "after "+calenderForTwenty.time.toString() +" ------ "+ rightNow.time.toString())


        Log.d(TAG,diff.toString())
        object : CountDownTimer(diff, 1000) {
            // adjust the milli seconds here
            override fun onTick(millisUntilFinished: Long) {

                val f: NumberFormat = DecimalFormat("00")

                val hour: Long = (millisUntilFinished / 3600000) % 24;

                val min: Long = (millisUntilFinished / 60000) % 60;

                val sec: Long = (millisUntilFinished / 1000) % 60;

                binding.tvHr.text=hour.toString()
                binding.tvMin.text=min.toString()
                binding.tvSec.text=sec.toString()

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
                fetchWinner()
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

    private fun fetchWinner() {

    }

    private fun getNamesCall() {
            RetrofitFactory.getRetrofitInstance(applicationContext).lotteryUsers().enqueue(object:retrofit2.Callback<List<String>> {
                override fun onFailure(call: Call<List<String>>, t: Throwable) {
                    onRequestFailure(t)
                }

                override fun onResponse(
                    call: Call<List<String>>,
                    response: Response<List<String>>
                ) {
                    if(response.isSuccessful && response.body()!=null){
                        setRecyclerView(response.body()!!)




                    }else{
                        onResponseFailure(response.code())
                    }
                    hideProgressBar()
                }
            })
    }

    private fun setRecyclerView(nameList: List<String>) {
        nameAdapter = NameAdapter(nameList)
        binding.recyclerView.adapter = nameAdapter

    }

    private fun paypalPaymentMethod() {

        val payPalPayment=PayPalPayment(BigDecimal(1),"ILS","deposit 1$ for first time",PayPalPayment.PAYMENT_INTENT_SALE)
        val intent=Intent(this,PaymentActivity::class.java)
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config)
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment)
        startActivityForResult(intent,PAYPAL_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode==PAYPAL_REQUEST_CODE){
            if(resultCode== Activity.RESULT_OK){

                Toast.makeText(applicationContext,"Success",Toast.LENGTH_SHORT).show()
                /*val confirmation: PaymentConfirmation? = data?.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION)
                if(confirmation!=null){
                    try {
                        val paymentDetails=confirmation.toJSONObject().toString(4)
                        startActivity(Intent(this,PayPalPaymentDetails))
                    }
                }*/
            }else if(resultCode== Activity.RESULT_CANCELED){
                Toast.makeText(applicationContext,"Cancel",Toast.LENGTH_SHORT).show()

            }else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
                Toast.makeText(applicationContext,"PayPal error",Toast.LENGTH_SHORT).show()

            }
        }


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

    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this,PayPalService::class.java))
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

    private fun showProgressBar() {
        binding.layoutProgressBar.progressBar.visibility = View.VISIBLE
    }


    private fun hideProgressBar() {
        binding.layoutProgressBar.progressBar.visibility = View.GONE
    }

    private fun onRequestFailure(t: Throwable) {
        if (t is NoConnectivityException) {
            Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(
                applicationContext,
                applicationContext?.resources?.getString(R.string.toast_something_wrong),
                Toast.LENGTH_SHORT
            ).show()

            Log.e(TAG, "On failure:", t)
        }
        hideProgressBar()
    }

    private fun onResponseFailure(code: Int) {
        Toast.makeText(
            applicationContext,
            applicationContext?.resources?.getString(R.string.toast_something_wrong),
            Toast.LENGTH_SHORT
        ).show()

        Log.e(TAG, "Problem in response: ${code}")
    }


}
