package com.example.vietcar.ui.district.fragment


import android.view.View
import android.widget.FrameLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vietcar.R
import com.example.vietcar.base.BaseFragment
import com.example.vietcar.click.ItemDistrictClick
import com.example.vietcar.common.Resource
import com.example.vietcar.common.Utils
import com.example.vietcar.data.model.location.district.District
import com.example.vietcar.databinding.FragmentDistrictBinding
import com.example.vietcar.ui.district.adapter.DistrictAdapter
import com.example.vietcar.ui.district.viewmodel.DistrictViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DistrictFragment : BaseFragment<FragmentDistrictBinding>(
    FragmentDistrictBinding::inflate
), ItemDistrictClick {

    private val args: DistrictFragmentArgs by navArgs()

    private val districtViewModel: DistrictViewModel by viewModels()

    private val districtAdapter = DistrictAdapter(this)

    private var provinceCode: String? = null

    private lateinit var frameLayout: FrameLayout

    override fun obServerLivedata() {
        super.obServerLivedata()

        provinceCode = args.provinceCode

        frameLayout = requireActivity().findViewById(R.id.frameLayout)
        frameLayout.visibility = View.VISIBLE

        districtViewModel.districtResponse.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {

                    districtAdapter.differ.submitList(resource.data?.data?.data)

                    binding.rvDistrict.adapter = districtAdapter
                    binding.rvDistrict.layoutManager = LinearLayoutManager(requireContext())
                    binding.rvDistrict.addItemDecoration(
                        DividerItemDecoration(
                            requireContext(),
                            DividerItemDecoration.VERTICAL
                        )
                    )

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

    override fun initData() {
        super.initData()

        if (provinceCode != "-1") {
            districtViewModel.getDistrict(provinceCode!!)
        } else {
            frameLayout.visibility = View.GONE
        }
    }

    override fun evenClick() {
        super.evenClick()

        binding.imgBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onItemDistrictClick(district: District) {
        backToLocationScreen(district)
    }

    private fun backToLocationScreen(district: District) {

        val args = bundleOf("districtData" to district)

        setFragmentResult("districtResult", args)

        findNavController().popBackStack()
    }

}