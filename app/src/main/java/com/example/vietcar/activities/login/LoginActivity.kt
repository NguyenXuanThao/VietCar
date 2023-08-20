package com.example.vietcar.activities.login

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import com.example.vietcar.activities.register.RegisterActivity
import com.example.vietcar.base.dialogs.ErrorDialog
import com.example.vietcar.data.model.login.LoginBody
import com.example.vietcar.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
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

        loginViewModel.loginResponse.observe(this) { loginResponse ->

            if (loginResponse.status == 0) {
                Log.d("ThaoNX", "Chuyển màn hình")
            } else {
                showDialogError(loginResponse.message)
            }
        }

    }

    private fun initData() {

        binding.btnLogin.setOnClickListener {
            val phoneNumber = binding.textIPPhone.editText?.text.toString()
            val password = binding.textIPPassword.editText?.text.toString()

            val bodyLogin = LoginBody(phone = phoneNumber, password = password)

            loginViewModel.login(bodyLogin)
        }
    }

    private fun eventClick() {
        binding.tvTransitToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
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