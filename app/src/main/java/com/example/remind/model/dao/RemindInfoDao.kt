package com.example.remind.model.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.remind.model.entity.RemindInfoEntity

@Dao
interface RemindInfoDao {

    @Insert
    fun saveRemindInfo(remindInfo: RemindInfoEntity)

    @Query("SELECT * FROM remind_entity")
    fun getRemindInfoList(): LiveData<List<RemindInfoEntity>>

    @Query("SELECT * FROM remind_entity WHERE remindTimeHour =:remindName")
    suspend fun getRemindInfo(remindName: String): RemindInfoEntity

    @Query("SELECT * FROM remind_entity WHERE id =:id")
    suspend fun getRecentRemindInfo(id: Int): RemindInfoEntity
}