package com.example.vietcar.ui.detail_product.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vietcar.data.model.product.ListProduct
import com.example.vietcar.data.model.product.ProductBody
import com.example.vietcar.data.model.product_to_cart.ProductToCart
import com.example.vietcar.di.repository.ICarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val carRepository: ICarRepository
) : ViewModel() {

    private val _relatedProductResponse: MutableLiveData<ListProduct> = MutableLiveData()
    val relatedProductResponse
        get() = _relatedProductResponse

    private val _productToCartResponse: MutableLiveData<ProductToCart> = MutableLiveData()
    val productToCartResponse
        get() = _productToCartResponse

    fun getRelatedProducts(productId: String) {
        viewModelScope.launch {
            _relatedProductResponse.value = carRepository.getRelatedProducts(productId)
        }
    }

    fun addProductToCart(body: ProductBody) {
        viewModelScope.launch {
            _productToCartResponse.value = carRepository.addProductToCart(body)
        }
    }

}