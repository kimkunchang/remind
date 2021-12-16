package com.example.remind.presentation

import android.media.RingtoneManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.remind.R
import com.example.remind.databinding.RemindSettingFragmentBinding
import java.net.URLDecoder

class RemindSettingFragment: Fragment() {

    companion object {
        fun newInstance() = RemindSettingFragment()
    }

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