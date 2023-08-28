package com.example.vietcar.base.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import com.example.vietcar.R

class AddProductDialog(
    context: Context,
    private val callback: AddProductCallBack?,
    private val name: String?,
    private val price: String?,
    private val avatar: String?,
    private val positiveButtonTitle: String
) : Dialog(context) {

    private var number = 1

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.dialog_add_product)

        val productName = findViewById<TextView>(R.id.tvName)
        val productPrice = findViewById<TextView>(R.id.tvPrice)
        val productNumber = findViewById<TextView>(R.id.tvProductNumberDialog)
        val productImage = findViewById<ImageView>(R.id.imgProductDialog)

        val price = price?.let { String.format("%,d đ", it.toLong()) }

        productName.text = name
        productPrice.text = price
        productNumber.text = number.toString()

        val uriImage = "https://vietcargroup.com${avatar}"
        Glide.with(context).load(uriImage)
            .into(productImage)


        val btnNegative = findViewById<CardView>(R.id.cvDeleteProduct)
        btnNegative.setOnClickListener {
            dismiss()
        }

        val btnPositive = findViewById<TextView>(R.id.btnAddProduct)
        btnPositive.text = positiveButtonTitle
        btnPositive.setOnClickListener {
            callback?.clickAddProduct(number)
            dismiss()
        }

        val btnDecrease = findViewById<ImageView>(R.id.imgDecrease)

        btnDecrease.setOnClickListener {
            if (number > 1) {
                number--
            }

            productNumber.text = number.toString()
        }

        val btnIncrease = findViewById<ImageView>(R.id.imgIncrease)

        btnIncrease.setOnClickListener {
            number++
            productNumber.text = number.toString()
        }
    }

    interface AddProductCallBack {
        fun clickAddProduct(number: Int)
    }
}