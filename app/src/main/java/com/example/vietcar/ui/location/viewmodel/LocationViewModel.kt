package com.example.vietcar.ui.location.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vietcar.common.Resource
import com.example.vietcar.data.model.location.city.ListCity
import com.example.vietcar.di.repository.ICarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LocationViewModel @Inject constructor(
    private val carRepository: ICarRepository
) : ViewModel() {

    private val _cityResponse: MutableLiveData<Resource<ListCity>> = MutableLiveData()
    val cityResponse
        get() = _cityResponse

    fun getCity() {
        viewModelScope.launch {
            try {
                val cityData = carRepository.getCity()
                _cityResponse.postValue(Resource.Success(cityData))
            } catch (exception: Exception) {
                _cityResponse.postValue(Resource.Error("Lỗi mạng: ${exception.message}"))
            }
        }
    }
}