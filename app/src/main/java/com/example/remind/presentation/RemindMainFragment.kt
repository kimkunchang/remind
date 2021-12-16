package com.example.remind.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.remind.R

class RemindMainFragment: Fragment() {

    companion object {
        fun newInstance() = RemindMainFragment()
    }

    //private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.remind_main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        view.findViewById<ConstraintLayout>(R.id.layout_btn_remind_add).setOnClickListener {
            it.findNavController().navigate(R.id.action_remind_setting_fragment)
        }



    }
}