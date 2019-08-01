package com.dawid.glowienkowski.engmobileapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_taboo_settings.*

class TabooSettingsActivity : AppCompatActivity() {

    var rounds: Int = 1
    var roundTime: Int = 30
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_taboo_settings)


        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                rounds = progress
                rounds_text.text = "Liczba rund: $progress"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                //
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
               //
            }
        })
        taboo_start_button.setOnClickListener {
            roundTime = Integer.parseInt(round_time_input.text.toString())
            val sharedPrefs = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
            sharedPrefs.edit()
                .putInt("rounds", rounds)
                .putInt("roundTime", roundTime)
                .putInt("sumScoreA", 0)
                .putInt("sumScoreB", 0)
                .putInt("roundCounter", 1)
                .putBoolean("turn", true)
                .apply()
            startTabooGameActivity()

        }
    }

    fun startTabooGameActivity() {
        val startTabooGameActivityIntent = Intent(applicationContext, TabooGameActivity::class.java)
        this.finish()
        startActivity(startTabooGameActivityIntent)
    }
}