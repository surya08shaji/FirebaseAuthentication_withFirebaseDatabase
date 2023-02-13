package com.example.realtimedb

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.realtimedb.adapter.MessageAdapter
import com.example.realtimedb.model.Message
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class WelcomeActivity : AppCompatActivity() {

    private var userId: String? = null

    private var databaseReference: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        initFirebase()

        setupSendButton()

        createFirebaseListener()

//        addUserChangeListener()

        val back: ImageView = findViewById(R.id.back)
        back.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, MainActivity::class.java))
        }

    }

    private fun initFirebase() {
        FirebaseApp.initializeApp(applicationContext)

        FirebaseDatabase.getInstance().setLogLevel(Logger.Level.DEBUG)

        databaseReference = FirebaseDatabase.getInstance().reference
    }

    private fun createFirebaseListener() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val toReturn: ArrayList<Message> = ArrayList();

                for (data in dataSnapshot.children) {
                    val messageData = data.getValue<Message>(Message::class.java)

                    val message = messageData?.let { it } ?: continue

                    toReturn.add(message)
                }

                toReturn.sortBy { message ->
                    message.timestamp
                }

                setupAdapter(toReturn)
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        databaseReference?.child("messages")?.addValueEventListener(postListener)
    }

    private fun setupAdapter(data: ArrayList<Message>) {

        val mainActivityRecyclerView: RecyclerView = findViewById(R.id.mainActivityRecyclerView)

        val linearLayoutManager = LinearLayoutManager(this)
        mainActivityRecyclerView.layoutManager = linearLayoutManager
        mainActivityRecyclerView.adapter = MessageAdapter(data) {
            Toast.makeText(this, "${it.text} clicked", Toast.LENGTH_SHORT).show()
        }

        //scroll to bottom
        mainActivityRecyclerView.scrollToPosition(data.size - 1)
    }

    private fun setupSendButton() {

        val mainActivitySendButton: ImageView = findViewById(R.id.mainActivitySendButton)
        val mainActivityEditText: EditText = findViewById(R.id.mainActivityEditText)
        mainActivitySendButton.setOnClickListener {
            if (!mainActivityEditText.text.toString().isEmpty()) {
                sendData()
            } else {
                Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendData() {
        val mainActivityEditText: EditText = findViewById(R.id.mainActivityEditText)
        databaseReference?.child("messages")
            ?.child(java.lang.String.valueOf(System.currentTimeMillis()))
            ?.setValue(Message(mainActivityEditText.text.toString()))

        mainActivityEditText.setText("")
    }
}
