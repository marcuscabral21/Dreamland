//Código com o tempo de sessão implementado
package com.example.dreamland;

import android.graphics.Color
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.Button
import android.widget.Chronometer
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.text.Html

class TicTacToe : AppCompatActivity(), View.OnClickListener {
    var playerOneActive = false
    private lateinit var playerOneScore: TextView
    private lateinit var playerTwoScore: TextView
    private lateinit var playerStatus: TextView
    private lateinit var timer: Chronometer
    private val buttons = arrayOfNulls<Button>(9)
    private lateinit var reset: Button
    private lateinit var playagain: Button
    var gameState = intArrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2)
    var winningPositions = arrayOf(
        intArrayOf(0, 1, 2), intArrayOf(3, 4, 5), intArrayOf(6, 7, 8), intArrayOf(0, 3, 6),
        intArrayOf(1, 4, 7), intArrayOf(2, 5, 8), intArrayOf(0, 4, 8), intArrayOf(2, 4, 6)
    )
    var rounds = 0
    private var playerOneScoreCount = 0
    private var playerTwoScoreCount = 0
    private var startTimeMillis: Long = 0
    private var endTimeMillis: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tic_tac_toe)

        playerOneScore = findViewById(R.id.score_Player1)
        playerTwoScore = findViewById(R.id.score_Player2)
        playerStatus = findViewById(R.id.textStatus)
        timer = findViewById(R.id.timer)
        reset = findViewById(R.id.btn_reset)
        playagain = findViewById(R.id.btn_play_again)


        playerOneScore.setTextColor(Color.parseColor("#c0ab5b"))
        playerTwoScore.setTextColor(Color.parseColor("#c0ab5b"))
        playerStatus.setTextColor(Color.parseColor("#c0ab5b"))
        timer.setTextColor(Color.parseColor("#c0ab5b"))

        playerOneScore.text = Html.fromHtml("<font color='#000000'>0</font>")
        playerTwoScore.text = Html.fromHtml("<font color='#000000'>0</font>")


        for (i in 0..8) {
            buttons[i] = findViewById(resources.getIdentifier("btn$i", "id", packageName))
            buttons[i]?.setOnClickListener(this)
        }

        reset.setOnClickListener {
            playAgain()
            playerOneScoreCount = 0
            playerTwoScoreCount = 0
            updatePlayerScore()
        }

        playagain.setOnClickListener { playAgain() }

        playerOneScoreCount = 0
        playerTwoScoreCount = 0
        playerOneActive = true
        rounds = 0
        startTimeMillis = SystemClock.elapsedRealtime()
        timer.start()
    }

    override fun onClick(view: View) {
        if ((view as Button).text.toString() != "") {
            return
        } else if (checkWinner()) {
            return
        }

        val buttonID = view.resources.getResourceEntryName(view.id)
        val gameStatePointer = buttonID.substring(buttonID.length - 1, buttonID.length).toInt()

        if (playerOneActive) {
            view.text = "X"
            view.setTextColor(Color.parseColor("#ffc34a"))
            gameState[gameStatePointer] = 0
        } else {
            view.text = "O"
            view.setTextColor(Color.parseColor("#70fc3a"))
            gameState[gameStatePointer] = 1
        }

        rounds++

        if (checkWinner()) {
            if (playerOneActive) {
                playerOneScoreCount++
                updatePlayerScore()
                playerStatus.text = "Player-1 has won"
            } else {
                playerTwoScoreCount++
                updatePlayerScore()
                playerStatus.text = "Player-2 has won"
            }
            timer.stop()
            endTimeMillis = SystemClock.elapsedRealtime()
            val elapsedTimeSeconds = (endTimeMillis - startTimeMillis) / 1000
            playerStatus.append("\nSession time: $elapsedTimeSeconds seconds")
        } else if (rounds == 9) {
            playerStatus.text = "No Winner"
            timer.stop()
            endTimeMillis = SystemClock.elapsedRealtime()
            val elapsedTimeSeconds = (endTimeMillis - startTimeMillis) / 1000
            playerStatus.append("\nSession time: $elapsedTimeSeconds seconds")
        } else {
            playerOneActive = !playerOneActive
        }
    }

    private fun checkWinner(): Boolean {
        var winnerResults = false

        for (winningPosition in winningPositions) {
            if (gameState[winningPosition[0]] == gameState[winningPosition[1]] && gameState[winningPosition[1]] == gameState[winningPosition[2]] && gameState[winningPosition[0]] != 2) {
                winnerResults = true
            }
        }

        return winnerResults
    }

    private fun playAgain() {
        rounds = 0
        playerOneActive = true

        for (i in buttons.indices) {
            gameState[i] = 2
            buttons[i]?.text = ""
        }

        playerStatus.text = "Status"
        timer.base = SystemClock.elapsedRealtime()
        timer.start()
        startTimeMillis = SystemClock.elapsedRealtime()
    }

    private fun updatePlayerScore() {
        playerOneScore.text = playerOneScoreCount.toString()
        playerTwoScore.text = playerTwoScoreCount.toString()
    }
}


/*package com.example.dreamland;

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TicTacToe : AppCompatActivity(), View.OnClickListener {
    var playerOneActive = false
    private lateinit var playerOneScore: TextView
    private lateinit var playerTwoScore: TextView
    private lateinit var playerStatus: TextView
    private val buttons = arrayOfNulls<Button>(9)
    private lateinit var reset: Button
    private lateinit var playagain: Button
    var gameState = intArrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2)
    var winningPositions = arrayOf(
        intArrayOf(0, 1, 2), intArrayOf(3, 4, 5), intArrayOf(6, 7, 8), intArrayOf(0, 3, 6),
        intArrayOf(1, 4, 7), intArrayOf(2, 5, 8), intArrayOf(0, 4, 8), intArrayOf(2, 4, 6)
    )
    var rounds = 0
    private var playerOneScoreCount = 0
    private var playerTwoScoreCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tic_tac_toe)

        playerOneScore = findViewById(R.id.score_Player1)
        playerTwoScore = findViewById(R.id.score_Player2)
        playerStatus = findViewById(R.id.textStatus)
        reset = findViewById(R.id.btn_reset)
        playagain = findViewById(R.id.btn_play_again)

        for (i in 0..8) {
            buttons[i] = findViewById(resources.getIdentifier("btn$i", "id", packageName))
            buttons[i]?.setOnClickListener(this)
        }

        reset.setOnClickListener {
            playAgain()
            playerOneScoreCount = 0
            playerTwoScoreCount = 0
            updatePlayerScore()
        }

        playagain.setOnClickListener { playAgain() }

        playerOneScoreCount = 0
        playerTwoScoreCount = 0
        playerOneActive = true
        rounds = 0
    }

    override fun onClick(view: View) {
        if ((view as Button).text.toString() != "") {
            return
        } else if (checkWinner()) {
            return
        }

        val buttonID = view.resources.getResourceEntryName(view.id)
        val gameStatePointer = buttonID.substring(buttonID.length - 1, buttonID.length).toInt()

        if (playerOneActive) {
            view.text = "X"
            view.setTextColor(Color.parseColor("#ffc34a"))
            gameState[gameStatePointer] = 0
        } else {
            view.text = "O"
            view.setTextColor(Color.parseColor("#70fc3a"))
            gameState[gameStatePointer] = 1
        }

        rounds++

        if (checkWinner()) {
            if (playerOneActive) {
                playerOneScoreCount++
                updatePlayerScore()
                playerStatus.text = "Player-1 has won"
            } else {
                playerTwoScoreCount++
                updatePlayerScore()
                playerStatus.text = "Player-2 has won"
            }
        } else if (rounds == 9) {
            playerStatus.text = "No Winner"
        } else {
            playerOneActive = !playerOneActive
        }
    }

    private fun checkWinner(): Boolean {
        var winnerResults = false

        for (winningPosition in winningPositions) {
            if (gameState[winningPosition[0]] == gameState[winningPosition[1]] && gameState[winningPosition[1]] == gameState[winningPosition[2]] && gameState[winningPosition[0]] != 2) {
                winnerResults = true
            }
        }

        return winnerResults
    }

    private fun playAgain() {
        rounds = 0
        playerOneActive = true

        for (i in buttons.indices) {
            gameState[i] = 2
            buttons[i]?.text = ""
        }

        playerStatus.text = "Status"
    }

    private fun updatePlayerScore() {
        playerOneScore.text = playerOneScoreCount.toString()
        playerTwoScore.text = playerTwoScoreCount.toString()
    }
}*/