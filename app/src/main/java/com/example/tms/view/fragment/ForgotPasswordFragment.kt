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
import com.example.tms.databinding.ForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordFragment : Fragment() {
    private lateinit var binding: ForgotPasswordBinding
    private lateinit var mAuth: FirebaseAuth
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        mAuth = FirebaseAuth.getInstance()
        binding = ForgotPasswordBinding.inflate(layoutInflater)
        binding.backButton.setOnClickListener(View.OnClickListener {
            getActivity()?.onBackPressed()
        })

        val emailAddress = binding.editTextForgotPassword.text

        binding.sendButton.setOnClickListener(View.OnClickListener {

            mAuth.sendPasswordResetEmail(emailAddress.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "Email sent.")
                            Toast.makeText(requireContext(), "Email sent!", Toast.LENGTH_SHORT).show()
                            Toast.makeText(requireContext(), "Check Spam", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.action_forgotPage_to_loginPage)
                        }
                    }
        })

        return binding.root
    }
}