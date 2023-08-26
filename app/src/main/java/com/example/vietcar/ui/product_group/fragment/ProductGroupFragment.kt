package com.example.vietcar.ui.product_group.fragment

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.vietcar.R
import com.example.vietcar.base.BaseFragment
import com.example.vietcar.databinding.FragmentProductGroupBinding
import com.example.vietcar.ui.product.adapter.ProductAdapter
import com.example.vietcar.ui.product_group.viewmodel.ProductGroupViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductGroupFragment : BaseFragment<FragmentProductGroupBinding>(
    FragmentProductGroupBinding::inflate
) {

    private val productAdapter = ProductAdapter()

    private var groupId: String? = null

    private var groupPosition: Int? = null

    private val productGroupViewModel: ProductGroupViewModel by viewModels()

    private val args: ProductGroupFragmentArgs by navArgs()

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onResume() {
        super.onResume()

        bottomNavigationView = requireActivity().findViewById(R.id.bottomNav)
        bottomNavigationView.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        bottomNavigationView.visibility = View.VISIBLE
    }

    override fun initView() {
        super.initView()

        bottomNavigationView = requireActivity().findViewById(R.id.bottomNav)
        bottomNavigationView.visibility = View.GONE
    }

    override fun obServerLivedata() {
        super.obServerLivedata()

        groupId = args.groupId

        groupPosition = args.position

        productGroupViewModel.productResponse.observe(viewLifecycleOwner) { products ->

            binding.tvTitle.text = products.data[groupPosition!!].name

            productAdapter.differ.submitList(products.data)
            binding.rvProductGroupFragment.adapter = productAdapter
            binding.rvProductGroupFragment.layoutManager = GridLayoutManager(requireContext(), 2)

        }
    }

    override fun initData() {
        super.initData()

        groupId?.let { productGroupViewModel.getListProductGroup(it) }

    }

}