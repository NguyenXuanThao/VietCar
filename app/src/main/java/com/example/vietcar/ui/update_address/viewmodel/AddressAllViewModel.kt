package com.example.vietcar.ui.update_address.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vietcar.common.Resource
import com.example.vietcar.data.model.address.AddressResult
import com.example.vietcar.data.model.address.ListAddress
import com.example.vietcar.data.model.address.UpdateDeliveryAddressBody
import com.example.vietcar.di.repository.ICarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject


@HiltViewModel
class AddressAllViewModel @Inject constructor(
    private val carRepository: ICarRepository
) : ViewModel() {

    private val _addressResponse: MutableLiveData<Resource<ListAddress>> = MutableLiveData()
    val addressResponse
        get() = _addressResponse

    private val _addressResult: MutableLiveData<Resource<AddressResult>> = MutableLiveData()
    val addressResult
        get() = _addressResult

    fun getAddress() {
        viewModelScope.launch {
            try {
                val addressData = carRepository.getAddress()
                _addressResponse.postValue(Resource.Success(addressData))
            } catch (exception: Exception) {
                _addressResponse.postValue(Resource.Error("Lỗi mạng: ${exception.message}"))
            }
        }
    }

    fun deleteAddress(deliveryId: Int) {
        viewModelScope.launch {
            try {
                val addressData = carRepository.deleteAddress(deliveryId)
                _addressResult.postValue(Resource.Success(addressData))
            } catch (exception: Exception) {
                _addressResult.postValue(Resource.Error("Lỗi mạng: ${exception.message}"))
            }
        }
    }

}