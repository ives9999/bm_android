package com.sportpassword.bm.Controllers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.sportpassword.bm.Adapters.Ad.HomeTotalAdBean
import com.sportpassword.bm.Adapters.HomeAdAdapter
import com.sportpassword.bm.R
import com.zhpan.bannerview.BannerViewPager
import java.util.ArrayList

class HomeTotalAdVC : BaseActivity() {

    protected var mDrawableList: MutableList<Int> = ArrayList()
    private val des = arrayOf("在这里\n你可以听到周围人的心声", "在这里\nTA会在下一秒遇见你", "在这里\n不再错过可以改变你一生的人")

    private lateinit var viewPager: BannerViewPager<HomeTotalAdBean>

    private val data: List<HomeTotalAdBean>
        get() {
            val list = ArrayList<HomeTotalAdBean>()
            for (i in mDrawableList.indices) {
                val customBean = HomeTotalAdBean()
                customBean.imageRes = mDrawableList[i]
                customBean.imageDescription = des[i]
                list.add(customBean)
            }
            return list
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_total_ad_vc)

        setMyTitle("廣告")
        initData()
        setupViewPager()
        viewPager.refreshData(data)
    }

    private fun setupViewPager() {
        viewPager = findViewById(R.id.ad_container)
        viewPager.apply {
            adapter = HomeAdAdapter()
            setLifecycleRegistry(lifecycle)
        }.create()
    }

    private fun initData() {
        for (i in 0..2) {
            val drawable = resources.getIdentifier("homead$i", "drawable", packageName)
            mDrawableList.add(drawable)
        }
    }

    fun submitBtnPressed(view: View) {
        goProduct()
    }

    fun cancelBtnPressed(view: View) {
        prev()
    }
}