package com.example.android.converse

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SmoothScroller
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ChatActivity : AppCompatActivity() {
    //define our views
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendButton: ImageView
    private lateinit var calendar: Calendar
    private lateinit var simpleDateFormat: SimpleDateFormat

    //define message adapter
    private lateinit var messageAdapter: MessageAdapter

    //define arraylist of messages
    private lateinit var messageList: ArrayList<Message>

    //define firebase realtime db ref
    private lateinit var mDbRef: DatabaseReference

    //sender room and receiver room is used to create a unique room for sender and receiver
    //so that message is private and message is not reflected in all the users
    var receiverRoom: String? = null
    var senderRoom: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        //get our data from userAdapter
        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")

        //init current users' sender ID using auth UID
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid

        //init firebase realtime db ref
        mDbRef = FirebaseDatabase.getInstance().getReference()

        //init the unique rooms for sender and receiver
        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid

        //display the person's name we're chatting with on the action bar
        supportActionBar?.title = name

        //init our views
        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        messageBox = findViewById(R.id.et_typeMessage)
        sendButton = findViewById(R.id.btn_send)

        //init our arraylist of messages
        messageList = ArrayList()

        //init our messages adapter
        messageAdapter = MessageAdapter(this, messageList)

        //init rec view
        //rec view always needs them to work with -> 1. Layout Manager and 2. Adapter

        //init layout manager
        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        //init adapter
        chatRecyclerView.adapter = messageAdapter

        //logic for adding data to rec view
        //going inside the DB and reading the values
        //valueEventListener always implements 2 overridden methods
        // -> 1. onDataChange and 2. onCancelled
        mDbRef.child("chats").child(senderRoom!!).child("messages")
                .addValueEventListener(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {

                        //empty the arrayList of users before begin traversing+fetching
                        messageList.clear()

                        //snapshot is used to get data from DB,
                        // so we need to traverse inside all the messages of DB
                        for (postSnapshot in snapshot.children) {

                            //create user object and fetching from DB using postSnapshot,
                            // for adding in arrayList of messages
                            val message = postSnapshot.getValue(Message::class.java)
                            messageList.add(message!!)


                        }
                        //we have to notify the adapter after any logic is performed on it
                        messageAdapter.notifyDataSetChanged()

                        chatRecyclerView.scrollToPosition(messageList.size-1)


                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })






        //main logic- add message to DB and from DB we fetch messages for diff users
        // which is displayed in rec view


        //add messages to DB after send button is pressed
        sendButton.setOnClickListener {

            //get the text of message box
            val message = messageBox.text.toString().trim()
            val timestamp = getDateTime()
            //init object of class Message
            val messageObject = Message(message, senderUid, timestamp)

            //we have to create a node of chats in the DB
            //update the sender room and at the same time update the receiver room
            if (message.isNotEmpty()) {
                mDbRef.child("chats").child(senderRoom!!).child("messages").push()
                        .setValue(messageObject).addOnSuccessListener {
                            mDbRef.child("chats").child(receiverRoom!!).child("messages").push()
                                    .setValue(messageObject)
                        }
            }

            //after message is sent, empty the message box
            messageBox.setText("")


        }






    }




    private fun getDateTime(): String {
        calendar = Calendar.getInstance()
        simpleDateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm")
        val date = simpleDateFormat.format(calendar.time)

        return date



    }


}