package com.example.dreamland

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import android.util.Log

class User : AppCompatActivity() {
    private var startTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user)

        startTime = System.currentTimeMillis()

        val nicknameEditText = findViewById<EditText>(R.id.nicknameEditText)
        val nicknameTextView = findViewById<TextView>(R.id.nicknameTextView)
        val sessionTimeTextView = findViewById<TextView>(R.id.sessionTimeTextView)

        val sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE)
        val savedNickname = sharedPreferences.getString("nickname", "")
        nicknameTextView.text = savedNickname

        // Recupera os tempos de sessão de todos os jogos
        val sessionTimeMathQuiz = sharedPreferences.getLong("sessionTimeMathQuiz", 0)
        val sessionTimeMemoryGame = sharedPreferences.getLong("sessionTimeMemoryGame", 0)
        // Adicione mais linhas para outros jogos

        // Soma os tempos de sessão para obter o tempo total da sessão
        val totalSessionTime = sessionTimeMathQuiz + sessionTimeMemoryGame // Adicione mais tempos de sessão aqui
        sessionTimeTextView.text = "Total session time: $totalSessionTime seconds"

        Log.d("UserActivity", "sessionTimeMathQuiz: $sessionTimeMathQuiz")
        Log.d("UserActivity", "sessionTimeMemoryGame: $sessionTimeMemoryGame")

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            insets
        }
    }

    override fun onPause() {
        super.onPause()

        val sessionTimeMillis = System.currentTimeMillis() - startTime
        val sessionTimeSeconds = sessionTimeMillis / 1000

        val sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putLong("sessionTimeMathQuiz", sessionTimeSeconds)
        editor.apply()
    }

    override fun onStop() {
        super.onStop()

        val sessionTimeMillis = System.currentTimeMillis() - startTime
        val sessionTimeSeconds = sessionTimeMillis / 1000

        val sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putLong("sessionTimeMathQuiz", sessionTimeSeconds)
        editor.apply()
    }

    fun onSaveBtnClick(view: View) {
        val nicknameEditText = findViewById<EditText>(R.id.nicknameEditText)
        val nicknameTextView = findViewById<TextView>(R.id.nicknameTextView)

        val sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE)
        val nickname = nicknameEditText.text.toString()
        val editor = sharedPreferences.edit()
        editor.putString("nickname", nickname)
        editor.apply()

        nicknameTextView.text = nickname
    }
}