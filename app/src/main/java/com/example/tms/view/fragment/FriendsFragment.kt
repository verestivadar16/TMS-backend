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
import com.example.tms.databinding.FriendsListBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class FriendsFragment : Fragment() {
    private lateinit var binding: FriendsListBinding
    private lateinit var friendArrayList: ArrayList<FriendListData>
    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        mAuth = FirebaseAuth.getInstance()
        binding = FriendsListBinding.inflate(layoutInflater)
        binding.backButton.setOnClickListener(View.OnClickListener {
            getActivity()?.onBackPressed()
        })
        binding.addButton.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.action_friends_page_to_add_friend_page)
        })


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerview = getView()?.findViewById<RecyclerView>(R.id.friends_list)
        if (recyclerview != null) {
            recyclerview.layoutManager = LinearLayoutManager(getContext())
        }
        val data = ArrayList<FriendListData>()
        val db = Firebase.firestore

        val docRef = db.collection("users").document(mAuth.currentUser?.uid.toString())

        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    try {
                        val friends = document["friends"] as ArrayList<*>
                        for(it in friends){
                            val uid = it.toString()

                            val docRef2 = db.collection("users").document(it.toString())

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

                                                    setFragmentResult("requestKey", bundleOf(("uid" to result)))
                                                    setFragmentResult("requestKey2", bundleOf(("userID" to result)))

                                                    findNavController().navigate(R.id.action_friends_page_to_chatpage)
                                                }


                                            }

                                        }catch (e:Exception){
                                            Toast.makeText(requireContext(),"Friends cant be loaded!",Toast.LENGTH_SHORT).show()
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

                    }catch (e:Exception){
                        Toast.makeText(requireContext(),"No friends yet!",Toast.LENGTH_SHORT).show()
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