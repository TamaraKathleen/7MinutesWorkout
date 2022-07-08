package eu.android.a7minutesworkout.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import eu.android.a7minutesworkout.models.HistoryEntity

@Database(entities = [HistoryEntity::class], version = 1)
abstract class HistoryDatabase: RoomDatabase() {

    abstract fun historyDao(): HistoryDao

    companion object{
        fun getInstance(context: Context): HistoryDatabase{
            return Room.databaseBuilder(
                context,
                HistoryDatabase::class.java,
                "history_database"
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}