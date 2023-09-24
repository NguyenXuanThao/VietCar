package com.example.vietcar.ui.customer.login

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.vietcar.R
import com.example.vietcar.base.BaseFragment
import com.example.vietcar.base.dialogs.ErrorDialog
import com.example.vietcar.data.model.login.LoginBody
import com.example.vietcar.databinding.FragmentLoginBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(
    FragmentLoginBinding::inflate
) {

    private lateinit var bottomNavigationView: BottomNavigationView
    private val loginViewModel: LoginViewModel by viewModels()

    override fun obServerLivedata() {
        super.obServerLivedata()

        loginViewModel.loginResponse.observe(this) { loginResponse ->

            if (loginResponse.status == 0) {
                saveData(loginResponse.status, loginResponse.token!!)
                findNavController().popBackStack()

            } else {
                loginResponse.message?.let { showDialogError(it) }
            }
        }
    }

    override fun initData() {
        super.initData()

        binding.btnLogin.setOnClickListener {
            val phoneNumber = binding.textIPPhone.editText?.text.toString()
            val password = binding.textIPPassword.editText?.text.toString()

            val bodyLogin = LoginBody(phone = phoneNumber, password = password)

            loginViewModel.login(bodyLogin)
        }
    }

    override fun initView() {
        super.initView()

        bottomNavigationView = requireActivity().findViewById(R.id.bottomNav)
        bottomNavigationView.visibility = View.GONE
    }

    override fun evenClick() {
        super.evenClick()

        binding.imgBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.tvTransitToRegister.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            findNavController().navigate(action)
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