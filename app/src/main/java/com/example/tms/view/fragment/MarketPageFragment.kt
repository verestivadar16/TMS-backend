package com.example.tms.view.fragment

import android.content.ContentValues
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tms.R
import com.example.tms.adapter.MarketAdaptor
import com.example.tms.data.MarketData
import com.example.tms.databinding.MarketPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.File


class MarketPageFragment : Fragment() {

    private lateinit var binding: MarketPageBinding
    private lateinit var mAuth: FirebaseAuth


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        binding = MarketPageBinding.inflate(layoutInflater)
        binding.profileButton.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.action_marketpage_to_profilpage)
        })
        binding.imageButtonBack.setOnClickListener(View.OnClickListener {
            getActivity()?.onBackPressed()
        })
        binding.searchButton.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.action_marketpage_to_searchitempage)
        })
        binding.newItemButton.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.action_marketpage_to_newlistingpage)
        })


        return binding.root
    }


    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)
        val recyclerview = getView()?.findViewById<RecyclerView>(R.id.market_items_list)
        if (recyclerview != null) {
            recyclerview.layoutManager = LinearLayoutManager(getContext())
        }
        val data = ArrayList<MarketData>()
        val db = Firebase.firestore
        requestUser()

        db.collection("products")
                .get()
                .addOnSuccessListener { users ->
                    for (snapshot in users) {
                        val userName = snapshot.getString("userName")!!
                        val userID = snapshot.getString("uid")!!
                        val profileImage = snapshot.getString("profileImage")!!
                        val imageName = snapshot.getString("image")!!
                        val description = snapshot.getString("description")!!
                        val price = snapshot.getString("price")!!
                        val productName = snapshot.getString("name")!!
                        val location = snapshot.getString("location")!!
                        val category = snapshot.getString("category")!!


                        val storageRef = FirebaseStorage.getInstance().reference.child("images/$imageName")
                        val localFile = File.createTempFile("tempImage", "jpg")
                        storageRef.getFile(localFile).addOnSuccessListener {
                            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)

                            val storageRef2 = FirebaseStorage.getInstance().reference.child("images/$profileImage")
                            val localFile2 = File.createTempFile("tempImage", "jpg")
                            storageRef2.getFile(localFile2).addOnSuccessListener {
                                val bitmapProfile = BitmapFactory.decodeFile(localFile2.absolutePath)

                                val product = MarketData(bitmapProfile, userName, userID, bitmap, productName, description, location, price)
                                data.add(product)
                                val adapter = MarketAdaptor(data)
                                if (recyclerview != null) {
                                    recyclerview.adapter = adapter
                                }
                                if (recyclerview != null) {
                                    recyclerview.adapter = adapter
                                }
                                adapter.onItemClick = {

                                    val result = it.sellerUid
                                    setFragmentResult("requestKey", bundleOf(("uid" to result)))
                                    setFragmentResult("requestKey2", bundleOf(("userID" to result)))


                                    findNavController().navigate(R.id.action_marketpage_to_chatpage)
                                }

                            }

                        }

                    }
                }
                .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Transaction failure.", e) }


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