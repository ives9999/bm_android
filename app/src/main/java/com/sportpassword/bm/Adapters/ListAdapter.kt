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
import com.vimeo.networking.Configuration
import com.vimeo.networking.Vimeo
import com.vimeo.networking.VimeoClient
import com.vimeo.networking.callbacks.ModelCallback
import com.vimeo.networking.model.Video
import com.vimeo.networking.model.error.VimeoError
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

        val webViewClient = WebViewClient()
        var embed: String = ""

        fun bind(data: Data) {
//            println(data.title)
//            println(data.featured_path)
//            println(data.vimeo)
//            println(data.youtube)
            nameView.text = data.title
            if (data.vimeo.isEmpty() && data.youtube.isEmpty() && data.featured_path.isNotEmpty()) {
                nameView.visibility = View.VISIBLE
                featuredView.visibility = View.VISIBLE
                videoView.visibility = View.INVISIBLE
                Picasso.with(context)
                        .load(data.featured_path)
                        .placeholder(R.drawable.loading_square)
                        .error(R.drawable.load_failed_square)
                        .into(featuredView)
            } else if (data.featured_path.isEmpty() && data.youtube.isNotEmpty()) {
                nameView.visibility = View.INVISIBLE
                featuredView.visibility = View.INVISIBLE
                videoView.visibility = View.VISIBLE

                val p = nameView.layoutParams as ConstraintLayout.LayoutParams
                p.height = 0
                p.setMargins(0, 0, 0, 0)
                nameView.layoutParams = p

                val p1 = featuredView.layoutParams as ConstraintLayout.LayoutParams
                p1.height = 0
                p1.setMargins(0, 0, 0, 0)
                nameView.layoutParams = p1

                videoView.settings.javaScriptEnabled = true
                videoView.webChromeClient = WebChromeClient()
                if (data.youtube.isNotEmpty()) {
                    var width: Int = if (screenWidth == 0) 320 else screenWidth
                    var height: Int = height(width)
                    //width -= 20

                    val p2 = videoView.layoutParams as ConstraintLayout.LayoutParams
                    p2.height = height
                    p2.width = width
                    p2.setMargins(0, 0, 0, 0)
                    videoView.layoutParams = p2

                    val html =
                            "<html><body><iframe type=\"text/html5\" width=\"" +
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