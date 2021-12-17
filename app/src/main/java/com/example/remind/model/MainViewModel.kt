package com.example.remind.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.remind.model.dao.RemindInfoDatabase
import com.example.remind.model.entity.RemindInfoEntity
import com.example.remind.repository.RemindRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val repository: RemindRepository = RemindRepository(RemindInfoDatabase.getInstance(application))

    var remindList: LiveData<List<RemindInfoEntity>> = repository.remindList

    fun saveRemindInfo(entity: RemindInfoEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.saveRemindInfo(entity)
    }

    fun getAllRemindList(): LiveData<List<RemindInfoEntity>> {
        return remindList
    }
}