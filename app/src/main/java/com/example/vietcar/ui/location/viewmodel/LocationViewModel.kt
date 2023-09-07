package com.example.vietcar.ui.location.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vietcar.common.Resource
import com.example.vietcar.data.model.address.AddressBody
import com.example.vietcar.data.model.address.AddressResult
import com.example.vietcar.data.model.address.ListAddress
import com.example.vietcar.data.model.location.city.ListCity
import com.example.vietcar.di.repository.ICarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LocationViewModel @Inject constructor(
    private val carRepository: ICarRepository
) : ViewModel() {

    private val _addressResponse: MutableLiveData<Resource<AddressResult>> = MutableLiveData()
    val addressResponse
        get() = _addressResponse

    fun addAddress(body: AddressBody) {
        viewModelScope.launch {
            try {
                val addressData = carRepository.addAddress(body)
                _addressResponse.postValue(Resource.Success(addressData))
            } catch (exception: Exception) {
                _addressResponse.postValue(Resource.Error("Lỗi mạng: ${exception.message}"))
            }
        }
    }
}