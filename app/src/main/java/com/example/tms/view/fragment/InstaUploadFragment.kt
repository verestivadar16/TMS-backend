package com.example.tms.view.fragment

import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
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
import com.example.tms.databinding.InstaUploadImageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.iceteck.silicompressorr.FileUtils.getPath
import com.iceteck.silicompressorr.SiliCompressor
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class InstaUploadFragment : Fragment() {
    private lateinit var binding: InstaUploadImageBinding
    private lateinit var imageUri: Uri
    private lateinit var mAuth : FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = InstaUploadImageBinding.inflate(layoutInflater)
        mAuth= FirebaseAuth.getInstance()

        binding.imageButtonBack.setOnClickListener(View.OnClickListener {
            getActivity()?.onBackPressed()
        })
        binding.buttonChooseImage.setOnClickListener(View.OnClickListener{

            selectImage()

        })

        binding.buttonUpload.setOnClickListener(){

            uploadImage()

        }
        return binding.root


    }


    private fun uploadImage() {

        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Uploading file ...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val db = Firebase.firestore

        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss",Locale.getDefault())
        val now = Date()
        val date = formatter.format(now)

        val docRef = db.collection("users").document(mAuth.currentUser?.uid.toString())
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val username = document.getString("userName")!!
                    val imageName = document.getString("profileImage")!!

                    try {

                        val post = hashMapOf(
                            "uid" to mAuth.currentUser?.uid,
                            "name" to username,
                            "description" to binding.editTextDescription.text.toString(),
                            "image" to imageUri.toString(),
                            "profileImage" to imageName
                        )

                        db.collection("posts").document(date)
                            .set(post)
                            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!")
                            }
                            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }

                        val fileName =imageUri.toString()
                        val filePath = SiliCompressor.with(requireContext()).compress(fileName, File.createTempFile("tempImage","jpg"))

                        val storageReference = FirebaseStorage.getInstance().getReference("images/$fileName")

                        storageReference.putFile(filePath.toUri()).addOnSuccessListener {
                            if(progressDialog.isShowing)progressDialog.dismiss()
                            findNavController().navigate(R.id.action_instauploadpage_to_instapostpage)
                        }.addOnFailureListener {
                            if(progressDialog.isShowing)progressDialog.dismiss()
                        }


                        Log.d(TAG, "DocumentSnapshot data: ${document.data}")


                    }catch (e:Exception){
                        if(progressDialog.isShowing)progressDialog.dismiss()
                        Toast.makeText(requireContext(), "Fill in the fields!", Toast.LENGTH_SHORT).show()
                    }


                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
// Add a new document with a generated ID


    }
    private fun selectImage() {

        val intent = Intent()
        intent.type= "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent,100)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 100 && resultCode == RESULT_OK){
            imageUri=data?.data!!
            binding.uploadedImage.setImageURI(imageUri)
        }

    }
}




