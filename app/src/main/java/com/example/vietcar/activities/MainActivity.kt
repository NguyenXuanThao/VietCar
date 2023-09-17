package com.example.vietcar.activities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.vietcar.R
import com.example.vietcar.common.DataLocal
import com.example.vietcar.databinding.ActivityMainBinding
import com.example.vietcar.ui.home.fragment.HomeFragmentDirections
import com.example.vietcar.ui.payment.fragment.PaymentFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        checkLogin()

        setupBottomBar()

        createNotifyChanel()

        val action = intent.action

        if (action == "OPEN_FRAGMENT_HISTORY") {
            val action = HomeFragmentDirections.actionBottomNavHomeToOrderHistoryFragment()
            navController.navigate(action)
        }

    }

    override fun onStart() {
        super.onStart()

        window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.statusBarColor = Color.TRANSPARENT
    }

    private fun setupBottomBar() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        setupWithNavController(binding.bottomNav, navController)
    }

    private fun createNotifyChanel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val chanel = NotificationChannel(
                PaymentFragment.CHANEL_ID,
                "Order Success",
                NotificationManager.IMPORTANCE_HIGH
            )

            val manager = getSystemService(
                NotificationManager::class.java
            )

            manager.createNotificationChannel(chanel)
        }
    }

//    private fun checkLogin() {
//        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
//        val token = sharedPreferences.getString("token_customer", "")
//
//        DataLocal.STATUS = sharedPreferences.getInt("status_key", 1)
//
//        DataLocal.BEARER_TOKEN = "Bearer $token"
//    }
}