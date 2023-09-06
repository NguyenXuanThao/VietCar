package com.example.vietcar.ui.payment.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vietcar.common.Resource
import com.example.vietcar.data.model.product.ListProduct
import com.example.vietcar.di.repository.ICarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val carRepository: ICarRepository
) : ViewModel() {

    private val _productResponse: MutableLiveData<Resource<ListProduct>> = MutableLiveData()
    val productResponse
        get() = _productResponse

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
}