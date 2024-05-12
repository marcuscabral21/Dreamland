package com.example.dreamland

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

private const val TAG = "MemoryGame"

class MemoryGame : AppCompatActivity() {

    private lateinit var buttons: List<ImageButton>
    private lateinit var cards: List<MemoryCard>
    private var indexOfSingleSelectedCard: Int? = null
    private lateinit var textViewTimer: TextView
    private lateinit var textViewTitle: TextView
    private lateinit var timer: CountDownTimer
    private var numPairsFound = 0
    private var millisUntilFinished: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memory_game)

        textViewTimer = findViewById(R.id.textViewTimer)
        textViewTitle = findViewById(R.id.textViewTitle)

        startTimer()

        val images = mutableListOf(R.drawable.ic_diamond, R.drawable.ic_time, R.drawable.ic_plane, R.drawable.ic_smiley)
        images.addAll(images)
        images.shuffle()

        val button1 = findViewById<ImageButton>(R.id.imageButton1)
        val button2 = findViewById<ImageButton>(R.id.imageButton2)
        val button3 = findViewById<ImageButton>(R.id.imageButton3)
        val button4 = findViewById<ImageButton>(R.id.imageButton4)
        val button5 = findViewById<ImageButton>(R.id.imageButton5)
        val button6 = findViewById<ImageButton>(R.id.imageButton6)
        val button7 = findViewById<ImageButton>(R.id.imageButton7)
        val button8 = findViewById<ImageButton>(R.id.imageButton8)

        buttons = listOf(button1, button2, button3, button4, button5, button6, button7, button8)

        cards = buttons.indices.map { index ->
            MemoryCard(images[index])
        }

        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                updateModels(index)
                updateViews()
            }
        }
    }

    private fun updateViews() {
        cards.forEachIndexed { index, card ->
            val button = buttons[index]
            if (card.isMatched) {
                button.alpha = 0.1f
            }
            button.setImageResource(if (card.isFaceUp) card.identifier else R.drawable.ic_code)
        }
    }

    private fun updateModels(position: Int) {
        val card = cards[position]
        if (card.isFaceUp) {
            return
        }
        if (indexOfSingleSelectedCard == null) {
            restoreCards()
            indexOfSingleSelectedCard = position
        } else {
            checkForMatch(indexOfSingleSelectedCard!!, position)
            indexOfSingleSelectedCard = null
        }
        card.isFaceUp = !card.isFaceUp
    }

    private fun restoreCards() {
        for (card in cards) {
            if (!card.isMatched) {
                card.isFaceUp = false
            }
        }
    }

    private fun checkForMatch(position1: Int, position2: Int) {
        if (cards[position1].identifier == cards[position2].identifier) {
            cards[position1].isMatched = true
            cards[position2].isMatched = true
            numPairsFound++
            if (numPairsFound == cards.size / 2) {
                textViewTitle.text = "Congratulations, you completed it!"
                timer.cancel()

                val elapsedTimeInSeconds = (Long.MAX_VALUE - millisUntilFinished) / 1000
                val sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putLong("sessionTimeMemoryGame", elapsedTimeInSeconds) // Use "sessionTimeMemoryGame" como a chave
                editor.apply()
            }
        }
    }

    private fun startTimer() {
        timer = object : CountDownTimer(Long.MAX_VALUE, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                this@MemoryGame.millisUntilFinished = millisUntilFinished
                val elapsedTimeInSeconds = (Long.MAX_VALUE - millisUntilFinished) / 1000
                val minutes = elapsedTimeInSeconds / 60
                val seconds = elapsedTimeInSeconds % 60
                val timeElapsedFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
                textViewTimer.text = "Session time: $timeElapsedFormatted"
            }

            override fun onFinish() {
                // Não será chamado neste caso, pois o contador é infinito
            }
        }

        timer.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }
}