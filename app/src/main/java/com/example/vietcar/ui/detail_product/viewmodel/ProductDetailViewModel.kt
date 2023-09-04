package com.example.vietcar.ui.detail_product.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vietcar.common.Resource
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

    private val _relatedProductResponse: MutableLiveData<Resource<ListProduct>> = MutableLiveData()
    val relatedProductResponse
        get() = _relatedProductResponse

    private val _productToCartResponse: MutableLiveData<Resource<ProductToCart>> = MutableLiveData()
    val productToCartResponse
        get() = _productToCartResponse

    fun getRelatedProducts(productId: String) {
        viewModelScope.launch {

            try {
                val relateProductData = carRepository.getRelatedProducts(productId)
                _relatedProductResponse.postValue(Resource.Success(relateProductData))
            } catch (exception: Exception) {
                _relatedProductResponse.postValue(Resource.Error("Lỗi mạng: ${exception.message}"))
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