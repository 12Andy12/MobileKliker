package com.example.kliker

import android.annotation.SuppressLint
import android.media.MediaActionSound
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.MediaStore.Audio.Media
import android.view.MotionEvent
import android.view.View
import android.view.View.*
import android.view.ViewGroup.LayoutParams.*
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.coroutines.*
import androidx.core.content.ContextCompat
import androidx.core.view.marginLeft



class MainActivity : AppCompatActivity() {
    companion object{
        var isGame:Boolean = false
    }
    //var isGame : Boolean = false
    var pDownX = 0
    var pDownY = 0
    var pUpX = 0
    var pUpY = 0
    lateinit var frame: ConstraintLayout
    lateinit var score: TextView
    lateinit var missCount: TextView
    lateinit var escaperCount: TextView
    lateinit var total: TextView
    lateinit var btnStart: Button
    //lateinit var missSound : MediaPlayer

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        frame = findViewById<ConstraintLayout>(R.id.container)
        score = TextView(this@MainActivity)
        missCount = TextView(this@MainActivity)
        escaperCount = TextView(this@MainActivity)
        btnStart = findViewById<Button>(R.id.btnStart)
        total = findViewById<TextView>(R.id.total)
        frame.addView(score)
        frame.addView(missCount)
        frame.addView(escaperCount)
        score.text = "0"
        missCount.text = "0"
        escaperCount.text = "0"
        total.text = "0"
        score.textSize = 20f
        missCount.textSize = 20f
        escaperCount.textSize = 20f
        score.textAlignment = TEXT_ALIGNMENT_CENTER
        score.layoutParams.width = MATCH_PARENT
        escaperCount.textAlignment = TEXT_ALIGNMENT_VIEW_END
        escaperCount.layoutParams.width = MATCH_PARENT

        btnStart.setOnClickListener {
            frame.removeView(btnStart)
            frame.removeView(total)
            score.text = "0"
            escaperCount.text = "0"
            missCount.text = "0"
            escaperCount.text = "0"
            isGame = true
            game()
        }


    }

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    fun game() = runBlocking {
        frame.setOnTouchListener { v, event ->
            val action = event.action
            when (action) {
                MotionEvent.ACTION_DOWN -> {
                    missCount.text = (missCount.text.toString().toInt() + 1).toString()
                    var missSound : MediaPlayer = MediaPlayer.create(this@MainActivity,R.raw.miss)
                    missSound.start()
                }
                MotionEvent.ACTION_MOVE -> {
                }
                MotionEvent.ACTION_UP -> {
                }
                MotionEvent.ACTION_CANCEL -> {
                }
                else -> {
                }
            }
//            println(arrayOf( pDownX,pDownY, pUpX,pUpY))
            true
        }
        // CountDownTimer(Общее время работы, интервал запуска) в миллисекундах
        var timer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisecUntilFinished: Long /*сколько осталось*/) {
                var a: beetle = beetle(this@MainActivity, frame, score, escaperCount, isGame)
            }

            override fun onFinish() {
                isGame = false
                frame.addView(btnStart)
                total.text = (score.text.toString().toInt() - missCount.text.toString().toInt()*5 - escaperCount.text.toString().toInt()*10).toString()
                frame.addView(total)
            }
        }.start(); //сразу запускаем


    }

}


