package com.example.numberslidepuzzle

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.numberslidepuzzle.MainActivity
import com.example.numberslidepuzzle.databinding.ActivityHomeBinding
import com.example.numberslidepuzzle.databinding.ActivityMainBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.nilePuzzleBtn.setOnClickListener{
            startActivity(Intent(this@HomeActivity, MainActivity::class.java))
        }
    }
}