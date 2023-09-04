package com.example.vietcar.ui.shopping_cart.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vietcar.common.Resource
import com.example.vietcar.data.model.product.ListProduct
import com.example.vietcar.data.model.product.ProductOfCartBody
import com.example.vietcar.data.model.product_to_cart.ProductToCart
import com.example.vietcar.di.repository.CarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ShoppingCartViewModel @Inject constructor(
    private val carRepository: CarRepository
) : ViewModel() {

    private val _productResponse: MutableLiveData<Resource<ListProduct>> = MutableLiveData()
    val productResponse
        get() = _productResponse

    private val _productOfCartResponse: MutableLiveData<Resource<ProductToCart>> = MutableLiveData()
    val productOfCartResponse
        get() = _productOfCartResponse

    fun getProductShoppingCart() {
        viewModelScope.launch {

            try {
                val productData = carRepository.getProductShoppingCart()
                _productResponse.postValue(Resource.Success(productData))
            } catch (exception: Exception) {
                _productResponse.postValue(Resource.Error("Lỗi mạng: ${exception.message}"))
            }
        }
    }

    fun updateQuantity(body: ProductOfCartBody) {
        viewModelScope.launch {

            try {
                val productOfCartData = carRepository.updateQuantity(body)
                _productOfCartResponse.postValue(Resource.Success(productOfCartData))
            } catch (exception: Exception) {
                _productOfCartResponse.postValue(Resource.Error("Lỗi mạng: ${exception.message}"))
            }
        }
    }

    fun deleteProductOfCart(cartId: Int) {
        viewModelScope.launch {

            try {
                val productOfCartData = carRepository.deleteProductOfCart(cartId)
                _productOfCartResponse.postValue(Resource.Success(productOfCartData))
            } catch (exception: Exception) {
                _productOfCartResponse.postValue(Resource.Error("Lỗi mạng: ${exception.message}"))
            }
        }
    }
}