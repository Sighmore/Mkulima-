package inoxoft.simon.shamba.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import inoxoft.simon.shamba.data.database.PluckingDatabase
import inoxoft.simon.shamba.data.model.PluckingRecord
import inoxoft.simon.shamba.data.database.DailySummary
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import android.util.Log

class PluckingViewModel(application: Application) : AndroidViewModel(application) {
    private val database = PluckingDatabase.getDatabase(application)
    private val pluckingDao = database.pluckingDao()
    
    private val _selectedDate = MutableStateFlow(Date())
    
    val dailyRecords: StateFlow<List<PluckingRecord>> = _selectedDate
        .flatMapLatest { date ->
            pluckingDao.getRecordsForDay(date)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val dailySummary: StateFlow<DailySummary?> = _selectedDate
        .flatMapLatest { date ->
            pluckingDao.getDailySummary(date)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    private val _todayRecords = MutableStateFlow<List<PluckingRecord>>(emptyList())
    val todayRecords: StateFlow<List<PluckingRecord>> = _todayRecords

    fun setDate(date: Date) {
        _selectedDate.value = date
    }

    fun addRecord(record: PluckingRecord) {
        viewModelScope.launch {
            pluckingDao.insert(record)
        }
    }

    fun getTodayRecords() {
        viewModelScope.launch {
            try {
                // Get today's timestamp
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                
                // Get records for today
                pluckingDao.getRecordsForDate(calendar.timeInMillis).collect { records ->
                    _todayRecords.value = records
                }
            } catch (e: Exception) {
                Log.e("PluckingViewModel", "Error fetching today's records", e)
            }
        }
    }
} 