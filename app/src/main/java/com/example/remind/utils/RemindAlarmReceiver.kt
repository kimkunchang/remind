package com.example.remind.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.remind.presentation.RemindNotificationActivity

class RemindAlarmReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent) {
        Log.w("KKC_TAG", "RemindAlarmReceiver -> onReceive")
        context?.let {
            it.startActivity(Intent(it.applicationContext, RemindNotificationActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                putExtras(Bundle().apply {
                    putInt(Constant.KEY_REMIND_ALARM_ID, intent.getIntExtra(Constant.KEY_REMIND_ALARM_ID, 0))
                    putString(Constant.KEY_REMIND_NAME, intent.getStringExtra(Constant.KEY_REMIND_NAME))
                    putInt(Constant.KEY_REMIND_TIME_HOUR, intent.getIntExtra(Constant.KEY_REMIND_TIME_HOUR, 0))
                    putInt(Constant.KEY_REMIND_TIME_MINUTE, intent.getIntExtra(Constant.KEY_REMIND_TIME_MINUTE, 0))
                })
            })
            Log.w("KKC_TAG", "RemindAlarmReceiver -> RemindNotificationActivity 이동")
        }
    }
}