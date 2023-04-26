package com.example.social

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.social.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        binding.singUpBtn.isEnabled = false
        binding.logInBtn.isEnabled = true

        binding.logInBtn.setOnClickListener {
            startActivity(Intent(this, LogInActivity::class.java))
            finish()
        }

        binding.signup.setOnClickListener {
            val name = binding.name.text.toString().trim()
            val email = binding.email.text.toString().trim()
            val phone = binding.editTextPhone.text.toString().trim()
            val password = binding.password.toString().trim()

            if (email.isBlank() || password.isBlank() || name.isBlank() || phone.isBlank()) {
                Toast.makeText(this, "Please provide complete info!", Toast.LENGTH_SHORT).show()
            } else if(!binding.checkBox.isChecked){
                Toast.makeText(this, "Please accept the term and condition", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "Initiating signup", Toast.LENGTH_SHORT).show()
                createAccount(email, password)
            }
        }
    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(ContentValues.TAG, "createUserWithEmail:success")

                    Toast.makeText(this, "Registered successfully! Please log in to proceed further", Toast.LENGTH_SHORT).show()
                    val user = auth.currentUser
                    updateUI(user)

                    startActivity(Intent(this, LogInActivity::class.java))
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {

    }
}