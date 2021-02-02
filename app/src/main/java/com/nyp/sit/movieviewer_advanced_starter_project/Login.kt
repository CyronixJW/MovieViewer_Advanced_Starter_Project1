package com.nyp.sit.movieviewer_advanced_starter_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.UserStateDetails
import com.amazonaws.mobile.client.results.SignInResult
import com.amazonaws.mobile.client.results.SignInState
import com.nyp.sit.movieviewer_advanced_starter_project.databinding.ActivityItemDetailBinding
import com.nyp.sit.movieviewer_advanced_starter_project.databinding.ActivityLoginBinding
import com.nyp.sit.movieviewer_advanced_starter_project.databinding.ActivityRegisterBinding
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    var appCoroutineScope: CoroutineScope? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        appCoroutineScope = CoroutineScope(Job() + Dispatchers.IO)
        AWSMobileClient.getInstance().initialize(this, object : Callback<UserStateDetails>{

            override fun onResult(result: UserStateDetails?) {
                Log.d("CognitoLab", result?.userState?.name.toString())
                var i = Intent(this@Login, ViewListOfMoviesActivity::class.java)
                startActivity(i)


            }

            override fun onError(e: Exception?) {
                Log.d("CognitoLab", "There is an error - ${e.toString()}")
            }

        })
    }
    fun Register(v:View)
    {
        var myIntent = Intent(this, Register::class.java)
        startActivity(myIntent)

    }
    fun runLogin(v: View) {
        //TODO 7 : Make use of AWSMobileClient to SignIn.

        appCoroutineScope?.launch {
            AWSMobileClient.getInstance().signIn(

                    loginID.text.toString(),
                    loginPassword.text.toString(),
                    null,
                    object : Callback<SignInResult> {
                        override fun onResult(result: SignInResult?) {
                            Log.d("CognitoLab", "Sign in result : ${result.toString()}")
                            if(result?.signInState == SignInState.DONE){

                                var i = Intent(v.context, ViewListOfMoviesActivity::class.java)
                                startActivity(i)
                            }
                        }

                        override fun onError(e: Exception?) {
                            Log.d("CognitoLab", "Sign in error : ${e.toString()}")
                        }
                    }
            )

        }
    }
}