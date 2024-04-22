package com.sportpassword.bm.v2.arena.read

import android.text.Layout
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sportpassword.bm.R
import com.sportpassword.bm.databinding.ReadArenaBinding
import com.squareup.picasso.Picasso

class Adapter(private val viewModel: ViewModel): RecyclerView.Adapter<Adapter.ViewHolder>() {

    var rows: List<ReadDao.Arena>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        //println("rows count: ${rows?.count()}")
        return rows?.count() ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val row = rows!![position]
        holder.bind(viewModel, row)
    }

    class ViewHolder private constructor(private val binding: ReadArenaBinding): RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ReadArenaBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }

        fun bind(viewModel: ViewModel, row: ReadDao.Arena) {
            binding.nameTV.text = row.name

            val images: List<ReadDao.Image> = row.images
            var featured_path: String? = null
            for (image in images) {
                if (image.isFeatured) {
                    featured_path = image.path
                }
            }
            if (featured_path != null) {
                Picasso.with(viewModel.context)
                    .load(featured_path)
                    .placeholder(R.drawable.loading_square)
                    .error(R.drawable.load_failed_square)
                    .into(binding.featuredIV)
            } else {
                binding.featuredIV.setImageResource(R.drawable.loading_square)
            }
        }
    }
}