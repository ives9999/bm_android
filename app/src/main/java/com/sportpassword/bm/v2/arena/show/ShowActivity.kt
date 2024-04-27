package com.sportpassword.bm.v2.arena.show

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sportpassword.bm.Views.IconTextText2
import com.sportpassword.bm.databinding.ActivityArenaShowBinding
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
        })
    }
}