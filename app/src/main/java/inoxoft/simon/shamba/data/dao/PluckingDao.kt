package inoxoft.simon.shamba.data.dao

import androidx.room.*
import inoxoft.simon.shamba.data.model.PluckingRecord
import inoxoft.simon.shamba.data.database.DailySummary
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface PluckingDao {
    @Insert
    suspend fun insert(record: PluckingRecord)

    @Query("SELECT * FROM plucking_records WHERE date = :date ORDER BY date DESC")
    fun getRecordsForDay(date: Date): Flow<List<PluckingRecord>>

    @Query("""
        SELECT 
            COUNT(DISTINCT pluckerName) as pluckerCount,
            SUM(kilos) as totalKilos,
            AVG(kilos) as averageKilos
        FROM plucking_records 
        WHERE date = :date
    """)
    suspend fun getDailySummary(date: Date): DailySummary

    @Query("SELECT * FROM plucking_records WHERE strftime('%Y-%m-%d', datetime(date/1000, 'unixepoch')) = strftime('%Y-%m-%d', datetime(:date/1000, 'unixepoch'))")
    fun getRecordsForDate(date: Long): Flow<List<PluckingRecord>>

    @Query("SELECT * FROM plucking_records ORDER BY date DESC")
    fun getAllRecords(): Flow<List<PluckingRecord>>
} 