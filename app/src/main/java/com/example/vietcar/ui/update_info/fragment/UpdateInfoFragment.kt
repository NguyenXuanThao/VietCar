package com.example.vietcar.ui.update_info.fragment


import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.vietcar.R
import com.example.vietcar.base.BaseFragment
import com.example.vietcar.base.dialogs.SuccessDialog
import com.example.vietcar.common.Resource
import com.example.vietcar.common.Utils
import com.example.vietcar.data.model.account.AccountBody
import com.example.vietcar.databinding.FragmentUpdateInfoBinding
import com.example.vietcar.ui.update_info.viewmodel.UpdateInfoViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateInfoFragment : BaseFragment<FragmentUpdateInfoBinding>(
    FragmentUpdateInfoBinding::inflate
), SuccessDialog.TransitToOtherScreen {

    private lateinit var bottomNavigationView: BottomNavigationView

    private val updateInfoViewModel: UpdateInfoViewModel by viewModels()

    private lateinit var frameLayout: FrameLayout

    private var name: String? = null
    private var email: String? = null
    private var birthday: String? = null
    private var gender: String? = null

    private lateinit var body: AccountBody

    private fun setVisibility(isLoading: Boolean) {
        frameLayout.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun obServerLivedata() {
        super.obServerLivedata()

        frameLayout = requireActivity().findViewById(R.id.frameLayout)

        updateInfoViewModel.accountInformationResponse.observe(viewLifecycleOwner) { resource ->

            setVisibility(resource is Resource.Loading)

            when (resource) {
                is Resource.Success -> {

                    val accountResponse = resource.data

                    if (accountResponse?.status == 0) {
                        Utils.showDialogSuccess(requireContext(), this, accountResponse.message!!)
                    } else {
                        Utils.showDialogError(requireContext(), accountResponse!!.message!!)
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

    override fun initView() {
        super.initView()

        bottomNavigationView = requireActivity().findViewById(R.id.bottomNav)
        bottomNavigationView.visibility = View.GONE

        handleCheckerRadio()
    }

    override fun evenClick() {
        super.evenClick()

        binding.btnSave.setOnClickListener {
            setData()
            Log.d("UpdateInfoFragment", body.toString())
            updateInfoViewModel.updateInfo(body)
        }
    }

    private fun setData() {

        name = binding.textIPName.editText!!.text.toString()
        email = binding.textIPEmail.editText!!.text.toString()
        birthday = binding.textIPBirthday.editText!!.text.toString()

        body = AccountBody(birthday, email, gender, name)


    }

    private fun handleCheckerRadio() {
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->

            when (checkedId) {

                R.id.radioButtonMale -> {
                    gender = "0"
                }

                R.id.radioButtonFemale -> {
                    gender = "1"
                }

                R.id.radioButtonOther -> {
                    gender = "2"
                }
            }
        }
    }

    override fun clickSwitchScreen() {
        val action = UpdateInfoFragmentDirections.actionUpdateInfoFragmentToBottomNavAccount()
        findNavController().navigate(action)
    }

}