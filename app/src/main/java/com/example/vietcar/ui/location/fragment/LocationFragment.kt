package com.example.vietcar.ui.location.fragment


import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.vietcar.R
import com.example.vietcar.base.BaseFragment
import com.example.vietcar.base.dialogs.SuccessDialog
import com.example.vietcar.common.Resource
import com.example.vietcar.common.Utils
import com.example.vietcar.data.model.address.AddressBody
import com.example.vietcar.data.model.location.city.City
import com.example.vietcar.data.model.location.district.District
import com.example.vietcar.data.model.location.wards.Wards
import com.example.vietcar.databinding.FragmentLocationBinding
import com.example.vietcar.ui.location.viewmodel.LocationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationFragment : BaseFragment<FragmentLocationBinding>(
    FragmentLocationBinding::inflate
), SuccessDialog.TransitToOtherScreen {

    private val locationViewModel: LocationViewModel by viewModels()

    private var provinceCode = "-1"
    private var districtCode = "-1"
    private var wardsCode = "-1"


    private lateinit var frameLayout: FrameLayout

    override fun obServerLivedata() {
        super.obServerLivedata()

        frameLayout = requireActivity().findViewById(R.id.frameLayout)

        locationViewModel.addressResponse.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {

                    if (resource.data?.status == 0) {

                        Utils.showDialogSuccess(
                            requireContext(),
                            this,
                            "Cập nhật địa chỉ thành công!"
                        )

                        frameLayout.visibility = View.GONE
                    }
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

    override fun initData() {
        super.initData()

    }

    override fun initView() {
        super.initView()

        checkCityResult()

        checkDistrictResult()

        checkWardsResult()
    }

    override fun evenClick() {
        super.evenClick()

        binding.edtCity.setOnClickListener {
            val action = LocationFragmentDirections.actionLocationFragmentToCityFragment()
            findNavController().navigate(action)
        }

        binding.edtDistrict.setOnClickListener {
            val action =
                LocationFragmentDirections.actionLocationFragmentToDistrictFragment(provinceCode)
            findNavController().navigate(action)
        }

        binding.edtWards.setOnClickListener {
            val action =
                LocationFragmentDirections.actionLocationFragmentToWardsFragment(districtCode)
            findNavController().navigate(action)
        }

        binding.btnConfirm.setOnClickListener {

            val name = binding.edtName.text.toString()
            val phone = binding.edtPhone.text.toString()
            val address = binding.edtAddress.text.toString()

            if (name.isEmpty() || phone.isEmpty() || address.isEmpty() || provinceCode == "-1" || districtCode == "-1" || wardsCode == "-1") {
                Toast.makeText(requireContext(), "Bạn chưa nhập đủ thông tin!", Toast.LENGTH_SHORT)
                    .show()
            } else {

                val body =
                    AddressBody(address, provinceCode, name, phone, districtCode, "0", wardsCode)

                Log.d("LocationFragment", body.toString())

                locationViewModel.addAddress(body)
            }

        }


    }

    private fun checkCityResult() {

        parentFragmentManager.setFragmentResultListener(
            "cityResult",
            viewLifecycleOwner
        ) { _, result ->
            val cityData = result.getParcelable<City>("cityData")

            binding.edtCity.setText(cityData!!.name)

            provinceCode = cityData.code!!

            Log.d("ThaoNX", "city: ${cityData.name} code: ${cityData.code}")
        }
    }

    private fun checkDistrictResult() {
        parentFragmentManager.setFragmentResultListener(
            "districtResult",
            viewLifecycleOwner
        ) { _, result ->
            val districtData = result.getParcelable<District>("districtData")

            binding.edtDistrict.setText(districtData!!.name)

            districtCode = districtData.code!!

            Log.d("ThaoNX", "city: ${districtData.name} code: ${districtData.code}")
        }
    }

    private fun checkWardsResult() {
        parentFragmentManager.setFragmentResultListener(
            "wardsResult",
            viewLifecycleOwner
        ) { _, result ->
            val wardsData = result.getParcelable<Wards>("wardsData")

            binding.edtWards.setText(wardsData!!.name)

            wardsCode = wardsData.code!!

            Log.d("ThaoNX", "city: ${wardsData.name} code: ${wardsData.code}")
        }
    }

    override fun clickSwitchScreen() {
        findNavController().popBackStack()
    }

}