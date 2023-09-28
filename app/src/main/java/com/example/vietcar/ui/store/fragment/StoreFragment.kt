package com.example.vietcar.ui.store.fragment


import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vietcar.R
import com.example.vietcar.base.BaseFragment
import com.example.vietcar.common.Resource
import com.example.vietcar.common.Utils
import com.example.vietcar.data.model.store.StoreBody
import com.example.vietcar.databinding.FragmentProductBinding
import com.example.vietcar.databinding.FragmentStoreBinding
import com.example.vietcar.ui.store.adapter.StoreAdapter
import com.example.vietcar.ui.store.viewmodel.StoreViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel

@AndroidEntryPoint
class StoreFragment : BaseFragment<FragmentStoreBinding>(
    FragmentStoreBinding::inflate
) {

    private val storeViewModel: StoreViewModel by viewModels()

    private val storeAdapter = StoreAdapter()

    private lateinit var frameLayout: FrameLayout

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {

        val body = StoreBody(20, 0)

        storeViewModel.getAllStore(body)
        super.onCreate(savedInstanceState)
    }

    override fun obServerLivedata() {
        super.obServerLivedata()

        frameLayout = requireActivity().findViewById(R.id.frameLayout)
        frameLayout.visibility = View.VISIBLE

        storeViewModel.storeResponse.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    storeAdapter.differ.submitList(resource.data?.data)
                    binding.rvStore.adapter = storeAdapter
                    binding.rvStore.layoutManager =
                        GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)
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

    override fun initView() {
        super.initView()

        bottomNavigationView = requireActivity().findViewById(R.id.bottomNav)
        bottomNavigationView.visibility = View.VISIBLE
    }

}