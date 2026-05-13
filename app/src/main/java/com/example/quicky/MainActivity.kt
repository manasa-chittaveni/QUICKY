package com.example.quicky

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // Example ViewModel (we’ll expand later)
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // For now, directly go to LoginActivity
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
