package com.example.vietcar.common

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import com.example.vietcar.base.dialogs.ConfirmDialog
import com.example.vietcar.base.dialogs.ErrorDialog
import com.example.vietcar.base.dialogs.SuccessDialog

object Utils {

    fun showDialogConfirm(
        context: Context,
        content: String,
        callBack: ConfirmDialog.ConfirmCallback
    ) {

        val confirmDialog = ConfirmDialog(
            context,
            callBack,
            content,
            "Đồng ý",
            "Hủy"
        )
        confirmDialog.show()
        confirmDialog.window?.setGravity(Gravity.CENTER)
        confirmDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        confirmDialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    fun showDialogError(context: Context, content: String) {
        val errorDialog = ErrorDialog(context, content)
        errorDialog.show()
        errorDialog.window?.setGravity(Gravity.CENTER)
        errorDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        errorDialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    fun showDialogSuccess(
        context: Context,
        callBack: SuccessDialog.TransitToOtherScreen,
        content: String
    ) {
        val successDialog = SuccessDialog(
            callBack,
            context,
            content
        )
        successDialog.show()
        successDialog.window?.setGravity(Gravity.CENTER)
        successDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        successDialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}