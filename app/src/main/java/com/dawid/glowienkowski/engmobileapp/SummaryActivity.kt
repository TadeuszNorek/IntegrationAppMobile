package com.dawid.glowienkowski.engmobileapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_summary.*

class SummaryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)

        val sharedPrefs = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val sumScoreA = sharedPrefs.getInt("sumScoreA", 0)
        val sumScoreB = sharedPrefs.getInt("sumScoreB", 0)
        val rounds = sharedPrefs.getInt("rounds",1)
        val roundCounter = sharedPrefs.getInt("roundCounter",0)
        summary_score_a_text.text = sumScoreA.toString()
        summary_score_b_text.text = sumScoreB.toString()

        if(roundCounter>rounds){
            if(sumScoreA>sumScoreB) {
                winner_text.text = "Wygrywa drużyna A!"
            } else if (sumScoreA<sumScoreB) {
                winner_text.text = "Wygrywa drużyna B!"
            } else winner_text.text = "Remis!"
        }

        next_round_button.setOnClickListener {

            if(roundCounter>rounds) {
                backToMainMenu()
            } else startTabooGameActivity()

        }


    }

    fun startTabooGameActivity() {
        val startTabooGameActivityIntent = Intent(applicationContext, TabooGameActivity::class.java)
        this.finish()
        startActivity(startTabooGameActivityIntent)
    }

    fun backToMainMenu() {
        val startMainAppCompatActivityIntent = Intent(applicationContext, MainActivity::class.java)
        this.finish()
        startActivity(startMainAppCompatActivityIntent)
    }



}