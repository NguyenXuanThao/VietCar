package com.example.vietcar.data.model.search_history

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "search_table")
data class SearchHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val query: String
)
