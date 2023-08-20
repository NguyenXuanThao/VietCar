package com.example.vietcar.activities.register

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import com.example.vietcar.R
import com.example.vietcar.base.dialogs.ErrorDialog
import com.example.vietcar.data.model.register.RegisterBody
import com.example.vietcar.data.model.register.RegisterResponse
import com.example.vietcar.databinding.ActivityRegisterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observerLiveData()
        initData()
        eventClick()
    }

    override fun onStart() {
        super.onStart()

        window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.statusBarColor = Color.TRANSPARENT
    }

    private fun observerLiveData() {

        registerViewModel.registerResponse.observe(this) { registerResponse ->
            if (registerResponse.status == 0) {
                Log.d("ThaoNX", "Chuyển màn hình")
            }else {
                showDialogError(registerResponse.message)
            }
        }
    }

    private fun initData() {

    }

    private fun eventClick() {

        binding.btnRegister.setOnClickListener {

            val phoneNumber = binding.textIPPhone.editText?.text.toString()
            val password = binding.textIPPassword.editText?.text.toString()
            val confirmPassword = binding.textIPConfirmPassword.editText?.text.toString()

            val registerBody = RegisterBody(phone = phoneNumber, password = password)

            if (!phoneNumber.matches(Regex("\\d{9,}"))) {
                showDialogError("Số điện thoại không hợp lệ!")
            } else {
                if (password.length < 8) {
                    showDialogError("Mật khẩu bao gồm tối thiểu 8 kí tự!")
                } else {
                    if (password == confirmPassword) {
                        registerViewModel.register(registerBody)
                    } else {
                        showDialogError("Mật khẩu không trùng khớp!")
                    }
                }
            }
        }
    }

    private fun showDialogError(content: String) {
        val errorDialog = ErrorDialog(this, content)
        errorDialog.show()
        errorDialog.window?.setGravity(Gravity.CENTER)
        errorDialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

}