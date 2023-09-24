package com.example.vietcar.ui.contact

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.vietcar.R
import com.example.vietcar.base.BaseFragment
import com.example.vietcar.databinding.FragmentContactBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class ContactFragment : BaseFragment<FragmentContactBinding>(
    FragmentContactBinding::inflate
) {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun initView() {
        super.initView()

        bottomNavigationView = requireActivity().findViewById(R.id.bottomNav)
        bottomNavigationView.visibility = View.GONE

    }

    override fun evenClick() {
        super.evenClick()

        binding.imgBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.cvHotline.setOnClickListener {
            val phoneNumber = "0349398702"
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
            startActivity(intent)
        }

        binding.cvMessenger.setOnClickListener {
            val facebookProfileUrl = "https://www.facebook.com/xuanthao1996/"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(facebookProfileUrl))
            startActivity(intent)
        }

        binding.cvZalo.setOnClickListener {
            val intent = requireActivity().packageManager.getLaunchIntentForPackage("com.zing.zalo")
            if (intent != null) {
                startActivity(intent)
            }
        }
    }

}