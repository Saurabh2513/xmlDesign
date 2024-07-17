package com.example.xmldesign.view.activity.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.xmldesign.R
import com.example.xmldesign.view.activity.MainActivity

class LogInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        val btnlogin = findViewById<Button>(R.id.loginBtn)
        val createAccountText = findViewById<TextView>(R.id.createAcTxt)
        btnlogin.setOnClickListener {
           val intent = Intent(this@LogInActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        createAccountText.setOnClickListener {
           val intent = Intent(this@LogInActivity, CreateAccountActivity::class.java)
            startActivity(intent)
        }
    }
}
