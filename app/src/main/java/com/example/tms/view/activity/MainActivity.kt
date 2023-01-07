package com.example.tms.view.activity


import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.marginBottom
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.RecyclerView
import com.example.tms.R
import com.example.tms.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    //    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val navController =
                Navigation.findNavController(this, R.id.nav_host_fragment) as NavController
        val bottomNavigationView = binding.bottomNavigationView as BottomNavigationView
        NavigationUI.setupWithNavController(bottomNavigationView, navController)


        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginPage -> hideBottomNav()
                R.id.startPage -> hideBottomNav()
                R.id.registerPage -> hideBottomNav()
                R.id.forgot_password -> hideBottomNav()
                R.id.chat_page -> hideBottomNav()
                else -> {
                    showBottomNav()

                }
            }
        }


    }

    private fun showBottomNav() {
        binding.bottomNavigationView.visibility = View.VISIBLE

    }

    private fun hideBottomNav() {
        binding.bottomNavigationView.visibility = View.GONE
    }

}
