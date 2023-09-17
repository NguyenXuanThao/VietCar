package com.example.vietcar.data.model.authentication

import java.io.File

data class AuthenticationBody(
    val Name: String?,
    val birthday: String?,
    val cmnd: String?,
    val issue_date: String?,
    val issued_by: String?,
    val address: String?,
    val gender: String?,
    val portrait: File?,
    val front_photo: File?,
    val back_photo: File?,
)
