package com.example.vietcar.click

import com.example.vietcar.data.model.search_history.SearchHistoryEntity

interface DeleteSearchHistory {

    fun deleteSearchHistoryClick(searchHistoryEntity: SearchHistoryEntity)
}