package com.example.remind.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.remind.model.entity.RemindInfoEntity
import java.util.*

object AlarmManagerHelper {

    fun registerAlarm(context: Context, item: RemindInfoEntity){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, RemindAlarmReceiver::class.java)
        intent.putExtra(Constant.KEY_REMIND_ALARM_ID, item.id)
        intent.putExtra(Constant.KEY_REMIND_NAME, item.remindName)
        intent.putExtra(Constant.KEY_REMIND_TIME_HOUR, item.remindTimeHour)
        intent.putExtra(Constant.KEY_REMIND_TIME_MINUTE, item.remindTimeMinute)

        Log.w("KKC_TAG", "registerAlarm -> item.id : ${item.id}")
        val pendingIntent = PendingIntent.getBroadcast(context, item.id, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val calendar: Calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, item.remindTimeHour)
        calendar.set(Calendar.MINUTE, item.remindTimeMinute)
        calendar.set(Calendar.SECOND, 0)

        alarmManager.setAlarmClock(AlarmManager.AlarmClockInfo(calendar.timeInMillis, pendingIntent), pendingIntent)
    }

    fun cancelAlarm(context: Context, alarmID: Int){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, RemindAlarmReceiver::class.java)
        val sender = PendingIntent.getBroadcast(context, alarmID, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        sender?.let {
            alarmManager.cancel(sender)
            sender.cancel()
            Log.w("KKC_TAG", "alarmManager cancel, PendingIntent cancel")
        }
    }
}