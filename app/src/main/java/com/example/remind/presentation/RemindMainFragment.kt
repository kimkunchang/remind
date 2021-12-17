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

class RemindMainFragment: Fragment() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: RemindMainFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.remind_main_fragment, container, false)

        activity?.let {
            binding.viewModel = viewModel
            binding.lifecycleOwner = this
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val remindAdapter = RemindListAdapter(requireContext())

        binding.rvRemindList.apply {
            adapter = remindAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        viewModel.remindList.observe(viewLifecycleOwner, { remindList ->
            remindList?.let {
                remindAdapter.setRemindList(it)
                Log.w("KKC_TAG", "output : $it")
            }
        })

        binding.layoutBtnRemindAdd.setOnClickListener {
            it.findNavController().navigate(R.id.action_remind_setting_fragment)
        }
    }
}