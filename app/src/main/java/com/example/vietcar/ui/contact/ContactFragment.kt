package com.example.vietcar.ui.contact

import android.content.Intent
import android.net.Uri
import androidx.navigation.fragment.findNavController
import com.example.vietcar.base.BaseFragment
import com.example.vietcar.databinding.FragmentContactBinding


class ContactFragment : BaseFragment<FragmentContactBinding>(
    FragmentContactBinding::inflate
) {


    override fun evenClick() {
        super.evenClick()

        binding.cvBack.setOnClickListener {
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