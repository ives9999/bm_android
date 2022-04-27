package com.sportpassword.bm.Controllers

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import com.google.gson.JsonParseException
import com.sportpassword.bm.Models.ProductTable
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.ProductService
import com.sportpassword.bm.Utilities.*
import kotlinx.android.synthetic.main.activity_show_product_vc.*
import kotlinx.android.synthetic.main.activity_show_product_vc.contentView
import kotlinx.android.synthetic.main.activity_show_product_vc.refresh

class ShowProductVC: ShowVC() {

    var myTable: ProductTable? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        setContentView(R.layout.activity_show_product_vc)

        bottom_button_count = 2

        super.onCreate(savedInstanceState)
        dataService = ProductService

        refreshLayout = refresh
        setRefreshListener()

        findViewById<Button>(R.id.signupButton) ?. let {
            it.text = "放入購物車"
        }

        init()
        refresh()
    }

    override fun init() {
        isPrevIconShow = true
        super.init()
    }

    override fun setBottomButtonPadding() {
        val padding: Int = (screenWidth - bottom_button_count * button_width) / (bottom_button_count + 1)
        //val leading: Int = bottom_button_count * padding + (bottom_button_count - 1) * button_width

        findViewById<Button>(R.id.signupButton) ?. let {
            val params: ViewGroup.MarginLayoutParams = it.layoutParams as ViewGroup.MarginLayoutParams
            params.width = button_width
            params.marginStart = padding
            it.layoutParams = params
        }

        findViewById<LinearLayout>(R.id.likeButton) ?. let {
            val params: ViewGroup.MarginLayoutParams = it.layoutParams as ViewGroup.MarginLayoutParams
            params.width = button_width
            params.marginStart = padding
            it.layoutParams = params
        }
    }

//    override fun initAdapter() {}

    override fun setData() {

        if (myTable != null) {
            setImages()
        }
    }

//    override fun refresh() {
//        //super.refresh()
//        if (product_token != null) {
//            Loading.show(mask)
//            val params: HashMap<String, String> = hashMapOf("token" to product_token!!, "member_token" to member.token!!)
//            dataService.getOne(this, params) { success ->
//                if (success) {
//                    superProduct = dataService.superModel as SuperProduct
//                    if (superProduct != null) {
//                        superProduct!!.filter()
//                        //superCourse!!.print()
//                        //setMyTitle(superProduct!!.name)
//                        setFeatured()
//                        setImages()
//                        setContent()
//                    }
//                }
//                closeRefresh()
//                Loading.hide(mask)
//            }
//        } else {
//            warning("沒有接收到商品token，所以無法顯示商品內頁，請洽管理員")
//        }
//    }

    override fun genericTable() {
        //println(dataService.jsonString)
        try {
            table = jsonToModel<ProductTable>(dataService.jsonString)
        } catch (e: JsonParseException) {
            warning(e.localizedMessage!!)
            //println(e.localizedMessage)
        }
        if (table != null) {
            myTable = table as ProductTable
            myTable!!.filterRow()
        } else {
            warning("解析伺服器所傳的字串失敗，請洽管理員")
        }
    }

    fun setImages() {
        val images: ArrayList<String> = myTable!!.images
        imageContainerView.showImages(images, this)
    }

    fun signupButtonPressed(view: View) {
        //print("purchase")
        toAddCart(myTable!!.token)
    }

    fun cancelBtnPressed(view: View) {
        prev()
    }
}