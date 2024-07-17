package com.example.xmldesign.view.activity.ui.view.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.xmldesign.databinding.ActivityLogInBinding
import com.google.firebase.auth.FirebaseAuth

class LogInActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var binding: ActivityLogInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // initialising Firebase auth object
        auth = FirebaseAuth.getInstance()

        binding.loginBtn.setOnClickListener {
            login()
        }
        binding.createAcTxt.setOnClickListener {
            val intent = Intent(this@LogInActivity, CreateAccountActivity::class.java)
            startActivity(intent)
        }
    }

    private fun login() {
        val email = binding.emailIdTxt.text.toString()
        val pass = binding.PassTxt.text.toString()

        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                Toast.makeText(this, "Successfully LoggedIn", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@LogInActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else
                Toast.makeText(this, "Log In failed ", Toast.LENGTH_SHORT).show()
        }
    }
}
