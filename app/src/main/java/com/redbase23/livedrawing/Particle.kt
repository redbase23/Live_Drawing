package com.redbase23.livedrawing

import android.graphics.Point
import android.graphics.PointF

class Particle (direction: PointF) {

    private val velocity:PointF=PointF()
    val position:PointF=PointF()

    init{
        velocity.x=direction.x
        velocity.y=direction.y
    }

    fun update(){
        position.x+=velocity.x
        position.y+=velocity.y
    }


}