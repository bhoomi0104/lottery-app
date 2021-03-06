package com.app.tutorialapp.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.tutorialapp.model.SignupReq
import com.app.tutorialapp.retrofitconfig.NoConnectivityException
import com.app.tutorialapp.retrofitconfig.RetrofitFactoryWoToken
import com.example.lotteryapp.HomeActivity
import com.example.lotteryapp.R
import com.example.lotteryapp.Utility
import com.example.lotteryapp.databinding.ActivitySignupBinding
import retrofit2.Call
import retrofit2.Response

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private val TAG = "signup"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupBinding.inflate(layoutInflater)

        setContentView(binding.root)
        binding.btnSignup.setOnClickListener {

            if (isValidInput()) {
                showProgressBar()
                signUpCall()
            }
        }


    }

    private fun signUpCall() {

        val singupReq = SignupReq()
        singupReq.username = binding.inputUsername.text?.trim().toString()
        singupReq.password = binding.inputPassword.text?.trim().toString()
        singupReq.name = binding.inputName.text?.trim().toString()

        RetrofitFactoryWoToken.getRetrofitInstance().signup(singupReq)
            .enqueue(object : retrofit2.Callback<Map<String, String>> {
                override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {

                    onRequestFailure(t)
                }


                override fun onResponse(
                    call: Call<Map<String, String>>,
                    response: Response<Map<String, String>>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val sharedPreferences1 =
                            getSharedPreferences(Utility.PREF_KEY_TOKENS, Context.MODE_PRIVATE)
                        sharedPreferences1.edit()
                            .putString(Utility.PREF_TOKEN, response.body()!!.get("accesstoken"))
                            .apply()


                        val sharedPreferences =
                            getSharedPreferences(
                                Utility.PREF_KEY_MYPREFRENCES,
                                Context.MODE_PRIVATE
                            )

                        sharedPreferences.edit().putBoolean(Utility.PREF_LOGGEDIN, true).apply()


                        val intent = Intent(this@SignupActivity, HomeActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    } else {
                        onResponseFailure(response.code())
                    }
                    hideProgressBar()
                }
            })


    }

    private fun isValidInput(): Boolean {
        val username = binding.inputUsername.text.toString().trim()
        val pass = binding.inputPassword.text.toString().trim()
        val name = binding.inputName.text.toString().trim()
        val confirmPass = binding.inputConfirmPass.text.toString().trim()

        if (username.isEmpty()) {
            binding.inputlayoutUsername.error = "Empty input not allowed"
            binding.inputUsername.requestFocus()
            return false
        }
        if (name.isEmpty()) {
            binding.inputlayoutName.error = "Empty input not allowed"
            binding.inputName.requestFocus()
            return false
        }
        if (pass.isEmpty()) {
            binding.inputlayoutPassword.error = "Empty input not allowed"
            binding.inputPassword.requestFocus()
            return false
        }

        if (confirmPass.isEmpty()) {
            binding.inputlayoutConfirmPass.error = "Empty input not allowed"
            binding.inputConfirmPass.requestFocus()
            return false
        }
        if(!pass.equals(confirmPass)){
            binding.inputlayoutConfirmPass.error = "Password doesn't match"
            binding.inputConfirmPass.requestFocus()
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