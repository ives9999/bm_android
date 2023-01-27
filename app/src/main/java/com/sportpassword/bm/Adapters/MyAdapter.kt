package com.sportpassword.bm.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.recyclerview.widget.RecyclerView
import com.sportpassword.bm.Controllers.List1CellDelegate
import com.sportpassword.bm.Models.Table
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.setImage
import com.sportpassword.bm.member
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation

abstract class MyAdapter<T: MyViewHolder>(private val resource: Int, private val viewHolderConstructor: (Context, View, List1CellDelegate?)-> T, val list1CellDelegate: List1CellDelegate?=null): RecyclerView.Adapter<T>() {

    var tableList: ArrayList<Table> = arrayListOf()

    override fun getItemCount(): Int {
        return tableList.size
    }

    fun setMyTableList(tableList: ArrayList<Table>) {
        this.tableList = tableList
    }

    override fun onBindViewHolder(holder: T, position: Int) {
        val row: Table = tableList[position]

        holder.bind(row, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): T {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val viewHolder: View = inflater.inflate(resource, parent, false)

        return viewHolderConstructor(parent.context, viewHolder, list1CellDelegate)
    }
}

open class MyViewHolder(val context: Context, val viewHolder: View, val list1CellDelegate: List1CellDelegate? = null): RecyclerView.ViewHolder(viewHolder) {

    var isLike: Boolean = false

    var titleLbl: TextView? = viewHolder.findViewById(R.id.titleLbl)
    var listFeatured: ImageView? = viewHolder.findViewById(R.id.listFeatured)

    var city: Button? = viewHolder.findViewById(R.id.cityBtn)
    var arena: Button? = viewHolder.findViewById(R.id.arenaBtn)

    var v: View? = viewHolder.findViewById(R.id.iconView)
    var refreshIcon: ImageButton? = viewHolder.findViewById(R.id.refreshIcon)
    var telIcon: ImageButton? = viewHolder.findViewById(R.id.telIcon)
    var mapIcon: ImageButton? = viewHolder.findViewById(R.id.mapIcon)
    var likeIcon: ImageButton? = viewHolder.findViewById(R.id.likeIcon)

    open fun bind(row: Table, idx: Int) {

        viewHolder.setOnClickListener {
            list1CellDelegate?.cellClick(row)
        }

        if (row.title.isNotEmpty()) {
            titleLbl?.text = row.title
        }
        if (row.name.isNotEmpty()) {
            titleLbl?.text = row.name
        }

        if (listFeatured != null && row.featured_path.isNotEmpty()) {

            Picasso.with(context)
                .load(row.featured_path)
                .transform(RoundedCornersTransformation(100, 0))
                //.placeholder(R.drawable.loading_square_120)
                .error(R.drawable.loading_square_120)
                .into(listFeatured)
        }

        if (city != null) {
            city!!.setOnClickListener {
                if (list1CellDelegate != null) {
                    list1CellDelegate.cellCity(row)
                }
            }
        }

        if (arena != null) {
            arena!!.setOnClickListener {
                if (list1CellDelegate != null) {
                    list1CellDelegate.cellArena(row)
                }
            }
        }

        if (v != null) {
            refreshIcon?.setOnClickListener {
                list1CellDelegate?.cellRefresh()
            }
            if (telIcon != null) {

                if (row.mobile_show.isNotEmpty()) {
                    if (list1CellDelegate != null) {
                        telIcon?.setOnClickListener {
                            list1CellDelegate.cellMobile(row)
                        }
                        telIcon?.visibility = View.VISIBLE
                    }
                } else if (row.tel_show.isNotEmpty()) {
                    if (list1CellDelegate != null) {
                        telIcon!!.setOnClickListener {
                            list1CellDelegate.cellMobile(row)
                        }
                        telIcon?.visibility = View.VISIBLE
                    }
                } else {
                    telIcon?.visibility = View.GONE
                }
            }
            if (mapIcon != null) {

                if (row.address == null || row.address.isEmpty()) {
                    mapIcon!!.visibility = View.GONE
                } else {
                    mapIcon!!.setOnClickListener {
                        if (list1CellDelegate != null) {
                            list1CellDelegate.cellShowMap(row)
                        }
                    }
                }
            }
        }

        if (likeIcon != null) {
            likeIcon!!.setOnClickListener {
                if (list1CellDelegate != null) {
                    list1CellDelegate.cellLike(row)
                    if (member.isLoggedIn) {
                        setLike()
                    }
                }
            }
            isLike = !row.like
            setLike()
        }
    }

    fun setLike() {
        isLike = !isLike
        if (isLike) {
            likeIcon?.setImage("like1")
        } else {
            likeIcon?.setImage("like")
        }
    }
}

//open class BaseAdapter(private val resource: Int, private val list1CellDelegate: List1CellDelegate?): RecyclerView.Adapter<BaseViewHolder>() {
//
//    var list: ArrayList<Table> = arrayListOf()
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
//
//        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
//        val viewHolder: View = inflater.inflate(resource, parent, false)
//
//        return BaseViewHolder(parent.context, viewHolder, list1CellDelegate)
//    }
//
//    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
//
//        val row: Table = list[position]
//
//        holder.bind(row, position)
//    }
//
//    override fun getItemCount(): Int {
//
//        return list.size
//    }
//}
//
//open class BaseViewHolder(val context: Context, val viewHolder: View, val list1CellDelegate: List1CellDelegate? = null): RecyclerView.ViewHolder(viewHolder) {
//
//    open fun bind(row: Table, idx: Int) {}
//}