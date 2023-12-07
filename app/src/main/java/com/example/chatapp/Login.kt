package com.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var login: Button
    private lateinit var signUp: Button
    private lateinit var mAuth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

        mAuth= FirebaseAuth.getInstance()
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        login = findViewById(R.id.login)
        signUp = findViewById(R.id.Signup)

        signUp.setOnClickListener{
            val intent = Intent(this,SignUp::class.java)
            startActivity(intent)
        }


        login.setOnClickListener{
            val email = email.text.toString()
            val password = password.text.toString()

            login(email,password)
        }
    }

    private fun login(email: String,password: String){
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this@Login,MainActivity::class.java)
                    finish()
                    startActivity(intent)
                } else {
                   Toast.makeText(this@Login,"User does not exist",Toast.LENGTH_SHORT).show()
                }
            }
    }
}