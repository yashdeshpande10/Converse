package com.example.android.converse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Register : AppCompatActivity() {

    //define our views
    private lateinit var etRegisterUserName: EditText
    private lateinit var etRegister_email: EditText
    private lateinit var etRegister_password: EditText
    private lateinit var btnRegister: Button

    //define firebase auth object and firebase realtime database object
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //init firebase auth object
        mAuth = FirebaseAuth.getInstance()

        //init our views
        etRegisterUserName = findViewById(R.id.ET_Register_userName)
        etRegister_email = findViewById(R.id.ET_Register_email)
        etRegister_password = findViewById(R.id.ET_Register_password)
        btnRegister = findViewById(R.id.btn_Register)

        //when clicked on reg button,
        // call reg func. -> takes name, email, password as arguments
        btnRegister.setOnClickListener {

            val name = etRegisterUserName.text.toString()
            val email = etRegister_email.text.toString()
            val password = etRegister_password.text.toString()
            register(name, email, password)
        }

    }

    //register func
    private fun register(name: String, email: String, password: String) {

        //user email and pw inputs cannot be empty, show toast warnings
        when {
            TextUtils.isEmpty(email.trim() { it <= ' ' }) -> {
                Toast.makeText(this, "Please enter Email", Toast.LENGTH_SHORT).show()
            }

            TextUtils.isEmpty(password.trim() { it <= ' ' }) -> {
                Toast.makeText(this, "Please enter Password", Toast.LENGTH_SHORT).show()

            }
            else -> {
                email.trim() { it <= ' ' }
                password.trim() { it <= ' ' }

                //create an auth instance and register user
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->

                        //if registration is successfully done
                        if (task.isSuccessful) {
                            //after successful auth, we need to maintain a table of registered users
                            //call func to add reg users -> takes name, email, uid as args
                            addUserToDatabase(name,email, mAuth.currentUser?.uid!!)

                            //toast to show reg success
                            Toast.makeText(
                                this,
                                "You are Registered successfully.",
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

    //func to add registered users to DB
    private fun addUserToDatabase(name: String, email: String, uid: String?) {

        //get object ref of firebase realtime DB
        mDbRef = FirebaseDatabase.getInstance().getReference()

        //create a users node in the DB where users' name, email and uid will be stored
        mDbRef.child("user").child(uid!!).setValue(User(name,email,uid))
    }
}





