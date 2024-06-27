package com.imranmelikov.futureintern_ad_03

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.imranmelikov.futureintern_ad_03.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private var startTime: Long = 0L
    private var timeInMilliseconds: Long = 0L
    private var timeSwapBuff: Long = 0L
    private var updateTime: Long = 0L
    private lateinit var handler: Handler
    private val updateTimerThread: Runnable = object : Runnable {
        @SuppressLint("DefaultLocale")
        override fun run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime
            updateTime = timeSwapBuff + timeInMilliseconds
            val secs = (updateTime / 1000).toInt()
            val mins = secs / 60
            val hrs = mins / 60
            val milliseconds = ((updateTime % 1000) / 10).toInt()
            binding.timeText.text = String.format("%02d:%02d:%02d:%02d", hrs, mins % 60, secs % 60, milliseconds)
            handler.postDelayed(this, 0)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        handler = Handler()

        binding.start.setOnClickListener {
            startTime = SystemClock.uptimeMillis()
            handler.postDelayed(updateTimerThread, 0)
            binding.start.isEnabled = false
            binding.stop.isEnabled = true
            binding.reset.isEnabled = true
        }

        binding.stop.setOnClickListener {
            timeSwapBuff += timeInMilliseconds
            handler.removeCallbacks(updateTimerThread)
            binding.start.isEnabled = true
            binding.stop.isEnabled = false
        }

        binding.reset.setOnClickListener {
            startTime = 0L
            timeInMilliseconds = 0L
            timeSwapBuff = 0L
            updateTime = 0L
            handler.removeCallbacks(updateTimerThread)
            binding.timeText.text = "00:00:00:00"
            binding.start.isEnabled = true
            binding.stop.isEnabled = true
        }
    }
}