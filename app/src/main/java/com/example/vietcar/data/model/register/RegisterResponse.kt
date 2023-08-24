package com.example.vietcar.data.model.register

data class RegisterResponse(
    val `data`: UserInformation,
    val message: String?,
    val status: Int?,
    val token: String?
)