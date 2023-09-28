package com.example.vietcar.ui.store_detail.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vietcar.common.Resource
import com.example.vietcar.data.model.store_detail.StoreDetailBody
import com.example.vietcar.data.model.store_detail.StoreDetailData
import com.example.vietcar.di.repository.ICarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class StoreDetailViewModel @Inject constructor(
    private val carRepository: ICarRepository
) : ViewModel() {

    private val _storeDetailResponse: MutableLiveData<Resource<StoreDetailData>> = MutableLiveData()
    val storeDetailResponse
        get() = _storeDetailResponse

    fun getStoreDetail(body: StoreDetailBody) {
        viewModelScope.launch {

            try {
                val storeDetailData = carRepository.getStoreDetail(body)
                _storeDetailResponse.postValue(Resource.Success(storeDetailData))
            } catch (exception: Exception) {
                _storeDetailResponse.postValue(Resource.Error("Lỗi mạng: ${exception.message}"))
            }
        }
    }
}