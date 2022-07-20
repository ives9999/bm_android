package com.sportpassword.bm.Adapters

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.sportpassword.bm.Controllers.List1CellDelegate
import com.sportpassword.bm.Models.MemberCoinTable
import com.sportpassword.bm.Models.Table
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.*
import kotlinx.android.synthetic.main.coin_list_cell.view.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.textColor

class MemberCoinAdapter(list1CellDelegate: List1CellDelegate?): MyAdapter<MemberCoinViewHolder>(
    R.layout.coin_list_cell, ::MemberCoinViewHolder, list1CellDelegate) {

    override fun onBindViewHolder(holder: MemberCoinViewHolder, position: Int) {

        val row: MemberCoinTable = tableList[position] as MemberCoinTable

        holder.bind(row, position)
    }
}

class MemberCoinViewHolder(context: Context, viewHolder: View, list1CellDelegate: List1CellDelegate? = null): MyViewHolder(context, viewHolder, list1CellDelegate) {

    val noLbl: TextView = viewHolder.noLbl
    val able_typeLbl: TextView = viewHolder.able_typeLbl
    val dateLbl: TextView = viewHolder.dateLbl
    val priceSignLbl: TextView = viewHolder.priceSignLbl
    val priceLbl: TextView = viewHolder.priceLbl
    val balanceSignLbl: TextView = viewHolder.balanceSignLbl
    val balanceLbl: TextView = viewHolder.balanceLbl
    val typeButton: Button = viewHolder.typeButton

    //_row is cartTable
    override fun bind(row: Table, idx: Int) {

        val _row: MemberCoinTable = row as MemberCoinTable
        _row.filterRow()
        val no: String = (idx + 1).toString() + "."

        noLbl.text = no
        balanceLbl.text = _row.balance.formattedWithSeparator()

        if (_row.able_type_show.isNotEmpty()) {
            able_typeLbl.text = _row.able_type_show
            able_typeLbl.setTextLook(16F, R.color.TEXT_WHITE)
        }
        dateLbl.text = _row.created_at.noSec()
        dateLbl.setTextLook(10F, R.color.MY_LIGHT_WHITE)

        priceSignLbl.setTextLook(10F, R.color.MY_WHITE)
        priceLbl.setTextLook(16F, R.color.MY_WHITE)

        balanceSignLbl.setTextLook(10F, R.color.MY_WHITE)
        balanceLbl.setTextLook(16F, R.color.MY_WHITE)

        typeButton.text =
            ((_row.in_out) then { _row.type_in_enum.chineseName }) ?: _row.type_out_enum.chineseName

        if (_row.in_out) {
            priceLbl.text = "+" + _row.coin.formattedWithSeparator()
            if (_row.type_in_enum == MEMBER_COIN_IN_TYPE.buy) {
                typeButton.setLook(R.color.MEMBER_COIN_BUY, R.color.MY_WHITE)
            } else if (_row.type_in_enum == MEMBER_COIN_IN_TYPE.gift) {
                typeButton.setLook(R.color.MEMBER_COIN_GIFT, R.color.MY_WHITE)
            } else {
                typeButton.visibility = View.INVISIBLE
            }
        } else {
            priceLbl.text = "-" + _row.coin.formattedWithSeparator()
            priceLbl.setTextLook(16F, R.color.MY_RED)
            if (_row.type_out_enum == MEMBER_COIN_OUT_TYPE.product) {
                typeButton.setLook(R.color.MEMBER_COIN_PAY, R.color.MY_WHITE)
            } else if (_row.type_out_enum == MEMBER_COIN_OUT_TYPE.course) {
                typeButton.setLook(R.color.MEMBER_COIN_PAY, R.color.MY_WHITE)
            } else {
                typeButton.visibility = View.INVISIBLE
            }
        }

        if (_row.name.isNotEmpty()) {
            able_typeLbl.text = _row.name
        }

        viewHolder.setOnClickListener {
            list1CellDelegate?.cellClick(row)
            //購買點數，前往查看訂單
//            if (MEMBER_COIN_IN_TYPE.enumFromString(_row.in_type) == MEMBER_COIN_IN_TYPE.buy && _row.order_token.length > 0 && list1CellDelegate != null) {
//                list1CellDelegate.cellClick(row)
//            }
//
//            //使用點數購買商品，前往查看訂單
//            if (!row.in_out && MEMBER_COIN_OUT_TYPE.enumFromString(row.out_type) == MEMBER_COIN_OUT_TYPE.product && row.able_type == "order" && list1CellDelegate != null) {
//                list1CellDelegate.cellClick(row)
//            }
        }

//        if (list1CellDelegate != null) {
//            refreshIcon?.setOnClickListener {
//                list1CellDelegate.cellRefresh()
//            }
//
//            viewHolder.editIcon.setOnClickListener {
//                list1CellDelegate.cellEdit(row)
//            }
//
//            viewHolder.deleteIcon.setOnClickListener {
//                list1CellDelegate.cellDelete(row)
//            }
//        } else {
//            viewHolder.iconView.visibility = View.GONE
//        }
    }
}