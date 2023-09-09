package com.example.vietcar.ui.shopping_cart.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vietcar.common.Resource
import com.example.vietcar.data.model.bill.BillBody
import com.example.vietcar.data.model.bill.BillResponse
import com.example.vietcar.data.model.product.ListProduct
import com.example.vietcar.data.model.product.ProductOfCartBody
import com.example.vietcar.data.model.product_to_cart.ProductToCart
import com.example.vietcar.di.repository.ICarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ShoppingCartViewModel @Inject constructor(
    private val carRepository: ICarRepository
) : ViewModel() {

    private val _productResponse: MutableLiveData<Resource<ListProduct>> = MutableLiveData()
    val productResponse
        get() = _productResponse

    private val _productOfCartResponse: MutableLiveData<Resource<ProductToCart>> = MutableLiveData()
    val productOfCartResponse
        get() = _productOfCartResponse

    private val _billResponse: MutableLiveData<Resource<BillResponse>> = MutableLiveData()
    val billResponse
        get() = _billResponse

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

    fun createDraftBill(body: BillBody) {
        viewModelScope.launch {

            try {
                val billData = carRepository.createDraftBill(body)
                _billResponse.postValue(Resource.Success(billData))
            } catch (exception: Exception) {
                _billResponse.postValue(Resource.Error("Lỗi mạng: ${exception.message}"))
            }
        }
    }
}