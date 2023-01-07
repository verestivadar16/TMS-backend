package com.example.tms.view.fragment

import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tms.R
import com.example.tms.databinding.RegistrationPageBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.iceteck.silicompressorr.SiliCompressor
import java.io.File

class RegisterFragment : Fragment() {
    private lateinit var binding: RegistrationPageBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        mAuth = FirebaseAuth.getInstance()
        binding = RegistrationPageBinding.inflate(layoutInflater)

        val currentUser = mAuth.currentUser
        if (currentUser != null) {

        }
        register()

        binding.imageButtonBack.setOnClickListener(View.OnClickListener {
            getActivity()?.onBackPressed()
        })

        return binding.root
    }


    private fun register() {
        binding.buttonRegister.setOnClickListener(View.OnClickListener {
            if (binding.editTextEmail.text.toString()
                            .isEmpty() || binding.editTextPassword.text.toString().isEmpty()
            ) {
                Toast.makeText(requireContext(), "Fill in the fields!", Toast.LENGTH_SHORT).show()
            } else {
                if (binding.editTextPassword.text.toString() != binding.editTextRePassword.text.toString()) {
                    Toast.makeText(requireContext(), "Passwords do not match!", Toast.LENGTH_SHORT).show()
                } else {
                    signUpUser(
                            binding.editTextEmail.text.toString(),
                            binding.editTextPassword.text.toString()
                    )
                }
            }

        })
    }

    private fun signUpUser(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        Toast.makeText(requireContext(), "Register succeeded.",
                                Toast.LENGTH_SHORT).show()
                        updateProfile()
                        //updateUI(user)
                        findNavController().navigate(R.id.action_registerPage_to_startPage)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(requireContext(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                        // updateUI(null)
                    }
                }

    }

    private fun updateProfile() {

        val db = Firebase.firestore

// Add a new document with a generated ID
        val post = hashMapOf(
                "userName" to binding.editTextUsername.text.toString(),
                "friends" to ArrayList<String>(),
                "profileImage" to "content://com.android.providers.media.documents/document/image%3A1000000103"
        )

        db.collection("users").document(mAuth.currentUser?.uid.toString())
                .set(post)
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot successfully written!")
                }
                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }

    }


}

