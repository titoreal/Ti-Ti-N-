package com.example.tttapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tttapp.databinding.ActivitySplashBinding
import com.example.tttapp.MainActivity


class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ocultar la barra de acción si está presente
        supportActionBar?.hide()

        // Animar el logo
        binding.ivLogo.alpha = 0f
        binding.ivLogo.animate().setDuration(1500).alpha(1f).withEndAction {
            // Animar el texto
            binding.tvAppName.animate().setDuration(1000).alpha(1f).withEndAction {
                // Iniciar la actividad principal
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            }
        }
    }
}
