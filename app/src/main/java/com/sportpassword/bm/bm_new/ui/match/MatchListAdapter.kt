package com.sportpassword.bm.bm_new.ui.match

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sportpassword.bm.bm_new.data.dto.match.MatchListDto
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
        private val tvStartDate = binding.tvStartDate
        private val tvStartTime = binding.tvStartTime
        private val tvEndDate = binding.tvEndDate
        private val tvEndTime = binding.tvEndTime
        private val tvLocation = binding.tvLocation

        init {
            binding.apply {

                itemView .setOnClickListener {
                    getItem(bindingAdapterPosition)?.let { data ->
                        listener?.onDetailClick(data)
                    }
                }

                btnDetail.setOnClickListener {
                    getItem(bindingAdapterPosition)?.let { data ->
                        listener?.onDetailClick(data)
                    }
                }

                btnSignUp.setOnClickListener {
                    getItem(bindingAdapterPosition)?.let { data ->
                        listener?.onSignUpClick(data)
                    }
                }
            }
        }

        fun bind(data: MatchListDto.Row) {
            tvNumber.text = (bindingAdapterPosition + 1).toString()
            tvName.text = data.name
            val start = data.matchStart.split(" ")
            val end = data.matchEnd.split(" ")
            tvStartDate.text = start.getOrNull(0)
            tvStartTime.text = start.getOrNull(1)
            tvEndDate.text = end.getOrNull(0)
            tvEndTime.text = end.getOrNull(1)
            tvLocation.text = data.arenaName
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
        fun onDetailClick(data: MatchListDto.Row)
        fun onSignUpClick(data: MatchListDto.Row)
    }
}