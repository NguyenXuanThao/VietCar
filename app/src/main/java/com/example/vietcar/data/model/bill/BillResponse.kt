package com.example.vietcar.data.model.bill

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BillResponse(
    val `data`: Data,
    val message: String?,
    val status: Int?
) : Parcelable