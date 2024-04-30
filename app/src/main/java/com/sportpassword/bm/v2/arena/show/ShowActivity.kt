package com.sportpassword.bm.v2.arena.show

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.sportpassword.bm.Views.IconTextText2
import com.sportpassword.bm.databinding.ActivityArenaShowBinding
import com.sportpassword.bm.extensions.formattedWithSeparator
import com.sportpassword.bm.v2.base.BaseActivity

class ShowActivity : BaseActivity() {

    private lateinit var binding: ActivityArenaShowBinding
    private lateinit var viewModel: ViewModel
    private var token: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent.hasExtra("token")) {
            token = intent.getStringExtra("token")!!
        }

        binding = ActivityArenaShowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.top.apply {
            setTitle("球館")
            showPrev(true)
        }

        viewModel = ViewModelProvider(this, ViewModelFactory(token, Repository(), error)).get(ViewModel::class.java)
        viewModel.showDao.observe(this, Observer {
            binding.top.setTitle(it.data.name)
            binding.nameTV.text = it.data.name
            binding.cityNameTV.setShow(it.data.zone.city_name)
            binding.areaNameTV.setShow(it.data.zone.area_name)
            binding.addressTV.setShow(it.data.zone.city_name + it.data.zone.area_name + it.data.zip + it.data.road)
            binding.telTV.setShow(it.data.tel)
            binding.timeTV.setShow(it.data.open_time + " ～ " + it.data.close_time)

            val block: String = if (it.data.block <= 0) "未提供" else it.data.block.toString()
            binding.blockTV.setShow(block)

            val bathroom: String = if (it.data.bathroom == -1) "未提供" else if (it.data.bathroom == 0) "無" else it.data.block.toString()
            binding.bathroomTV.setShow(bathroom)

            val air_condition: String = if (it.data.air_condition <= 0) "未提供" else it.data.air_condition.toString()
            binding.airconditionTV.setShow(air_condition)

            val parking: String = if (it.data.parking <= 0) "未提供" else it.data.parking.toString()
            binding.parkingTV.setShow(parking)

            binding.fbTV.setShow(it.data.fb)
            binding.youtubeTV.setShow(it.data.youtube)
            binding.lineTV.setShow(it.data.line)
            binding.websiteTV.setShow(it.data.website)
            binding.pvTV.setShow(it.data.pv.formattedWithSeparator())

            val imageList = ArrayList<SlideModel>()
            val images: ArrayList<ShowDao.Image> = it.data.images
            for (image in images) {
                imageList.add(SlideModel(image.path, ScaleTypes.FIT))
            }
            binding.images.setImageList(imageList)

            binding.chargeTV.text = it.data.charge
            binding.contentTV.text = it.data.content
        })
    }
}