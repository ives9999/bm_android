package com.sportpassword.bm.Adapters

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.VideoView
import com.sportpassword.bm.Models.Data
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.CompletionHandler
import com.sportpassword.bm.Utilities.VIMEO_TOKEN
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_test.*
import org.jetbrains.anko.margin

/**
 * Created by ives on 2018/2/23.
 */
class ListAdapter(val context: Context, val screenWidth: Int=0, val itemClick: (Data) -> Unit): RecyclerView.Adapter<ListAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.tab_list_item, parent, false)
        return ViewHolder(view, screenWidth, itemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(lists[position])
    }

    var lists: ArrayList<Data> = arrayListOf()
        get() = field
        set(value) {
            field = value
        }

    override fun getItemCount(): Int {
        return lists.size
    }

    inner class ViewHolder(itemView: View, val screenWidth: Int=0, val itemClick: (Data) -> Unit): RecyclerView.ViewHolder(itemView) {
        val nameView = itemView.findViewById<TextView>(R.id.listTitleTxt)
        val featuredView = itemView.findViewById<ImageView>(R.id.listFeatured)
        val videoView = itemView.findViewById<WebView>(R.id.listVideo)

        fun bind(data: Data) {
            if (data.vimeo.isEmpty() && data.youtube.isEmpty()) {
                nameView.visibility = View.VISIBLE
                featuredView.visibility = View.VISIBLE
                videoView.visibility = View.INVISIBLE
                nameView.text = data.title
                if (data.featured_path.isNotEmpty()) {
                    Picasso.with(context)
                            .load(data.featured_path)
                            .placeholder(R.drawable.loading_square)
                            .error(R.drawable.load_failed_square)
                            .into(featuredView)
                } else {
                    featuredView.setImageResource(R.drawable.loading_square)
                }
            } else if (data.featured_path.isEmpty() && data.youtube.isNotEmpty()) {
                nameView.visibility = View.INVISIBLE
//                nameView.setPadding(0, 0, 0, 0)
//                val p = nameView.layoutParams as ConstraintLayout.LayoutParams
//                p.height = 0
//                p.setMargins(0, 0, 0, 0)
//                nameView.layoutParams = p
                //zeroView(nameView)

                featuredView.visibility = View.INVISIBLE
//                featuredView.setPadding(0, 0, 0, 0)
//                val p1 = featuredView.layoutParams as ConstraintLayout.LayoutParams
//                p1.height = 0
//                p1.setMargins(0, 0, 0, 0)
//                featuredView.layoutParams = p1
                //zeroView(featuredView)

                videoView.visibility = View.VISIBLE
                videoView.settings.javaScriptEnabled = true
                videoView.webChromeClient = WebChromeClient()
                if (data.youtube.isNotEmpty()) {
                    var width: Int = if (screenWidth == 0) 320 else screenWidth
                    var height: Int = height(width)
                    width += 1

                    val html =
                            "<html><body style=\"margin:0;padding:0;\"><iframe type=\"text/html5\" width=\"" +
                                    width +
                                    "\" height=\"" +
                                    height +
                                    "\" src=\"https://www.youtube.com/embed/" +
                                    data.youtube +
                                    "\" frameborder=\"0\" allow=\"autoplay; encrypted-media\" allowfullscreen></iframe></body></html>"
                    //println(html)
                    videoView.loadData(html, "text/html; charset=utf-8", "UTF-8")
                }

            }
//            println("${data.title}: featured => ${data.featured_path}")
//            println("${data.title}: vimeo => ${data.vimeo}")
//            println("${data.title}: youbute => ${data.youtube}")
            itemView.setOnClickListener{itemClick(data)}
        }

        private fun height(width: Int): Int {
            val _width: Float = width.toFloat()
            val _height: Float = _width*3.0f/5.0f
            var height: Int = _height.toInt()
            return height
        }

        private fun zeroView(view: View) {
            val p = view.layoutParams as ConstraintLayout.LayoutParams
            p.height = 0
            p.setMargins(0, 0, 0, 0)
            view.layoutParams = p
            view.setPadding(0, 0, 0, 0)
            view.visibility = View.INVISIBLE
        }

//        private fun getEmbed(uri: String, complete: CompletionHandler) {
//            if (vimeoClient != null) {
//                vimeoClient!!.fetchNetworkContent(uri, object : ModelCallback<Video>(Video::class.java) {
//                    override fun success(t: Video?) {
//                        //println(t)
//                        embed = t!!.embed.html
//                        //println(embed)
//                        complete(true)
//                    }
//
//                    override fun failure(error: VimeoError?) {
//                        //println(error!!.localizedMessage)
//                        complete(false)
//                    }
//                })
//            }
//        }
    }

}