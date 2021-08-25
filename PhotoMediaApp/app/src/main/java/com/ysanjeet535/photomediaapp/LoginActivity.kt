package com.ysanjeet535.photomediaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private val TAG = "LoginActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val bLogin = findViewById<Button>(R.id.bLogin)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)

        val auth = FirebaseAuth.getInstance()
        if(auth.currentUser != null){
            goPostActivity()
        }

        bLogin.setOnClickListener {
            bLogin.isEnabled = false
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            if(email.isBlank() || password.isBlank()){
                Toast.makeText(this,"Invalid details try again!!",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            //Now firebase authetication check
            val auth = FirebaseAuth.getInstance()
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task->
                bLogin.isEnabled = true
                if(task.isSuccessful){
                    Toast.makeText(this,"Worked!",Toast.LENGTH_SHORT).show()
                    goPostActivity()
                }else{
                    Log.i(TAG,"Sing in failed",task.exception)
                    Toast.makeText(this,"failed!",Toast.LENGTH_SHORT).show()
                }


            }
        }



    }

    private fun goPostActivity() {
        Toast.makeText(this,"Go post",Toast.LENGTH_SHORT).show()
        Log.i(TAG,"gopostactivity")
        val intent = Intent(this,PostActivity::class.java)
        startActivity(intent)
        finish()


    }
}