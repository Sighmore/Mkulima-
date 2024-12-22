package inoxoft.simon.shamba.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "plucking_records")
data class PluckingRecord(
        @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
        val pluckerName: String,
        val kilos: Double,
        val rate: Double,
        val amount: Double,
        val date: Date
) 