package com.sportpassword.bm.Controllers

import android.os.Bundle
import com.sportpassword.bm.Models.SuperProduct
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.ProductService
import com.sportpassword.bm.Services.StoreService
import com.sportpassword.bm.Utilities.Loading
import com.sportpassword.bm.Utilities.image
import com.sportpassword.bm.member
import kotlinx.android.synthetic.main.activity_show_product_vc.featured
import kotlinx.android.synthetic.main.activity_show_product_vc.refresh
import kotlinx.android.synthetic.main.mask.*

class ShowProductVC: BaseActivity() {

    var source: String  = "product" //course
    var product_token: String? = null    // course token
    var superProduct: SuperProduct? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_product_vc)

        if (intent.hasExtra("title")) {
            val title = intent.getStringExtra("title")
            setMyTitle(title)
        }

        if (intent.hasExtra("product_token")) {
            product_token = intent.getStringExtra("product_token")
        }
        dataService = ProductService

//        if (intent.hasExtra("sourse")) {
//            source = intent.getStringExtra("source")
//        }

        refreshLayout = refresh
        setRefreshListener()
        refresh()
    }

    override fun refresh() {
        //super.refresh()
        if (product_token != null) {
            Loading.show(mask)
            val params: HashMap<String, String> = hashMapOf("token" to product_token!!, "member_token" to member.token!!)
            dataService.getOne(this, params) { success ->
                if (success) {
                    superProduct = dataService.superModel as SuperProduct
                    if (superProduct != null) {
                        superProduct!!.filter()
                        //superCourse!!.print()
                        //setMyTitle(superProduct!!.name)
                        setFeatured()
                        setImages()
                        setContent()
                    }
                }
                closeRefresh()
                Loading.hide(mask)
            }
        }
    }

    fun setFeatured() {
        if (superProduct!!.featured_path.isNotEmpty()) {
            val featured_path = superProduct!!.featured_path
            featured_path.image(this, featured)
        } else {
            featured.setImageResource(R.drawable.loading_square_120)
        }
    }

    fun setImages() {

    }

    fun setContent() {

    }
}