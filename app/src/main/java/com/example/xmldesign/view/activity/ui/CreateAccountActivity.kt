package com.example.xmldesign.view.activity.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.xmldesign.R
import com.example.xmldesign.view.activity.MainActivity

class CreateAccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_account)

        val btnlogin = findViewById<Button>(R.id.loginBtn)
        val createAccountText = findViewById<TextView>(R.id.createAcTxt)
        btnlogin.setOnClickListener {
            val intent = Intent(this@CreateAccountActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        createAccountText.setOnClickListener {
            val intent = Intent(this@CreateAccountActivity, LogInActivity::class.java)
            startActivity(intent)
        }
    }
}