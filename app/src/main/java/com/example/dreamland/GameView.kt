package com.example.dreamland

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View

class GameView(var c : Context, var gameTask : GameTask):View(c)
{
    private var myPaint: Paint? = null
    private var speed = 1
    private var time = 0
    private var score = 0
    private var myCarPosition = 0
    private val otherCars = ArrayList<HashMap<String,Any>>()

    var carGoingBackwards = false
    var gameOver = false
    var viewWidth = 0
    var viewHeight = 0
    init {
        myPaint = Paint()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        viewWidth = this.measuredWidth
        viewHeight = this.measuredHeight

        if (time % 700 < 10 +speed){
            val map = HashMap<String,Any>()
            map["lane"] = (0..2).random()
            map["startTime"] = time
            otherCars.add(map)
        }
        time = time + 10 + speed
        val carWidth = viewWidth / 5
        val carHeight = carWidth + 10
        myPaint!!.style = Paint.Style.FILL
        val d = resources.getDrawable(R.drawable.red_car,null)

        d.setBounds(
            myCarPosition * viewWidth / 3 + viewWidth / 15 + 25,
            viewHeight-2 - carHeight,
            myCarPosition * viewWidth / 3 + viewWidth / 15 + carWidth - 25,
            viewHeight - 2
        )
        d.draw(canvas!!)
        myPaint!!.color = Color.GREEN
        var highScore = 0

        val carsToDraw = ArrayList(otherCars) // Cria uma cópia da lista otherCars
        for (i in otherCars.indices){
            try {
                val carX = otherCars[i]["lane"] as Int * viewWidth / 3 + viewWidth / 15
                var carY = time - otherCars[i]["startTime"] as Int
                //val d2 = resources.getDrawable(R.drawable.blue_car,null)

                val d2 = if (carGoingBackwards) {
                    resources.getDrawable(R.drawable.blue_car, null)
                } else {
                    resources.getDrawable(R.drawable.blue_car, null)
                }

                d2.setBounds(
                    carX + 25 , carY - carHeight , carX + carWidth - 25, carY
                )
                d2.draw(canvas)
                if (otherCars[i]["lane"] as Int == myCarPosition){
                    if (carY > viewHeight - 2 - carHeight
                        && carY < viewHeight - 2){

                        gameTask.closeGame(score)
                    }
                }
                if (carY > viewHeight + carHeight)
                {
                    otherCars.removeAt(i)
                    score++
                    speed = 1 + Math.abs(score / 8)
                    if (score > highScore){
                        highScore = score
                    }
                }
            }
            catch (e:Exception){
                e.printStackTrace()
            }
        }
        myPaint!!.color = Color.WHITE
        myPaint!!.textSize = 40f
        canvas.drawText("Score : $score",80f,80f,myPaint!!)
        canvas.drawText("Speed : $speed",380f,80f,myPaint!!)
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event!!.action){
            MotionEvent.ACTION_DOWN ->{
                if (gameOver) {
                    // Reset the game state and start a new game
                    resetGame()
                }else {
                val x1 = event.x
                if (x1 < viewWidth / 2){
                    if (myCarPosition > 0){
                        myCarPosition--
                    }
                }
                if (x1 > viewWidth / 2) {
                    if (myCarPosition < 2) {
                        myCarPosition++
                    }
                }

                }
                invalidate()
            }
            MotionEvent.ACTION_UP ->{}
        }
        return true
    }

        fun resetGame() {
            // Reset all game variables to their initial state
            speed = 1
            time = 0
            score = 0
            myCarPosition = 0
            otherCars.clear()
            gameOver = false
        }
}