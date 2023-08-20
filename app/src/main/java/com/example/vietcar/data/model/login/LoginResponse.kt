package com.example.vietcar.data.model.login

data class LoginResponse(
    val status: Int,
    val message: String,
    val token: String?,
    val data: UserInformation?
)
