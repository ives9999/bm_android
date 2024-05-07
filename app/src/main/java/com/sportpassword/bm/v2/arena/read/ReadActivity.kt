package com.sportpassword.bm.v2.arena.read

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.sportpassword.bm.Utilities.LoadingAnimation
import com.sportpassword.bm.databinding.ActivityArenaReadBinding
import com.sportpassword.bm.extensions.Alert
import com.sportpassword.bm.v2.arena.show.ShowActivity
import com.sportpassword.bm.v2.base.BaseActivity
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ReadActivity : BaseActivity() {

    private lateinit var binding: ActivityArenaReadBinding
    private lateinit var viewModel: ViewModel
    private lateinit var adapter: Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityArenaReadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingAnimation = LoadingAnimation(this)

        binding.top.apply {
            setTitle("球館")
            showPrev(true)
        }

        binding.bottom.apply {

        }

        viewModel = ViewModelProvider(this, ViewModelFactory(Repository())).get(
            ViewModel::class.java)
        adapter = Adapter(viewModel)
        binding.recyclerView.adapter = adapter

        viewModel.getRead().onEach {
            adapter.submitData(it)
        }.launchIn(lifecycleScope)

        adapter.addLoadStateListener { loadState ->
            /*
            * loadState.refresh - represents the load state for loading the PagingData for the first time.
              loadState.prepend - represents the load state for loading data at the start of the list.
              loadState.append - represents the load state for loading data at the end of the list.
            * */

            if (loadState.refresh is LoadState.Loading || loadState.append is LoadState.Loading) {
                binding.recyclerView.visibility = View.INVISIBLE
                binding.empty.visibility = View.INVISIBLE
                loadingAnimation.start()
            } else {
                binding.recyclerView.visibility = View.VISIBLE
                binding.empty.visibility = View.INVISIBLE
                loadingAnimation.stop()

                // If we have an error, show a toast
                val errorState = when {
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.prepend is LoadState.Error ->  loadState.prepend as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }
                errorState?.let {
                    binding.recyclerView.visibility = View.INVISIBLE
                    binding.empty.visibility = View.INVISIBLE
                    Alert.warning(this, it.error.toString())
                }
            }
        }

//        viewModel.status.observe(this, Observer {
//            println("status: $it")
//        })

//        viewModel.readDao.observe(this, Observer {
            //println("rows: $it")

            //adapter.readDao = it
            //adapter.notifyDataSetChanged()
//        })

//        viewModel.isEmpty.observe(this, Observer {
//            if (it) {
//                binding.recyclerView.visibility = View.INVISIBLE
//                binding.empty.visibility = View.INVISIBLE
//            } else {
//                binding.recyclerView.visibility = View.VISIBLE
//                binding.empty.visibility = View.INVISIBLE
//            }
//        })
//
//        viewModel.isShowError.observe(this, Observer {
//            if (it) {
//                Alert.warning(this, error.msg)
//            }
//        })
//
//        viewModel.isShowLoading.observe(this, Observer {
//            if (it) {
//                loadingAnimation.start()
//            } else {
//                loadingAnimation.stop()
//            }
//        })

        viewModel.token.observe(this, Observer {
            val intent = Intent(this, ShowActivity::class.java)
            intent.putExtra("token", it)
            startActivity(intent)
        })
    }
}