package com.example.remind.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.remind.R
import com.example.remind.presentation.notification.RemindNotificationActivity
import com.example.remind.utils.Constant
import java.util.*

class RemindAlarmService: Service() {

    companion object {
        const val serviceNotifyChannelID = "service_channel_id"
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let{
            val alarmId = it.getIntExtra(Constant.KEY_REMIND_ALARM_ID, 0)
            val hour = it.getIntExtra(Constant.KEY_REMIND_TIME_HOUR, 0)
            val minute = it.getIntExtra(Constant.KEY_REMIND_TIME_MINUTE, 0)

            val notification = getServiceNotification(this, getContent(hour, minute), alarmId)

            startForeground(1, notification.build())
            registerAlarm(this, alarmId, hour, minute)
        }

        return START_STICKY
    }

    private fun registerAlarm(context: Context, alarmId: Int, hour: Int, minute: Int){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, RemindAlarmReceiver::class.java)
        intent.putExtra(Constant.KEY_REMIND_ALARM_ID, alarmId)

        val pendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val calendar: Calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    private fun getContent(hour: Int, minute: Int): String {
        val amPm = if(hour < 12) "AM" else "PM"
        val displayHour = if(hour < 13) String.format("%02d", hour) else String.format("%02d", hour - 12)
        val displayMinute = String.format("%02d", minute)
        return "$displayHour:$displayMinute $amPm"
    }

    private fun getServiceNotification(context: Context, content: String, alarmId: Int): NotificationCompat.Builder {
        val intent = Intent(context, RemindNotificationActivity::class.java)
        intent.putExtras(Bundle().apply {
            putInt(Constant.KEY_REMIND_ALARM_ID, alarmId)
        })

        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notifyBuilder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                serviceNotifyChannelID,
                context.getString(R.string.string_remind_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(channel)
            NotificationCompat.Builder(context, serviceNotifyChannelID)
        } else {
            NotificationCompat.Builder(context)
        }

        notifyBuilder.setSmallIcon(R.drawable.ic_alarm_black_24dp)
        notifyBuilder.setContentTitle(context.getString(R.string.string_remind_notification_name))
        notifyBuilder.setContentText(content)
        notifyBuilder.setContentIntent(pendingIntent)

        return notifyBuilder
    }
}