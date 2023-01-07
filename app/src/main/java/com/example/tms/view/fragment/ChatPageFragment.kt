package com.example.tms.view.fragment

import android.content.ContentValues
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tms.R
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tms.databinding.ChatInputBinding
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.setFragmentResultListener
import com.example.tms.adapter.MessageAdapter
import com.example.tms.data.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.File


class ChatPageFragment : Fragment() {
    private lateinit var binding: ChatInputBinding
    private lateinit var mAuth : FirebaseAuth
    private lateinit var receiverUid : String
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageBox : EditText
    private lateinit var sendButton: ImageView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var mDbRef : DatabaseReference

    var receiverRoom : String? = null
    var senderRoom : String? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        binding = ChatInputBinding.inflate(layoutInflater)
        binding.imageButtonBack.setOnClickListener(View.OnClickListener {
            getActivity()?.onBackPressed()
        })


        return binding.root
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)
        val recyclerview = getView()?.findViewById<RecyclerView>(R.id.message_list)
        if (recyclerview != null) {
            recyclerview.layoutManager = LinearLayoutManager(getContext())
        }

        mAuth = FirebaseAuth.getInstance()

        requestUser()

        setFragmentResultListener("requestKey2") { key, bundle ->
            // Any type can be passed via to the bundle
            receiverUid = bundle.getString("userID")!!

            // Do something with the result...

            val senderUid = mAuth.currentUser?.uid.toString()
            chatRecyclerView = binding.messageList
            messageList = ArrayList()
            senderRoom = receiverUid + senderUid
            receiverRoom = senderUid + receiverUid

            messageAdapter = MessageAdapter(requireContext(),messageList)

            chatRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            chatRecyclerView.adapter = messageAdapter

            mDbRef = FirebaseDatabase.getInstance().getReference()

            //logic for adding data to recyclerview
            mDbRef.child("chats").child(senderRoom!!).child("messages")
                .addValueEventListener(object: ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {

                        messageList.clear()

                        for(postSnapshot in snapshot.children){

                            val message = postSnapshot.getValue(Message::class.java)
                            messageList.add(message!!)

                        }
                        messageAdapter.notifyDataSetChanged()

                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })


            // adding the message to database
            binding.chatSendMsg.setOnClickListener() {
                val message = binding.inputMessage.text.toString()

                val messageObject = Message(message,senderUid)

                mDbRef.child("chats").child(senderRoom!!).child("messages").push()
                    .setValue(messageObject).addOnSuccessListener {
                        mDbRef.child("chats").child(receiverRoom!!).child("messages").push()
                            .setValue(messageObject)
                    }


                binding.inputMessage.text?.clear()

            }

        }


    }

    private fun requestUser() {
        val db = Firebase.firestore

        setFragmentResultListener("requestKey") { key, bundle ->
            // Any type can be passed via to the bundle
            receiverUid = bundle.getString("uid")!!
            // Do something with the result...
            val docRef = db.collection("users").document(receiverUid)
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val userName = document.getString("userName")!!
                        val imageName = document.getString("profileImage")!!
                        val storageRef = FirebaseStorage.getInstance().reference.child("images/$imageName")
                        val localFile = File.createTempFile("tempImage", "jpg")
                        storageRef.getFile(localFile).addOnSuccessListener {
                            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                            binding.name.text=userName
                            binding.userImage.setImageBitmap(bitmap)
                        }
                        Log.d(ContentValues.TAG, "DocumentSnapshot data: ${document.data}")
                    } else {
                        Log.d(ContentValues.TAG, "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(ContentValues.TAG, "get failed with ", exception)
                }

        }


    }
}