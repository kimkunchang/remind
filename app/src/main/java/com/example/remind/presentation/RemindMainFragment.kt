package com.example.remind.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.remind.R
import com.example.remind.databinding.RemindMainFragmentBinding

class RemindMainFragment: Fragment() {

    companion object {
        fun newInstance() = RemindMainFragment()
    }

    //private lateinit var viewModel: MainViewModel

    private lateinit var binding: RemindMainFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.remind_main_fragment, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        binding.layoutBtnRemindAdd.setOnClickListener {
            it.findNavController().navigate(R.id.action_remind_setting_fragment)
        }
    }
}