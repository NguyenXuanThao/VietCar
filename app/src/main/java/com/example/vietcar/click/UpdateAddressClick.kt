package com.example.vietcar.click

import android.view.View
import com.example.vietcar.data.model.address.Address

interface UpdateAddressClick {

    fun expandClick(address: Address, anchorView: View)
}