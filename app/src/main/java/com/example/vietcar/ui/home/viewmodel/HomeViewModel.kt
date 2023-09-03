package com.example.vietcar.ui.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vietcar.common.Resource
import com.example.vietcar.data.model.category.ListCategory
import com.example.vietcar.data.model.product.ProductBody
import com.example.vietcar.data.model.product_group.ListProductGroup
import com.example.vietcar.data.model.product_to_cart.ProductToCart
import com.example.vietcar.di.repository.ICarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val carRepository: ICarRepository
) : ViewModel() {

    private val _categoryResponse: MutableLiveData<Resource<ListCategory>> = MutableLiveData()
    val categoryResponse
        get() = _categoryResponse

    private val _listProductGroupResponse: MutableLiveData<Resource<ListProductGroup>> =
        MutableLiveData()
    val listProductGroupResponse
        get() = _listProductGroupResponse

    private val _productToCartResponse: MutableLiveData<Resource<ProductToCart>> = MutableLiveData()
    val productToCartResponse
        get() = _productToCartResponse


    fun getCategory() {
        viewModelScope.launch {
            try {
                val categoryData = carRepository.getCategory()
                _categoryResponse.postValue(Resource.Success(categoryData))
            } catch (exception: Exception) {
                _categoryResponse.postValue(Resource.Error("Lỗi mạng: ${exception.message}"))
            }
        }
    }

    fun getProductGroup() {
        viewModelScope.launch {
            try {
                val listProductGroup = carRepository.getProductGroup()
                _listProductGroupResponse.postValue(Resource.Success(listProductGroup))
            } catch (exception: Exception) {
                _listProductGroupResponse.postValue(Resource.Error("Lỗi mạng: ${exception.message}"))
            }
        }
    }

    fun addProductToCart(body: ProductBody) {
        viewModelScope.launch {

            try {
                val productToCart = carRepository.addProductToCart(body)
                _productToCartResponse.postValue(Resource.Success(productToCart))
            } catch (exception: Exception) {
                _productToCartResponse.postValue(Resource.Error("Lỗi mạng: ${exception.message}"))
            }
        }
    }
}