package com.example.realtimedb

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SigningActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signing)

        mAuth = FirebaseAuth.getInstance()

        val email: EditText = findViewById(R.id.emailL)
        val password: EditText = findViewById(R.id.passwordL)
        val login: Button = findViewById(R.id.loginL)

        login.setOnClickListener {
            if (TextUtils.isEmpty(email.text.toString())) {
                Toast.makeText(applicationContext, "Enter Username!", Toast.LENGTH_LONG).show()
            }
            if (TextUtils.isEmpty(password.text.toString())) {
                Toast.makeText(applicationContext, "Enter password!", Toast.LENGTH_LONG).show()
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email.toString()).matches()) {
                Toast.makeText(applicationContext, "Success!!!", Toast.LENGTH_LONG).show()
            }

            mAuth!!.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this, WelcomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this,
                            "Authentication Failed" + task.exception,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

    }

    public override fun onStart() {
        super.onStart()

        if (mAuth!!.currentUser != null) {
            startActivity(Intent(this, WelcomeActivity::class.java))
            finish()
        }
    }
}


//    override fun onResume() {
//        super.onResume()
//        progressBar.visibility= View.GONE
//    }

//    fun loginButtonClicked(view: View){
//        if(TextUtils.isEmpty(email.text.toString())){
//            Toast.makeText(applicationContext,"Enter Username!", Toast.LENGTH_LONG).show()
//            return
//        }
//        if(TextUtils.isEmpty(password.text.toString())){
//            Toast.makeText(applicationContext,"Enter password!", Toast.LENGTH_LONG).show()
//            return
//        }
//        progressBar.visibility=View.VISIBLE

//        mAuth!!.signInWithEmailAndPassword(email.text.toString(),password.text.toString())
//            .addOnCompleteListener(this){task ->
//
//                progressBar.visibility=View.GONE
//
//                if(task.isSuccessful){
//                    val intent=Intent(this,welcome::class.java)
//                    startActivity(intent)
//                    finish()
//                }else{
//                    if(password.text.toString().length<6){
//                        password.error="Password is too short, enter minimum 6 characters"
//                    }
//                    Toast.makeText(this,"Authentication Failed"+task.exception,Toast.LENGTH_SHORT).show()
//                }
//            }
//    }