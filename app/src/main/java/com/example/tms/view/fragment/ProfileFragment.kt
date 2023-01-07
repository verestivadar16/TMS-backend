package com.example.tms.view.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tms.R
import com.example.tms.databinding.ProfilePageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.iceteck.silicompressorr.SiliCompressor
import java.io.ByteArrayOutputStream
import java.io.File


class ProfileFragment : Fragment() {
    private lateinit var binding: ProfilePageBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var imageUri: Uri
    private lateinit var friends : ArrayList<*>
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = ProfilePageBinding.inflate(layoutInflater)
        mAuth = FirebaseAuth.getInstance()
        binding.backButton.setOnClickListener(View.OnClickListener {
            getActivity()?.onBackPressed()
        })
        binding.friendsButton.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.action_profile_page_to_friends_page)
        })
        binding.logOut.setOnClickListener(View.OnClickListener {
            mAuth.signOut()
            findNavController().navigate(R.id.action_profile_page_to_start_page)
        })


        requestUser()

        binding.updateProfile.setOnClickListener(View.OnClickListener {
            try {
                updateProfile()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "$e", Toast.LENGTH_SHORT).show()
            }
        })

        binding.profileImage.setOnClickListener(View.OnClickListener {
            selectImage()
        })

        binding.takePhoto.setOnClickListener(View.OnClickListener {
            takePhoto()
        })

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


                        try{
                            friends = document["friends"] as ArrayList<*>

                        }catch (e:Exception){

                        }



                        val storageRef = FirebaseStorage.getInstance().reference.child("images/$imageName")
                        val localFile = File.createTempFile("tempImage", "jpg")
                        storageRef.getFile(localFile).addOnSuccessListener {
                            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                            binding.profileImage.setImageBitmap(bitmap)
                            binding.username.hint = userName
                        }
                        Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    } else {
                        Log.d(TAG, "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "get failed with ", exception)
                }

    }


    private fun updateProfile() {

        val db = Firebase.firestore

        var username =  binding.username.hint.toString()

        if(binding.username.text.isNotEmpty()){
             username = binding.username.text.toString()
        }

        val post = hashMapOf(
            "userName" to username,
            "profileImage" to imageUri.toString(),
            "friends" to friends
        )


        db.collection("users").document(mAuth.currentUser?.uid.toString())
                .set(post)
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot successfully written!")
                }
                .addOnFailureListener {
                        e -> Log.w(TAG, "Error writing document", e) }

        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Updating profile ...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val fileName = imageUri.toString()

        val storageReference = FirebaseStorage.getInstance().getReference("images/$fileName")

        val filePath = SiliCompressor.with(requireContext()).compress(fileName, File.createTempFile("tempImage", "jpg"))

        storageReference.putFile(filePath.toUri()).addOnSuccessListener {
            if (progressDialog.isShowing) progressDialog.dismiss()
        }.addOnFailureListener {
            if (progressDialog.isShowing) progressDialog.dismiss()
        }

    }



    private fun selectImage() {

        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent, 100)

    }

    private fun takePhoto() {
        val intent = Intent()

        intent.action = MediaStore.ACTION_IMAGE_CAPTURE
        startActivityForResult(intent, 150)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data!!
            binding.profileImage.setImageURI(imageUri)
        }

        if (requestCode == 150 && resultCode == Activity.RESULT_OK) {

            val bitmap = data?.extras?.get("data") as Bitmap

            imageUri = getImageUri(requireContext(), bitmap)!!
            binding.profileImage.setImageBitmap(bitmap)
        }
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
                inContext.getContentResolver(),
                inImage,
                "Temp",
                null
        )
        return Uri.parse(path)
    }

}