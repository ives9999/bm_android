package com.sportpassword.bm.v2.arena.read

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sportpassword.bm.databinding.ActivityArenaBinding
import com.sportpassword.bm.v2.arena.ArenaViewModelFactory

class ReadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArenaBinding
    private lateinit var viewModel: ViewModel
    private lateinit var adapter: Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityArenaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.top.apply {
            setTitle("球館")
            showPrev(true)
        }

        binding.bottom.apply {

        }

        viewModel = ViewModelProvider(this, ArenaViewModelFactory(Reposity())).get(
            ViewModel::class.java)
        adapter = Adapter(viewModel)
        binding.recyclerView.adapter = adapter

//        viewModel.status.observe(this, Observer {
//            println("status: $it")
//        })

        viewModel.readDao.observe(this, Observer {
            println("rows: $it")

            adapter.readDao = it
            adapter.notifyDataSetChanged()
        })

        viewModel.isEmpty.observe(this, Observer {
            if (it) {
                binding.recyclerView.visibility = View.INVISIBLE
                binding.empty.visibility = View.INVISIBLE
            } else {
                binding.recyclerView.visibility = View.VISIBLE
                binding.empty.visibility = View.INVISIBLE
            }
        })
    }
}