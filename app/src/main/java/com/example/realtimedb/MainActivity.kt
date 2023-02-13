package com.example.realtimedb

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.realtimedb.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    private var mFirebaseDatabaseInstances: FirebaseDatabase? = null
    private var mFirebaseDatabase: DatabaseReference? = null

    private var userId: String? = null
    private var emailAddress: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val register: Button = findViewById(R.id.registerR)
        val login: TextView = findViewById(R.id.loginR)
        val userName: EditText = findViewById(R.id.userNameR)
        val email: EditText = findViewById(R.id.emailR)
        val password: EditText = findViewById(R.id.passwordR)

        mAuth = FirebaseAuth.getInstance()

        mFirebaseDatabaseInstances = FirebaseDatabase.getInstance()

        //if already logged in go to sign in screen
        if (mAuth!!.currentUser != null) {
            startActivity(Intent(this, WelcomeActivity::class.java))
            finish()
        }
        login.setOnClickListener {
            startActivity(Intent(this, SigningActivity::class.java))
            finish()
        }

        register.setOnClickListener {
            if (TextUtils.isEmpty(userName.text.toString())) {
                Toast.makeText(applicationContext, "Enter Username!", Toast.LENGTH_LONG).show()
            }
            if (TextUtils.isEmpty(email.text.toString())) {
                Toast.makeText(applicationContext, "Enter email address!", Toast.LENGTH_LONG).show()
            }
            if (TextUtils.isEmpty(password.text.toString())) {
                Toast.makeText(applicationContext, "Enter password!", Toast.LENGTH_LONG).show()
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email.toString()).matches()) {
                Toast.makeText(applicationContext, "Success!!!", Toast.LENGTH_LONG).show()
            }
            mAuth!!.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener(this) { task ->
                    Toast.makeText(
                        this,
                        "createUserWithEmail:onComplete" + task.isSuccessful,
                        Toast.LENGTH_SHORT
                    ).show()

                    if (task.isSuccessful) {
                        mFirebaseDatabase = mFirebaseDatabaseInstances!!.getReference("users")

                        val user = FirebaseAuth.getInstance().currentUser
                        userId = user!!.uid
                        emailAddress = user.email

                        val myUser = User(userName.text.toString(), emailAddress!!)
                        mFirebaseDatabase!!.child(userId!!).setValue(myUser)

                        startActivity(Intent(this, WelcomeActivity::class.java))
                        finish()
//                        val name = userName.text.toString()
//                        intent.putExtra("name",name)
                    } else {
                        Toast.makeText(
                            this,
                            "Authentication Failed" + task.exception,
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e("MyTag", task.exception.toString())
                    }
                }
        }
    }
}
