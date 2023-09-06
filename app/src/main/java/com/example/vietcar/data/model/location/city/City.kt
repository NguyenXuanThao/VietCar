package com.example.vietcar.data.model.location.city

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class City(
    val _id: String?,
    val code: String?,
    val isDeleted: Boolean?,
    val name: String?,
    val name_with_type: String?,
    val slug: String?,
    val type: String?
) : Parcelable