package com.example.vietcar.ui.search.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vietcar.data.model.search_history.SearchHistoryEntity
import com.example.vietcar.di.repository.ICarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SearchViewModel @Inject constructor(
    private val carRepository: ICarRepository,
) : ViewModel() {

    private val _textResponse: MutableLiveData<List<SearchHistoryEntity>> = MutableLiveData()
    val textResponse
        get() = _textResponse

    fun insertText(searchHistoryEntity: SearchHistoryEntity) {
        viewModelScope.launch {
            carRepository.insertText(searchHistoryEntity)
        }
    }

    fun deleteText(searchHistoryEntity: SearchHistoryEntity) {
        viewModelScope.launch {
            carRepository.deleteText(searchHistoryEntity)
        }
    }

    fun getText() {
        viewModelScope.launch {
            carRepository.getAllText().catch { ex ->
                Log.e("fresher", "getAllNotes Exception : $ex")
                _textResponse.value = arrayListOf()
            }.collect { value ->
                _textResponse.value = value
            }
        }
    }
}