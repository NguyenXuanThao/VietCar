package com.example.vietcar.ui.change_password.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vietcar.common.Resource
import com.example.vietcar.data.model.account.AccountInformation
import com.example.vietcar.data.model.password.PasswordBody
import com.example.vietcar.di.repository.ICarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UpdatePasswordViewModel @Inject constructor(
    private val carRepository: ICarRepository
) : ViewModel() {

    private val _accountInformationResponse: MutableLiveData<Resource<AccountInformation>> =
        MutableLiveData()
    val accountInformationResponse
        get() = _accountInformationResponse

    fun changePassword(body: PasswordBody) {
        viewModelScope.launch {

            try {
                val accountData = carRepository.changePassword(body)
                _accountInformationResponse.postValue(Resource.Success(accountData))
            } catch (exception: Exception) {
                _accountInformationResponse.postValue(Resource.Error("Lỗi mạng: ${exception.message}"))
            }
        }
    }
}