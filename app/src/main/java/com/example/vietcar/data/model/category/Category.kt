package com.example.vietcar.data.model.category

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    val avatar: String?,
    val description: String?,
    val id: Int?,
    val name: String?,
    val status: Int?
): Parcelable