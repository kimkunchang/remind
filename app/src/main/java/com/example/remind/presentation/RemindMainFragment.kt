package com.example.remind.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.remind.R
import com.example.remind.databinding.RemindMainFragmentBinding
import com.example.remind.model.MainViewModel
import com.example.remind.model.entity.RemindInfoEntity

class RemindMainFragment: Fragment(), OnStatusRemindListener {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: RemindMainFragmentBinding

    private lateinit var remindAdapter: RemindListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.remind_main_fragment, container, false)

        activity?.let {
            binding.viewModel = viewModel
            binding.lifecycleOwner = this
        }

        remindAdapter = RemindListAdapter()
        remindAdapter.setOnStatusRemindListener(this)

        viewModel.remindList.observe(viewLifecycleOwner, { remindList ->
            remindList?.let {
                remindAdapter.setRemindList(it)
                Log.w("KKC_TAG", "output : $it")
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvRemindList.apply {
            adapter = remindAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding.layoutBtnRemindAdd.setOnClickListener {
            it.findNavController().navigate(R.id.action_remind_setting_fragment)
        }
    }

    override fun onStatusChange(remindInfo: RemindInfoEntity) {
        Log.w("KKC_TAG", "onStatusChange -> remindInfo.alarmOnOffStatus : ${remindInfo.alarmOnOffStatus}")
        if(remindInfo.alarmOnOffStatus){
            remindInfo.cancelAlarm(requireContext())
            viewModel.updateRemindInfo(remindInfo)
        } else {
            remindInfo.registerAlarm(requireContext())
            viewModel.updateRemindInfo(remindInfo)
        }
    }
}