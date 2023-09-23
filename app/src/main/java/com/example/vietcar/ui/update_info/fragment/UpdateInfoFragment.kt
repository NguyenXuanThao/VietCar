package com.example.vietcar.ui.update_info.fragment


import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.vietcar.R
import com.example.vietcar.base.BaseFragment
import com.example.vietcar.base.dialogs.SuccessDialog
import com.example.vietcar.common.Resource
import com.example.vietcar.common.Utils
import com.example.vietcar.data.model.account.AccountBody
import com.example.vietcar.databinding.FragmentUpdateInfoBinding
import com.example.vietcar.ui.update_info.viewmodel.UpdateInfoViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


@AndroidEntryPoint
class UpdateInfoFragment : BaseFragment<FragmentUpdateInfoBinding>(
    FragmentUpdateInfoBinding::inflate
), SuccessDialog.TransitToOtherScreen {

    companion object {
        private const val REQUEST_READ_EXTERNAL_STORAGE = 2
    }

    private lateinit var bottomNavigationView: BottomNavigationView

    private val updateInfoViewModel: UpdateInfoViewModel by viewModels()

    private lateinit var frameLayout: FrameLayout

    private var name: String? = null
    private var email: String? = null
    private var birthday: String? = null
    private var gender: String? = null

    private var selectedImageUri: Uri? = null

    private lateinit var body: AccountBody

    private val args: UpdateInfoFragmentArgs by navArgs()

    private fun setVisibility(isLoading: Boolean) {
        frameLayout.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                if (data != null) {
                    selectedImageUri = data.data

                    try {
                        val bitmap = MediaStore.Images.Media.getBitmap(
                            requireActivity().contentResolver,
                            selectedImageUri
                        )
                        binding.imgAvatar.setImageBitmap(bitmap)

                        val filePath = getRealPathFromURI(selectedImageUri)
                        val fileName = getFileNameFromPath(filePath)

                        val file = File(filePath!!)
                        val requestFile =
                            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)

                        val body =
                            MultipartBody.Part.createFormData("image", file.name, requestFile)

                        updateInfoViewModel.updateAvatar(image = body)

                        Log.d("ImagePickerFragment", "Đường dẫn tệp hình ảnh: $filePath")
                        Log.d("ImagePickerFragment", "Tên tệp hình ảnh: $fileName")
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check for READ_EXTERNAL_STORAGE permission
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_READ_EXTERNAL_STORAGE
            )
        } else {
            // Permission is already granted, you can perform your actions here
        }
    }

    override fun obServerLivedata() {
        super.obServerLivedata()

        frameLayout = requireActivity().findViewById(R.id.frameLayout)

        updateInfoViewModel.accountInformationResponse.observe(viewLifecycleOwner) { resource ->

            setVisibility(resource is Resource.Loading)

            when (resource) {
                is Resource.Success -> {

                    val accountResponse = resource.data

                    if (accountResponse?.status == 0) {
                        Utils.showDialogSuccess(requireContext(), this, accountResponse.message!!)
                    } else {
                        Utils.showDialogError(requireContext(), accountResponse!!.message!!)
                    }
                }

                is Resource.Error -> {
                    val errorMessage = resource.message ?: "Có lỗi mạng"
                    Utils.showDialogError(requireContext(), errorMessage)
                }

                is Resource.Loading -> {

                }
            }
        }

        updateInfoViewModel.avatarResponse.observe(viewLifecycleOwner) { resource ->

            setVisibility(resource is Resource.Loading)

            when (resource) {
                is Resource.Success -> {

                    val avatar = resource.data

                    Log.d("UpdateInfo", avatar!!.status.toString())

                }

                is Resource.Error -> {
                    val errorMessage = resource.message ?: "Có lỗi mạng"
                    Utils.showDialogError(requireContext(), errorMessage)
                }

                is Resource.Loading -> {

                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // READ_EXTERNAL_STORAGE permission granted, you can perform your actions here
                Log.d("UpdateInfoFragment", "bạn đã cấp quyền truy cập")
            } else {
                // Permission denied, handle the user's response
                Log.d("UpdateInfoFragment", "bạn chưa cấp quyền truy cập")
            }
        }
    }

    override fun initView() {
        super.initView()

        bottomNavigationView = requireActivity().findViewById(R.id.bottomNav)
        bottomNavigationView.visibility = View.GONE

        val image = args.image

        Log.d("NX4A", image.toString())

        if (image != "https://vietcargroup.comnull") {

            Glide.with(requireContext()).load(image)
                .into(binding.imgAvatar)
        }

        handleCheckerRadio()
    }

    override fun evenClick() {
        super.evenClick()

        binding.btnSave.setOnClickListener {
            setData()
            Log.d("UpdateInfoFragment", body.toString())
            updateInfoViewModel.updateInfo(body)
        }

        binding.imgCamera.setOnClickListener {
            openImagePicker()
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imagePickerLauncher.launch(intent)
    }

    private fun getRealPathFromURI(uri: Uri?): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = requireActivity().contentResolver.query(uri!!, projection, null, null, null)
        cursor?.use {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            it.moveToFirst()
            return it.getString(columnIndex)
        }
        return null
    }

    private fun getFileNameFromPath(filePath: String?): String {
        return if (filePath != null) {
            val slashIndex = filePath.lastIndexOf('/')
            if (slashIndex != -1 && slashIndex < filePath.length - 1) {
                filePath.substring(slashIndex + 1)
            } else {
                ""
            }
        } else {
            ""
        }
    }

    private fun setData() {

        name = binding.textIPName.editText!!.text.toString()
        email = binding.textIPEmail.editText!!.text.toString()
        birthday = binding.textIPBirthday.editText!!.text.toString()

        body = AccountBody(birthday, email, gender, name)


    }

    private fun handleCheckerRadio() {
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->

            when (checkedId) {

                R.id.radioButtonMale -> {
                    gender = "0"
                }

                R.id.radioButtonFemale -> {
                    gender = "1"
                }

                R.id.radioButtonOther -> {
                    gender = "2"
                }
            }
        }
    }

    override fun clickSwitchScreen() {
        val action = UpdateInfoFragmentDirections.actionUpdateInfoFragmentToBottomNavAccount()
        findNavController().navigate(action)
    }

}