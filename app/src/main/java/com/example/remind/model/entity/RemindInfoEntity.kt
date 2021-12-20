package com.example.remind.model.entity

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.remind.service.RemindAlarmService
import com.example.remind.utils.Constant
import com.example.remind.service.RemindAlarmReceiver

@Entity(tableName = "remind_entity")
data class RemindInfoEntity(
    @PrimaryKey
    @ColumnInfo val alarmID: Int = 0,
    var remindName: String,
    var remindTimeHour: Int,
    var remindTimeMinute: Int,
    var remindRingToneUrl: String,
    var alarmOnOffStatus: Boolean
) {

    val displayTime: String
        get() {
            val amPm = if(remindTimeHour < 12) "AM" else "PM"
            val displayHour = if(remindTimeHour < 13) String.format("%02d", remindTimeHour) else String.format("%02d", remindTimeHour - 12)
            val displayMinute = String.format("%02d", remindTimeMinute)
            return "$displayHour:$displayMinute $amPm"
        }

    val displayShorTime: String
        get() {
            val displayHour = String.format("%02d", remindTimeHour)
            val displayMinute = String.format("%02d", remindTimeMinute)
            return "$displayHour:$displayMinute"
        }

    fun registerAlarm(context: Context){
        val intent = Intent(context, RemindAlarmService::class.java)
        intent.putExtra(Constant.KEY_REMIND_ALARM_ID, alarmID)
        intent.putExtra(Constant.KEY_REMIND_TIME_HOUR, remindTimeHour)
        intent.putExtra(Constant.KEY_REMIND_TIME_MINUTE, remindTimeMinute)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }

        this.alarmOnOffStatus = true
    }


    fun cancelAlarm(context: Context){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, RemindAlarmReceiver::class.java)

        val sender = PendingIntent.getBroadcast(context, alarmID, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        sender?.let {
            alarmManager.cancel(sender)
            sender.cancel()
        }

        val serviceIntent = Intent(context, RemindAlarmService::class.java)
        context.stopService(serviceIntent)

        this.alarmOnOffStatus = false
    }

}