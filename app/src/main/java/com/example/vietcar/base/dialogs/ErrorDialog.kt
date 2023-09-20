package com.example.vietcar.base.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.TextView
import com.example.vietcar.R

class ErrorDialog(
    context: Context,
    private val errorContent: String,
    private val textButton: String? = null
): Dialog(context) {

    @SuppressLint("MissingInflatedId", "InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_error)

        val tvContentError = findViewById<TextView>(R.id.tvContentError)
        tvContentError.text = errorContent

        val btnOK = findViewById<TextView>(R.id.tvOk)
        textButton?.let {
            btnOK.text = textButton
        }
        btnOK.setOnClickListener {
            dismiss()
        }
    }
}