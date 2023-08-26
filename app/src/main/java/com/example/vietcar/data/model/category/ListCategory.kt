package com.example.vietcar.data.model.category

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class ListCategory(
    val `data`: List<Category>,
    val message: String,
    val status: Int
) : Parcelable