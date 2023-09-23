package com.example.vietcar.ui.update_info.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vietcar.common.Resource
import com.example.vietcar.data.model.account.AccountBody
import com.example.vietcar.data.model.account.AccountInformation
import com.example.vietcar.di.repository.ICarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateInfoViewModel @Inject constructor(
    private val carRepository: ICarRepository
) : ViewModel() {

    private val _accountInformationResponse: MutableLiveData<Resource<AccountInformation>> =
        MutableLiveData()
    val accountInformationResponse
        get() = _accountInformationResponse

    fun updateInfo(body: AccountBody) {
        viewModelScope.launch {
            try {
                val accountInformationData = carRepository.updateInfo(body)
                _accountInformationResponse.postValue(Resource.Success(accountInformationData))
            } catch (exception: Exception) {
                _accountInformationResponse.postValue(Resource.Error("Lỗi mạng: ${exception.message}"))
            }
        }
    }
}