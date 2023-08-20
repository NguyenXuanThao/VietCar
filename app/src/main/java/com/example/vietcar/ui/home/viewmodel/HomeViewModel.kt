package com.example.vietcar.ui.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vietcar.data.model.category.Category
import com.example.vietcar.data.model.product_group.ProductGroup
import com.example.vietcar.di.repository.ICarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val carRepository: ICarRepository
) : ViewModel() {

    private val _categoryResponse: MutableLiveData<Category> = MutableLiveData()
    val categoryResponse
        get() = _categoryResponse

    private val _productGroupResponse: MutableLiveData<ProductGroup> = MutableLiveData()
    val productGroupResponse
        get() = _productGroupResponse


    fun getCategory() {
        viewModelScope.launch {
            _categoryResponse.value = carRepository.getCategory()
        }
    }

    fun getProductGroup() {
        viewModelScope.launch {
            _productGroupResponse.value = carRepository.getProductGroup()
        }
    }
}