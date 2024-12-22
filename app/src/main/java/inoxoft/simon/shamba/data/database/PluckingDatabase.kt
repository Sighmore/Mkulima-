package inoxoft.simon.shamba.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import inoxoft.simon.shamba.data.dao.PluckingDao
import inoxoft.simon.shamba.data.model.PluckingRecord
import inoxoft.simon.shamba.data.util.Converters

@Database(entities = [PluckingRecord::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class PluckingDatabase : RoomDatabase() {
    abstract fun pluckingDao(): PluckingDao

    companion object {
        @Volatile
        private var INSTANCE: PluckingDatabase? = null

        fun getDatabase(context: Context): PluckingDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PluckingDatabase::class.java,
                    "plucking_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
} 