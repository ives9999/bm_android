package com.sportpassword.bm.Adapters

import android.content.Context
import android.graphics.BitmapFactory
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.sportpassword.bm.Models.Table
import com.sportpassword.bm.R
import com.squareup.picasso.Picasso
import java.net.URL

class CollectionAdapter(val context: Context, val iden: String="teach", val screenWidth: Int=0, val itemClick: (Table) -> Unit): RecyclerView.Adapter<CollectionAdapter.ViewHolder>() {

//    var lists: ArrayList<SuperData> = arrayListOf()
//        get() = field
//        set(value) {
//            field = value
//        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.collection_item, parent, false)
        return ViewHolder(view, iden, screenWidth, itemClick)
    }

    override fun getItemCount(): Int {
        return 1
        //return lists.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //holder.bind(lists[position])
    }


    inner class ViewHolder(itemView: View, val iden: String="team", val screenWidth: Int=0, val itemClick: (Table) -> Unit): RecyclerView.ViewHolder(itemView) {
        val titleLbl = itemView.findViewById<TextView>(R.id.titleLbl)
        val featuredView = itemView.findViewById<ImageView>(R.id.featuredView)
        val pvView = itemView.findViewById<ImageView>(R.id.pvIcon)
        val pvLbl = itemView.findViewById<TextView>(R.id.pvLbl)
        val dateView = itemView.findViewById<ImageView>(R.id.dateIcon)
        val dateLbl = itemView.findViewById<TextView>(R.id.dateLbl)
        val constraintLayout = itemView.findViewById<ConstraintLayout>(R.id.constraintLayout)

        private val set = ConstraintSet()

        fun bind(superData: Table) {
            titleLbl.text = superData.title

            if (superData.featured_path.isNotEmpty()) {
                Picasso.get()
                        .load(superData.featured_path)
                        .placeholder(R.drawable.loading_square)
                        .error(R.drawable.load_failed_square)
                        .into(featuredView)
            } else {
                featuredView.setImageResource(R.drawable.loading_square)
            }
            //println("featured_path: ${superData.featured_path}")
            try {
                val url = URL(superData.featured_path)
                val bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                val w = bmp.width
                val h = bmp.height
                //println("${w}:${h}")
                val ratio = String.format("%d:%d", w, h)

                set.clone(constraintLayout)
                set.setDimensionRatio(featuredView.id, ratio)
                set.applyTo(constraintLayout)
            } catch (e: Exception) {
                //println(e.localizedMessage)
            }

            //pvLbl.text = "瀏覽數：" + superData.data[PV_KEY]!!["show"] as String

            //var createdAt: String = superData.data[CREATED_AT_KEY]!!["show"] as String
            //dateLbl.text = createdAt.toDateTime()!!.toMyString("yyyy-MM-dd")
            itemView.setOnClickListener{itemClick(superData)}
        }
    }
}