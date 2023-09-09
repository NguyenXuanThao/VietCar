package com.example.vietcar.ui.confirm_order.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vietcar.common.Resource
import com.example.vietcar.data.model.bill.ListBill
import com.example.vietcar.di.repository.ICarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderConfirmViewModel @Inject constructor(
    private val carRepository: ICarRepository
) : ViewModel() {

    private val _billResponse: MutableLiveData<Resource<ListBill>> = MutableLiveData()
    val billResponse
        get() = _billResponse

    fun getListBill() {
        viewModelScope.launch {
            try {
                val billData = carRepository.getListBill()
                _billResponse.postValue(Resource.Success(billData))
            } catch (exception: Exception) {
                _billResponse.postValue(Resource.Error("Lỗi mạng: ${exception.message}"))
            }
        }
    }
}