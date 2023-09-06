package com.example.vietcar.data.model.location.wards

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Wards(
    val _id: String?,
    val code: String?,
    val isDeleted: Boolean?,
    val name: String?,
    val name_with_type: String?,
    val parent_code: String?,
    val path: String?,
    val path_with_type: String?,
    val slug: String?,
    val type: String?
) : Parcelable