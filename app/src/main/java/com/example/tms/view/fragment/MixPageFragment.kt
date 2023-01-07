package com.example.tms.view.fragment

import android.content.ContentValues
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tms.R
import com.example.tms.adapter.MixPageAdapter
import com.example.tms.data.MixPageData
import com.example.tms.databinding.MixPageBinding
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import com.example.tms.adapter.InstaAdaptor
import com.example.tms.adapter.MarketAdaptor
import com.example.tms.data.ContentConstants
import com.example.tms.data.InstaPostData
import com.example.tms.data.MarketData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class MixPageFragment : Fragment() {
    private lateinit var binding: MixPageBinding
    private lateinit var mAuth :FirebaseAuth
    private lateinit var data : ArrayList<MixPageData>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MixPageBinding.inflate(layoutInflater)
        mAuth = FirebaseAuth.getInstance()

        requestUser()

        binding.imageButtonBack.setOnClickListener(View.OnClickListener {
            getActivity()?.onBackPressed()
        })
        binding.inboxButton.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.action_mix_page_to_inboxpage)
        })
        binding.profileButton.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.action_mix_page_to_profilepage)
        })
        binding.searchButton.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.action_mix_page_to_mix_content_page)
        })
        return binding.root
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)

        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        var default = sharedPref.getBoolean("switchValue", true)
        val default2 = sharedPref.getBoolean("switchValue2", true)
        val default3 = sharedPref.getBoolean("switchValue3", true)
        val default4 = sharedPref.getBoolean("switchValue4", true)
        val editor = sharedPref.edit()

        loadLayout(default.toString(), default2.toString(), default3.toString(), default4.toString())

        setFragmentResultListener("switches") { key, bundle ->
            // Any type can be passed via to the bundle
            var response = bundle.getString("switch1")
            var response2 = bundle.getString("switch2")
            var response3 = bundle.getString("switch3")
            var response4 = bundle.getString("switch4")

            // Do something with the result...
            editor.putBoolean("switchValue", response.toBoolean()).apply()
            editor.putBoolean("switchValue2", response2.toBoolean()).apply()
            editor.putBoolean("switchValue3", response3.toBoolean()).apply()
            editor.putBoolean("switchValue4", response4.toBoolean()).apply()
            loadLayout(response!!, response2!!, response3!!, response4!!)
        }
    }

    private fun loadLayout(response : String, response2 : String, response3 : String, response4 : String)
    {
        val recyclerview = getView()?.findViewById<RecyclerView>(R.id.post_list)

        val bitmap0 = BitmapFactory.decodeResource(getResources(), R.drawable.golfr32);
        val bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.eventimage1);
        val bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.warningimage1);

        if (recyclerview != null) {
            recyclerview.layoutManager = LinearLayoutManager(getContext())
        }

        val data = ArrayList<MixPageData>()
        val db = Firebase.firestore


        db.collection("posts")
            .get()
            .addOnSuccessListener { users ->
                var count = 0
                data.add(MixPageData(R.drawable.avatar_button, "Pasiunea ne uneste","Va invitam sambata 25.01.2023 incepand cu ora 13:00, locatie:..",bitmap1, "3",response3))
                for (snapshot in users) {
                    if(count<2) {
                        val name = snapshot.getString("name")!!
                        val imageName = snapshot.getString("image")!!
                        val description = snapshot.getString("description")!!
                        val profileImage = snapshot.getString("profileImage")!!
                        val storageRef =
                            FirebaseStorage.getInstance().reference.child("images/$imageName")
                        val localFile = File.createTempFile("tempImage", "jpg")
                        storageRef.getFile(localFile).addOnSuccessListener {
                            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)

                            val storageRef2 =
                                FirebaseStorage.getInstance().reference.child("images/$profileImage")
                            val localFile2 = File.createTempFile("tempImage", "jpg")
                            storageRef2.getFile(localFile2).addOnSuccessListener {
                                val bitmap2 = BitmapFactory.decodeFile(localFile2.absolutePath)

                                val post =
                                    MixPageData(name, description, bitmap, bitmap2, "1", response)
                                data.add(post)
                                val adapter = context?.let { MixPageAdapter(it, data) }
                                if (recyclerview != null) {
                                    recyclerview.adapter = adapter
                                }

//                            binding.listview.adapter = activity?.let { InstaAdaptor(it, postArrayList) }

                            }

                        }
                        count++
                    }
                }
            }
            .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Transaction failure.", e) }


        db.collection("products")
            .get()
            .addOnSuccessListener { users ->
                var count=0

                    for (snapshot in users) {

                        if (count <= 2) {

                            val userName = snapshot.getString("userName")!!
                            val userID = snapshot.getString("uid")!!
                            val profileImage = snapshot.getString("profileImage")!!
                            val imageName = snapshot.getString("image")!!
                            val description = snapshot.getString("description")!!
                            val price = snapshot.getString("price")!!
                            val productName = snapshot.getString("name")!!
                            val location = snapshot.getString("location")!!
                            val category = snapshot.getString("category")!!

                            val storageRef =
                                FirebaseStorage.getInstance().reference.child("images/$imageName")
                            val localFile = File.createTempFile("tempImage", "jpg")
                            storageRef.getFile(localFile).addOnSuccessListener {
                                val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)

                                val storageRef2 =
                                    FirebaseStorage.getInstance().reference.child("images/$profileImage")
                                val localFile2 = File.createTempFile("tempImage", "jpg")
                                storageRef2.getFile(localFile2).addOnSuccessListener {
                                    val bitmapProfile =
                                        BitmapFactory.decodeFile(localFile2.absolutePath)

                                    val product = MixPageData(
                                        bitmap,
                                        bitmapProfile,
                                        userName,
                                        productName,
                                        description,
                                        price,
                                        "2",
                                        response2
                                    )
                                    data.add(product)
                                    val adapter = context?.let { MixPageAdapter(it, data) }
                                    if (recyclerview != null) {
                                        recyclerview.adapter = adapter
                                    }


                                }

                            }
                            count++
                        }
                    }

                data.add(MixPageData(R.drawable.avatar_button, "Pasiunea ne uneste","Va invitam sambata 15.10.2022 incepand cu ora 13:00, locatie:..",bitmap1, "3",response3))
                data.add(MixPageData(R.drawable.avatar_button, "Traffic Jam Warning!",bitmap2, "4",response4))

            }
            .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Transaction failure.", e) }



//        data.add(MixPageData( R.drawable.tire,
//            R.drawable.avatar,
//            "Lakatos Brendon",
//            "Anvelope 195X65XR18",
//            "Nearly new bought them last year...",
//            "230 LEI",
//            "2",response2))

//        data.add(MixPageData("Tamas","got my new car",bitmap0, R.drawable.avatar_button, "1",response))

//        data.add(MixPageData("Tamas","got my new car",bitmap0, R.drawable.avatar_button, "1",response))
//        data.add(MixPageData(R.drawable.tire,
//            R.drawable.avatar_button,
//            "Kiss Elemer ",
//            "Anvelope 195X65XR18",
//            "Nearly new bought them last year...",
//            "230 LEI",
//            "2",response2))
        val bitmap = BitmapFactory.decodeResource(getResources(),
            R.drawable.audib5);



//        data.add(MixPageData("Tamas","got my new car",bitmap, R.drawable.avatar_button, "1",response))
//        data.add(MixPageData("Balazs","got my new car",bitmap, R.drawable.avatar_button, "1",response))

        val adapter = context?.let { MixPageAdapter(it, data) }
        if (recyclerview != null) {
            recyclerview.adapter = adapter
        }
        if (recyclerview != null) {
            recyclerview.adapter = adapter
        }
    }

    private fun requestPosts() {


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