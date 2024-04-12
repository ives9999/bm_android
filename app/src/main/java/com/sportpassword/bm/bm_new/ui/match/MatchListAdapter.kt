package com.sportpassword.bm.bm_new.ui.match

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sportpassword.bm.bm_new.data.dto.match.MatchListDto
import com.sportpassword.bm.bm_new.ui.util.Zone
import com.sportpassword.bm.databinding.ItemMatchBinding
import com.sportpassword.bm.extensions.formattedWithSeparator
import com.sportpassword.bm.extensions.toTwoString

class MatchListAdapter :
    PagingDataAdapter<MatchListDto.Row, MatchListAdapter.ViewHolder>(DiffCallback) {

    private var listener: Listener? = null

    fun --(listener: Listener) {
        this.listener = listener
    }

    inner class ViewHolder(binding: ItemMatchBinding) : RecyclerView.ViewHolder(binding.root) {
        private val tvNumber = binding.tvNum
        private val tvName = binding.tvName
        private val startDate = binding.startDate
        private val endDate = binding.endDate
        private val location = binding.location
        private val city = binding.tvCity
        private val signUpPrice = binding.tvSignUpPrice

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
            //add by ives 2023/08/06 如果只有個位數則捕0
            tvNumber.text = (bindingAdapterPosition + 1).toTwoString() + "."
            tvName.text = data.name
            startDate.setContent(data.matchStart.dropLast(3))
            endDate.setContent(data.matchEnd.dropLast(3))
            location.setContent(data.arenaName)
            city.text = Zone.cityIdToString(data.cityId)
            with(data) {
                signUpPrice.text =
                    //add by ives 2023/08/06 金額加入會計符號
                    if (priceMin == priceMax) "NT$ $${priceMax.formattedWithSeparator()}" else "NT$ $${priceMin.formattedWithSeparator()} - $${priceMax.formattedWithSeparator()}"
            }
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