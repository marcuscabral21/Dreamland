//Código com o tempo de sessão implementado
package com.example.dreamland

import android.graphics.Color
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.Button
import android.widget.Chronometer
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
//import kotlinx.android.synthetic.main.activity_car_race.*

class CarRace : AppCompatActivity(), GameTask {
    private lateinit var rootLayout: LinearLayout
    private lateinit var startBtn: Button
    private lateinit var mGameView: GameView
    private lateinit var score: TextView
    private lateinit var sessionTimer: Chronometer
    private var startTime: Long = 0
    private var isGameRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_car_race)

        startBtn = findViewById(R.id.startBtn)
        rootLayout = findViewById(R.id.rootLayout)
        score = findViewById(R.id.score)
        sessionTimer = findViewById(R.id.session_timer)

        mGameView = GameView(this, this)

        startBtn.setOnClickListener {
            startGame()
        }
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


/*package com.example.dreamland

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CarRace : AppCompatActivity(),GameTask {
    lateinit var rootLayout :LinearLayout
    lateinit var startBtn:Button
    lateinit var mGameView : GameView
    lateinit var score:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_car_race)
        startBtn = findViewById(R.id.startBtn)
        rootLayout = findViewById(R.id.rootLayout)
        score = findViewById(R.id.score)
        mGameView = GameView(this,this)

        startBtn.setOnClickListener {
            mGameView.setBackgroundResource(R.drawable.road)
            rootLayout.addView(mGameView)
            startBtn.visibility = View.GONE
            score.visibility = View.GONE
        }
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/
    }

    /*override fun closeGame(mScore: Int) {
        score.text = "Score : $mScore"
        rootLayout.removeView(mGameView)
        startBtn.visibility = View.VISIBLE
        score.visibility = View.VISIBLE
    }*/
    override fun closeGame(mScore: Int) {
        score.text = "Score : $mScore"
        rootLayout.removeView(mGameView)
        startBtn.visibility = View.VISIBLE
        score.visibility = View.VISIBLE
        mGameView.resetGame() // Adicione esta linha
    }
}*/