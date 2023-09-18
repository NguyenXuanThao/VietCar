package com.example.vietcar.ui.change_password.fragment


import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.vietcar.R
import com.example.vietcar.base.BaseFragment
import com.example.vietcar.common.Resource
import com.example.vietcar.common.Utils
import com.example.vietcar.data.model.password.PasswordBody
import com.example.vietcar.databinding.FragmentUpdatePasswordBinding
import com.example.vietcar.ui.change_password.viewmodel.UpdatePasswordViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UpdatePasswordFragment : BaseFragment<FragmentUpdatePasswordBinding>(
    FragmentUpdatePasswordBinding::inflate
) {

    private lateinit var bottomNavigationView: BottomNavigationView

    private val updatePasswordViewModel: UpdatePasswordViewModel by viewModels()

    override fun onResume() {
        super.onResume()

        bottomNavigationView = requireActivity().findViewById(R.id.bottomNav)
        bottomNavigationView.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        bottomNavigationView.visibility = View.VISIBLE
    }

    override fun obServerLivedata() {
        super.obServerLivedata()

        updatePasswordViewModel.accountInformationResponse.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {

                    if (resource.data?.status != 0) {
                        Utils.showDialogError(requireContext(), resource.data?.message!!)
                    }else {
                        findNavController().popBackStack()
                    }
                }

                is Resource.Error -> {
                    val errorMessage = resource.message ?: "Có lỗi mạng"
                    Utils.showDialogError(requireContext(), errorMessage)
                }

                is Resource.Loading -> {
                }
            }
        }
    }

    override fun evenClick() {
        super.evenClick()

        binding.cvBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnUpdate.setOnClickListener {

            val oldPassword = binding.textIPCurrentPass.editText!!.text.toString()
            val newPassword = binding.textIPNewPass.editText!!.text.toString()
            val confirmPassword = binding.textIDConfirm.editText!!.text.toString()

            val body = PasswordBody(oldPassword, newPassword)

            if (newPassword != confirmPassword) {
                Utils.showDialogError(requireContext(), "mật khẩu không trùng khớp")
            } else {
                updatePasswordViewModel.changePassword(body)
            }
        }
    }

    override fun initView() {
        super.initView()

        bottomNavigationView = requireActivity().findViewById(R.id.bottomNav)
        bottomNavigationView.visibility = View.GONE

    }

}