package com.example.jumbotest.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.jumbotest.databinding.ActivityHelloBinding

/**
 * Initial activity displaying initial hello screen.
 */
class HelloActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHelloBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelloBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.buttonStart.setOnClickListener {
            startActivity(Intent(this, ProductsActivity::class.java))
        }
    }
}
