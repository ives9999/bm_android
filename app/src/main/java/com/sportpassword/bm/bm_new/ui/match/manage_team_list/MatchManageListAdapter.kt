package com.sportpassword.bm.bm_new.ui.match.manage_team_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sportpassword.bm.bm_new.data.dto.match.MatchTeamListDto
import com.sportpassword.bm.databinding.ItemMatchTeamBinding

class MatchManageListAdapter :
    PagingDataAdapter<MatchTeamListDto.Row, MatchManageListAdapter.ViewHolder>(DiffCallback) {

    private var listener: Listener? = null

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    inner class ViewHolder(binding: ItemMatchTeamBinding) : RecyclerView.ViewHolder(binding.root) {
        private val viewBinding = binding

        init {
            binding.apply {

                itemView.setOnClickListener {
                    getItem(bindingAdapterPosition)?.let { data ->
                        listener?.onDetailClick(data)
                    }
                }

                btnEdit.setOnClickListener {
                    getItem(bindingAdapterPosition)?.let { data ->
                        listener?.onEditClick(data)
                    }
                }

                btnDel.setOnClickListener {
                    getItem(bindingAdapterPosition)?.let { data ->
                        listener?.onDelClick(data)
                    }
                }

                btnWallet.setOnClickListener {
                    getItem(bindingAdapterPosition)?.let { data ->
                        listener?.onPaymentClick(data)
                    }
                }

                btnDetail.setOnClickListener {
                    getItem(bindingAdapterPosition)?.let { data ->
                        listener?.onDetailClick(data)
                    }
                }
            }
        }

        fun bind(data: MatchTeamListDto.Row) {
            viewBinding.apply {
                tvNum.text = (bindingAdapterPosition + 1).toString()
                tvMatchName.text = data.match.name
                tvTeamName.text = data.name
                startDate.setContent(data.match.matchStart.dropLast(3))
                endDate.setContent(data.match.matchEnd.dropLast(3))
                teamGroup.setContent(data.matchGroup.name)
                signFee.setContent("NT$${data.matchGroup.price}")
                limitGroups.setContent(data.matchGroup.limit.toString())
                signDate.setContent(data.createdAt.dropLast(3))
            }
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

    companion object DiffCallback : DiffUtil.ItemCallback<MatchTeamListDto.Row>() {

        override fun areItemsTheSame(
            oldItem: MatchTeamListDto.Row,
            newItem: MatchTeamListDto.Row
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: MatchTeamListDto.Row,
            newItem: MatchTeamListDto.Row
        ): Boolean {
            return oldItem == newItem
        }
    }

    interface Listener {
        fun onEditClick(data: MatchTeamListDto.Row)
        fun onDelClick(data: MatchTeamListDto.Row)
        fun onDetailClick(data: MatchTeamListDto.Row)
        fun onPaymentClick(data: MatchTeamListDto.Row)
    }
}