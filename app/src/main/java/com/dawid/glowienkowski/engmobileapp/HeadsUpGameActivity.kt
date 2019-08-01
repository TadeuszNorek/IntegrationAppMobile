package com.dawid.glowienkowski.engmobileapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Base64
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import kotlinx.android.synthetic.main.activity_heads_up_game.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HeadsUpGameActivity : AppCompatActivity(),
    GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener
{
    var gDetector: GestureDetectorCompat? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_heads_up_game)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        this.gDetector = GestureDetectorCompat(this, this)
        gDetector?.setOnDoubleTapListener(this)
        val timer = MyCounter(60000.toLong(), 1000)

        getCardSet()

        timer.start()
    }

    ////////////////////////////////////////////////////////////////////////////
    //Motion handling

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        return true
    }

    override fun onDown(e: MotionEvent?): Boolean {
        return true    }

    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
        this.startHeadsUpIncorrectActivity()
        return true
    }

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        return true    }

    override fun onLongPress(e: MotionEvent?) {
    }

    override fun onDoubleTap(e: MotionEvent?): Boolean {
        return true    }

    override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
        return true    }

    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
        this.startHeadsUpCorrectActivity()
        return true
    }

    override fun onShowPress(e: MotionEvent?) {
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        this.gDetector?.onTouchEvent(event)
        // Be sure to call the superclass implementation
        return super.onTouchEvent(event)
    }
    ////////////////////////////////////////////////////////////////////////////

    fun startHeadsUpCorrectActivity() {
        val startHeadsUpCorrectActivityIntent = Intent(applicationContext, HeadsUpCorrectActivity::class.java)
        this.finish()
        startActivity(startHeadsUpCorrectActivityIntent)
    }

    fun startHeadsUpIncorrectActivity() {
        val startHeadsUpIncorrectActivityIntent = Intent(applicationContext, HeadsUpIncorrectActivity::class.java)
        this.finish()
        startActivity(startHeadsUpIncorrectActivityIntent)
    }
//////////////////////////////////////////////////
    fun getCardSet(){

        val sharedPrefs = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val username = sharedPrefs.getString("user_login", "")
        val pass = sharedPrefs.getString("user_password", "")
        val url = "api/headsupcards/$username"

        val retrofit = Retrofit.Builder()
            .baseUrl("http://156.17.226.135:8080/engwebapp/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiConnection = retrofit.create(APIConnection::class.java)

        val base = "$username:$pass"

        val authHeader = "Basic ${Base64.encodeToString(base.toByteArray(), Base64.NO_WRAP)}"

        apiConnection.getHeadsUpCards(authHeader, url)
            .enqueue(object : Callback<List<HeadsUpCardModel>> {

                override fun onResponse(call: Call<List<HeadsUpCardModel>>, response: Response<List<HeadsUpCardModel>>) {
                    if(response.code() >= 300){
                        Toast.makeText(this@HeadsUpGameActivity,
                            "${response.code()} ${response.errorBody()}",
                            //"Błędny login lub hasło",
                            Toast.LENGTH_LONG).show()
                    } else {
                        val cards = response.body()
                        pickACard(cards)
                    }
                }

                override fun onFailure(call: Call<List<HeadsUpCardModel>>, t: Throwable) {

                    Toast.makeText(this@HeadsUpGameActivity, "Błąd połączenia", Toast.LENGTH_LONG).show()
                }
            })

    }

    fun pickACard(headsUpCardsSet: List<HeadsUpCardModel>?) {

        if (headsUpCardsSet.isNullOrEmpty()){
            Toast.makeText(this@HeadsUpGameActivity, "Skończyły się karty :(", Toast.LENGTH_LONG).show()
        } else {
            val size = headsUpCardsSet?.size
            val random = (0 until size!!).random()
            val card = headsUpCardsSet?.get(random)
            author_text.text = card?.author
            title_text.text = card?.title
        }

    }
//////////////////////////////////////////
    inner class MyCounter(millisInFuture: Long, countDownInterval: Long) : CountDownTimer(millisInFuture, countDownInterval) {

        override fun onFinish() {
            startHeadsUpIncorrectActivity()
        }

        override fun onTick(millisUntilFinished: Long) {
            timer_text.text = (millisUntilFinished/1000).toString()
        }
    }
}