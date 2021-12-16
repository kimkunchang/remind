package com.example.remind

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.remind.databinding.RemindMainActivityBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: RemindMainActivityBinding

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.remind_main_activity)

        navController = Navigation.findNavController(this, R.id.main_container)
    }

    override fun onResume() {
        super.onResume()
    }
}