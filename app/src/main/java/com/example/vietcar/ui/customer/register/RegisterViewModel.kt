package com.example.vietcar.ui.customer.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vietcar.data.model.register.RegisterBody
import com.example.vietcar.data.model.register.RegisterResponse
import com.example.vietcar.di.repository.ICarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val carRepository: ICarRepository
) : ViewModel() {

    private val _registerResponse: MutableLiveData<RegisterResponse> = MutableLiveData()
    val registerResponse
        get() = _registerResponse

    fun register(registerBody: RegisterBody) {
        viewModelScope.launch {
            _registerResponse.value = carRepository.register(registerBody)
        }
    }
}