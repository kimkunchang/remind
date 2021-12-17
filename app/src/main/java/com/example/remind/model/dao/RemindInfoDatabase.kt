package com.example.remind.model.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.remind.model.entity.RemindInfoEntity

@Database(entities = [RemindInfoEntity::class], version = 1, exportSchema = false)
abstract class RemindInfoDatabase: RoomDatabase() {

    companion object {
        private const val DATABASE_NAME = "RemindInfoDB"

        private var INSTANCE: RemindInfoDatabase? = null

        fun getInstance(context: Context): RemindInfoDatabase {
            if(INSTANCE == null){
                synchronized(RemindInfoDatabase::class){
                    INSTANCE = Room.databaseBuilder(
                        context,
                        RemindInfoDatabase::class.java,
                        DATABASE_NAME
                    ).build()
                }
            }
            return INSTANCE!!
        }
    }

    abstract fun remindInfoDao(): RemindInfoDao

}