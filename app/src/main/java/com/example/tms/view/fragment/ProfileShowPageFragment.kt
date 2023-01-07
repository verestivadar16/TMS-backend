package com.example.tms.view.fragment

import android.content.ContentValues
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tms.R
import com.example.tms.adapter.InstaAdaptor
import com.example.tms.data.InstaPostData
import com.example.tms.databinding.ProfilePageBinding
import com.example.tms.databinding.ProfileShowPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class ProfileShowPageFragment : Fragment() {
    private lateinit var binding: ProfileShowPageBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var imageUri: Uri
    private lateinit var postArrayList: ArrayList<InstaPostData>
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = ProfileShowPageBinding.inflate(layoutInflater)
        mAuth = FirebaseAuth.getInstance()
        binding.backButton.setOnClickListener(View.OnClickListener {
            getActivity()?.onBackPressed()
        })
        binding.friendsButton.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.action_profile_show_page_to_friends_page)
        })
        binding.editProfileButton.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.action_profile_show_page_to_profile_page)
        })

        postArrayList = ArrayList()

        requestUser()
        requestPosts()

        return binding.root
    }

    private fun requestUser() {
        val db = Firebase.firestore

        val docRef = db.collection("users").document(mAuth.currentUser?.uid.toString())
        docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val userName = document.getString("userName")!!
                        val imageName = document.getString("profileImage")!!
                        val storageRef = FirebaseStorage.getInstance().reference.child("images/$imageName")
                        val localFile = File.createTempFile("tempImage", "jpg")
                        storageRef.getFile(localFile).addOnSuccessListener {
                            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                            binding.profileImage.setImageBitmap(bitmap)
                            binding.username.hint = userName
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

    private fun requestPosts() {
        val db = Firebase.firestore
        db.collection("posts")
                .whereEqualTo("uid", mAuth.currentUser?.uid)
                .get()
                .addOnSuccessListener { posts ->
                    for (snapshot in posts) {
                        val name = snapshot.getString("name")!!
                        val imageName = snapshot.getString("image")!!
                        val description = snapshot.getString("description")!!
                        val profileImage = snapshot.getString("profileImage")!!
                        val storageRef = FirebaseStorage.getInstance().reference.child("images/$imageName")
                        val localFile = File.createTempFile("tempImage", "jpg")
                        storageRef.getFile(localFile).addOnSuccessListener {
                            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)

                            val storageRef2 = FirebaseStorage.getInstance().reference.child("images/$profileImage")
                            val localFile2 = File.createTempFile("tempImage", "jpg")
                            storageRef2.getFile(localFile2).addOnSuccessListener {
                                val bitmap2 = BitmapFactory.decodeFile(localFile2.absolutePath)

                                val post = InstaPostData(name, description, bitmap, bitmap2)
                                postArrayList.add(post)
                                binding.listview.adapter = activity?.let { InstaAdaptor(it, postArrayList) }
                            }
                        }



                }
            }
            .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Transaction failure.", e) }


    }

}