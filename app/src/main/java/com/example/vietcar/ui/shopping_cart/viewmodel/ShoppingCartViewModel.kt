package com.example.vietcar.ui.shopping_cart.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private val _productResponse: MutableLiveData<ListProduct> = MutableLiveData()
    val productResponse
        get() = _productResponse

    private val _productOfCartResponse: MutableLiveData<ProductToCart> = MutableLiveData()
    val productOfCartResponse
        get() = _productOfCartResponse

    fun getProductShoppingCart() {
        viewModelScope.launch {
            _productResponse.value = carRepository.getProductShoppingCart()
        }
    }

    fun updateQuantity(body: ProductOfCartBody) {
        viewModelScope.launch {
            _productOfCartResponse.value = carRepository.updateQuantity(body)
        }
    }

    fun deleteProductOfCart(cartId : Int) {
        viewModelScope.launch {
            _productOfCartResponse.value = carRepository.deleteProductOfCart(cartId)
        }
    }
}