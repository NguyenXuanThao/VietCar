package com.example.vietcar.ui.wards.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vietcar.common.Resource
import com.example.vietcar.data.model.location.wards.ListWards
import com.example.vietcar.di.repository.ICarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class WardsViewModel @Inject constructor(
    private val carRepository: ICarRepository
) : ViewModel() {

    private val _wardsResponse: MutableLiveData<Resource<ListWards>> = MutableLiveData()
    val wardsResponse
        get() = _wardsResponse

    fun getWards(districtCode: String) {
        viewModelScope.launch {
            try {
                val wardsData = carRepository.getWards(districtCode)
                _wardsResponse.postValue(Resource.Success(wardsData))
            } catch (exception: Exception) {
                _wardsResponse.postValue(Resource.Error("Lỗi mạng: ${exception.message}"))
            }
        }
    }
}