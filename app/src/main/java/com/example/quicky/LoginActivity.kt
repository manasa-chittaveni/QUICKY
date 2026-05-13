package com.example.quicky

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.quicky.ui.theme.QuickyTheme
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class LoginActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private var storedVerificationId: String? = null
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        setContent {
            QuickyTheme {
                LoginScreen(
                    onSendOtp = { mobile -> sendOtp(mobile) },
                    onVerifyOtp = { otp -> verifyOtp(otp) }
                )
            }
        }
    }

    private fun sendOtp(mobile: String) {
        if (mobile.isEmpty() || mobile.length < 10) {
            Toast.makeText(this, "Enter valid mobile number", Toast.LENGTH_SHORT).show()
            return
        }

        // ✅ Map your test number explicitly
        val phoneNumber = if (mobile == "8919420131") {
            "+918919420131" // Test number added in Firebase Console
        } else {
            "+91$mobile"    // Real numbers (requires billing)
        }

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun verifyOtp(otp: String) {
        if (storedVerificationId != null && otp.isNotEmpty()) {
            val credential = PhoneAuthProvider.getCredential(storedVerificationId!!, otp)
            signInWithPhoneAuthCredential(credential)
        } else {
            Toast.makeText(this, "Enter OTP", Toast.LENGTH_SHORT).show()
        }
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Toast.makeText(this@LoginActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            storedVerificationId = verificationId
            resendToken = token
            Toast.makeText(this@LoginActivity, "OTP sent successfully", Toast.LENGTH_SHORT).show()
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                    startActivity(android.content.Intent(this, HomeActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Verification Failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
}

@Composable
fun LoginScreen(
    onSendOtp: (String) -> Unit,
    onVerifyOtp: (String) -> Unit
) {
    var mobile by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = mobile,
            onValueChange = { mobile = it },
            label = { Text("Mobile Number") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = otp,
            onValueChange = { otp = it },
            label = { Text("OTP") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { onSendOtp(mobile) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Send OTP")
        }

        Button(
            onClick = { onVerifyOtp(otp) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Verify OTP")
        }
    }
}
