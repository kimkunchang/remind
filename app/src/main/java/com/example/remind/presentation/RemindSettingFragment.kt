package com.example.remind.presentation

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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
    private lateinit var currentRingtoneUri: Uri
    private lateinit var currentRingtonePath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        alarmID = sageArgs.remindAlarmId
        currentRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(activity, RingtoneManager.TYPE_ALARM)
        currentRingtonePath = currentRingtoneUri.toString()
        Log.w("KKC_TAG", "onCreate -> currentRingtonePath : $currentRingtonePath")
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
                currentRingtoneUri = Uri.parse(entity.remindRingToneUrl)
                currentRingtonePath = entity.remindRingToneUrl
                binding.entity = entity
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvRingtoneTitle.text = getCurrentRingtoneTitle(currentRingtoneUri)

        binding.layoutRemindRingtoneSetting.setOnClickListener { showRingToneDialog() }

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
                    remindRingToneUrl = currentRingtonePath,
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
                    remindRingToneUrl = currentRingtonePath,
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

    private var requestRingToneDialog: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == RESULT_OK){
            val pickUri: Uri? = it.data?.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
            pickUri?.let { pick ->
                Log.w("KKC_TAG", "pickUri : $pick")
                currentRingtoneUri = pick
                currentRingtonePath = currentRingtoneUri.toString()
                Log.w("KKC_TAG", "pickUri -> currentRingtonePath : $currentRingtonePath")

                binding.tvRingtoneTitle.text = getCurrentRingtoneTitle(pick)
            }
        }
    }

    private fun showRingToneDialog(){
        val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "벨소리")
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false)
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, false)
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,RingtoneManager.TYPE_ALARM)

        requestRingToneDialog.launch(intent)
    }

    private fun getCurrentRingtoneTitle(ringToneUri: Uri): String{
        //val defaultRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(activity, RingtoneManager.TYPE_ALARM)
        val encodedQuery = ringToneUri.encodedQuery
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