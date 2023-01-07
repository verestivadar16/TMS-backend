package com.example.tms.view.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tms.R
import com.example.tms.adapter.MarketAdaptor
import com.example.tms.data.MarketData
import com.example.tms.databinding.ActivityMainBinding.inflate
import com.example.tms.databinding.MarketUploadPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.iceteck.silicompressorr.SiliCompressor
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MarketUploadFragment : Fragment() {
    private lateinit var binding: MarketUploadPageBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var imageUri: Uri

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = MarketUploadPageBinding.inflate(layoutInflater)
        mAuth = FirebaseAuth.getInstance()
        binding.imageButtonBack.setOnClickListener(View.OnClickListener {
            getActivity()?.onBackPressed()
        })
        binding.profileButton.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.action_marketuploadpage_to_profilpage)
        })
        binding.uploadedImage.setOnClickListener(View.OnClickListener {
            selectImage()
        })
        binding.buttonUploadItem.setOnClickListener(View.OnClickListener {
            addProduct()
        })

        return binding.root
    }


    private fun addProduct(){
        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Uploading file ...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val db = Firebase.firestore

        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
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
                                    "price" to binding.itemUpPrice.text.toString(),
                                    "description" to binding.itemUpDescription.text.toString(),
                                    "category" to binding.itemUpCategory.text.toString(),
                                    "name" to binding.itemUpName.text.toString(),
                                    "location" to binding.itemUpLocation.text.toString(),
                                    "image" to imageUri.toString(),
                                    "profileImage" to imageName,
                                    "userName" to username
                            )

                            db.collection("products").document(date)
                                    .set(post)
                                    .addOnSuccessListener { Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!")
                                    }
                                    .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error writing document", e) }

                            val fileName =imageUri.toString()
                            val filePath = SiliCompressor.with(requireContext()).compress(fileName, File.createTempFile("tempImage","jpg"))

                            val storageReference = FirebaseStorage.getInstance().getReference("images/$fileName")

                            storageReference.putFile(filePath.toUri()).addOnSuccessListener {
                                if(progressDialog.isShowing)progressDialog.dismiss()
                                Toast.makeText(requireContext(), "Product uploaded Succsessfully!", Toast.LENGTH_SHORT).show()
                            }.addOnFailureListener {
                                if(progressDialog.isShowing)progressDialog.dismiss()
                            }


                            Log.d(ContentValues.TAG, "DocumentSnapshot data: ${document.data}")


                        }catch (e:Exception){
                            if(progressDialog.isShowing)progressDialog.dismiss()
                            Toast.makeText(requireContext(), "Fill in the fields!$e", Toast.LENGTH_SHORT).show()
                        }


                    } else {
                        Log.d(ContentValues.TAG, "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(ContentValues.TAG, "get failed with ", exception)
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

        if(requestCode == 100 && resultCode == Activity.RESULT_OK){
            imageUri=data?.data!!
            binding.uploadedImage.setImageURI(imageUri)
        }

    }


}