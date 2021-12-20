package com.example.remind.presentation

import android.media.RingtoneManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.remind.R
import com.example.remind.databinding.RemindSettingFragmentBinding
import com.example.remind.model.MainViewModel
import com.example.remind.model.entity.RemindInfoEntity
import java.net.URLDecoder
import java.util.*

class RemindSettingFragment: Fragment() {

    private lateinit var binding: RemindSettingFragmentBinding
    private val viewModel: MainViewModel by viewModels()
    private var alarmID = 0
    private val sageArgs: RemindSettingFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        alarmID = sageArgs.remindAlarmId
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.remind_setting_fragment, container, false)

        activity?.let {
            binding.viewModel = viewModel
            binding.lifecycleOwner = this
        }

        if(alarmID != 0) {
            viewModel.getRemindInfo(alarmID) { entity ->
                binding.entity = entity
            }
        }

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

            var entity: RemindInfoEntity

            if(alarmID != 0){
                entity = RemindInfoEntity(
                    alarmID = alarmID,
                    remindName = binding.etRemindName.text.toString(),
                    remindTimeHour = binding.tpAlarmTime.hour,
                    remindTimeMinute = binding.tpAlarmTime.minute,
                    alarmOnOffStatus = true
                )
                viewModel.updateRemindInfo(entity)

            } else {
                alarmID = Random().nextInt(Int.MAX_VALUE)

                entity = RemindInfoEntity(
                    alarmID = alarmID,
                    remindName = binding.etRemindName.text.toString(),
                    remindTimeHour = binding.tpAlarmTime.hour,
                    remindTimeMinute = binding.tpAlarmTime.minute,
                    alarmOnOffStatus = true
                )

                viewModel.saveRemindInfo(entity)
            }

            entity.registerAlarm(requireContext())

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