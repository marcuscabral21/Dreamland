package com.example.dreamland

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.Button
import android.widget.Chronometer
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class CarRace : AppCompatActivity(), GameTask {
    private lateinit var rootLayout: RelativeLayout
    private lateinit var startBtn: Button
    private lateinit var mGameView: GameView
    private lateinit var score: TextView
    private lateinit var sessionTimer: Chronometer
    private lateinit var menuButton: ImageButton
    private var isGameRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_car_race)

        startBtn = findViewById(R.id.startBtn)
        rootLayout = findViewById(R.id.rootLayout)
        score = findViewById(R.id.score)
        sessionTimer = findViewById(R.id.session_timer)
        menuButton = findViewById<ImageButton>(R.id.menuButton) // Adicione esta linha para inicializar o botão do menu

        mGameView = GameView(this, this)

        startBtn.setOnClickListener {
            startGame()
        }

        // Adicione este bloco de código para adicionar um OnClickListener ao botão do menu
        menuButton.setOnClickListener {
            // Aqui você pode adicionar a lógica para voltar ao menu
            // Por exemplo, você pode iniciar uma nova Activity que é o seu menu
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Configurando o botão infoButton
        val infoButton = findViewById<ImageButton>(R.id.infoButton)
        infoButton.setOnClickListener {
            showInstructionsDialog()
        }
    }

    private fun showInstructionsDialog() {
        val dialogMessage = "\nHow to play Car Race:\n\nTap middle, right or left.\n\nHold on for as long as possible."
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Instructions")
        builder.setMessage(dialogMessage)
        builder.setPositiveButton("OK") { dialog, which ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun startGame() {
        if (!isGameRunning) {
            isGameRunning = true
            mGameView.setBackgroundResource(R.drawable.road)
            rootLayout.addView(mGameView)
            startBtn.visibility = View.GONE
            sessionTimer.visibility = View.VISIBLE
            sessionTimer.base = SystemClock.elapsedRealtime()
            sessionTimer.start()
            score.visibility = View.GONE
        }
    }

    override fun closeGame(mScore: Int) {
        isGameRunning = false
        sessionTimer.stop()
        val elapsedTime = SystemClock.elapsedRealtime() - sessionTimer.base
        val seconds = (elapsedTime / 1000).toInt()
        val minutes = seconds / 60
        val remainderSeconds = seconds % 60
        val timeFormatted = String.format("%02d:%02d", minutes, remainderSeconds)
        rootLayout.removeView(mGameView)
        startBtn.visibility = View.VISIBLE
        sessionTimer.visibility = View.VISIBLE
        score.visibility = View.VISIBLE
        score.text = "Score: $mScore"
        sessionTimer.text = "Session time: $timeFormatted"
        mGameView.resetGame()
    }

    private fun enableEdgeToEdge() {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT
    }
}