package com.example.xmldesign.view.activity.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.xmldesign.databinding.ActivityCreateAccountBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class CreateAccountActivity : AppCompatActivity() {
    lateinit var binding: ActivityCreateAccountBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialising auth object
        auth = Firebase.auth


        binding.loginBtn.setOnClickListener {
            signUpUser()
        }
        binding.createAcTxt.setOnClickListener {
            val intent = Intent(this@CreateAccountActivity, LogInActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signUpUser() {
        val userName = binding.userNameTxt.text.toString()
        val email = binding.emailIdTxt.text.toString()
        val pass = binding.PassTxt.text.toString()
        val passCon = binding.ConfPassTxt.text.toString()


        // check pass
        if (email.isBlank() || pass.isBlank() || passCon.isBlank()) {
            Toast.makeText(this, "Email and Password can't be blank", Toast.LENGTH_SHORT).show()
            return
        }
//        if (userName.isBlank() || pass.isBlank() || passCon.isBlank()) {
//            Toast.makeText(this, "Email and Password can't be blank", Toast.LENGTH_SHORT).show()
//            return
//        }

        if (pass != passCon) {
            Toast.makeText(this, "Password and Confirm Password do not match", Toast.LENGTH_SHORT)
                .show()
            return
        }
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                val intent = Intent(this@CreateAccountActivity,LogInActivity::class.java)
                startActivity(intent)
                finish()
                Toast.makeText(this, "Successfully Singed Up", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Singed Up Failed!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}