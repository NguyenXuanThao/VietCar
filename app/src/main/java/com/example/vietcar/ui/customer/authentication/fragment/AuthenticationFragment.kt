package com.example.vietcar.ui.customer.authentication.fragment

import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import com.example.vietcar.R
import com.example.vietcar.base.BaseFragment
import com.example.vietcar.common.Resource
import com.example.vietcar.common.Utils
import com.example.vietcar.data.model.authentication.AuthenticationBody
import com.example.vietcar.databinding.FragmentAuthenticationBinding
import com.example.vietcar.ui.customer.authentication.viewmodel.AuthenticationViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@AndroidEntryPoint
class AuthenticationFragment : BaseFragment<FragmentAuthenticationBinding>(
    FragmentAuthenticationBinding::inflate
) {

    private lateinit var currentPhotoPath: String

    private var imagePortrait: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val uri = currentPhotoPath.let { Uri.fromFile(File(it)) }
            if (uri != null) {
                binding.imgPortrait.scaleType = ImageView.ScaleType.FIT_XY
                binding.imgPortrait.setImageURI(uri)
            }
        }
    }

    private var imageFront: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
//            val data: Intent? = result.data
//            if (data != null) {
//                val selectedImage = data.extras?.get("data") as Bitmap
//                binding.imgFront.scaleType = ImageView.ScaleType.FIT_XY
//                binding.imgFront.setImageBitmap(selectedImage)
//            }

            val uri = currentPhotoPath.let { Uri.fromFile(File(it)) }
            if (uri != null) {
                binding.imgFront.scaleType = ImageView.ScaleType.FIT_XY
                binding.imgFront.setImageURI(uri)
            }
        }
    }

    private var imageBack: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
//            val data: Intent? = result.data
//            if (data != null) {
//                val selectedImage = data.extras?.get("data") as Bitmap
//                binding.imgBack.scaleType = ImageView.ScaleType.FIT_XY
//                binding.imgBack.setImageBitmap(selectedImage)
//            }

            val uri = currentPhotoPath.let { Uri.fromFile(File(it)) }
            if (uri != null) {
                binding.imgBack.scaleType = ImageView.ScaleType.FIT_XY
                binding.imgBack.setImageURI(uri)
            }
        }
    }

    private var name: String? = null
    private var birthday: String? = null
    private var idCard: String? = null
    private var issueDate: String? = null
    private var issueBy: String? = null
    private var address: String? = null
    private var gender: String? = null
    private var portraitFile: File? = null
    private var frontPhotoFile: File? = null
    private var backPhotoFile: File? = null

    private val authenticationViewModel: AuthenticationViewModel by viewModels()

    override fun obServerLivedata() {
        super.obServerLivedata()

        authenticationViewModel.authenticationStatusResponse.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {

                    Log.d(
                        "AuthenticationFragment",
                        "status: ${resource.data?.status}; message: ${resource.data?.message}"
                    )
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

    override fun initData() {
        super.initData()
    }

    override fun initView() {
        super.initView()

        handleCheckerRadio()
    }

    override fun evenClick() {
        super.evenClick()

        binding.imgPortrait.setOnClickListener {
            takePortraitPhoto()
        }

        binding.imgFront.setOnClickListener {
            takeFrontPhoto()
        }

        binding.imgBack.setOnClickListener {
            takeBackPhoto()
        }

        binding.btnConfirm.setOnClickListener {

            setData()
            val body = AuthenticationBody(
                name,
                birthday,
                idCard,
                issueDate,
                issueBy,
                address,
                gender,
                portraitFile,
                frontPhotoFile,
                backPhotoFile
            )

            Log.d("AuthenticationFragment", body.toString())

            authenticationViewModel.authentication(body)
        }
    }

    private fun setData() {
        name = binding.textIPName.editText!!.text.toString()
        birthday = binding.textIPBirthDay.editText!!.text.toString()
        idCard = binding.textIDCard.editText!!.text.toString()
        issueDate = binding.textIPIssueDate.editText!!.text.toString()
        issueBy = binding.textIPIssueBy.editText!!.text.toString()
        address = binding.textIPAddress.editText!!.text.toString()
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

    private fun takePortraitPhoto() {
//        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        imagePortrait.launch(cameraIntent)

        val photoFile: File? = try {
            createImageFile()
        } catch (ex: IOException) {
            // Error occurred while creating the File
            null
        }

        if (photoFile != null) {

            portraitFile = photoFile

            val photoURI: Uri = FileProvider.getUriForFile(
                requireContext(),
                "your.package.name.provider",
                photoFile
            )
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            imagePortrait.launch(cameraIntent)
        }
    }

    private fun createImageFile(): File {
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? =
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for using with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    private fun takeFrontPhoto() {
//        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        imageFront.launch(intent)

        val photoFile: File? = try {
            createImageFile()
        } catch (ex: IOException) {
            // Error occurred while creating the File
            null
        }

        if (photoFile != null) {

            frontPhotoFile = photoFile

            val photoURI: Uri = FileProvider.getUriForFile(
                requireContext(),
                "your.package.name.provider",
                photoFile
            )
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            imageFront.launch(cameraIntent)
        }
    }

    private fun takeBackPhoto() {
//        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        imageBack.launch(intent)

        val photoFile: File? = try {
            createImageFile()
        } catch (ex: IOException) {
            // Error occurred while creating the File
            null
        }

        if (photoFile != null) {

            backPhotoFile = photoFile

            val photoURI: Uri = FileProvider.getUriForFile(
                requireContext(),
                "your.package.name.provider",
                photoFile
            )
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            imageBack.launch(cameraIntent)
        }

    }
}