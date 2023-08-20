package com.example.vietcar.data.model.login

import com.example.vietcar.common.DataLocal

data class LoginBody(
    val phone: String,
    val password: String,
    val tokenfirebase: String = DataLocal.TOKEN_FIREBASE,
    val uuid: String = DataLocal.UUID,
    val os: String = DataLocal.OS
)
