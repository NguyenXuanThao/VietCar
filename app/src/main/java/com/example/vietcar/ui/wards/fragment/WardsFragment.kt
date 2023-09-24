package com.example.vietcar.ui.wards.fragment


import android.util.Log
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
import com.example.vietcar.click.ItemWardsClick
import com.example.vietcar.common.Resource
import com.example.vietcar.common.Utils
import com.example.vietcar.data.model.location.wards.Wards
import com.example.vietcar.databinding.FragmentWardsBinding
import com.example.vietcar.ui.wards.adapter.WardsAdapter
import com.example.vietcar.ui.wards.viewmodel.WardsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WardsFragment : BaseFragment<FragmentWardsBinding>(
    FragmentWardsBinding::inflate
), ItemWardsClick {

    private val args: WardsFragmentArgs by navArgs()

    private val wardsViewModel: WardsViewModel by viewModels()

    private val wardsAdapter = WardsAdapter(this)

    private var districtCode: String? = null

    private lateinit var frameLayout: FrameLayout

    override fun obServerLivedata() {
        super.obServerLivedata()

        districtCode = args.districtCode

        frameLayout = requireActivity().findViewById(R.id.frameLayout)
        frameLayout.visibility = View.VISIBLE

        wardsViewModel.wardsResponse.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {

                    wardsAdapter.differ.submitList(resource.data?.data?.data)

                    binding.rvWards.adapter = wardsAdapter
                    binding.rvWards.layoutManager = LinearLayoutManager(requireContext())
                    binding.rvWards.addItemDecoration(
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

        Log.d("WardsFragment", districtCode.toString())

        if (districtCode != "-1") {
            wardsViewModel.getWards(districtCode!!)
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

    override fun onItemWardsClick(wards: Wards) {
        backToLocationScreen(wards)
    }

    private fun backToLocationScreen(wards: Wards) {

        val args = bundleOf("wardsData" to wards)

        setFragmentResult("wardsResult", args)

        findNavController().popBackStack()
    }

}