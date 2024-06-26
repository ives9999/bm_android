package com.sportpassword.bm.bm_new.ui.match.detail.groups

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sportpassword.bm.R
import com.sportpassword.bm.Views.IconTextText2
import com.sportpassword.bm.bm_new.data.dto.match.MatchDetailDto
import com.sportpassword.bm.databinding.ItemMatchGroupBinding
import com.sportpassword.bm.extensions.formattedWithSeparator
import com.sportpassword.bm.extensions.toTwoString

class MatchGroupsAdapter :
    ListAdapter<MatchDetailDto.MatchGroup, MatchGroupsAdapter.ViewHolder>(DiffCallback) {

    private var listener: Listener? = null
    var signupStartShow: String = ""
    var signupEndShow: String = ""

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    inner class ViewHolder(binding: ItemMatchGroupBinding) : RecyclerView.ViewHolder(binding.root) {
        private val tvNumber = binding.tvNum
        private val tvPrice = binding.tvPrice
        private val tvName = binding.tvName
        private val tvNumPerson = binding.tvPersons
        private val tvSignGroups = binding.tvGroups
        private val tvLimitGroups = binding.tvLimitGrous
        private val signupStartITT: IconTextText2 = binding.signupStartITT
        private val signupEndITT: IconTextText2 = binding.signupEndITT
        private val contentITT: IconTextText2 = binding.contentITT
        private val context = itemView.context

        init {
            binding.apply {
                btnSignUp.setOnClickListener {
                    getItem(bindingAdapterPosition)?.let { data ->
                        listener?.onSignUpClick(data)
                    }
                }
            }
        }

        fun bind(data: MatchDetailDto.MatchGroup) {
            //add by ives 2023/08/06 如果只有個位數則捕0
            tvNumber.text = (bindingAdapterPosition + 1).toTwoString() + "."
            //add by ives 2023/08/06 金額加入會計符號
            tvPrice.text = "NT$ ${data.price.formattedWithSeparator()} 元"
            //tvPrice.text = context.getString(R.string.match_sign_price, data.price)
            tvName.text = data.name
            tvNumPerson.text = context.getString(R.string.match_num_person, data.number)
            tvSignGroups.text = context.getString(R.string.match_sign_group, data.signupCount)
            tvLimitGroups.text = context.getString(R.string.match_limit_group, data.limit)

            signupStartITT.setShow(signupStartShow.dropLast(3))
            signupEndITT.setShow(signupEndShow.dropLast(3))
            contentITT.setShow(data.content)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMatchGroupBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object DiffCallback : DiffUtil.ItemCallback<MatchDetailDto.MatchGroup>() {

        override fun areItemsTheSame(
            oldItem: MatchDetailDto.MatchGroup,
            newItem: MatchDetailDto.MatchGroup
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: MatchDetailDto.MatchGroup,
            newItem: MatchDetailDto.MatchGroup
        ): Boolean {
            return oldItem == newItem
        }
    }

    interface Listener {
        fun onSignUpClick(data: MatchDetailDto.MatchGroup)
    }
}