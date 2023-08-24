package com.example.vietcar.ui.customer.login

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.vietcar.R
import com.example.vietcar.base.BaseFragment
import com.example.vietcar.base.dialogs.ErrorDialog
import com.example.vietcar.data.model.login.LoginBody
import com.example.vietcar.databinding.FragmentHomeBinding
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
                backToHomeFragment()
            } else {
                loginResponse.message?.let { showDialogError(it) }
            }
        }
    }

    private fun backToHomeFragment() {
        val phoneNumber = binding.textIPPhone.editText?.text.toString()
        val password = binding.textIPPassword.editText?.text.toString()

        val result = Bundle().apply {
            putString("phoneNumber", phoneNumber)
            putString("password", password)
        }

        setFragmentResult("loginResult", result)

        findNavController().popBackStack()
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

        binding.tvTransitToRegister.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            findNavController().navigate(action)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        bottomNavigationView.visibility = View.VISIBLE
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

}