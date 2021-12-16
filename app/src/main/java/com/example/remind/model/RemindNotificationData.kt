package com.example.remind.model

data class RemindNotificationData(
    val remindTitle: String?,
    val remindTimeHour: Int,
    val remindTimeMinute: Int
)