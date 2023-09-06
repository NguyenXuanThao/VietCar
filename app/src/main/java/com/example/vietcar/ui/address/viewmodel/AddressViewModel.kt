package com.example.vietcar.ui.address.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vietcar.common.Resource
import com.example.vietcar.data.model.address.ListAddress
import com.example.vietcar.di.repository.ICarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject


@HiltViewModel
class AddressViewModel @Inject constructor(
    private val carRepository: ICarRepository
) : ViewModel() {

    private val _addressResponse: MutableLiveData<Resource<ListAddress>> = MutableLiveData()
    val addressResponse
        get() = _addressResponse

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
}