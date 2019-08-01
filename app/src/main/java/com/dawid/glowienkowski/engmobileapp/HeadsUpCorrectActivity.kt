package com.dawid.glowienkowski.engmobileapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.concurrent.schedule

class HeadsUpCorrectActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_heads_up_correct)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val timer = Timer("schedule", true)
        timer.schedule(3000.toLong()){
            startHeadsUpGameActivity()
        }

    }

    fun startHeadsUpGameActivity() {
        val sharedPrefs = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        var headsUpScore = sharedPrefs.getInt("headsUpScore", 0)
        headsUpScore++
        var headsUpRoundCounter = sharedPrefs.getInt("headsUpRoundsCounter", 0)
        headsUpRoundCounter++
        sharedPrefs.edit()
            .putInt("headsUpScore", headsUpScore)
            .putInt("headsUpRoundsCounter", headsUpRoundCounter)
            .apply()
        if(headsUpRoundCounter>=5){
            val startHeadsUpGameActivityIntent = Intent(applicationContext, HeadsUpSummaryActivity::class.java)
            this.finish()
            startActivity(startHeadsUpGameActivityIntent)
        } else {
            val startHeadsUpGameActivityIntent = Intent(applicationContext, HeadsUpGameActivity::class.java)
            this.finish()
            startActivity(startHeadsUpGameActivityIntent)
        }
    }

}