package com.example.vietcar.data.model.login

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserInformation(
    val id: Int,
    val name: String?,
    val phone: String?,
    val email: String?,
    val gender: String?,
    val birthday: String?,
    val code: String?
) : Parcelable
