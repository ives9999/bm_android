package com.sportpassword.bm.Controllers

import android.content.Intent
import android.os.Bundle
import com.facebook.internal.Utility
import com.sportpassword.bm.R
import tw.com.ecpay.paymentgatewaykit.manager.*

class PaymentVC : BaseActivity() {

    var ecpay_token: String = ""
    var order_token: String = ""
    var tokenExpireDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_vc)

        if (intent.hasExtra("ecpay_token")) {
            ecpay_token = intent.getStringExtra("ecpay_token")!!
        }
        if (intent.hasExtra("order_token")) {
            order_token = intent.getStringExtra("order_token")!!
        }
        if (intent.hasExtra("tokenExpireDate")) {
            tokenExpireDate = intent.getStringExtra("tokenExpireDate")!!
        }

        val title: String = getString(R.string.app_name)
        setMyTitle(title)

        PaymentkitManager.initialize(this, ServerType.Stage)
        PaymentkitManager.createPayment(this, "", ecpay_token, LanguageCode.zhTW, true, title, PaymentkitManager.RequestCode_CreatePayment)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        println("onActivityResult(), requestCode:" + requestCode + ", resultCode" + resultCode)
        if (requestCode == PaymentkitManager.RequestCode_CreatePayment) {
            PaymentkitManager.createPaymentResult(this, resultCode, data, CallbackFunction {
                when (it.callbackStatus) {
                    CallbackStatus.Success -> if (it.getRtnCode() == 1) {
                        val sb = StringBuffer()
                        sb.append("PaymentType:")
                        sb.append("\r\n")
                        sb.append(getPaymentTypeName(it.getPaymentType()))
                        sb.append("\r\n")
                        sb.append("\r\n")
                        sb.append("OrderInfo.MerchantTradeNo")
                        sb.append("\r\n")
                        sb.append(it.getOrderInfo().getMerchantTradeNo())
                        sb.append("\r\n")
                        sb.append("OrderInfo.TradeNo")
                        sb.append("\r\n")
                        sb.append(it.getOrderInfo().getMerchantTradeNo())
                        if (it.getPaymentType() == PaymentType.CreditCard ||
                                it.getPaymentType() == PaymentType.CreditInstallment ||
                                it.getPaymentType() == PaymentType.PeriodicFixedAmount ||
                                it.getPaymentType() == PaymentType.NationalTravelCard
                        ) {
                            sb.append("\r\n")
                            sb.append("\r\n")
                            sb.append("CardInfo.AuthCode")
                            sb.append("\r\n")
                            sb.append(it.getCardInfo().getAuthCode())
                            sb.append("\r\n")
                            sb.append("CardInfo.Gwsr")
                            sb.append("\r\n")
                            sb.append(it.getCardInfo().getGwsr())
                            sb.append("\r\n")
                            sb.append("CardInfo.ProcessDate")
                            sb.append("\r\n")
                            sb.append(it.getCardInfo().getProcessDate())
                            sb.append("\r\n")
                            sb.append("CardInfo.Amount")
                            sb.append("\r\n")
                            sb.append(it.getCardInfo().getAmount())
                            sb.append("\r\n")
                            sb.append("CardInfo.Eci")
                            sb.append("\r\n")
                            sb.append(it.getCardInfo().getEci())
                            sb.append("\r\n")
                            sb.append("CardInfo.Card6No")
                            sb.append("\r\n")
                            sb.append(it.getCardInfo().getCard6No())
                            sb.append("\r\n")
                            sb.append("CardInfo.Card4No")
                            sb.append("\r\n")
                            sb.append(it.getCardInfo().getCard4No())
                        }
                        if (it.getPaymentType() == PaymentType.CreditCard) {
                            sb.append("\r\n")
                            sb.append("CardInfo.RedDan")
                            sb.append("\r\n")
                            sb.append(it.getCardInfo().getRedDan())
                            sb.append("\r\n")
                            sb.append("CardInfo.RedDeAmt")
                            sb.append("\r\n")
                            sb.append(it.getCardInfo().getRedDeAmt())
                            sb.append("\r\n")
                            sb.append("CardInfo.RedOkAmt")
                            sb.append("\r\n")
                            sb.append(it.getCardInfo().getRedOkAmt())
                            sb.append("\r\n")
                            sb.append("CardInfo.RedYet")
                            sb.append("\r\n")
                            sb.append(it.getCardInfo().getRedYet())
                        }
                        if (it.getPaymentType() == PaymentType.CreditInstallment) {
                            sb.append("\r\n")
                            sb.append("CardInfo.Stage")
                            sb.append("\r\n")
                            sb.append(it.getCardInfo().getStage())
                            sb.append("\r\n")
                            sb.append("CardInfo.Stast")
                            sb.append("\r\n")
                            sb.append(it.getCardInfo().getStast())
                            sb.append("\r\n")
                            sb.append("CardInfo.Staed")
                            sb.append("\r\n")
                            sb.append(it.getCardInfo().getStaed())
                        }
                        if (it.getPaymentType() == PaymentType.ATM) {
                            sb.append("\r\n")
                            sb.append("\r\n")
                            sb.append("ATMInfo.BankCode")
                            sb.append("\r\n")
                            sb.append(it.getAtmInfo().getBankCode())
                            sb.append("\r\n")
                            sb.append("ATMInfo.vAccount")
                            sb.append("\r\n")
                            sb.append(it.getAtmInfo().getvAccount())
                            sb.append("\r\n")
                            sb.append("ATMInfo.ExpireDate")
                            sb.append("\r\n")
                            sb.append(it.getAtmInfo().getExpireDate())
                        }
                        if (it.getPaymentType() == PaymentType.CVS) {
                            sb.append("\r\n")
                            sb.append("\r\n")
                            sb.append("CVSInfo.PaymentNo")
                            sb.append("\r\n")
                            sb.append(it.getCvsInfo().getPaymentNo())
                            sb.append("\r\n")
                            sb.append("CVSInfo.ExpireDate")
                            sb.append("\r\n")
                            sb.append(it.getCvsInfo().getExpireDate())
                            sb.append("\r\n")
                            sb.append("CVSInfo.PaymentURL")
                            sb.append("\r\n")
                            sb.append(it.getCvsInfo().getPaymentURL())
                        }
                        if (it.getPaymentType() == PaymentType.Barcode) {
                            sb.append("\r\n")
                            sb.append("\r\n")
                            sb.append("BarcodeInfo.ExpireDate")
                            sb.append("\r\n")
                            sb.append(it.getBarcodeInfo().getExpireDate())
                            sb.append("\r\n")
                            sb.append("BarcodeInfo.Barcode1")
                            sb.append("\r\n")
                            sb.append(it.getBarcodeInfo().getBarcode1())
                            sb.append("\r\n")
                            sb.append("BarcodeInfo.Barcode2")
                            sb.append("\r\n")
                            sb.append(it.getBarcodeInfo().getBarcode2())
                            sb.append("\r\n")
                            sb.append("BarcodeInfo.Barcode3")
                            sb.append("\r\n")
                            sb.append(it.getBarcodeInfo().getBarcode3())
                        }
                        warning(sb.toString())
                    } else {
                        val sb = StringBuffer()
                        sb.append(it.getRtnCode())
                        sb.append("\r\n")
                        sb.append(it.getRtnMsg())
                        warning(sb.toString())
                    }

                    CallbackStatus.Fail -> {
                        warning("Fail Code=" + it.getRtnCode() +
                                ", Msg=" + it.getRtnMsg())
                    }

                    CallbackStatus.Cancel -> {
                        warning("交易取消")
                    }
                }

            })
        }
    }

    fun getPaymentTypeName(paymentType: PaymentType): String {
        return when(paymentType) {
            PaymentType.CreditCard -> "信用卡"
            PaymentType.CreditInstallment -> "信用卡分期"
            PaymentType.ATM -> "ATM虛擬帳號"
            PaymentType.CVS -> "超商代碼"
            PaymentType.Barcode -> "超商條碼"
            PaymentType.PeriodicFixedAmount -> "信用卡定期定額"
            PaymentType.NationalTravelCard -> "國旅卡"
            else -> ""
        }
    }
}


























