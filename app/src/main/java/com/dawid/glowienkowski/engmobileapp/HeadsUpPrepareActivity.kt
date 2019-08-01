package com.dawid.glowienkowski.engmobileapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_heads_up_prepare.*

class HeadsUpPrepareActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_heads_up_prepare)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val sharedPrefs = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        sharedPrefs.edit()
            .putInt("headsUpScore", 0)
            .putInt("headsUpRoundsCounter", 0)
            .apply()

        val timer = MyCounter(5000, 1000)
        timer.start()
    }

    fun startHeadsUpGameActivity() {
        val startHeadsUpGameActivityIntent = Intent(applicationContext, HeadsUpGameActivity::class.java)
        this.finish()
        startActivity(startHeadsUpGameActivityIntent)
    }


    inner class MyCounter(millisInFuture: Long, countDownInterval: Long) : CountDownTimer(millisInFuture, countDownInterval) {
        override fun onTick(millisUntilFinished: Long) {
            prepare_time_text.text = (millisUntilFinished/1000).toString()
        }

        override fun onFinish() {
                startHeadsUpGameActivity()
            }



    }
}