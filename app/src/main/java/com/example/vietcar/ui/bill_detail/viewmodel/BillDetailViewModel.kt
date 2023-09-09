package com.example.vietcar.ui.bill_detail.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vietcar.common.Resource
import com.example.vietcar.data.model.bill_detail.BillDetail
import com.example.vietcar.di.repository.ICarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class BillDetailViewModel @Inject constructor(
    private val carRepository: ICarRepository
) : ViewModel() {

    private val _billDetailResponse: MutableLiveData<Resource<BillDetail>> = MutableLiveData()
    val billDetailResponse
        get() = _billDetailResponse


    fun getBillDetail(id: Int) {
        viewModelScope.launch {
            try {
                val billDetail = carRepository.getBillDetail(id)
                _billDetailResponse.postValue(Resource.Success(billDetail))
            } catch (exception: Exception) {
                _billDetailResponse.postValue(Resource.Error("Lỗi mạng: ${exception.message}"))
            }
        }
    }
}