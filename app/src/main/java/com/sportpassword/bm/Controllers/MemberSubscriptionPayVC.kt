package com.sportpassword.bm.Controllers

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.gson.Gson
import com.sportpassword.bm.Models.OrderTable
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.MemberService
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.databinding.ActivityMemberSubscriptionPayVcBinding
import com.sportpassword.bm.member

class MemberSubscriptionPayVC : BaseActivity() {

    private lateinit var binding: ActivityMemberSubscriptionPayVcBinding
    private lateinit var view: ViewGroup

    var name: String = ""
    var price: Int = 0
    var kind: String = ""

    var bottom_button_count: Int = 3
    val button_width: Int = 300

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMemberSubscriptionPayVcBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)

        name = (intent.hasExtra("name") then { intent.getStringExtra("name") }) ?: ""
        price = (intent.hasExtra("price") then { intent.getIntExtra("price", 0) }) ?: 0
        kind = (intent.hasExtra("kind") then { intent.getStringExtra("kind") }) ?: "gold"

        val kind_enum: MEMBER_SUBSCRIPTION_KIND = MEMBER_SUBSCRIPTION_KIND.stringToEnum(kind)
        val kind_chinese: String = kind_enum.chineseName

        setMyTitle(kind_chinese+"會員付款")

        findViewById<TextView>(R.id.prizeLbl) ?. let {

            var prize: String = ""
            val lottery_num: Int = kind_enum.lottery()
            val lottery_chinese: String = lottery_num.numberToChinese() + "張"

            prize += kind_chinese + "福利\n"
            prize += kind_chinese + "福利有 " + lottery_chinese + " 抽獎券，每月系統會舉行抽獎，抽獎券越多，越容易中獎。\n"

            //it.setSpecialTextColor(prize, "三張", R.color.MY_RED)
            //it.setSpecialTextBold(prize, "三張", 180)
            it.setSpecialTextColorAndBold(prize, lottery_chinese, R.color.MY_RED, 180, 20)
        }

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

            //println(member.subscription!!)
            it.setOnClickListener {
                if (member.subscription!!.isNotEmpty()) {
                    runOnUiThread {
                        warning("您已經有訂閱了，請先退訂，再重新訂閱")
                    }
                } else {

                    loadingAnimation.start()
                    MemberService.subscription(this, kind) { success ->
                        runOnUiThread {
                            loadingAnimation.stop()
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
                                warning(MemberService.msg)
                            }
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