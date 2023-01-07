package com.example.tms.view.fragment

import android.content.ContentValues
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tms.R
import com.example.tms.adapter.FriendListAdapter
import com.example.tms.data.FriendListData
import com.example.tms.databinding.AddEventPageBinding
import com.example.tms.databinding.AddFriendPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class AddFriendFragment : Fragment() {
    private lateinit var binding: AddFriendPageBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var friends : ArrayList<*>

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = AddFriendPageBinding.inflate(layoutInflater)
        mAuth = FirebaseAuth.getInstance()

        binding.backButton.setOnClickListener(View.OnClickListener {
            getActivity()?.onBackPressed()
        })
        binding.confirmButton.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.friend_list_page)
        })

        binding.confirmButton.setOnClickListener(View.OnClickListener {
            addFriend()
        })
        return binding.root
    }

    private fun addFriend(){

        val db = Firebase.firestore

        val docRef = db.collection("users").document(mAuth.currentUser?.uid.toString())
        docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val userName = document.getString("userName")!!
                        val imageName = document.getString("profileImage")!!
                        try {
                            friends = document["friends"] as ArrayList<*>
                        }catch (e:Exception){
                            Toast.makeText(requireContext(),"$e",Toast.LENGTH_SHORT).show()
                        }


                        var newfriend =  binding.editTextUsername.hint.toString()

                        if(binding.editTextUsername.text.isNotEmpty()){
                            newfriend = binding.editTextUsername.text.toString()
                        }

                        val newFriendList : ArrayList<String> = ArrayList()

                        try {
                            for(it in friends){
                                newFriendList.add(it.toString())
                            }
                        }catch (e:Exception){
                            Toast.makeText(requireContext(),"$e",Toast.LENGTH_SHORT).show()
                        }

                        newFriendList.add(newfriend)

                        val post = hashMapOf(
                                "friends" to newFriendList,
                                "userName" to userName,
                                "profileImage" to imageName
                        )
                        db.collection("users").document(mAuth.currentUser?.uid.toString())
                                .set(post)
                                .addOnSuccessListener {
                                    Toast.makeText(requireContext(), "Friend added successfully!", Toast.LENGTH_SHORT).show()
                                    Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!")
                                }
                                .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error writing document", e) }

                        Log.d(ContentValues.TAG, "DocumentSnapshot data: ${document.data}")
                    } else {
                        Log.d(ContentValues.TAG, "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(ContentValues.TAG, "get failed with ", exception)
                }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerview = getView()?.findViewById<RecyclerView>(R.id.add_friends_list)
        if (recyclerview != null) {
            recyclerview.layoutManager = LinearLayoutManager(getContext())
        }
        val data = ArrayList<FriendListData>()
        val db = Firebase.firestore

        val docRef = db.collection("users")

        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    try {
                        for(it in document){
                            val uid = it.id

                            val docRef2 = db.collection("users").document(uid)

                            docRef2.get()
                                .addOnSuccessListener { document2 ->
                                    if (document2 != null) {
                                        try {
                                            val userName = document2.getString("userName")!!
                                            val imageName = document2.getString("profileImage")!!
                                            val storageRef = FirebaseStorage.getInstance().reference.child("images/$imageName")
                                            val localFile = File.createTempFile("tempImage", "jpg")

                                            storageRef.getFile(localFile).addOnSuccessListener {
                                                val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)

                                                val contact = FriendListData(uid,userName, bitmap)
                                                data.add(contact)
                                                val adapter = FriendListAdapter(data)
                                                if (recyclerview != null) {
                                                    recyclerview.adapter = adapter
                                                }
                                                if (recyclerview != null) {
                                                    recyclerview.adapter = adapter
                                                }


                                                adapter.onItemClick = { it2 ->

                                                    val result = it2.uid

                                                    binding.editTextUsername.hint = result
                                                }


                                            }

                                        }catch (e:Exception){
                                            Toast.makeText(requireContext(),"Friends cant be loaded!",Toast.LENGTH_SHORT).show()
                                        }

                                        Log.d(ContentValues.TAG, "DocumentSnapshot data: ${document}")
                                    } else {
                                        Log.d(ContentValues.TAG, "No such document")
                                    }
                                }
                                .addOnFailureListener { exception ->
                                    Log.d(ContentValues.TAG, "get failed with ", exception)
                                }

                        }

                    }catch (e:Exception){
                        Toast.makeText(requireContext(),"No users found!",Toast.LENGTH_SHORT).show()
                    }


                    Log.d(ContentValues.TAG, "DocumentSnapshot data: ${document}")
                } else {
                    Log.d(ContentValues.TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "get failed with ", exception)
            }



    }

}