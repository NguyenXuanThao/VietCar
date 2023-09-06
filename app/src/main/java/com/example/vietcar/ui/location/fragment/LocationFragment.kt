package com.example.vietcar.ui.location.fragment


import android.util.Log
import android.widget.FrameLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.vietcar.base.BaseFragment
import com.example.vietcar.data.model.location.city.City
import com.example.vietcar.data.model.location.district.District
import com.example.vietcar.data.model.location.wards.Wards
import com.example.vietcar.databinding.FragmentLocationBinding
import com.example.vietcar.ui.location.viewmodel.LocationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationFragment : BaseFragment<FragmentLocationBinding>(
    FragmentLocationBinding::inflate
) {

    private val locationViewModel: LocationViewModel by viewModels()

    private var provinceCode = "-1"
    private var districtCode = "-1"

    private lateinit var frameLayout: FrameLayout

    override fun obServerLivedata() {
        super.obServerLivedata()

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
                LocationFragmentDirections.actionLocationFragmentToDistrictFragment(provinceCode.toString())
            findNavController().navigate(action)
        }

        binding.edtWards.setOnClickListener {
            val action =
                LocationFragmentDirections.actionLocationFragmentToWardsFragment(districtCode.toString())
            findNavController().navigate(action)
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

            districtCode = wardsData.code!!

            Log.d("ThaoNX", "city: ${wardsData.name} code: ${wardsData.code}")
        }
    }

}