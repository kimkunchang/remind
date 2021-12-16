package com.example.remind.presentation

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.remind.R
import com.example.remind.utils.RemindAlarmReceiver
import com.example.remind.databinding.RemindSettingFragmentBinding
import java.net.URLDecoder
import java.util.*

class RemindSettingFragment: Fragment() {

    private lateinit var binding: RemindSettingFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.remind_setting_fragment, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvRingtoneTitle.text = getCurrentRingtoneTitle()

        binding.btnRemindSave.setOnClickListener {
            testSaveAlarm()
            it.findNavController().popBackStack()
        }
    }

    private fun testSaveAlarm(){

        val intent = Intent(activity, RemindAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(activity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val calendar: Calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 32)
        calendar.set(Calendar.SECOND, 0)

        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setAlarmClock(AlarmManager.AlarmClockInfo(calendar.timeInMillis, pendingIntent), pendingIntent)
    }

    private fun getCurrentRingtoneTitle(): String{
        val defaultRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(activity, RingtoneManager.TYPE_ALARM)
        val encodedQuery = defaultRingtoneUri.encodedQuery
        var title = ""
        encodedQuery?.let {
            var startIndex = it.indexOf("=")
            var endIndex = it.indexOf("&")
            title = it.substring(startIndex+1, endIndex)
            title = URLDecoder.decode(title, "UTF-8")
        }
        return title
    }
}