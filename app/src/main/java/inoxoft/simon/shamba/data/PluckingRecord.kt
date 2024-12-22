package inoxoft.simon.shamba.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "plucking_records")
data class PluckingRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val pluckerName: String,
    val kilos: Double,
    val rate: Double,
    val amount: Double,
    val date: Date = Date()
) 