package com.example.remind.presentation

import com.example.remind.model.entity.RemindInfoEntity

interface OnStatusRemindListener {
    fun onStatusChange(remindInfo: RemindInfoEntity)
}