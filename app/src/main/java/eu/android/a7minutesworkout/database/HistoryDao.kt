package eu.android.a7minutesworkout.database

import androidx.room.*
import eu.android.a7minutesworkout.models.HistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {

    @Insert
    suspend fun insert(historyEntity: HistoryEntity)

    @Query("SELECT * FROM `history-table`")
    fun fetchAllDates(): Flow<List<HistoryEntity>>

    @Query("DELETE FROM `history-table`")
    suspend fun deleteAllDates()
}