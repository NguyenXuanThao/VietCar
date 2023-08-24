package com.example.vietcar.data.model.login

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class LoginResponse(
    val status: Int?,
    val message: String?,
    val token: String?,
    val data: UserInformation?
) : Parcelable
