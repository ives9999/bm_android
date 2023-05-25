package com.sportpassword.bm.bm_new.ui.match

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sportpassword.bm.bm_new.data.dto.MatchListDto
import com.sportpassword.bm.databinding.ItemMatchBinding

class MatchListAdapter :
    PagingDataAdapter<MatchListDto.Row, MatchListAdapter.ViewHolder>(DiffCallback) {

    private var listener: Listener? = null

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    inner class ViewHolder(binding: ItemMatchBinding) : RecyclerView.ViewHolder(binding.root) {
        private val tvNumber = binding.tvNum
        private val tvName = binding.tvName
        private val tvCity = binding.tvCity
        private val tvArena = binding.tvArena
        private val ivCover = binding.ivCoverPhoto

        init {
        }

        fun bind(data: MatchListDto.Row) {
            tvNumber.text = (bindingAdapterPosition + 1).toString()


            tvName.text = data.name
//            Glide.with(ivCover).load("https://bm.sportpassword.com${data.featuredPath}")
//                .into(ivCover)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMatchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<MatchListDto.Row>() {

        override fun areItemsTheSame(
            oldItem: MatchListDto.Row,
            newItem: MatchListDto.Row
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: MatchListDto.Row,
            newItem: MatchListDto.Row
        ): Boolean {
            return oldItem == newItem
        }
    }

    interface Listener {
        fun onPhoneClick(num: String?)
    }
}