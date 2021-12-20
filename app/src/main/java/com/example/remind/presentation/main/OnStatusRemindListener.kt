package com.example.remind.presentation.main

import com.example.remind.model.entity.RemindInfoEntity

interface OnStatusRemindListener {
    fun onStatusChange(remindInfo: RemindInfoEntity)
}