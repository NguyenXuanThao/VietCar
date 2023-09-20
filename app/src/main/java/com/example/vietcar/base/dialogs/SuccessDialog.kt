package com.example.vietcar.base.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.TextView
import com.example.vietcar.R

class SuccessDialog(
    private val callBack: TransitToOtherScreen,
    context: Context,
    private val successContent: String,
) : Dialog(context) {

    @SuppressLint("MissingInflatedId", "InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_success)

        val tvContentSuccess = findViewById<TextView>(R.id.tvContentSuccess)
        tvContentSuccess.text = successContent

        val btnOK = findViewById<TextView>(R.id.tvSuccess)

        btnOK.setOnClickListener {
            dismiss()

            callBack.clickSwitchScreen()
        }
    }

    interface TransitToOtherScreen {
        fun clickSwitchScreen()
    }
}