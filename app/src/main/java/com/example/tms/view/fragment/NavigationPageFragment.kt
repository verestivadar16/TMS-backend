package com.example.tms.view.fragment

import android.Manifest
import android.app.AlertDialog
import android.content.ContentValues
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tms.R
import com.example.tms.databinding.NavigationPageBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import kotlin.system.exitProcess


//AIzaSyCXPxzAnc8icMuBAYqWnbtw5S2eaT5opMg

class NavigationPageFragment : Fragment(), OnMapReadyCallback {
    //    private lateinit var REQUEST_LOCATION
    private lateinit var binding: NavigationPageBinding
    private lateinit var mMap: GoogleMap
    private lateinit var locationManager: LocationManager
    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = NavigationPageBinding.inflate(layoutInflater)
        mAuth = FirebaseAuth.getInstance()
        val alertDialog: AlertDialog
        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {

            } else {

                val builder = AlertDialog.Builder(context)
                builder.setTitle("You cant access the maps")
                builder.setMessage("You have to allow your location.")

                builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                    Toast.makeText(
                        context,
                        android.R.string.yes, Toast.LENGTH_SHORT
                    ).show()
                    Manifest.permission.ACCESS_FINE_LOCATION
                }

                builder.setNegativeButton(android.R.string.no) { dialog, which ->
                    Toast.makeText(
                        context,
                        android.R.string.no, Toast.LENGTH_SHORT
                    ).show()
                    exitProcess(1)
                }
                builder.show()

            }
        }

        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        requestUser()


//        DirectionAPI.openGoogleMapsNavigationFromAToB(this, -34.0, 151.0, -30.0, 150.0)

        binding.addEventButton.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.action_navigation_to_ad_event_page)
        })
        binding.navigationAvatar.setOnClickListener({
            findNavController().navigate(R.id.action_navigation_to_profile_page)
        })
//        onMapReady(mMap)
        return binding.root
    }


    override fun onMapReady(googleMap: GoogleMap) {
        var mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(
            MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney")
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    private fun requestUser() {
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
                        binding.navigationAvatar.setImageBitmap(bitmap)
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

















