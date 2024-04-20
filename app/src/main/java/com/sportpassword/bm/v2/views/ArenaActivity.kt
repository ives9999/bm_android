package com.sportpassword.bm.v2.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.sportpassword.bm.Views.ShowTop2
import com.sportpassword.bm.databinding.ActivityArenaBinding
import com.sportpassword.bm.v2.repositorys.ArenaReposity
import com.sportpassword.bm.v2.viewmodels.ArenaViewModel
import com.sportpassword.bm.v2.viewmodels.ArenaViewModelFactory

class ArenaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArenaBinding
    private lateinit var viewModel: ArenaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityArenaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ArenaViewModelFactory(ArenaReposity())).get(ArenaViewModel::class.java)

        binding.top.apply {
            setTitle("球館")
            showPrev(true)
        }

        binding.bottom.apply {

        }
    }
}