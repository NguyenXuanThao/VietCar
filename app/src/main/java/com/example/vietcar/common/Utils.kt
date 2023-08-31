package com.example.vietcar.common

import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import com.example.vietcar.base.dialogs.ConfirmDialog

object Utils {

    fun showDialogConfirm(context: Context, callBack: ConfirmDialog.ConfirmCallback) {

        val confirmDialog = ConfirmDialog(
            context,
            callBack,
            "Bạn chưa đăng nhập. Đăng nhập ngay bây gi để thực hiện chức năng này?",
            "Đồng ý",
            "Hủy"
        )
        confirmDialog.show()
        confirmDialog.window?.setGravity(Gravity.CENTER)
        confirmDialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}