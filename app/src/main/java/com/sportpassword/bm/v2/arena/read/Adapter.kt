package com.sportpassword.bm.v2.arena.read

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sportpassword.bm.R
import com.sportpassword.bm.databinding.ReadArenaBinding
import com.sportpassword.bm.extensions.avatar
import com.sportpassword.bm.extensions.featured
import com.sportpassword.bm.extensions.formattedWithSeparator
import com.sportpassword.bm.extensions.noSec

class Adapter(private val viewModel: ViewModel): PagingDataAdapter<PageArena, Adapter.ViewHolder>(ReadDiffCallback()) {

    //var readDao: ReadDao? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ReadArenaBinding.inflate(inflater)
        return ViewHolder(parent.context, binding)
    }

//    override fun getItemCount(): Int {
//        //println("rows count: ${readDao?.data?.rows?.count()}")
//        var count = 0
//        readDao ?. let {
//            count = it.data.rows.count()
//        }
//        return count
//    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pageArena = getItem(position)
        pageArena ?. let {
//            val row = it.data.rows[position]
//            val meta = it.data.meta
            holder.bind(it, position, viewModel)
        }
    }

    class ViewHolder (val context: Context, val binding: ReadArenaBinding): RecyclerView.ViewHolder(binding.root) {
//        companion object {
//            fun from(parent: ViewGroup): ViewHolder {
//                val layoutInflater = LayoutInflater.from(parent.context)
//                val binding = ReadArenaBinding.inflate(layoutInflater, parent, false)
//
//                return ViewHolder(binding)
//            }
//        }

        fun bind(row: PageArena, position: Int, viewModel: ViewModel) {

            //val idx: Int = (meta.currentPage - 1)*meta.perpage + position + 1
            //binding.nameTV.text = "${idx.toString()}.${row.name}"
            binding.nameTV.text = "${row.name}"

            val images: List<ReadDao.Image> = row.images
            var featured_path: String? = null
            for (image in images) {
                if (image.isFeatured) {
                    featured_path = image.path
                }
            }
            if (featured_path != null) {
                binding.featuredIV.featured(featured_path, false, 38)
//                Glide.with(context!!)
//                    .load(featured_path)
//                    .placeholder(R.drawable.nophoto)
//                    .error(R.drawable.load_failed_square)
//                    .into(binding.featuredIV)
//                Picasso.get()
//                    .load(featured_path)
//                    .networkPolicy(NetworkPolicy.NO_CACHE)
//                    .memoryPolicy(MemoryPolicy.NO_CACHE)
//                    .error(R.drawable.load_failed_square)
//                    .into(binding.featuredIV)
            } else {
                binding.featuredIV.setImageResource(R.drawable.loading_square)
            }

            binding.avatarIV.avatar(row.member.avatar, true)


            binding.cityNameTV.text = row.zone.city_name
            binding.pvTV.text = row.pv.formattedWithSeparator()

            binding.memberNameTV.text = row.member.name
            binding.createdATTV.text = row.created_at.noSec()

            binding.featuredIV.setOnClickListener{
                viewModel.toShohw(row.token)
            }
        }
    }

    class ReadDiffCallback: DiffUtil.ItemCallback<PageArena>() {
        override fun areItemsTheSame(oldItem: PageArena, newItem: PageArena): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PageArena, newItem: PageArena): Boolean {
            return oldItem == newItem
        }

    }
}