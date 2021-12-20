package com.example.remind.model.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.remind.model.entity.RemindInfoEntity

@Dao
interface RemindInfoDao {

    @Insert
    fun saveRemindInfo(remindInfo: RemindInfoEntity)

    @Query("SELECT * FROM remind_entity")
    fun getRemindInfoList(): LiveData<List<RemindInfoEntity>>

    @Query("SELECT * FROM remind_entity WHERE remindTimeHour =:remindName")
    suspend fun getRemindInfo(remindName: String): RemindInfoEntity

    @Query("SELECT * FROM remind_entity WHERE alarmID =:alarmID")
    fun getRecentRemindInfo(alarmID: Int): RemindInfoEntity

    @Update
    fun updateRemindInfo(entity: RemindInfoEntity)
}