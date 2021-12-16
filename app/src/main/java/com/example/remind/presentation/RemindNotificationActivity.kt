package com.example.remind.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.remind.MainActivity
import com.example.remind.R
import com.example.remind.databinding.RemindNotificationActivityBinding

class RemindNotificationActivity: AppCompatActivity() {

    private lateinit var binding: RemindNotificationActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.remind_notification_activity)

        binding.btnRemindComplete.setOnClickListener {
            startActivity(Intent(this@RemindNotificationActivity, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            })
            finish()
        }
    }

}