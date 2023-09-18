package com.example.vietcar.ui.city.fragment


import android.view.View
import android.widget.FrameLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vietcar.R
import com.example.vietcar.base.BaseFragment
import com.example.vietcar.click.ItemCityClick
import com.example.vietcar.common.Resource
import com.example.vietcar.common.Utils
import com.example.vietcar.data.model.location.city.City
import com.example.vietcar.databinding.FragmentCityBinding
import com.example.vietcar.ui.city.adapter.CityAdapter
import com.example.vietcar.ui.city.viewmodel.CityViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CityFragment : BaseFragment<FragmentCityBinding>(
    FragmentCityBinding::inflate
), ItemCityClick {

    private val cityViewModel: CityViewModel by viewModels()

    private val cityAdapter = CityAdapter(this)

    private lateinit var frameLayout: FrameLayout

    override fun obServerLivedata() {
        super.obServerLivedata()

        frameLayout = requireActivity().findViewById(R.id.frameLayout)
        frameLayout.visibility = View.VISIBLE

        cityViewModel.cityResponse.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {

                    cityAdapter.differ.submitList(resource.data?.data?.data)

                    binding.rvCity.adapter = cityAdapter
                    binding.rvCity.layoutManager = LinearLayoutManager(requireContext())
                    binding.rvCity.addItemDecoration(
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

        cityViewModel.getCity()
    }

    override fun evenClick() {
        super.evenClick()

        binding.cvBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onClickCityItem(city: City) {
        backToLocationScreen(city)
    }

    private fun backToLocationScreen(city: City) {

        val args = bundleOf("cityData" to city)

        setFragmentResult("cityResult", args)

        findNavController().popBackStack()
    }

}