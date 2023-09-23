package com.example.vietcar.ui.customer.register

import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.vietcar.base.BaseFragment
import com.example.vietcar.base.dialogs.ErrorDialog
import com.example.vietcar.data.model.register.RegisterBody
import com.example.vietcar.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>(
    FragmentRegisterBinding::inflate
) {

    private val registerViewModel: RegisterViewModel by viewModels()

    override fun obServerLivedata() {
        super.obServerLivedata()

        registerViewModel.registerResponse.observe(this) { registerResponse ->
            if (registerResponse.status == 0) {
                saveData(registerResponse.status, registerResponse.token!!)
                findNavController().popBackStack()
            } else {
                registerResponse.message?.let { showDialogError(it) }
            }
        }
    }

    override fun evenClick() {
        super.evenClick()

        binding.cvBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.tvTransitToLogin.setOnClickListener {
            findNavController().popBackStack()
        }

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
        val errorDialog = ErrorDialog(requireContext(), content)
        errorDialog.show()
        errorDialog.window?.setGravity(Gravity.CENTER)
        errorDialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    private fun saveData(status: Int, token: String) {
        val sharedPreferences =
            requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putInt("status_key", status)
        editor.putString("token_customer", token)

        editor.apply()
    }

}