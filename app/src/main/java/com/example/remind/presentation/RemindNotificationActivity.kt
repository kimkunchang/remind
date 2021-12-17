package com.example.remind.presentation

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.remind.utils.Constant
import com.example.remind.MainActivity
import com.example.remind.R
import com.example.remind.databinding.RemindNotificationActivityBinding
import com.example.remind.model.RemindNotificationData
import com.example.remind.utils.RemindAlarmReceiver

class RemindNotificationActivity: AppCompatActivity() {

    private lateinit var binding: RemindNotificationActivityBinding

    private var alarmID: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.remind_notification_activity)

        intent.extras?.let {
            binding.data = RemindNotificationData(
                remindTitle = it.getString(Constant.KEY_REMIND_NAME),
                remindTimeHour = it.getInt(Constant.KEY_REMIND_TIME_HOUR),
                remindTimeMinute = it.getInt(Constant.KEY_REMIND_TIME_MINUTE))
            alarmID = it.getInt(Constant.KEY_REMIND_ALARM_ID)
        }

        binding.btnRemindComplete.setOnClickListener {
            completeRemindAlarm()
        }
    }

    private fun completeRemindAlarm(){
        Log.w("KKC_TAG", "completeRemindAlarm -> alarmID : $alarmID")
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(this, RemindAlarmReceiver::class.java)
        val sender = PendingIntent.getBroadcast(this, alarmID, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        sender?.let {
            alarmManager.cancel(sender)
            sender.cancel()
            Log.w("KKC_TAG", "alarmManager cancel, PendingIntent cancel")
        }

        // todo 해당 리마인드 리스트 아이템에서 비활성화 변경 기능 구현

        startActivity(Intent(this@RemindNotificationActivity, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
        finish()
    }

}