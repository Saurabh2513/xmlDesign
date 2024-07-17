package com.example.xmldesign.view.activity.ui.view.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.xmldesign.R

class LogOrCreActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_or_cre)
        val btn = findViewById<Button>(R.id.bookNowBtn)

        btn.setOnClickListener {
            val intent = Intent(this@LogOrCreActivity, LogInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}