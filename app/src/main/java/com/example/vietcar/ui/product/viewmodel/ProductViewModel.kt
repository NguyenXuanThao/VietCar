package com.example.vietcar.ui.product.viewmodel

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
class ProductViewModel @Inject constructor(
    private val carRepository: CarRepository
) : ViewModel() {

    private val _productResponse: MutableLiveData<ListProduct> = MutableLiveData()
    val productResponse
        get() = _productResponse

    private val _productToCartResponse: MutableLiveData<ProductToCart> = MutableLiveData()
    val productToCartResponse
        get() = _productToCartResponse

    fun getAllProduct() {
        viewModelScope.launch {
            _productResponse.value = carRepository.getAllProduct()
        }
    }

    fun addProductToCart(body: ProductBody) {
        viewModelScope.launch {
            _productToCartResponse.value = carRepository.addProductToCart(body)
        }
    }
}