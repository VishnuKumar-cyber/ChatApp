package com.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatActivity : AppCompatActivity() {
    private lateinit var messageRecyclerView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendButton: ImageView
    private lateinit var messageadapter: Messageadapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var mDbRef: DatabaseReference

    var receiverRoom:String?= null
    var senderRoom:String?=null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val intent = Intent()
        val name = intent.getStringExtra("name")
        val receiveruid= intent.getStringExtra("uid")
        val senderuid= FirebaseAuth.getInstance().currentUser?.uid

        mDbRef=FirebaseDatabase.getInstance().getReference()


        senderRoom = receiveruid + senderuid
        receiverRoom= senderuid+receiveruid

        supportActionBar?.title = name

        messageRecyclerView = findViewById(R.id.charRecycleView)
        messageBox = findViewById(R.id.messageBox)
        sendButton = findViewById(R.id.sentButton)
        messageList= ArrayList()
        messageadapter= Messageadapter(this,messageList)

        messageRecyclerView.layoutManager =LinearLayoutManager(this)
        messageRecyclerView.adapter =messageadapter

        mDbRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for (postSnapshot in snapshot.children)
                    {
                        val message = postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    messageadapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        sendButton.setOnClickListener{
            val message =messageBox.text.toString()
            val messageObject = Message(message,senderuid)

            mDbRef.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    mDbRef.child("chats").child(receiverRoom!!).child("messages").push()
                        .setValue(messageObject)
                }
            messageBox.setText("")

        }

    }
}