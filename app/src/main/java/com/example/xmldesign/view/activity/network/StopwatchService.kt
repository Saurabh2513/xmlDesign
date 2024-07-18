package com.example.xmldesign.view.activity.network

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.xmldesign.R
import com.example.xmldesign.view.activity.ui.view.activity.MainActivity

class StopwatchService : Service() {

    companion object {
        const val CHANNEL_ID = "StopwatchServiceChannel"
        const val PREFS_NAME = "StopwatchPrefs"
        const val ELAPSED_TIME_KEY = "ElapsedTime"
    }

    private var startTime = 0L
    private var elapsedTime = 0L
    private val handler = Handler(Looper.getMainLooper())
    private var isRunning = false
    private lateinit var sharedPreferences: SharedPreferences

    private val updateTimeRunnable: Runnable = object : Runnable {
        override fun run() {
            elapsedTime = System.currentTimeMillis() - startTime
            sendUpdate()
            if (isRunning) {
                handler.postDelayed(this, 1000)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        elapsedTime = sharedPreferences.getLong(ELAPSED_TIME_KEY, 0L)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null && intent.action != null) {
            when (intent.action) {
                "START" -> startStopwatch()
                "STOP" -> stopStopwatch()
                "RESET" -> resetStopwatch()
            }
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @SuppressLint("ForegroundServiceType")
    private fun startStopwatch() {
        if (!isRunning) {
            startForeground(1, createNotification())
            startTime = System.currentTimeMillis() - elapsedTime
            handler.post(updateTimeRunnable)
            isRunning = true
        }
    }

    private fun stopStopwatch() {
        if (isRunning) {
            handler.removeCallbacks(updateTimeRunnable)
            saveElapsedTime()
            stopForeground(true)
            isRunning = false
        }
    }

    private fun resetStopwatch() {
        stopStopwatch()
        startTime = 0L
        elapsedTime = 0L
        saveElapsedTime()
        sendUpdate()
    }

    private fun saveElapsedTime() {
        sharedPreferences.edit().putLong(ELAPSED_TIME_KEY, elapsedTime).apply()
    }

    private fun sendUpdate() {
        val intent = Intent("StopwatchUpdate")
        intent.putExtra("elapsedTime", elapsedTime)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Stopwatch Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(serviceChannel)
        }
    }

    private fun createNotification(): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Stopwatch Running")
            .setContentText("Stopwatch is running in the background")
            .setSmallIcon(R.drawable.baseline_access_time_24)
            .setContentIntent(pendingIntent)
            .build()
    }
}