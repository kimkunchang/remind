package com.example.remind.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remind_entity")
data class RemindInfoEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo val id: Int = 0,
    var remindName: String,
    var remindTimeHour: Int,
    var remindTimeMinute: Int,
    var alarmOnOffStatus: Boolean
)