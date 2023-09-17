package com.example.vietcar.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.vietcar.data.model.search_history.SearchHistoryEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface CarDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertText(searchHistoryEntity: SearchHistoryEntity)

    @Query("SELECT * FROM search_table")
    fun getText(): Flow<List<SearchHistoryEntity>>

    @Delete
    suspend fun deleteText(searchHistoryEntity: SearchHistoryEntity)
}