package com.sportpassword.bm.Controllers

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.sportpassword.bm.Adapters.GroupSection
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.OrderService
import com.sportpassword.bm.Utilities.JSONParse
import com.sportpassword.bm.Utilities.Loading
import com.sportpassword.bm.Utilities.getField
import com.sportpassword.bm.member
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.activity_payment_vc.*
import kotlinx.android.synthetic.main.mask.*
import kotlinx.android.synthetic.main.payment_cell.*
import tw.com.ecpay.paymentgatewaykit.manager.*
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

class PaymentVC : MyTableVC() {

    var ecpay_token: String = ""
    var order_token: String = ""
    var tokenExpireDate: String = ""
    var superOrder: SuperOrder? = null

    val rows1: ArrayList<ArrayList<HashMap<String, String>>> = arrayListOf(
            arrayListOf(
                    hashMapOf("name" to "商品名稱", "value" to "", "key" to "product_name"),
                    hashMapOf("name" to "商品屬性", "value" to "", "key" to "attribute")
            ),
            arrayListOf(
                    hashMapOf("name" to "訂單編號", "value" to "", "key" to "order_no"),
                    hashMapOf("name" to "商品數量", "value" to "", "key" to "quantity_show"),
                    hashMapOf("name" to "商品金額", "value" to "product_price", "key" to "product_price_show"),
                    hashMapOf("name" to "運費", "value" to "shipping_fee", "key" to "shipping_fee_show"),
                    hashMapOf("name" to "訂單總金額", "value" to "", "key" to "amount_show"),
                    hashMapOf("name" to "訂單建立時間", "value" to "", "key" to "created_at_show"),
                    hashMapOf("name" to "訂單狀態", "value" to "", "key" to "order_process_show")
            ),
            arrayListOf(
                    hashMapOf("name" to "付款方式", "value" to "", "key" to "gateway_ch"),
                    hashMapOf("name" to "付款時間", "value" to "", "key" to "payment_at_show"),
                    hashMapOf("name" to "付款狀態", "value" to "", "key" to "payment_process_show")
            ),
            arrayListOf(
                    hashMapOf("name" to "到貨方式", "value" to "", "key" to "method_ch"),
                    hashMapOf("name" to "到貨時間", "value" to "", "key" to "shipping_at_show"),
                    hashMapOf("name" to "到貨狀態", "value" to "", "key" to "shipping_process_show")
            ),
            arrayListOf(
                    hashMapOf("name" to "訂購人姓名", "value" to "", "key" to "order_name"),
                    hashMapOf("name" to "訂購人電話", "value" to "", "key" to "order_tel"),
                    hashMapOf("name" to "訂購人EMail", "value" to "", "key" to "order_email"),
                    hashMapOf("name" to "訂購人住址", "value" to "", "key" to "address")
            )
    )

    var paymentSections: ArrayList<Section> = arrayListOf(Section(), Section(), Section(), Section(), Section())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_vc)

        sections = arrayListOf("商品", "訂單", "付款", "物流", "訂購人")

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

        dataService = OrderService
        recyclerView = payment_list
        refreshLayout = refresh
        maskView = mask
        setRefreshListener()

        initAdapter()
        //refresh()

        if (ecpay_token.length > 0) {
            PaymentkitManager.initialize(this, ServerType.Prod)
            PaymentkitManager.createPayment(this, ecpay_token, LanguageCode.zhTW, false, title, PaymentkitManager.RequestCode_CreatePayment)
        } else {
            refresh()
        }
    }

    override fun refresh() {
        Loading.show(mask)
        val params: HashMap<String, String> = hashMapOf("token" to order_token!!, "member_token" to member.token!!)
        dataService.getOne(this, params) { success ->
            if (success) {
                superOrder = dataService.superModel as SuperOrder
                if (superOrder != null) {
                    superOrder!!.filter()
                    //superOrder!!.print()

                    setMyTitle(superOrder!!.product.name)
                    setupOrderData()
                    setData()
//                    println(items);
                    adapter.notifyDataSetChanged()
                }
            }
            closeRefresh()
            Loading.hide(mask)
        }
    }

    private fun setupOrderData() {

        if (superOrder != null) {
            val mirror = SuperOrder::class
            mirror.memberProperties.forEach { property->
                val label = property.name
                for ((idx, row) in rows1.withIndex()) {
                    for ((idx1, row1) in row.withIndex()) {
                        val key: String = row1["key"]!!
                        if (key == label) {
                            val type1 = getClassType(property)
                            when (type1) {
                                "String" -> {
                                    val t: String? = superOrder!!.getField<String>(label)
                                    if (t != null) {
                                        rows1[idx][idx1]["value"] = t
                                    }
                                }
                                "int" -> {
                                    val t: Int? = superOrder!!.getField<Int>(label)
                                    if (t != null) {
                                        rows1[idx][idx1]["value"] = t.toString()
                                    }
                                }
                            }
                        }
                    }
                }
            }

            //println(rows1)
        }
    }

    override fun initAdapter(include_section: Boolean) {
        adapter = GroupAdapter()
        setData()

        recyclerView.adapter = adapter
    }

    private fun setData() {
        adapter.clear()
        for ((idx, section) in sections.withIndex()) {
            val expandableGroup: ExpandableGroup = ExpandableGroup(GroupSection(section), true)
            val items: ArrayList<Item> = generateItems(idx)
            paymentSections[idx].update(items)
            //paymentSections[idx].addAll(items)
            expandableGroup.add(paymentSections[idx])
            adapter.add(expandableGroup)
        }
    }

    override fun generateItems(section: Int): ArrayList<Item> {
        val res: ArrayList<Item> = arrayListOf()
        val _rows1: ArrayList<HashMap<String, String>> = rows1[section]
        for ((idx, _rows) in _rows1.withIndex()) {
            val paymentItem: PaymentItem = PaymentItem(this, _rows);
            res.add(paymentItem)
        }

        return res
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //println("onActivityResult(), requestCode:" + requestCode + ", resultCode" + resultCode)
        if (requestCode == PaymentkitManager.RequestCode_CreatePayment) {
            PaymentkitManager.createPaymentResult(this, resultCode, data, CallbackFunction {
                when (it.callbackStatus) {
                    CallbackStatus.Success -> if (it.getRtnCode() == 1) {
                        info("完成付款，我們將儘速將商品運送給您，謝謝您的光顧!!", "", "關閉") {
                            refresh()
                            //goProduct()
                        }
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
                    else -> {
                        warning("回傳值無法解析，請洽管理員")
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

    fun successMsg(it: CreatePaymentCallbackData): String {
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

        return sb.toString()
    }

    fun getClassType(it: KProperty1<out SuperModel, Any?>): String {
        val type = it.returnType.toString()
        val tmps = JSONParse.getSubType(type)
        val subType = tmps.get("type")!!

        return subType
    }
}

class PaymentItem(val context: Context, val row: HashMap<String, String>): Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.titleLbl.text = row["name"]
        viewHolder.contentLbl.text = row["value"]
    }

    override fun getLayout() = R.layout.payment_cell
}


























