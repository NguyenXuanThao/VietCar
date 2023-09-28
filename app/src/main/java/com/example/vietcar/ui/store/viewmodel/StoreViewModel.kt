package com.example.vietcar.ui.store.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vietcar.common.Resource
import com.example.vietcar.data.model.store.StoreBody
import com.example.vietcar.data.model.store.StoreData
import com.example.vietcar.di.repository.ICarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class StoreViewModel @Inject constructor(
    private val carRepository: ICarRepository
) : ViewModel() {

    private val _storeResponse: MutableLiveData<Resource<StoreData>> = MutableLiveData()
    val storeResponse
        get() = _storeResponse

    fun getAllStore(body: StoreBody) {
        viewModelScope.launch {

            try {
                val storeData = carRepository.getAllStore(body)
                _storeResponse.postValue(Resource.Success(storeData))
            } catch (exception: Exception) {
                _storeResponse.postValue(Resource.Error("Lỗi mạng: ${exception.message}"))
            }
        }
    }

}