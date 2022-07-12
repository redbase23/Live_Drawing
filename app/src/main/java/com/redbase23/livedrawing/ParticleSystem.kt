package com.redbase23.livedrawing

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

class ParticleSystem {

    private var duration:Float=0f
    private var particles: ArrayList<Particle> = ArrayList()
    private val random = Random()
    var isRunning = false

    fun initParticles(numParticles:Int){
        for(i in 0 until numParticles){
            var angle:Double =random.nextInt(360).toDouble()
            angle*=(3.14/180)

            //Option 1 - Slow particles
//            val speed =random.nextFloat()/3

            //Option 2 - Fast particles
            val speed=(random.nextInt(10)+1)

            val direction: PointF = PointF(cos(angle).toFloat()*speed, sin(angle).toFloat()*speed)



            particles.add(Particle((direction),255,random.nextInt(256),random.nextInt(256),random.nextInt(256)))
        }
    }

    fun emitParticles(startPosition: PointF){
        isRunning=true

        //Option 1 - System lasts for half a minute
        duration=30f

        //Option 2 - System lasts for 2 seconds
//        duration=2f

        for (p in particles){
            p.position.x=startPosition.x
            p.position.y=startPosition.y
        }

    }

    fun update(fps:Long){
        duration-=1f/fps

        for(p in particles){
            p.update()
        }
        if(duration<0){
            isRunning=false
        }
    }

    fun draw(canvas: Canvas, paint: Paint){
        for (p in particles){

            //Option 1 - Colored particles
            paint.setARGB(p.alfa, p.red,p.green,p.blue)


            //Option 2 - White particles
//            paint.color=Color.argb(255,255,255,255)

            //Option 1 - Big particles
//            val sizeX=25f
//            val sizeY=25f

            //Option 2 - Medium particles
//            val sizeX=10f
//            val sizeY=10f

            //Option 3 - Tiny particles
            val sizeX = 12f
            val sizeY = 12f

            //Draw the particles
            //Option 1 - Square particles
//            canvas.drawRect(p.position.x, p.position.y,
//            p.position.x+sizeX, p.position.y + sizeY,
//            paint)

            //Option 2 - Circle particles
            canvas.drawCircle(p.position.x,p.position.y,sizeX,paint)
        }
    }
}