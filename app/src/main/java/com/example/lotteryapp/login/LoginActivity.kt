package com.app.tutorialapp.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.tutorialapp.model.LoginReq
import com.app.tutorialapp.retrofitconfig.NoConnectivityException
import com.app.tutorialapp.retrofitconfig.RetrofitFactoryWoToken
import com.example.lotteryapp.HomeActivity
import com.example.lotteryapp.R
import com.example.lotteryapp.Utility
import com.example.lotteryapp.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Response

class LoginActivity : AppCompatActivity() {


    private lateinit var binding: ActivityLoginBinding
    private val TAG = "Login"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)
        binding.btnLogin.setOnClickListener {

            if (isValidInput()) {
                showProgressBar()
                loginCall()

            }
        }
        binding.tvSignup.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignupActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        //iMobile.doOnTextChanged { text, start, before, count -> iMobile.setError(null)  }


        /* binding.tvForgotpass.setOnClickListener {
             val intent = Intent(this, ForgotPassActivity::class.java)
             startActivity(intent)
         }
 */
    }

    private fun loginCall() {

        val loginReq = LoginReq()
        loginReq.username = binding.inputUsername.text?.trim().toString()
        loginReq.password = binding.inputPassword.text?.trim().toString()



        RetrofitFactoryWoToken.getRetrofitInstance().login(loginReq)
            .enqueue(object : retrofit2.Callback<Map<String, String>> {
                override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                    onRequestFailure(t)
                }

                override fun onResponse(
                    call: Call<Map<String, String>>,
                    response: Response<Map<String, String>>
                ) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {

                            val sharedPreferences1 =
                                getSharedPreferences(Utility.PREF_TOKEN, Context.MODE_PRIVATE)
                            sharedPreferences1.edit()
                                .putString(
                                    Utility.PREF_TOKEN,
                                    response.body()!!.get("access token")
                                )
                                .apply()


                            val sharedPreferences =
                                getSharedPreferences(
                                    Utility.PREF_KEY_MYPREFRENCES,
                                    Context.MODE_PRIVATE
                                );

                            sharedPreferences.edit().putBoolean(Utility.PREF_LOGGEDIN, true).apply()

                            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        }
                    } else if(response.code()==401) {

                        binding.inputlayoutUsername.error = "Invalid Credentials"
                        binding.inputUsername.requestFocus()
                    }else{
                        onResponseFailure(response.code())
                    }
                    hideProgressBar()
                }
            })


    }

    private fun isValidInput(): Boolean {
        val username = binding.inputUsername.text?.trim().toString()
        val pass = binding.inputPassword.text?.trim().toString()

        if (username.isEmpty()) {
            binding.inputlayoutUsername.error = "Empty input not allowed"
            binding.inputUsername.requestFocus()
            return false
        }
        if (pass.isEmpty()) {
            binding.inputlayoutPassword.error = "Empty input not allowed"
            binding.inputPassword.requestFocus()
            return false
        }


        return true
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