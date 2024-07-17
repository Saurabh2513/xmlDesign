package com.example.xmldesign.view.activity.ui.view.activity


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.xmldesign.R
import com.example.xmldesign.databinding.ActivityMainBinding
import com.example.xmldesign.view.activity.model.User
import com.example.xmldesign.view.activity.network.SharedPreferencesHelper
import com.example.xmldesign.view.activity.network.StopwatchService
import com.example.xmldesign.view.activity.ui.view.adapter.UserAdapter
import com.google.firebase.auth.FirebaseAuth
import kotlin.text.*


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: UserAdapter
    private lateinit var userList: ArrayList<User>
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    // timer
    private var startTime = 0L
    private var elapsedTime = 0L
    private val handler = Handler(Looper.getMainLooper())
    private var isRunning = false

    private val updateTimeRunnable: Runnable = object : Runnable {
        override fun run() {
            elapsedTime = System.currentTimeMillis() - startTime
            val seconds = (elapsedTime / 1000).toInt()
            val minutes = seconds / 60
            val hours = minutes / 60
            val displaySeconds = seconds % 60
            val displayMinutes = minutes % 60
            binding.tvTime.text =
                String.format("%02d:%02d:%02d", hours, displayMinutes, displaySeconds)
            if (isRunning) {
                handler.postDelayed(this, 1000)
            }
        }
    }
    private val timeUpdateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null && intent.hasExtra("elapsedTime")) {
                val elapsedTime = intent.getLongExtra("elapsedTime", 0L)
                updateTime(elapsedTime)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferencesHelper = SharedPreferencesHelper(this)


        recyclerView = findViewById(R.id.recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        userList = arrayListOf()
        userAdapter = UserAdapter(userList)
        recyclerView.adapter = userAdapter

        fetchUserEmails()
        startStopwatch()
        binding.btnStart.setOnClickListener {
            stopStopwatch()
        }
        binding.btnStop.setOnClickListener {
            startStopwatch()
        }
        binding.logout.setOnClickListener {
            logout()
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(timeUpdateReceiver, IntentFilter("StopwatchUpdate"))
    }
    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(timeUpdateReceiver)
    }





    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong("startTime", startTime)
        outState.putBoolean("isRunning", isRunning)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        startTime = savedInstanceState.getLong("startTime")
        isRunning = savedInstanceState.getBoolean("isRunning")
        if (isRunning) {
            handler.post(updateTimeRunnable)
        } else {
            updateTimeRunnable.run()
        }
    }
    private fun startStopwatch() {
        val intent = Intent(this, StopwatchService::class.java)
        intent.action = "START"
        startService(intent)
    }

    private fun stopStopwatch() {
        val intent = Intent(this, StopwatchService::class.java)
        intent.action = "STOP"
        startService(intent)
    }

    private fun updateTime(elapsedTime: Long) {
        val seconds = (elapsedTime / 1000).toInt()
        val minutes = seconds / 60
        val hours = minutes / 60
        val displaySeconds = seconds % 60
        val displayMinutes = minutes % 60
        binding.tvTime.text = String.format("%02d:%02d:%02d", hours, displayMinutes, displaySeconds)
    }

    private fun logout() {
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userEmail = currentUser.email
            if (userEmail != null) {
                sharedPreferencesHelper.saveEmail(userEmail)
            }
            auth.signOut()
            val loginIntent = Intent(this, LogOrCreActivity::class.java)
            startActivity(loginIntent)
            finish()
        }
    }

    private fun fetchUserEmails() {
        val emails = sharedPreferencesHelper.getEmails()
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        for (email in emails) {
            userList.add(User(email))
        }
        if (currentUser != null) {
            val userEmail = currentUser.email
            if (userEmail != null) {
                userList.add(User(email = userEmail))
                userAdapter.notifyDataSetChanged()
            }
        } else {
        }
    }
}
