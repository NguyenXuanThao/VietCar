package com.example.vietcar.ui.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vietcar.data.model.category.ListCategory
import com.example.vietcar.data.model.product_group.ListProductGroup
import com.example.vietcar.di.repository.ICarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val carRepository: ICarRepository
) : ViewModel() {

    private val _categoryResponse: MutableLiveData<ListCategory> = MutableLiveData()
    val categoryResponse
        get() = _categoryResponse

    private val _listProductGroupResponse: MutableLiveData<ListProductGroup> = MutableLiveData()
    val listProductGroupResponse
        get() = _listProductGroupResponse


    fun getCategory() {
        viewModelScope.launch {
            _categoryResponse.value = carRepository.getCategory()
        }
    }

    fun getProductGroup() {
        viewModelScope.launch {
            _listProductGroupResponse.value = carRepository.getListProductGroup()
        }
    }
}