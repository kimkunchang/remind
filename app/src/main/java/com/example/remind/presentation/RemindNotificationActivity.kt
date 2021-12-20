package com.example.remind.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.remind.MainActivity
import com.example.remind.R
import com.example.remind.databinding.RemindNotificationActivityBinding
import com.example.remind.model.MainViewModel
import com.example.remind.utils.Constant

class RemindNotificationActivity: AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: RemindNotificationActivityBinding

    private var alarmID: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.remind_notification_activity)

        intent.extras?.let {
            alarmID = it.getInt(Constant.KEY_REMIND_ALARM_ID)
        }

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.getRemindInfo(alarmID) { entity ->
            binding.entity = entity
        }

        binding.btnRemindComplete.setOnClickListener {
            completeRemindAlarm()
        }
    }

    private fun completeRemindAlarm(){
        Log.w("KKC_TAG", "completeRemindAlarm -> alarmID : $alarmID")

        viewModel.getRemindInfo(alarmID) { entity ->
            entity.cancelAlarm(this@RemindNotificationActivity)
            viewModel.updateRemindInfo(entity)
        }

        startActivity(Intent(this@RemindNotificationActivity, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
        finish()
    }

}