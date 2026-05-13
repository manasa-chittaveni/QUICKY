package com.example.quicky

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

sealed class LoginState {
    object Success : LoginState()
    data class Error(val message: String) : LoginState()
}

class LoginViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var verificationId: String? = null

    private val _loginState = MutableLiveData<LoginState>()
    val loginState: LiveData<LoginState> get() = _loginState

    fun sendOtp(activity: Activity, mobile: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+91$mobile")
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    auth.signInWithCredential(credential).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            _loginState.value = LoginState.Success
                        } else {
                            _loginState.value = LoginState.Error("Auto verification failed")
                        }
                    }
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    _loginState.value = LoginState.Error(e.message ?: "Verification failed")
                }

                override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                    this@LoginViewModel.verificationId = verificationId
                }
            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun verifyOtp(otp: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId ?: "", otp)
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _loginState.value = LoginState.Success
            } else {
                _loginState.value = LoginState.Error("Invalid OTP")
            }
        }
    }
}
