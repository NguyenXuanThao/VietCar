package com.example.vietcar.base.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.TextView
import com.example.vietcar.R

class ConfirmDialog(
    context: Context,
    private val callback: ConfirmCallback?,
    private val message: String?,
    private val positiveButtonTitle: String,
    private val negativeButtonTitle: String
) : Dialog(context) {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_confirm)

        val tvTitle = findViewById<TextView>(R.id.tvContent)
        tvTitle.text = message


        val btnNegative = findViewById<TextView>(R.id.tvCancel)
        btnNegative.text = negativeButtonTitle
        btnNegative.setOnClickListener {
            dismiss()
        }

        val btnPositive = findViewById<TextView>(R.id.tvOk)
        btnPositive.text = positiveButtonTitle
        btnPositive.setOnClickListener {
            callback?.confirmTranSitToLoginScreen()
            dismiss()
        }
    }

    interface ConfirmCallback {
        fun confirmTranSitToLoginScreen()
    }
}