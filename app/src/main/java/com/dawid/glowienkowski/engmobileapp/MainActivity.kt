package com.dawid.glowienkowski.engmobileapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPrefs = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        if (sharedPrefs.getString("user_login", "").isBlank()){
            val startLoginActivityIntent = Intent(applicationContext, LoginActivity::class.java)

            this.finish()

            startActivity(startLoginActivityIntent)
        } else {
            setContentView(R.layout.activity_main)
            val username = sharedPrefs.getString("user_login","")
            taboo_text.text = "Witaj, $username"

            taboo_button.setOnClickListener {

                startTabooSettingsActivity()

            }
            headsup_button.setOnClickListener {

                startHeadsUpPrepareActivity()
            }
        }


    }

    fun startTabooSettingsActivity() {

        val startTabooSettingsActivityIntent = Intent(applicationContext, TabooSettingsActivity::class.java)
        this.finish()
        startActivity(startTabooSettingsActivityIntent)
    }

    fun startHeadsUpPrepareActivity() {
        val startHeadsUpPrepareActivityIntent = Intent(applicationContext, HeadsUpPrepareActivity::class.java)
        this.finish()
        startActivity(startHeadsUpPrepareActivityIntent)
    }
}
