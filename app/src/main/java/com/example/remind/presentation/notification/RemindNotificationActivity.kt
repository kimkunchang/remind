package com.example.remind.presentation.notification

import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.remind.MainActivity
import com.example.remind.R
import com.example.remind.databinding.RemindNotificationActivityBinding
import com.example.remind.model.MainViewModel
import com.example.remind.utils.Constant

class RemindNotificationActivity: AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: RemindNotificationActivityBinding

    private var alarmID: Int = 0

    private lateinit var ringToneManager: RingtoneManager
    private var ringTone: Ringtone? = null
    private var playRingToneUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.remind_notification_activity)

        ringToneManager = RingtoneManager(this)

        intent.extras?.let {
            alarmID = it.getInt(Constant.KEY_REMIND_ALARM_ID)
        }

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.getRemindInfo(alarmID) { entity ->
            playRingToneUrl = entity.remindRingToneUrl
            playRingTone()
            binding.entity = entity
        }

        binding.btnRemindComplete.setOnClickListener { completeRemindAlarm() }
    }

    private fun completeRemindAlarm(){
        viewModel.getRemindInfo(alarmID) { entity ->
            entity.cancelAlarm(this@RemindNotificationActivity)
            viewModel.updateRemindInfo(entity)
        }

        releaseRingTone()

        startActivity(Intent(this@RemindNotificationActivity, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        releaseRingTone()
    }

    private fun playRingTone(){
        releaseRingTone()
        try {
            val position = ringToneManager.getRingtonePosition(Uri.parse(playRingToneUrl))
            ringTone = ringToneManager.getRingtone(position)
            ringTone?.play()
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun releaseRingTone(){
        ringTone?.let{
            if(it.isPlaying){
                it.stop()
            }
        }
        ringTone = null
    }
}