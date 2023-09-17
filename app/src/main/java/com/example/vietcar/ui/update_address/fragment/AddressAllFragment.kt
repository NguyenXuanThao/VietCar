package com.example.vietcar.ui.update_address.fragment


import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vietcar.R
import com.example.vietcar.base.BaseFragment
import com.example.vietcar.click.UpdateAddressClick
import com.example.vietcar.common.Resource
import com.example.vietcar.common.Utils
import com.example.vietcar.data.model.address.Address
import com.example.vietcar.databinding.FragmentAddressAllBinding
import com.example.vietcar.ui.update_address.adapter.AddressAllAdapter
import com.example.vietcar.ui.update_address.viewmodel.AddressAllViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddressAllFragment : BaseFragment<FragmentAddressAllBinding>(
    FragmentAddressAllBinding::inflate
), UpdateAddressClick {

    private val addressAllAdapter = AddressAllAdapter(this)

    private val addressAllViewModel: AddressAllViewModel by viewModels()

    private lateinit var frameLayout: FrameLayout

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

    override fun obServerLivedata() {
        super.obServerLivedata()

        frameLayout = requireActivity().findViewById(R.id.frameLayout)
        frameLayout.visibility = View.VISIBLE

        addressAllViewModel.addressResponse.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    addressAllAdapter.differ.submitList(resource.data?.data)

                    Log.d("obServerLivedata", resource.data?.data.toString())
                    binding.rvAddress.adapter = addressAllAdapter
                    binding.rvAddress.layoutManager =
                        LinearLayoutManager(requireContext())

                    binding.rvAddress.addItemDecoration(
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

        addressAllViewModel.addressResult.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {

                    Toast.makeText(requireContext(), resource.data?.message, Toast.LENGTH_SHORT)
                        .show()
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

        addressAllViewModel.getAddress()
    }

    override fun evenClick() {
        super.evenClick()

        binding.btnAddAddress.setOnClickListener {
            val action = AddressAllFragmentDirections.actionAddressAllFragmentToLocationFragment()
            findNavController().navigate(action)
        }
    }

    override fun expandClick(address: Address, anchorView: View) {
        showMenu(address, anchorView)
    }

    private fun showMenu(address: Address, anchorView: View) {
        val popupMenu = PopupMenu(requireContext(), anchorView)
        popupMenu.menuInflater.inflate(R.menu.update_address_menu, popupMenu.menu)

        // Set item click listeners
        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.action_update -> {
                    val action = AddressAllFragmentDirections.actionAddressAllFragmentToLocationFragment(address)
                    findNavController().navigate(action)
                    true
                }

                R.id.action_delete -> {
                    addressAllViewModel.deleteAddress(address.id!!)

                    addressAllViewModel.getAddress()
                    true
                }

                else -> false
            }
        }

        popupMenu.show()
    }
}