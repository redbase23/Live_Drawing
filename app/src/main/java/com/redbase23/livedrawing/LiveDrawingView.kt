package com.redbase23.livedrawing

import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceView

class LiveDrawingView(
    context:Context,
    screenX:Int) : SurfaceView(context),Runnable{

    private lateinit var thread: Thread

    @Volatile
    private var drawing:Boolean =false
    private var paused =true

    private val debugging = true
    private lateinit var canvas: Canvas
    private val paint = Paint()
    private var fps:Long=0
    private val millisInSecond:Long=1000
    private val fontSize:Int=screenX/20
    private val fontMargin:Int=screenX/75

    private var resetButton: RectF = RectF(0f,0f,100f,100f)
    private var togglePauseButton:RectF = RectF(0f,150f,100f,250f)

    private val particleSystem = ArrayList<ParticleSystem>()
    private var nextSystem =0
    private val maxSystems =1000
    private val particlesPerSystem=100

    init{
        //Initialize the particles and their system
        for (i in 0 until maxSystems){
            particleSystem.add(ParticleSystem())
            particleSystem[i].initParticles(particlesPerSystem)
        }
    }


    private fun draw(){
        if(holder.surface.isValid){
            canvas=holder.lockCanvas()

            canvas.drawColor(Color.argb(255,0,0,0))

            paint.color=Color.argb(255,255,255,255)
            //Draw the buttons
            canvas.drawRect(resetButton,paint)
            canvas.drawRect(togglePauseButton,paint)

            paint.textSize=fontSize.toFloat()

            if(debugging){
                printDebuggingText()
            }

            //draw the particle systems
            for (i in 0 until nextSystem){
                particleSystem[i].draw(canvas,paint)
            }

            holder.unlockCanvasAndPost(canvas)
        }
    }

    private fun printDebuggingText() {
        val debugSize=fontSize/2
        val debugStart=300
        paint.textSize=debugSize.toFloat()
        canvas.drawText("fps: $fps",10f,debugStart.toFloat(),paint)
        canvas.drawText("Systems: $nextSystem",10f,
            (debugStart+fontMargin+debugSize).toFloat(),paint)
        canvas.drawText("Particles: ${nextSystem*particlesPerSystem}",10f,
            (debugStart+fontMargin*2+debugSize*2).toFloat(),paint)
    }

    override fun run() {
        while(drawing){
            val frameStartTime =System.currentTimeMillis()

            if(!paused){
                update()
            }
            draw()

            val timeThisFrame=System.currentTimeMillis()-frameStartTime
            if(timeThisFrame>0){
                fps=millisInSecond/timeThisFrame
            }
        }
    }

    private fun update() {
        //update the particles
        for (i in 0 until particleSystem.size){
            if(particleSystem[i].isRunning){
                particleSystem[i].update(fps)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

       if(event.action and MotionEvent.ACTION_MASK == MotionEvent.ACTION_MOVE){
           particleSystem[nextSystem].emitParticles(PointF(
               event.x,event.y
           ))

           nextSystem++

           if(nextSystem==maxSystems){
               nextSystem=0
           }
       }

        if(event.action and MotionEvent.ACTION_MASK == MotionEvent.ACTION_DOWN){
            //Clear the screen of all particles
            if (resetButton.contains(event.x,event.y)){
                nextSystem=0
                paused=true
            }
            //Pause the particles
            if(togglePauseButton.contains(event.x, event.y)){
                paused=!paused
            }

        }
        return true
    }

    fun pause(){
        drawing=false
        try{
            thread.join()
        }catch (e:InterruptedException){
            Log.e("Error: ","joining thread")
        }
    }

    fun resume(){
        drawing=true
        thread=Thread(this)
        thread.start()
    }


}