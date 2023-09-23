package com.example.vietcar.ui.personal_information.fragment

import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.vietcar.R
import com.example.vietcar.base.BaseFragment
import com.example.vietcar.common.Resource
import com.example.vietcar.common.Utils
import com.example.vietcar.data.model.account.Data
import com.example.vietcar.databinding.FragmentPersonalInformationBinding
import com.example.vietcar.ui.personal_information.viewmodel.PersonalInformationViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonalInformationFragment : BaseFragment<FragmentPersonalInformationBinding>(
    FragmentPersonalInformationBinding::inflate
) {

    private val personalInformationViewModel: PersonalInformationViewModel by viewModels()

    private lateinit var bottomNavigationView: BottomNavigationView

    private lateinit var frameLayout: FrameLayout

    override fun obServerLivedata() {
        super.obServerLivedata()

        frameLayout = requireActivity().findViewById(R.id.frameLayout)
        frameLayout.visibility = View.VISIBLE

        personalInformationViewModel.accountInformationResponse.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {

                    updateData(resource.data?.data)
                    frameLayout.visibility = View.GONE
                }

                is Resource.Error -> {
                    val errorMessage = resource.message ?: "Có lỗi mạng"
                    Utils.showDialogError(requireContext(), errorMessage)
                    frameLayout.visibility = View.GONE
                }

                is Resource.Loading -> {
                    frameLayout.visibility = View.VISIBLE
                }
            }
        }
    }



    override fun evenClick() {
        super.evenClick()

        binding.cvBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun updateData(data: Data?) {

        val uriImage = "https://vietcargroup.com${data!!.image}"
        Glide.with(requireContext()).load(uriImage)
            .into(binding.imgAvatar)

        binding.tvName.text = data.name
        binding.tvPhone.text = data.phone
        binding.tvEmail.text = data.email
        binding.tvBirth.text = data.birthday

        when (data.gender) {
            0 -> {
                binding.tvGender.text = "Nam"
            }

            1 -> {
                binding.tvGender.text = "Nữ"
            }

            else -> {
                binding.tvGender.text = "khác"
            }
        }
    }

    override fun initData() {
        super.initData()

        personalInformationViewModel.getAccountInformation()
    }

    override fun initView() {
        super.initView()

        bottomNavigationView = requireActivity().findViewById(R.id.bottomNav)
        bottomNavigationView.visibility = View.GONE

    }


}