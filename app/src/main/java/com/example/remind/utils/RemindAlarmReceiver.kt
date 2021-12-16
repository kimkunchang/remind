package com.example.remind.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.remind.presentation.RemindNotificationActivity

class RemindAlarmReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.w("KKC_TAG", "RemindAlarmReceiver -> onReceive")
        context?.let {
            it.startActivity(Intent(it.applicationContext, RemindNotificationActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                putExtras(Bundle().apply {
                    putString(Constant.KEY_REMIND_NAME, "내시경 약먹기")
                    putInt(Constant.KEY_REMIND_TIME_HOUR, 8)
                    putInt(Constant.KEY_REMIND_TIME_MINUTE, 30)
                })
            })
            Log.w("KKC_TAG", "RemindAlarmReceiver -> RemindNotificationActivity 이동")
        }
    }
}