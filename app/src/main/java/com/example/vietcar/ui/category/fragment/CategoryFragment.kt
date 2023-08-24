package com.example.vietcar.ui.category.fragment

import android.annotation.SuppressLint
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.addCallback
import androidx.core.view.GravityCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.vietcar.R
import com.example.vietcar.base.BaseFragment
import com.example.vietcar.data.model.category.Category
import com.example.vietcar.databinding.FragmentCategoryBinding
import com.example.vietcar.ui.category.viewmodel.CategoryViewModel
import com.example.vietcar.ui.product.adapter.ProductAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CategoryFragment : BaseFragment<FragmentCategoryBinding>(
    FragmentCategoryBinding::inflate
) {
    private val categoryViewModel: CategoryViewModel by viewModels()
    private var productAdapter = ProductAdapter()
    private val args: CategoryFragmentArgs by navArgs()
    private var category: Category? = null

    val categoryList = listOf(
        Category(
            avatar = "https://vietcargroup.com/avatar/1669603756.png",
            null,
            null,
            "Gía đỡ, số điện thoại-Tâu sạc",
            null
        ),
        Category(
            avatar = "https://vietcargroup.com/avatar/1669603756.png",
            null,
            null,
            "Fragment 1",
            null
        ),
        Category(
            avatar = "https://vietcargroup.com/avatar/1669603756.png",
            null,
            null,
            "Fragment 1",
            null
        ),
        Category(
            avatar = "https://vietcargroup.com/avatar/1669603756.png",
            null,
            null,
            "Fragment 1",
            null
        ),
        Category(
            avatar = "https://vietcargroup.com/avatar/1669603756.png",
            null,
            null,
            "Fragment 1",
            null
        ),
        Category(
            avatar = "https://vietcargroup.com/avatar/1669603756.png",
            null,
            null,
            "Fragment 1",
            null
        ),
    )

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onDestroy() {
        super.onDestroy()
        bottomNavigationView.visibility = View.VISIBLE
    }

    @SuppressLint("SuspiciousIndentation")
    override fun obServerLivedata() {

        category = args.category

        binding.tvTitle.text = category?.name

        categoryViewModel.productResponse.observe(viewLifecycleOwner) { products ->
            productAdapter.differ.submitList(products.data)
            binding.rvCategoryFragment.adapter = productAdapter
            binding.rvCategoryFragment.layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    override fun initData() {

        category?.let { categoryViewModel.getAllProduct(it.id.toString()) }
    }

    override fun initView() {
        super.initView()

        bottomNavigationView = requireActivity().findViewById(R.id.bottomNav)
        bottomNavigationView.visibility = View.GONE

        setUpNavigationView()

        openCloseMenu()
    }

    private fun openCloseMenu() {
        binding.imgCategory.setOnClickListener {
            if (binding.drawer.isDrawerOpen(GravityCompat.START)) {
                binding.drawer.closeDrawer(GravityCompat.START)
            } else {
                binding.drawer.openDrawer(GravityCompat.START)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (binding.drawer.isDrawerOpen(GravityCompat.START)) {
                binding.drawer.closeDrawer(GravityCompat.START)
            } else {
                findNavController().navigateUp()
            }
        }
    }

    private fun setUpNavigationView() {
        val menu = binding.navigationView.menu

        categoryList.forEachIndexed { index, menuItemObject ->
            val menuItem = menu.add(R.id.dynamic_group, Menu.NONE, index, null)
            menuItem.setActionView(R.layout.custom_layout_menu)
            val actionView = menuItem.actionView

            val iconImageView = actionView?.findViewById<ImageView>(R.id.imgCategory)
            val textTextView = actionView?.findViewById<TextView>(R.id.tvNameCategory)

            Glide.with(requireContext()).load(menuItemObject.avatar)
                .into(iconImageView!!)
            textTextView?.text = menuItemObject.name

            actionView.tag = index

            actionView.setOnClickListener { view ->
                binding.drawer.closeDrawer(GravityCompat.START)

                val selectedItemIndex = view?.tag as? Int

                if (selectedItemIndex != null) {
                    Log.d("Menu Click", "Selected item index: $selectedItemIndex")
                } else {
                    Log.d("Menu Click", "Tag is null or not an Int")
                }
            }
        }
    }


}