package com.example.vietcar.ui.payment.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vietcar.common.Resource
import com.example.vietcar.data.model.address.ListAddress
import com.example.vietcar.data.model.bill_detail.BillDetail
import com.example.vietcar.data.model.bill_detail.OrderBody
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

    private val _addressResponse: MutableLiveData<Resource<ListAddress>> = MutableLiveData()
    val addressResponse
        get() = _addressResponse

    private val _billResponse: MutableLiveData<Resource<BillDetail>> = MutableLiveData()
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

    fun getAddress() {
        viewModelScope.launch {
            try {
                val addressData = carRepository.getAddress()
                _addressResponse.postValue(Resource.Success(addressData))
            } catch (exception: java.lang.Exception) {
                _addressResponse.postValue(Resource.Error("Lỗi mạng: ${exception.message}"))
            }
        }
    }

    fun orderProduct(body: OrderBody) {
        viewModelScope.launch {
            try {
                val billData = carRepository.orderProduct(body)
                _billResponse.postValue(Resource.Success(billData))
            } catch (exception: java.lang.Exception) {
                _billResponse.postValue(Resource.Error("Lỗi mạng: ${exception.message}"))
            }
        }
    }
}