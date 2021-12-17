package com.example.remind.model.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.remind.model.entity.RemindInfoEntity
import kotlinx.coroutines.CoroutineScope

@Database(entities = [RemindInfoEntity::class], version = 1, exportSchema = false)
abstract class RemindInfoDatabase: RoomDatabase() {

    companion object {
        private const val DATABASE_NAME = "RemindInfoDB"

        @Volatile
        private var INSTANCE: RemindInfoDatabase? = null

        fun getInstance(context: Context): RemindInfoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RemindInfoDatabase::class.java,
                    DATABASE_NAME
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    abstract fun remindInfoDao(): RemindInfoDao

}