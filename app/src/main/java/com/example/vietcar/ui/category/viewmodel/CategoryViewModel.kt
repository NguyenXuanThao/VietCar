package com.example.vietcar.ui.category.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vietcar.common.Resource
import com.example.vietcar.data.model.product.ListProduct
import com.example.vietcar.data.model.product.ProductBody
import com.example.vietcar.data.model.product_to_cart.ProductToCart
import com.example.vietcar.di.repository.CarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val carRepository: CarRepository
) : ViewModel() {

    private val _productResponse: MutableLiveData<Resource<ListProduct>> = MutableLiveData()
    val productResponse
        get() = _productResponse

    private val _productToCartResponse: MutableLiveData<Resource<ProductToCart>> = MutableLiveData()
    val productToCartResponse
        get() = _productToCartResponse

    fun getListProductCategory(categoryId: String) {
        viewModelScope.launch {
            try {
                val productData = carRepository.getListProductCategory(categoryId)
                _productResponse.postValue(Resource.Success(productData))
            } catch (exception: Exception) {
                _productResponse.postValue(Resource.Error("Lỗi mạng: ${exception.message}"))
            }
        }
    }

    fun addProductToCart(body: ProductBody) {
        viewModelScope.launch {
            try {
                val productToCartData = carRepository.addProductToCart(body)
                _productToCartResponse.postValue(Resource.Success(productToCartData))
            } catch (exception: Exception) {
                _productToCartResponse.postValue(Resource.Error("Lỗi mạng: ${exception.message}"))
            }
        }
    }
}