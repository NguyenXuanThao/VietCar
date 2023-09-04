package com.example.vietcar.ui.category.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.view.GravityCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.vietcar.R
import com.example.vietcar.base.BaseFragment
import com.example.vietcar.base.dialogs.AddProductDialog
import com.example.vietcar.base.dialogs.ConfirmDialog
import com.example.vietcar.click.ItemShoppingCartClick
import com.example.vietcar.common.DataLocal
import com.example.vietcar.common.Resource
import com.example.vietcar.common.Utils
import com.example.vietcar.data.model.category.ListCategory
import com.example.vietcar.data.model.login.LoginResponse
import com.example.vietcar.data.model.product.Product
import com.example.vietcar.data.model.product.ProductBody
import com.example.vietcar.databinding.FragmentCategoryBinding
import com.example.vietcar.ui.category.viewmodel.CategoryViewModel
import com.example.vietcar.ui.product.adapter.ProductAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CategoryFragment : BaseFragment<FragmentCategoryBinding>(
    FragmentCategoryBinding::inflate
), ItemShoppingCartClick, ConfirmDialog.ConfirmCallback, AddProductDialog.AddProductCallBack {
    private val categoryViewModel: CategoryViewModel by viewModels()
    private val args: CategoryFragmentArgs by navArgs()
    private var position: Int? = null
    private var categories: ListCategory? = null

    private var status = 1

    private var productId = 0

    private var isClickAddProduct = false

    private var menuIndex = -2

    private var productAdapter = ProductAdapter(this)

    private lateinit var bottomNavigationView: BottomNavigationView

    private lateinit var frameLayout: FrameLayout

    override fun onResume() {
        super.onResume()

        bottomNavigationView = requireActivity().findViewById(R.id.bottomNav)
        bottomNavigationView.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        bottomNavigationView.visibility = View.VISIBLE
    }

    override fun checkLogin() {
        super.checkLogin()

        Log.d("HomeFragment", "checkLogin")

        parentFragmentManager.setFragmentResultListener(
            "loginResult", viewLifecycleOwner
        ) { _, result ->
            val loginData = result.getParcelable<LoginResponse>("loginData")

            status = loginData!!.status!!
            saveData(loginData.status!!)

            Log.d("HomeFragment", loginData.status.toString())
        }

        retrieveData()
    }

    @SuppressLint("SuspiciousIndentation")
    override fun obServerLivedata() {

        frameLayout = requireActivity().findViewById(R.id.frameLayout)
        frameLayout.visibility = View.VISIBLE

        position = args.position
        categories = args.listCategory

        if (position != null && categories!!.data.isNotEmpty()) {
            binding.tvTitle.text = categories!!.data[position!!].name
        }

        categoryViewModel.productResponse.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    productAdapter.differ.submitList(resource.data?.data)
                    binding.rvCategoryFragment.adapter = productAdapter
                    binding.rvCategoryFragment.layoutManager =
                        GridLayoutManager(requireContext(), 2)

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

        categoryViewModel.productToCartResponse.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    if (isClickAddProduct) {
                        Toast.makeText(requireContext(), resource.data?.message, Toast.LENGTH_SHORT)
                            .show()
                        isClickAddProduct = false
                    }

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

        if (menuIndex == -2) {

            categories!!.data[position!!].id?.toString()
                ?.let { categoryViewModel.getListProductCategory(it) }
        } else {
            categories!!.data[menuIndex].id?.toString()
                ?.let { categoryViewModel.getListProductCategory(it) }
        }
    }

    override fun initView() {
        super.initView()

        bottomNavigationView = requireActivity().findViewById(R.id.bottomNav)
        bottomNavigationView.visibility = View.GONE

        setUpNavigationView()

        openCloseMenu()
    }

    /**
     * translation screen
     */
    private fun transitToLoginScreen() {
        val action = CategoryFragmentDirections.actionCategoryFragmentToLoginFragment()
        findNavController().navigate(action)
    }


    /**
     * save data
     */
    private fun saveData(status: Int) {
        val sharedPreferences =
            requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putInt("status_key", status)

        editor.apply()
    }

    private fun retrieveData() {
        val sharedPreferences =
            requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        val retrievedStatus = sharedPreferences.getInt("status_key", 1)

        status = retrievedStatus
    }

    /**
     * set up navigation view
     */
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

        categories?.data?.forEachIndexed { index, menuItemObject ->
            val menuItem = menu.add(R.id.dynamic_group, Menu.NONE, index, null)
            menuItem.setActionView(R.layout.custom_layout_menu)
            val actionView = menuItem.actionView

            val iconImageView = actionView?.findViewById<ImageView>(R.id.imgCategory)
            val textTextView = actionView?.findViewById<TextView>(R.id.tvNameCategory)

            val uriImage = "https://vietcargroup.com${menuItemObject.avatar}"
            val uriScreen = "https://vietcargroup.com/avatar/1669603516.png"

            if (menuItemObject.avatar != DataLocal.EMPTY) {
                Glide.with(requireContext()).load(uriImage).into(iconImageView!!)
            } else {
                Glide.with(requireContext()).load(uriScreen).into(iconImageView!!)
            }

            textTextView?.text = menuItemObject.name

            actionView.tag = index

            actionView.setOnClickListener { view ->
                binding.drawer.closeDrawer(GravityCompat.START)

                val selectedItemIndex = view?.tag as? Int

                if (selectedItemIndex != null) {
                    menuIndex = selectedItemIndex
                    categories!!.data[selectedItemIndex].id?.toString()
                        ?.let { categoryViewModel.getListProductCategory(it) }
                    binding.tvTitle.text = categories!!.data[selectedItemIndex].name
                } else {
                    Log.d("Menu Click", "Tag is null or not an Int")
                }
            }
        }
    }

    /**
     * onItemClick listener
     */
    override fun onClickShoppingCartItem(product: Product) {
        if (status == 0) {
            showDialogProductInfo(product)
        } else {
            Utils.showDialogConfirm(requireContext(), this)
        }
    }

    /**
     * Confirm dialog
     */
    override fun confirmTranSitToLoginScreen() {
        transitToLoginScreen()
    }

    /**
     * Add Product Dialog
     */
    private fun showDialogProductInfo(product: Product) {
        productId = product.id!!
        val addProductDialog = AddProductDialog(
            requireContext(),
            this,
            product.name,
            product.net_price.toString(),
            product.avatar,
            "Thêm vào giỏ"
        )
        addProductDialog.show()
        addProductDialog.window?.setGravity(Gravity.CENTER)
        addProductDialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun clickAddProduct(number: Int) {
        isClickAddProduct = true
        Log.d("CategoryFragment", "number $number; product id $productId")

        val body = ProductBody(productId, number)
        categoryViewModel.addProductToCart(body)
    }

}