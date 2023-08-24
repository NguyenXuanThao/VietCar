package com.example.vietcar.data.model.register

import com.example.vietcar.common.DataLocal

data class RegisterBody(
    val phone: String?,
    val password: String?,
    val tokenfirebase: String = DataLocal.TOKEN_FIREBASE,
    val uuid: String = DataLocal.UUID,
    val os: String = DataLocal.OS
)
