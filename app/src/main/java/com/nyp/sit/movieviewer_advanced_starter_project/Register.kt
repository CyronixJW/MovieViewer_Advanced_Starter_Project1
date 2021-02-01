package com.nyp.sit.movieviewer_advanced_starter_project

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputBinding
import com.nyp.sit.movieviewer_advanced_starter_project.
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.UserStateDetails
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler
import com.amazonaws.services.cognitoidentityprovider.model.SignUpResult
import com.nyp.sit.movieviewer_advanced_starter_project.databinding.ActivityRegisterBinding
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class Register : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    var appCoroutineScope: CoroutineScope? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        appCoroutineScope = CoroutineScope(Job() + Dispatchers.IO)
        AWSMobileClient.getInstance().initialize(this, object : Callback<UserStateDetails>{

            override fun onResult(result: UserStateDetails?) {
                Log.d("CognitoLab", result?.userState?.name.toString())
            }

            override fun onError(e: Exception?) {
                Log.d("CognitoLab", "There is an error - ${e.toString()}")
            }

        })
        btn_register.setOnClickListener {


            runRegister()
        }



    }


    fun runRegister()
    {
        var loginName = registerLoginET.text.toString()
        var password = registerPasswordET.text.toString()
        var Email = registerEmail.text.toString()
        var AdminNo = registerAdmin.text.toString()
        var PemGrp = registerPemGrp.text.toString()

        appCoroutineScope?.launch(Dispatchers.IO)
        {
            var userPool = CognitoUserPool(applicationContext, AWSMobileClient.getInstance().configuration)

            var userAttributes = CognitoUserAttributes()

            userAttributes.addAttribute("email", Email)

            userAttributes.addAttribute("custom:AdminNumber", AdminNo)
            userAttributes.addAttribute("custom:PemGrp", PemGrp)


            userPool.signUp(

                loginName,
                password,
                userAttributes,
                null, object: SignUpHandler{
                    override fun onSuccess(user: CognitoUser?, signUpResult: SignUpResult?) {
                        Log.d("CognitoLab", "Sign up success ${signUpResult?.userConfirmed}")
                    }

                    override fun onFailure(exception: Exception?) {
                        Log.d("CognitoLab", "Exception : ${exception?.message}")
                    }


                }

            )
        }


    }




}