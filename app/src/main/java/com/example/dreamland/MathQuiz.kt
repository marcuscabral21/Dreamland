package com.example.dreamland

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.util.Random

class MathQuiz : AppCompatActivity() {

    private lateinit var startButton: Button
    private lateinit var gamelayout: LinearLayout
    private lateinit var dashbord: LinearLayout
    private lateinit var questionTextView: TextView
    private lateinit var option1Button: Button
    private lateinit var option2Button: Button
    private lateinit var option3Button: Button
    private lateinit var option4Button: Button
    private lateinit var timerTextView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var questionNumberTextView: Button
    private lateinit var sessionTimeTextView: TextView
    private lateinit var menuButton: ImageButton

    private var score = 0
    private var currentQuestionIndex = 0
    private lateinit var currentQuestion: Question
    private var timer: CountDownTimer? = null
    private var sessionTimer: CountDownTimer? = null
    private var elapsedTime = 0L

    private val random = Random()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_math_quiz)

        startButton = findViewById(R.id.start_button)
        gamelayout = findViewById(R.id.gamelayout)
        dashbord = findViewById(R.id.dashbord)
        progressBar = findViewById(R.id.progressBar)
        timerTextView = findViewById(R.id.timerTextView)
        questionNumberTextView = findViewById(R.id.questionNumberTextView)
        sessionTimeTextView = findViewById(R.id.sessionTimeTextView)
        questionTextView = findViewById(R.id.questionTextView)
        menuButton = findViewById(R.id.menuButton)

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

        dashbord.visibility = View.VISIBLE
        gamelayout.visibility = View.GONE

        startButton.setOnClickListener {
            startGame()
        }

        option1Button = findViewById(R.id.option1Button)
        option2Button = findViewById(R.id.option2Button)
        option3Button = findViewById(R.id.option3Button)
        option4Button = findViewById(R.id.option4Button)

        option1Button.setOnClickListener { onOptionSelected(it) }
        option2Button.setOnClickListener { onOptionSelected(it) }
        option3Button.setOnClickListener { onOptionSelected(it) }
        option4Button.setOnClickListener { onOptionSelected(it) }
    }

    private fun showInstructionsDialog() {
        val dialogMessage = "\nHow to play Math Quiz Game:\n\nSettle the score before time runs out."
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
        dashbord.visibility = View.GONE
        gamelayout.visibility = View.VISIBLE
        score = 0
        currentQuestionIndex = 0
        updateScoreDisplay()
        startTimer()
        startSessionTimer(600000) // 10 minutes (600 seconds)
        showNextQuestion()
    }

    private fun showNextQuestion() {
        currentQuestion = generateRandomQuestion()
        updateQuestionUI()
        questionNumberTextView.text = "    Question ${currentQuestionIndex + 1}/100    "
        currentQuestionIndex++
    }

    private fun updateQuestionUI() {
        questionTextView.text = currentQuestion.question
        val options = currentQuestion.options
        option1Button.text = options[0]
        option2Button.text = options[1]
        option3Button.text = options[2]
        option4Button.text = options[3]
    }

    private fun startTimer() {
        timer?.cancel()
        val duration = 20000L // Total duration of the timer in milliseconds
        val interval = 50L // Update interval in milliseconds

        timer = object : CountDownTimer(duration, interval) {
            override fun onTick(millisUntilFinished: Long) {
                val progress = ((duration - millisUntilFinished) * 100 / duration).toInt()
                progressBar.progress = progress
                timerTextView.text = "${millisUntilFinished / 1000}"
            }

            override fun onFinish() {
                progressBar.progress = 100
                timerTextView.text = "Time's up!"
                endGame()
            }
        }.start()
    }

    private fun startSessionTimer(sessionDurationInMillis: Long) {
        sessionTimer?.cancel() // Cancela o timer da sessão anterior

        sessionTimer = object : CountDownTimer(sessionDurationInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                elapsedTime += 1000
                val secondsElapsed = elapsedTime / 1000
                sessionTimeTextView.text = "Session Time: $secondsElapsed s"
            }

            override fun onFinish() {
                sessionTimeTextView.text = "Session Time: ${sessionDurationInMillis / 1000}s"
                // Adicione o código que você deseja executar quando o tempo da sessão acabar
            }
        }.start()
    }

    private fun generateRandomQuestion(): Question {
        val num1 = random.nextInt(16)
        val num2 = random.nextInt(16)
        val correctAnswer = num1 + num2
        val options = generateOptions(correctAnswer)

        return Question("$num1 + $num2?", options, options.indexOf(correctAnswer.toString()))
    }

    private fun generateOptions(correctAnswer: Int): List<String> {
        val options = mutableListOf<String>()
        options.add(correctAnswer.toString())

        while (options.size < 4) {
            val wrongAnswer = correctAnswer + random.nextInt(20) - 10
            if (wrongAnswer != correctAnswer && !options.contains(wrongAnswer.toString())) {
                options.add(wrongAnswer.toString())
            }
        }

        return options.shuffled()
    }

    private fun endGame() {
        Toast.makeText(this, "Game Over! Your score: $score", Toast.LENGTH_SHORT).show()
        Handler(Looper.getMainLooper()).postDelayed({
            dashbord.visibility = View.VISIBLE
            gamelayout.visibility = View.GONE
        }, 1000)
        timer?.cancel()
        sessionTimer?.cancel()

        // Salva o tempo da sessão em SharedPreferences
        val sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putLong("sessionTimeMathQuiz", elapsedTime) // Use "sessionTimeMathQuiz" como a chave
        editor.apply()
    }

    private fun onOptionSelected(view: View) {
        val selectedOption = when (view.id) {
            R.id.option1Button -> 0
            R.id.option2Button -> 1
            R.id.option3Button -> 2
            R.id.option4Button -> 3
            else -> -1
        }

        if (selectedOption == currentQuestion.correctOption) {
            score += 10
            updateScoreDisplay()
            view.setBackgroundColor(ContextCompat.getColor(this, R.color.correctColor))
            startTimer() // Reinicia o cronômetro
        } else {
            view.setBackgroundColor(ContextCompat.getColor(this, R.color.incorrectColor))
            Toast.makeText(this, "Wrong! Game Over.", Toast.LENGTH_SHORT).show()
            endGame()
            return
        }

        Handler(Looper.getMainLooper()).postDelayed({
            view.setBackgroundResource(R.drawable.rounded_button_background)
            if (currentQuestionIndex < 100) {
                showNextQuestion()
            } else {
                endGame()
            }
        }, 1000)
    }

    private fun updateScoreDisplay() {
        findViewById<TextView>(R.id.scoreTextView)?.text = "Score: $score"
    }
}