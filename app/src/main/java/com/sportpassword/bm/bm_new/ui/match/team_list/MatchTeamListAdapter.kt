package com.sportpassword.bm.bm_new.ui.match.team_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sportpassword.bm.bm_new.data.dto.match.MatchListDto
import com.sportpassword.bm.bm_new.ui.util.Zone
import com.sportpassword.bm.databinding.ItemMatchTeamBinding

class MatchTeamListAdapter :
    PagingDataAdapter<MatchListDto.Row, MatchTeamListAdapter.ViewHolder>(DiffCallback) {

    private var listener: Listener? = null

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    inner class ViewHolder(binding: ItemMatchTeamBinding) : RecyclerView.ViewHolder(binding.root) {
        private val tvNumber = binding.tvNum
        private val tvName = binding.tvName
        private val startDate = binding.startDate
        private val endDate = binding.endDate
        private val location = binding.location
        private val city = binding.tvCity

        init {
            binding.apply {

                itemView.setOnClickListener {
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
            startDate.setContent(data.matchStart.dropLast(3))
            endDate.setContent(data.matchEnd.dropLast(3))
            location.setContent(data.arenaName)
            city.text = Zone.cityIdToString(data.cityId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMatchTeamBinding.inflate(
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