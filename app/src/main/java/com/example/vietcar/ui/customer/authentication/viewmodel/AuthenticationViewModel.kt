package com.example.vietcar.ui.customer.authentication.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vietcar.common.Resource
import com.example.vietcar.data.model.authentication.AuthenticationBody
import com.example.vietcar.data.model.authentication.AuthenticationResponse
import com.example.vietcar.di.repository.ICarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val carRepository: ICarRepository
) : ViewModel() {

    private val _authenticationStatusResponse: MutableLiveData<Resource<AuthenticationResponse>> =
        MutableLiveData()
    val authenticationStatusResponse
        get() = _authenticationStatusResponse

    fun authentication(body: AuthenticationBody) {
        viewModelScope.launch {

            try {
                val authenticationStatus = carRepository.authentication(body)
                _authenticationStatusResponse.postValue(Resource.Success(authenticationStatus))
            } catch (exception: Exception) {
                _authenticationStatusResponse.postValue(Resource.Error("Lỗi mạng: ${exception.message}"))
            }

        }
    }
}