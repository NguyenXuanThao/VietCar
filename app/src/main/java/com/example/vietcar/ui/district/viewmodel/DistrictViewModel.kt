package com.example.vietcar.ui.district.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vietcar.common.Resource
import com.example.vietcar.data.model.location.district.ListDistrict
import com.example.vietcar.di.repository.ICarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DistrictViewModel @Inject constructor(
    private val carRepository: ICarRepository
) : ViewModel() {

    private val _districtResponse: MutableLiveData<Resource<ListDistrict>> = MutableLiveData()
    val districtResponse
        get() = _districtResponse

    fun getDistrict(provinceCode: String) {
        viewModelScope.launch {
            try {
                val districtData = carRepository.getDistrict(provinceCode)
                _districtResponse.postValue(Resource.Success(districtData))
            } catch (exception: Exception) {
                _districtResponse.postValue(Resource.Error("Lỗi mạng: ${exception.message}"))
            }
        }
    }
}