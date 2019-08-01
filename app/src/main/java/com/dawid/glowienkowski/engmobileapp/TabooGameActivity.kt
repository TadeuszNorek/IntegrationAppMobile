package com.dawid.glowienkowski.engmobileapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Base64
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_taboo_game.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TabooGameActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_taboo_game)
        val sharedPrefs = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val rounds = sharedPrefs.getInt("rounds", 2)
        val roundTime = sharedPrefs.getInt("roundTime", 30)
        var scoreA: Int = 0
        var scoreB: Int = 0
        var sumScoreA = sharedPrefs.getInt("sumScoreA", 0)
        var sumScoreB = sharedPrefs.getInt("sumScoreB", 0)
        var turn = sharedPrefs.getBoolean("turn", true)
        getCardSet()
        updateView()
        val timer = MyCounter(roundTime*1000.toLong(), 1000)
        good_button.setOnClickListener{
            if(turn){
                sumScoreA++
                scoreA++
                team_score_text.text = sumScoreA.toString()
                round_score_text.text = scoreA.toString()
                sharedPrefs.edit().putInt("sumScoreA", sumScoreA).apply()
            } else {
                sumScoreB++
                scoreB++
                team_score_text.text = sumScoreB.toString()
                round_score_text.text = scoreB.toString()
                sharedPrefs.edit().putInt("sumScoreB", sumScoreB).apply()
            }
            getCardSet()
        }
        bad_button.setOnClickListener {
            if(turn){
                sumScoreA--
                scoreA--
                team_score_text.text = sumScoreA.toString()
                round_score_text.text = scoreA.toString()
                sharedPrefs.edit().putInt("sumScoreA", sumScoreA).apply()
            } else {
                sumScoreB--
                scoreB--
                team_score_text.text = sumScoreB.toString()
                round_score_text.text = scoreB.toString()
                sharedPrefs.edit().putInt("sumScoreB", sumScoreB).apply()
            }
            getCardSet()
        }
        drop_button.setOnClickListener {

            getCardSet()
        }

        timer.start()
    }

    fun getCardSet(){

        val sharedPrefs = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val username = sharedPrefs.getString("user_login", "")
        val pass = sharedPrefs.getString("user_password", "")
        val url = "api/taboocards/$username"

        val retrofit = Retrofit.Builder()
            .baseUrl("http://156.17.226.135:8080/engwebapp/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiConnection = retrofit.create(APIConnection::class.java)

        val base = "$username:$pass"

        val authHeader = "Basic ${Base64.encodeToString(base.toByteArray(), Base64.NO_WRAP)}"

        apiConnection.getTabooCards(authHeader, url)
            .enqueue(object : Callback<List<TabooCardModel>> {

                override fun onResponse(call: Call<List<TabooCardModel>>, response: Response<List<TabooCardModel>>) {
                    if(response.code() >= 300){
                        Toast.makeText(this@TabooGameActivity,
                            "${response.code()} ${response.errorBody()}",
                            //"Błędny login lub hasło",
                            Toast.LENGTH_LONG).show()
                    } else {
                        val cards = response.body()
                        pickACard(cards)
                    }
                }

                override fun onFailure(call: Call<List<TabooCardModel>>, t: Throwable) {

                    Toast.makeText(this@TabooGameActivity, "Błąd połączenia", Toast.LENGTH_LONG).show()
                }
            })

    }

    fun pickACard(tabooCardsSet: List<TabooCardModel>?) {

        if (tabooCardsSet.isNullOrEmpty()){
            Toast.makeText(this@TabooGameActivity, "Skończyły się karty :(", Toast.LENGTH_LONG).show()
        } else {
            val size = tabooCardsSet?.size
            val random = (0 until size!!).random()
            val card = tabooCardsSet?.get(random)
            keyword_text.text = card?.keyword
            taboo_word_1_text.text = card?.tabooWord1
            taboo_word_2_text.text = card?.tabooWord2
            taboo_word_3_text.text = card?.tabooWord3
            taboo_word_4_text.text = card?.tabooWord4
            taboo_word_5_text.text = card?.tabooWord5
        }

    }

    fun updateView() {

        val sharedPrefs = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val turn = sharedPrefs.getBoolean("turn", true)
        val roundCounter = sharedPrefs.getInt("roundCounter", 1)
        round_text.text = roundCounter.toString()

        if (turn){
            team_text.text = "Drużyna A"
            val sumScoreA = sharedPrefs.getInt("sumScoreA", 0)
            team_score_text.text = sumScoreA.toString()
        } else {
            team_text.text = "Drużyna B"
            val sumScoreB = sharedPrefs.getInt("sumScoreB", 0)
            team_score_text.text = sumScoreB.toString()
        }

    }

    fun  startSummaryActivity(){

        val startSummaryActivityIntent = Intent(applicationContext, SummaryActivity::class.java)

        this.finish()
        startActivity(startSummaryActivityIntent)
    }

    inner class MyCounter(millisInFuture: Long, countDownInterval: Long) : CountDownTimer(millisInFuture, countDownInterval) {

        override fun onFinish() {
            val sharedPrefs = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
            val turn = sharedPrefs.getBoolean("turn", true)
            var roundCounter = sharedPrefs.getInt("roundCounter", 0)
            sharedPrefs.edit().putBoolean("turn", !turn).apply()
            if(turn==false) {roundCounter++
            sharedPrefs.edit()
                .putInt("roundCounter", roundCounter)
                .apply()}
            startSummaryActivity()
            if(roundCounter>sharedPrefs.getInt("rounds",1)){

            }
        }

        override fun onTick(millisUntilFinished: Long) {
            time_left_text.text = (millisUntilFinished/1000).toString()
        }
    }
}