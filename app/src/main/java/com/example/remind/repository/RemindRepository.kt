package com.example.remind.repository

import androidx.lifecycle.LiveData
import com.example.remind.model.dao.RemindInfoDao
import com.example.remind.model.dao.RemindInfoDatabase
import com.example.remind.model.entity.RemindInfoEntity

class RemindRepository(mDatabase: RemindInfoDatabase) {

    private val dao = mDatabase.remindInfoDao()
    val remindList: LiveData<List<RemindInfoEntity>> = dao.getRemindInfoList()

//    companion object {
//        private var sInstance: RemindRepository? = null
//
//        fun getInstance(database: RemindInfoDatabase): RemindRepository {
//            return sInstance
//                ?: synchronized(this){
//                    val instance = RemindRepository(database)
//                    sInstance = instance
//                    instance
//                }
//        }
//    }

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