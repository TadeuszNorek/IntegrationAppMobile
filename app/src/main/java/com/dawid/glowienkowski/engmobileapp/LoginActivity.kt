package com.dawid.glowienkowski.engmobileapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Pobranie instancji SharedPreferences o nazwie "sharedPrefs"
        val sharedPrefs = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)

        // Sprawdzenie, czy w pobranej instancji znajduje się zapisany adres e-mail
        // Jeżeli tak, to użytkownik jest już 'zalogowany" i uruchamiana jest aktywność MainActivity
        // Jeżeli nie, należy wyświetlić ekran logowania
        if (sharedPrefs.getString("user_login", "").isNotBlank()) {
            startMainActivity()
        } else {

            // Załadowanie widoku logowania
            setContentView(R.layout.activity_login)

            // Przypisanie guzika logowania
            log_in_button.setOnClickListener {

                // Sprawdzenie, czy pola są puste
                // Oraz czy zawierają poprawne dane
                if (areRequiredFieldNotEmpty()) {
                    loginToApi()
                }
            }
        }
    }

    fun areRequiredFieldNotEmpty(): Boolean {

        when {
            login_input.text.isNullOrBlank() -> {
                login_input.error = "Login nie może być pusty!"
                return false
            }

            password_input.text.isNullOrBlank() -> {
                password_input.error = "Hasło nie może być puste!"
                return false
            }

            else -> return true
        }
    }

    // Metoda uruchamiająca MainActivity
    // Odpowiada również za zakończenie LoginActivity
    fun startMainActivity() {
        val startMainActivityIntent = Intent(applicationContext, MainActivity::class.java)

        this.finish()

        startActivity(startMainActivityIntent)
    }

    // Metoda zapisująca podany string jako login w SharedPreferences
    fun saveUserLogin(login: String) {

        // Pobranie instancji SharedPreferences
        val sharedPrefs = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)

        // Umieszczenie zmiennej typu string pod kluczem "user_login"
        sharedPrefs.edit().putString("user_login", login).apply()

    }

    // Metoda zapisująca podany string jako hasło w SharedPreferences
    fun saveUserPassword(pass: String) {

        // Pobranie instancji SharedPreferences
        val sharedPrefs = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)

        // Umieszczenie zmiennej typu string pod kluczem "user_password"
        sharedPrefs.edit().putString("user_password", pass).apply()

    }

    fun loginToApi() {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://156.17.226.135:8080/engwebapp/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiConnection = retrofit.create(APIConnection::class.java)

        val base = "${login_input.text.toString()}:${password_input.text.toString()}"

        val authHeader = "Basic ${Base64.encodeToString(base.toByteArray(), Base64.NO_WRAP)}"

        apiConnection.loginUser(authHeader).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if(response.code() >= 300){
                    Toast.makeText(this@LoginActivity,
                        "${response.code()} ${response.errorBody()}",
                        //"Błędny login lub hasło",
                        Toast.LENGTH_LONG).show()
                } else {
                    saveUserLogin(login_input.text.toString())
                    saveUserPassword(password_input.text.toString())
                    startMainActivity()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {

                Toast.makeText(this@LoginActivity, "Błąd połączenia", Toast.LENGTH_LONG).show()
            }
        }

        )

    }
}