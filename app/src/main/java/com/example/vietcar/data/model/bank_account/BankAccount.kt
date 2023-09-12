package com.example.vietcar.data.model.bank_account

import com.example.vietcar.R

data class BankAccount(
    val image: Int,
    val bankName: String,
    val accountOwner: String,
    val branch: String,
    val accountNumber: Int
)

object BankAccountData {

    fun getData(): ArrayList<BankAccount> {

        return arrayListOf(
            BankAccount(
                R.drawable.img_default,
                "Techcombank",
                "Nguyễn Xuân Thao",
                "Hà Nội",
                1234567889
            ),
            BankAccount(
                R.drawable.img_default,
                "Vietcombank",
                "Nguyễn Xuân Thao",
                "Hà Nội",
                1234567889
            ),
            BankAccount(
                R.drawable.img_default,
                "Agrhibank",
                "Nguyễn Xuân Thao",
                "Hà Nội",
                1234567889
            ),
            BankAccount(R.drawable.img_default, "BIDV", "Nguyễn Xuân Thao", "Hà Nội", 1234567889),
        )
    }
}
