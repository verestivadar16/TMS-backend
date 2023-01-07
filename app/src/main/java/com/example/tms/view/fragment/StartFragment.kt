package com.example.tms.view.fragment

import android.content.ContentValues
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.tms.R
import com.example.tms.databinding.StartPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class StartFragment : Fragment() {
    private lateinit var binding: StartPageBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser

        if (user != null) {
            // User is signed in
            requestUser()
            findNavController().navigate(R.id.action_startFragment_to_InstaFragment)

        }

        binding = StartPageBinding.inflate(layoutInflater)

        binding.loginButton.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.action_startFragment_to_loginFragment)
        })
        binding.registerButton.setOnClickListener {
            findNavController().navigate((R.id.action_startFragment_to_registerFragment))
        }
        return binding.root
    }

    private fun requestUser() {
        val db = Firebase.firestore

        val docRef = db.collection("users").document(mAuth.currentUser?.uid.toString())
        docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val userName = document.getString("userName")!!
                        Toast.makeText(requireContext(), "Logged in as:\n$userName", Toast.LENGTH_SHORT).show()
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