package com.example.tms.view.fragment

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tms.R
import com.example.tms.databinding.LoginPageBinding
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {
    private lateinit var binding: LoginPageBinding
    private lateinit var mAuth: FirebaseAuth
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = LoginPageBinding.inflate(layoutInflater)
        mAuth = FirebaseAuth.getInstance()

        login()

        binding.imageButtonBack.setOnClickListener(View.OnClickListener {
            getActivity()?.onBackPressed()
        })

        binding.forgotPasswordButton.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.action_loginPage_to_forgot_password_page)
        })
        return binding.root
    }

    private fun login() {
        binding.loginButton.setOnClickListener(View.OnClickListener {
            if (binding.editTextEmail.text.toString().isEmpty() ||
                    binding.editTextPassword.text.toString().isEmpty()
            ) {
                Toast.makeText(requireContext(), "Fill in the fields!", Toast.LENGTH_SHORT).show()
            } else {
                signInUser(
                        binding.editTextEmail.text.toString(),
                        binding.editTextPassword.text.toString()
                )
            }
        })
    }

    private fun signInUser(email: String, password: String) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        Toast.makeText(requireContext(), "Authentication successful.",
                                Toast.LENGTH_SHORT).show()
                        //val user = mAuth.currentUser
                        findNavController().navigate(R.id.action_loginPage_to_insta_page)
                        //updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(requireContext(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                        //updateUI(null)
                    }
                }
    }
}