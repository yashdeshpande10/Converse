package com.example.android.converse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Login : AppCompatActivity() {

    //define our views
    private lateinit var etLogin_email: EditText
    private lateinit var etLogin_password: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvNewRegister: TextView

    //define firebase auth object
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //init firebase auth object
        mAuth = FirebaseAuth.getInstance()

        //init our views
        etLogin_email = findViewById(R.id.ET_Login_email)
        etLogin_password = findViewById(R.id.ET_Login_password)
        btnLogin = findViewById(R.id.btn_Login)
        tvNewRegister = findViewById(R.id.TV_NewUserRegister)

        //when clicked on TV, go to register activity
        tvNewRegister.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

        //when clicked on login button,
        // login func. -> takes ETemail and ETpassword as arguments
        btnLogin.setOnClickListener {

            login(etLogin_email, etLogin_password)
        }




    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
        updateUi(currentUser)
    }

    private fun updateUi(firebaseUser: FirebaseUser?) {
        if (firebaseUser != null){
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    //login func which takes ETemail and ETpassword as parameters
    private fun login(etLogin_email: EditText, etLogin_password: EditText) {

        //Email and Password edittexts shouldnt be empty, give toast warnings
        when {
            TextUtils.isEmpty(etLogin_email.text.toString().trim() { it <= ' ' }) -> {
                Toast.makeText(this, "Please enter Email", Toast.LENGTH_SHORT).show()
            }

            TextUtils.isEmpty(etLogin_password.text.toString().trim() { it <= ' ' }) -> {
                Toast.makeText(this, "Please enter Password", Toast.LENGTH_SHORT).show()

            }
            else -> {
                val email: String = etLogin_email.text.toString().trim() { it <= ' ' }
                val password: String = etLogin_password.text.toString().trim() { it <= ' ' }

                //create firebase auth instance and register user
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->

                        //if registration is successfully done
                        if (task.isSuccessful) {
                            //firebase registered user
                            val firebaseUser: FirebaseUser = task.result!!.user!!

                            //after successful registration, show toast
                            Toast.makeText(
                                this,
                                "You are Logged in successfully.",
                                Toast.LENGTH_SHORT
                            )
                                .show()

                            //after toast, go to main activity
                            val intent = Intent(this, MainActivity::class.java)
                            finish()
                            startActivity(intent)


                        } else {
                            //if reg is not successful, show error toast
                            Toast.makeText(
                                this,
                                task.exception!!.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }
            }


        }
    }
}