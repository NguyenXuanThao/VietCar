package com.example.vietcar.base.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
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

        requestWindowFeature(Window.FEATURE_NO_TITLE)

        val rootView = layoutInflater.inflate(R.layout.dialog_success, null, false)

        val tvContentSuccess = rootView.findViewById<TextView>(R.id.tvContentSuccess)
        tvContentSuccess.text = successContent

        val btnOK = rootView.findViewById<TextView>(R.id.tvSuccess)

        btnOK.setOnClickListener {
            dismiss()

            callBack.clickSwitchScreen()
        }
        setContentView(rootView)
    }

    interface TransitToOtherScreen {
        fun clickSwitchScreen()
    }
}