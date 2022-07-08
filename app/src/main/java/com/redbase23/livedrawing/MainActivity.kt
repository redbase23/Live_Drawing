package com.redbase23.livedrawing

import android.app.Activity
import android.graphics.Point
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : Activity() {

    private lateinit var liveDrawingView: LiveDrawingView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val size= Point()

        if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.R){
            val metrics=windowManager.currentWindowMetrics
            size.x=metrics.bounds.width()
            size.y=metrics.bounds.height()

        } else{
            @Suppress("DEPRECATION")
            val display = windowManager.defaultDisplay
            @Suppress("DEPRECATION")
            display.getSize(size)
        }

        liveDrawingView = LiveDrawingView(this,size.x)
        setContentView(liveDrawingView)
    }

    override fun onResume(){
        super.onResume()
        liveDrawingView.resume()
    }

    override fun onPause() {
        super.onPause()
        liveDrawingView.pause()
    }
}