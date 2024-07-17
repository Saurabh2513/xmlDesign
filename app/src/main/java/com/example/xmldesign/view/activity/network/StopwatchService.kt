package com.example.xmldesign.view.activity.network

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class StopwatchService : Service() {

    private var startTime = 0L
    private var elapsedTime = 0L
    private val handler = Handler(Looper.getMainLooper())
    private var isRunning = false
    private val updateTimeRunnable: Runnable = object : Runnable {
        override fun run() {
            elapsedTime = System.currentTimeMillis() - startTime
            val intent = Intent("StopwatchUpdate")
            intent.putExtra("elapsedTime", elapsedTime)
            LocalBroadcastManager.getInstance(this@StopwatchService).sendBroadcast(intent)
            if (isRunning) {
                handler.postDelayed(this, 1000)
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null && intent.action != null) {
            when (intent.action) {
                "START" -> startStopwatch()
                "STOP" -> stopStopwatch()
            }
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun startStopwatch() {
        if (!isRunning) {
            startTime = System.currentTimeMillis() - elapsedTime
            handler.post(updateTimeRunnable)
            isRunning = true
        }
    }

    private fun stopStopwatch() {
        if (isRunning) {
            handler.removeCallbacks(updateTimeRunnable)
            isRunning = false
        }
    }
    private fun sendUpdate() {
        val intent = Intent("StopwatchUpdate")
        intent.putExtra("elapsedTime", elapsedTime)
        LocalBroadcastManager.getInstance(this@StopwatchService).sendBroadcast(intent)
    }
}