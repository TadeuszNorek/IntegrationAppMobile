package com.dawid.glowienkowski.engmobileapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_heads_up_summary.*
import kotlinx.android.synthetic.main.activity_summary.*

class HeadsUpSummaryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_heads_up_summary)

        val sharedPrefs = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val headsUpScore = sharedPrefs.getInt("headsUpScore", 0)
        heads_up_score_text.text = headsUpScore.toString()
        play_again_button.setOnClickListener {
            startHeadsUpPrepareActivity()
        }
        home_button.setOnClickListener {
            backToMainMenu()
        }

    }

    fun startHeadsUpPrepareActivity() {
        val startHeadsUpPrepareActivityIntent = Intent(applicationContext, HeadsUpPrepareActivity::class.java)
        this.finish()
        startActivity(startHeadsUpPrepareActivityIntent)
    }

    fun backToMainMenu() {
        val startMainAppCompatActivityIntent = Intent(applicationContext, MainActivity::class.java)
        this.finish()
        startActivity(startMainAppCompatActivityIntent)
    }
}