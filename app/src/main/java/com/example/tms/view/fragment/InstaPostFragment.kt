package com.example.tms.view.fragment

import android.app.Activity
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.example.tms.data.Posts
import com.example.tms.databinding.InstaPostBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.File


class InstaPostFragment : Fragment() {
    private lateinit var binding: InstaPostBinding
    private lateinit var postArrayList: ArrayList<InstaPostData>
    private lateinit var mAuth: FirebaseAuth

    lateinit var rootView: View
    lateinit var mContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = InstaPostBinding.inflate(layoutInflater)
        mAuth = FirebaseAuth.getInstance()

        binding.imageButtonBack.setOnClickListener(View.OnClickListener {
            getActivity()?.onBackPressed()
        })
        binding.imageButtonToUploadImage.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.action_instapostpage_to_instauploadpage)
        })
        binding.inboxButton.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.action_instapostpage_to_inboxpage)
        })

        binding.profileButton.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.action_instapostpage_to_profile_page)
        })

        binding.searchButton.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.action_instapostpage_to_mix_page)
        })

        postArrayList = ArrayList()

        requestUser()
        requestPosts()


        return binding.root
    }

    private fun requestPosts() {
        val db = Firebase.firestore
        db.collection("posts")
                .get()
                .addOnSuccessListener { users ->
                    for (snapshot in users) {
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
                .addOnFailureListener { e -> Log.w(TAG, "Transaction failure.", e) }

    }

    private fun requestUser() {
        mAuth = FirebaseAuth.getInstance()

        val db = Firebase.firestore

        val docRef = db.collection("users").document(mAuth.currentUser?.uid.toString())
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val imageName = document.getString("profileImage")!!

                    val storageRef = FirebaseStorage.getInstance().reference.child("images/$imageName")
                    val localFile = File.createTempFile("tempImage", "jpg")
                    storageRef.getFile(localFile).addOnSuccessListener {
                        val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                        binding.profileButton.setImageBitmap(bitmap)
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