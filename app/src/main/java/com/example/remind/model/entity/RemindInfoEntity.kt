package com.example.remind.model.entity

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.remind.utils.Constant
import com.example.remind.utils.RemindAlarmReceiver
import java.util.*

@Entity(tableName = "remind_entity")
data class RemindInfoEntity(
    @PrimaryKey
    @ColumnInfo val alarmID: Int = 0,
    var remindName: String,
    var remindTimeHour: Int,
    var remindTimeMinute: Int,
    var alarmOnOffStatus: Boolean
) {

    val displayTime: String
        get() {
            val amPm = if(remindTimeHour < 12) "AM" else "PM"
            val displayHour = if(remindTimeHour <12) String.format("%02d", remindTimeHour) else String.format("%02d", remindTimeHour - 12)
            val displayMinute = String.format("%02d", remindTimeMinute)
            return "$displayHour:$displayMinute $amPm"
        }

    val displayShorTime: String
        get() {
            val displayHour = if(remindTimeHour <12) String.format("%02d", remindTimeHour) else String.format("%02d", remindTimeHour - 12)
            val displayMinute = String.format("%02d", remindTimeMinute)
            return "$displayHour:$displayMinute"
        }

    fun registerAlarm(context: Context){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, RemindAlarmReceiver::class.java)
        intent.putExtra(Constant.KEY_REMIND_ALARM_ID, alarmID)
//        intent.putExtra(Constant.KEY_REMIND_NAME, remindName)
//        intent.putExtra(Constant.KEY_REMIND_TIME_HOUR, remindTimeHour)
//        intent.putExtra(Constant.KEY_REMIND_TIME_MINUTE, remindTimeMinute)

        Log.w("KKC_TAG", "registerAlarm -> alarmID : $alarmID")
        val pendingIntent = PendingIntent.getBroadcast(context, alarmID, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val calendar: Calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, remindTimeHour)
        calendar.set(Calendar.MINUTE, remindTimeMinute)
        calendar.set(Calendar.SECOND, 0)

        //alarmManager.setAlarmClock(AlarmManager.AlarmClockInfo(calendar.timeInMillis, pendingIntent), pendingIntent)

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)

        this.alarmOnOffStatus = true
    }

    fun cancelAlarm(context: Context){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, RemindAlarmReceiver::class.java)
        Log.d("KKC_TAG", "cancelAlarm -> alarmID : $alarmID")
        val sender = PendingIntent.getBroadcast(context, alarmID, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        sender?.let {
            alarmManager.cancel(sender)
            sender.cancel()
            Log.w("KKC_TAG", "alarmManager cancel, PendingIntent cancel")
        }
        this.alarmOnOffStatus = false
    }

}