package com.example.dreamland

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {
    private lateinit var nicknameTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        nicknameTextView = findViewById(R.id.nicknameTextView)

        val sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE)
        val savedNickname = sharedPreferences.getString("nickname", "")
        nicknameTextView.text = savedNickname

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val CarRaceBtn = findViewById<ImageButton>(R.id.CarRaceBtn)
        CarRaceBtn.setOnClickListener {
            JogoCarro()
        }

        val MathQuizBtn = findViewById<ImageButton>(R.id.MathQuizBtn)
        MathQuizBtn.setOnClickListener {
            JogoMatematica()
        }

        val TicTacToeBtn = findViewById<ImageButton>(R.id.TicTacToeBtn)
        TicTacToeBtn.setOnClickListener {
            JogoVelha()
        }

        val MemoryGameBtn = findViewById<ImageButton>(R.id.MemoryGameBtn)
        MemoryGameBtn.setOnClickListener {
            JogoMemoria()
        }

        val GoUserBtn = findViewById<ImageButton>(R.id.GoUserBtn)
        GoUserBtn.setOnClickListener {
            GoUser()
        }
    }

    override fun onResume() {
        super.onResume()

        val sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE)
        val savedNickname = sharedPreferences.getString("nickname", "")
        nicknameTextView.text = savedNickname
    }

    private fun JogoCarro(){
        val CarRace = Intent(this, CarRace::class.java)
        startActivity(CarRace)
    }

    private fun JogoMatematica(){
        val MathQuiz = Intent(this, MathQuiz::class.java)
        startActivity(MathQuiz)
    }

    private fun JogoVelha(){
        val TicTacToe = Intent(this, TicTacToe::class.java)
        startActivity(TicTacToe)
    }

    private fun JogoMemoria(){
        val MemoryGame = Intent(this, MemoryGame::class.java)
        startActivity(MemoryGame)
    }

    private fun GoUser(){
        val user = Intent(this, User::class.java)
        startActivity(user)
    }
}