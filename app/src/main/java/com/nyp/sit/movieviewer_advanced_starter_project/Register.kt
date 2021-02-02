package com.nyp.sit.movieviewer_advanced_starter_project

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputBinding
import android.widget.Toast
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
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)
        binding.registerLoginET.setText("someID")
        appCoroutineScope = CoroutineScope(Job() + Dispatchers.IO)
        AWSMobileClient.getInstance().initialize(this, object : Callback<UserStateDetails> {

            override fun onResult(result: UserStateDetails?) {
                Log.d("CognitoLab", result?.userState?.name.toString())
            }

            override fun onError(e: Exception?) {
                Log.d("CognitoLab", "There is an error - ${e.toString()}")
            }

        })
    }
    fun displayToast(message: String)
    {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

        fun runRegister(v: View) {
            var loginName = registerLoginET.text.toString()
            var password = registerPasswordET.text.toString()
            var Email = registerEmail.text.toString()
            var AdminNo = registerAdmin.text.toString()
            var PemGrp = registerPemGrp.text.toString()

            appCoroutineScope?.launch(Dispatchers.IO)
            {
                var userPool =
                    CognitoUserPool(v.context, AWSMobileClient.getInstance().configuration)

                var userAttributes = CognitoUserAttributes()

                userAttributes.addAttribute("email", Email)

                userAttributes.addAttribute("custom:AdminNumber", AdminNo)
                userAttributes.addAttribute("custom:PemGrp", PemGrp)


                userPool.signUp(

                    loginName,
                    password,
                    userAttributes,
                    null, object : SignUpHandler {
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

    fun runVerificationCode(v: View)
    {

        appCoroutineScope?.launch{

            AWSMobileClient.getInstance().confirmSignUp(
                binding.registerLoginET.text.toString(),
                binding.verificationET.text.toString(),
                object : Callback<com.amazonaws.mobile.client.results.SignUpResult>{

                    override fun onResult(result: com.amazonaws.mobile.client.results.SignUpResult?) {
                        Log.d("CognitoLab", "Signup result - ${result?.confirmationState}")
                    }

                    override fun onError(e: Exception?) {
                        Log.d("CognitoLab", "Sign up result error - ${e.toString()}")
                    }
                }


            )
            var myIntent = Intent(this@Register, Login::class.java)
            startActivity(myIntent)


        }
    }
    fun runResendVerificationCode(v: View)
    {
        appCoroutineScope?.launch {

            AWSMobileClient.getInstance()
                .resendSignUp(binding.registerLoginET.text.toString(),
                object : Callback<com.amazonaws.mobile.client.results.SignUpResult>{

                    override fun onResult(result: com.amazonaws.mobile.client.results.SignUpResult?) {
                        Log.d("CognitoLab", "Resend Verification success to ${result?.userCodeDeliveryDetails?.destination}")
                    }

                    override fun onError(e: Exception?) {
                        Log.d("CognitoLab", "Resend Verification exception : ${e.toString()}")
                    }
                })

        }
    }





}