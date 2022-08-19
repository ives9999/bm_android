package com.sportpassword.bm.Controllers

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.gson.Gson
import com.sportpassword.bm.Models.OrderTable
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.MemberService
import com.sportpassword.bm.Services.OrderService
import com.sportpassword.bm.Utilities.GATEWAY
import com.sportpassword.bm.Utilities.Loading
import com.sportpassword.bm.Utilities.then
import com.sportpassword.bm.member
import kotlinx.android.synthetic.main.mask.*

class MemberLevelUpPayVC : BaseActivity() {

    var name: String = ""
    var price: Int = 0
    var kind: String = ""

    var bottom_button_count: Int = 3
    val button_width: Int = 400

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_level_up_pay_vc)

        name = (intent.hasExtra("name") then { intent.getStringExtra("name") }) ?: ""
        price = (intent.hasExtra("price") then { intent.getIntExtra("price", 0) }) ?: 0
        kind = (intent.hasExtra("kind") then { intent.getStringExtra("kind") }) ?: "gold"

        setMyTitle(name+"會員")

        setBottomThreeView()
        init()
    }

    override fun init() {
        isPrevIconShow = true
        super.init()
    }

    fun setBottomThreeView() {
        findViewById<Button>(R.id.submitBtn) ?. let {
            it.text = "訂閱"
            it.setOnClickListener {
                Loading.show(mask)
                MemberService.subscription(this, kind) { success ->
                    runOnUiThread {
                        Loading.hide(mask)
                    }

                    if (success) {
                        if (MemberService.jsonString.isNotEmpty()) {
                            //println(MemberService.jsonString)
                            try {
                                val table: OrderUpdateResTable = Gson().fromJson(
                                    MemberService.jsonString,
                                    OrderUpdateResTable::class.java
                                )

                                if (!table.success) {
                                    runOnUiThread {
                                        warning(table.msg)
                                    }
                                } else {
                                    val orderTable: OrderTable? = table.model
                                    if (orderTable != null) {
                                        orderTable.filterRow()
                                        val ecpay_token: String = orderTable.ecpay_token
                                        val ecpay_token_ExpireDate: String =
                                            orderTable.ecpay_token_ExpireDate

                                        runOnUiThread {
                                            info("訂閱已經成立，是否前往付款？", "關閉", "付款") {
                                                toPayment(
                                                    orderTable.token,
                                                    ecpay_token,
                                                    ecpay_token_ExpireDate
                                                )
                                            }
                                        }

                                    } else {
                                        runOnUiThread {
                                            warning("無法拿到伺服器傳回值")
                                        }
                                    }
                                }
                            } catch (e: java.lang.Exception) {
                                runOnUiThread {
                                    warning(e.localizedMessage!!)
                                }
                            }
                        }
                    } else {
                        runOnUiThread {
                            warning(OrderService.msg)
                        }
                    }
                }
            }
        }

        findViewById<Button>(R.id.threeBtn) ?. let {
            it.text = "取消訂閱"
            it.setOnClickListener {
                //coinReturn()
            }
        }

        findViewById<Button>(R.id.cancelBtn) ?. let {
            it.text = "回上一頁"
            it.setOnClickListener {
                prev()
            }
        }

        setBottomButtonPadding()
    }

    fun setBottomButtonPadding() {

        val padding: Int = (screenWidth - bottom_button_count * button_width) / (bottom_button_count + 1)
        //val leading: Int = bottom_button_count * padding + (bottom_button_count - 1) * button_width

        findViewById<Button>(R.id.submitBtn) ?. let {
            if (it.visibility == View.VISIBLE) {
                val params: ViewGroup.MarginLayoutParams =
                    it.layoutParams as ViewGroup.MarginLayoutParams
                params.width = button_width
                params.marginStart = padding
                it.layoutParams = params
            }
        }

        findViewById<Button>(R.id.threeBtn) ?. let {
            if (it.visibility == View.VISIBLE) {
                val params: ViewGroup.MarginLayoutParams =
                    it.layoutParams as ViewGroup.MarginLayoutParams
                params.width = button_width
                params.marginStart = padding
                it.layoutParams = params
            }
        }

        findViewById<Button>(R.id.cancelBtn) ?. let {
            val params: ViewGroup.MarginLayoutParams = it.layoutParams as ViewGroup.MarginLayoutParams
            params.width = button_width
            params.marginStart = padding
            it.layoutParams = params
        }
    }
}