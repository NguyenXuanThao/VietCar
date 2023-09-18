package com.example.vietcar.ui.search.fragment

import android.annotation.SuppressLint
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vietcar.R
import com.example.vietcar.base.BaseFragment
import com.example.vietcar.click.DeleteSearchHistory
import com.example.vietcar.data.model.search_history.SearchHistoryEntity
import com.example.vietcar.databinding.FragmentSearchBinding
import com.example.vietcar.ui.search.adapter.SearchHistoryAdapter
import com.example.vietcar.ui.search.viewmodel.SearchViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(
    FragmentSearchBinding::inflate
), DeleteSearchHistory {

    private val searchViewModel: SearchViewModel by viewModels()

    private lateinit var bottomNavigationView: BottomNavigationView

    private var searchHistoryAdapter = SearchHistoryAdapter(this)

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

        searchViewModel.textResponse.observe(viewLifecycleOwner) { textList ->

            searchHistoryAdapter.differ.submitList(textList)
            binding.rvSearchHistory.adapter = searchHistoryAdapter
            binding.rvSearchHistory.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun initData() {
        super.initData()

        searchViewModel.getText()
    }

    override fun initView() {
        super.initView()

        bottomNavigationView = requireActivity().findViewById(R.id.bottomNav)
        bottomNavigationView.visibility = View.GONE
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun evenClick() {
        super.evenClick()

        binding.cvBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.edtSearch.requestFocus()

        binding.edtSearch.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (binding.edtSearch.right - binding.edtSearch.compoundDrawables[2].bounds.width())) {
                    handleSearchIconClick()

                    return@setOnTouchListener true
                }
            }
            return@setOnTouchListener false
        }
    }

    override fun deleteSearchHistoryClick(searchHistoryEntity: SearchHistoryEntity) {
        searchViewModel.deleteText(searchHistoryEntity)
    }

    private fun handleSearchIconClick() {

        Log.d("SearchFragment", "icon search click")

        val text = binding.edtSearch.text.toString()

        val action = SearchFragmentDirections.actionSearchFragmentToDetailSearchFragment(text)
        findNavController().navigate(action)

        if (text.isNotEmpty()) {
            val searchHistoryEntity = SearchHistoryEntity(query = text)

            searchViewModel.insertText(searchHistoryEntity)
        }
    }

}