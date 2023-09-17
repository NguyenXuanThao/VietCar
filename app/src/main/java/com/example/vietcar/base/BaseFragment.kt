package com.example.vietcar.base

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.vietcar.common.DataLocal

abstract class BaseFragment<VB : ViewBinding>(
    private val bindingInflater: (inflater: LayoutInflater) -> VB,
) : Fragment() {

    private var _binding: VB? = null

    val binding
        get() = _binding as VB

    override fun onAttach(context: Context) {
        super.onAttach(context)

        Log.d("BaseFragment", "onAttach")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(inflater)

        Log.d("BaseFragment", "onCreateView")

        if (_binding == null)
            throw IllegalArgumentException("Binding can not be null")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("BaseFragment", "onViewCreated")

        checkLogin()
        obServerLivedata()
        initData()
        initView()
        evenClick()
    }

    open fun checkLogin() {
        val sharedPreferences =
            requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token_customer", "")

        DataLocal.STATUS = sharedPreferences.getInt("status_key", 1)

        DataLocal.BEARER_TOKEN = "Bearer $token"
    }

    open fun obServerLivedata() {}

    open fun initData() {}

    open fun initView() {}

    open fun evenClick() {}

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}