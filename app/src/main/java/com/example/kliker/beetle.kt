package com.example.kliker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.graphics.Point
import android.media.MediaPlayer
import android.os.Build
import android.os.CountDownTimer
import android.view.Display
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import kotlinx.coroutines.*
import java.lang.Math.*
import kotlin.math.sin

fun rand(left: Int, right: Int): Int {
    return (left..right).random()
}

var imgBeetle = arrayOf(
    R.drawable.icon_beetle1,
    R.drawable.icon_beetle2,
    R.drawable.icon_cyberbeetle
)

class beetle {
    init {
        print("create")
    }
    lateinit var imageView: ImageView
    lateinit var score: TextView
    lateinit var escaperCount: TextView
    var isGame : Boolean = false
    var width: Int
    var type: Int
    var height:Int
    var alive : Boolean = true
    lateinit var context : Context
    constructor(c: Context, frame: ConstraintLayout, sc : TextView, ec : TextView, g : Boolean) {
        context = c
        score = sc
        isGame = g;
        escaperCount = ec
        val display: Display = (context.getSystemService() as WindowManager?)!!.defaultDisplay
        val p = Point()
        display.getSize(p)
        width = p.x
        height = p.y

        print((0..2).random())
        imageView = ImageView(context)
        imageView.x = rand(300, width - 600).toFloat()
        imageView.y = rand(300, height - 600).toFloat()
        type = rand(0, imgBeetle.size - 1)
        imageView.setImageDrawable(
            ContextCompat.getDrawable(
                context,
                imgBeetle[type]
            )
        )
        var lp: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(300, 300)
        //lp.setMargins(rand(0, 100), rand(0, 100), rand(0, 100), rand(0, 100))
        frame.addView(imageView, lp)
        beetleLive(frame)

    }

    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    private fun beetleLive(frame: ConstraintLayout) = runBlocking {
        launch {
            var timer = object : CountDownTimer(3000000, 1000) {
                override fun onTick(millisecUntilFinished: Long /*сколько осталось*/) {
                    frame.removeView(imageView)
                    if(!MainActivity.isGame) {
                        alive = false
                        escaperCount.text = (escaperCount.text.toString().toInt() + 1).toString()
                    }
                    if(!alive)
                        cancel()
                    move()
                    if (alive && !(imageView.x < -100 || imageView.y < -100 || imageView.x > width-100 || imageView.y > height-100))
                        frame.addView(imageView)
                    else if(!alive)
                        cancel()
                    else
                    {
                        escaperCount.text = (escaperCount.text.toString().toInt() + 1).toString()
                        cancel()
                    }


                }

                override fun onFinish() {
                }
            }.start(); //сразу запускаем
        }


        imageView.setOnTouchListener { v: View, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    frame.removeView(imageView)
                    alive = false
                    if(type == 0)
                        score.text = (score.text.toString().toInt() + 10).toString()
                    else if(type == 1)
                        score.text = (score.text.toString().toInt() + 30).toString()
                    else if(type == 2)
                        score.text = (score.text.toString().toInt() + 100).toString()
                    else
                        score.text = (score.text.toString().toInt() + 1).toString()

                    var touchSound : MediaPlayer = MediaPlayer.create(context,R.raw.touch)

                    touchSound.start()
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
    }
    private fun move() {
        var dist : Int
        if(type == 0)
            dist = 20
        else if(type == 1)
            dist = 50
        else if(type == 2)
            dist = 100
        else
            dist = 20

        imageView.rotation = rand(0,359).toFloat()

        var r = imageView.rotation
        r = 90 - imageView.rotation
        var s = sin(r/57.2958)

        var distanceY = abs(s)*dist
        var distanceX = sqrt(dist*dist - (distanceY*distanceY))

        if(r<=-270 && r>=-360)
            distanceX *= -1
        if(r<=-180 && r>=-270)
        {
            distanceX *= -1
            distanceY *= -1
        }
        if(r<=-90 && r>-180)
        {
            distanceX *= -1
        }
        if(r>0 && r<=90)
        {
            distanceY*=-1;
        }

        var timer = object : CountDownTimer(1000, 100) {
            override fun onTick(millisecUntilFinished: Long /*сколько осталось*/) {
                if(!MainActivity.isGame)
                    alive = false
                if(!alive)
                    cancel()
                imageView.x += distanceX.toInt()
                imageView.y += distanceY.toInt()
                if(type==2)
                    imageView.rotation = rand(0,359).toFloat()
            }

            override fun onFinish() {}
        }.start(); //сразу запускаем
    }

}