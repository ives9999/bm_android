package com.sportpassword.bm.v2.arena.read

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sportpassword.bm.databinding.ActivityArenaReadBinding
import com.sportpassword.bm.extensions.Alert
import com.sportpassword.bm.v2.arena.ArenaViewModelFactory
import com.sportpassword.bm.v2.arena.show.ShowActivity
import com.sportpassword.bm.v2.error.Error

class ReadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArenaReadBinding
    private lateinit var viewModel: ViewModel
    private lateinit var adapter: Adapter
    var error: Error = Error(0, "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityArenaReadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.top.apply {
            setTitle("球館")
            showPrev(true)
        }

        binding.bottom.apply {

        }

        viewModel = ViewModelProvider(this, ArenaViewModelFactory(Reposity(), error)).get(
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
            println(it)
            if (it) {
                binding.recyclerView.visibility = View.INVISIBLE
                binding.empty.visibility = View.INVISIBLE
            } else {
                binding.recyclerView.visibility = View.VISIBLE
                binding.empty.visibility = View.INVISIBLE
            }
        })

        viewModel.isShowError.observe(this, Observer {
            if (it) {
                Alert.warning(this, error.msg)
            }
        })

        viewModel.isToShow.observe(this, Observer { event ->
            event.getContentIfNotHandled() ?. let {
                if (it) {
                    val intent = Intent(this, ShowActivity::class.java)
                    startActivity(intent)
                }
            }
        })
    }
}