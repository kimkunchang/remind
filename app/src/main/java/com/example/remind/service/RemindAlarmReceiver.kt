package com.example.remind.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.remind.presentation.notification.RemindNotificationActivity
import com.example.remind.utils.Constant

class RemindAlarmReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent) {
        context?.let {
            it.startActivity(Intent(it.applicationContext, RemindNotificationActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                putExtras(Bundle().apply {
                    putInt(Constant.KEY_REMIND_ALARM_ID, intent.getIntExtra(Constant.KEY_REMIND_ALARM_ID, 0))
                })
            })
        }
    }
}