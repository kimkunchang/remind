package com.example.remind.repository

import androidx.lifecycle.LiveData
import com.example.remind.model.dao.RemindInfoDatabase
import com.example.remind.model.entity.RemindInfoEntity

class RemindRepository(mDatabase: RemindInfoDatabase) {

    private val dao = mDatabase.remindInfoDao()
    val remindList: LiveData<List<RemindInfoEntity>> = dao.getRemindInfoList()

    fun saveRemindInfo(entity: RemindInfoEntity){
        dao.saveRemindInfo(entity)
    }

    fun updateRemindInfo(entity: RemindInfoEntity){
        dao.updateRemindInfo(entity)
    }

    fun getRemindInfo(alarmID: Int): RemindInfoEntity{
        return dao.getRecentRemindInfo(alarmID)
    }
}