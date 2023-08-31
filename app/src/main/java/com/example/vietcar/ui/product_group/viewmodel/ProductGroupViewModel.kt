package com.example.vietcar.ui.product_group.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vietcar.data.model.product.ListProduct
import com.example.vietcar.data.model.product.ProductBody
import com.example.vietcar.data.model.product_to_cart.ProductToCart
import com.example.vietcar.di.repository.CarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductGroupViewModel @Inject constructor(
    private val carRepository: CarRepository
) : ViewModel() {

    private val _productResponse: MutableLiveData<ListProduct> = MutableLiveData()
    val productResponse
        get() = _productResponse

    private val _productToCartResponse: MutableLiveData<ProductToCart> = MutableLiveData()
    val productToCartResponse
        get() = _productToCartResponse

    fun getListProductGroup(groupId: String) {
        viewModelScope.launch {
            _productResponse.value = carRepository.getListProductGroup(groupId)
        }
    }

    fun addProductToCart(body: ProductBody) {
        viewModelScope.launch {
            _productToCartResponse.value = carRepository.addProductToCart(body)
        }
    }
}