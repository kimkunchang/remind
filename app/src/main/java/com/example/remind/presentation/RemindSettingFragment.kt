package com.example.remind.presentation

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.remind.MainActivity
import androidx.navigation.findNavController
import com.example.remind.R
import com.example.remind.utils.RemindAlarmReceiver
import com.example.remind.databinding.RemindSettingFragmentBinding
import com.example.remind.model.dao.RemindInfoDatabase
import com.example.remind.model.entity.RemindInfoEntity
import com.example.remind.utils.Constant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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

        binding.btnRemindSave.setOnClickListener { saveRemind() }
    }

    private fun saveRemind(){
        if(verifyRemindInfo()){
            var hour = binding.tpAlarmTime.hour

            var minute = binding.tpAlarmTime.minute

            Log.w("KKC_TAG", "hour : $hour, minute : $minute")

            CoroutineScope(Dispatchers.IO).launch {
                context?.let{
                    RemindInfoDatabase.getInstance(it).remindInfoDao().saveRemindInfo(
                        RemindInfoEntity(
                            remindName = binding.etRemindName.text.toString(),
                            remindTimeHour = binding.tpAlarmTime.hour,
                            remindTimeMinute = binding.tpAlarmTime.minute,
                            alarmOnOffStatus = true
                        )
                    )
                    val entityList = RemindInfoDatabase.getInstance(it).remindInfoDao().getRemindInfoList()

                    var alarmID = if(entityList.isEmpty()){
                        1
                    } else {
                        val recentEntity = entityList[entityList.size -1]
                        recentEntity.id
                    }

                    registerAlarm(alarmID, binding.etRemindName.text.toString(), binding.tpAlarmTime.hour, binding.tpAlarmTime.minute)
                }
            }
            context?.let {
                Toast.makeText(it, getString(R.string.string_remind_alarm_register), Toast.LENGTH_SHORT).show()
            }
            this@RemindSettingFragment.findNavController().popBackStack()
        }
    }

    private fun verifyRemindInfo(): Boolean {
        return when{
            (binding.etRemindName.text?.toString()?.isEmpty() ?: false) ->{
                Toast.makeText(context?.applicationContext, getString(R.string.string_remind_name_empty), Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }


    private fun registerAlarm(id: Int, remindName: String, hour: Int, minute: Int){
        Log.w("KKC_TAG", "registerAlarm -> id : $id")
        val intent = Intent(activity, RemindAlarmReceiver::class.java)
        intent.putExtra(Constant.KEY_REMIND_ALARM_ID, id)
        intent.putExtra(Constant.KEY_REMIND_NAME, remindName)
        intent.putExtra(Constant.KEY_REMIND_TIME_HOUR, hour)
        intent.putExtra(Constant.KEY_REMIND_TIME_MINUTE, minute)

        val pendingIntent = PendingIntent.getBroadcast(activity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val calendar: Calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
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