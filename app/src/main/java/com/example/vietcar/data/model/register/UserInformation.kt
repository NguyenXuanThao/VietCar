package com.example.vietcar.data.model.register

data class UserInformation(
    val id: Int,
    val name: String?,
    val phone: String?,
    val email: String?,
    val gender: String?,
    val birthday: String?,
    val code: String?
)