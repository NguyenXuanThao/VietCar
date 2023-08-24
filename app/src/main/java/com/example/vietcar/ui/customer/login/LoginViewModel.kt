package com.example.vietcar.ui.customer.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vietcar.data.model.login.LoginBody
import com.example.vietcar.data.model.login.LoginResponse
import com.example.vietcar.di.repository.CarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val carRepository: CarRepository
) : ViewModel() {

    private val _loginResponse: MutableLiveData<LoginResponse> = MutableLiveData()
    val loginResponse
        get() = _loginResponse

    fun login(bodyLogin: LoginBody) {
        viewModelScope.launch {
            _loginResponse.value = carRepository.login(bodyLogin)
        }
    }
}