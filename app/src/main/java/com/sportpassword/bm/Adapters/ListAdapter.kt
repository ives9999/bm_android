package com.sportpassword.bm.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

/**
 * Created by ives on 2018/2/23.
 */
class ListAdapter(val context: Context, val vimeoClient: VimeoClient?=null, val itemClick: (Data) -> Unit): RecyclerView.Adapter<ListAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.tab_list_item, parent, false)
        return ViewHolder(view, vimeoClient, itemClick)
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

    inner class ViewHolder(itemView: View, val vimeoClient: VimeoClient?=null, val itemClick: (Data) -> Unit): RecyclerView.ViewHolder(itemView) {
        val nameView = itemView.findViewById<TextView>(R.id.listTitleTxt)
        val featuredView = itemView.findViewById<ImageView>(R.id.listFeatured)
        val webViewClient = WebViewClient()
        val videoView = itemView.findViewById<WebView>(R.id.listVideo)
        val featuredContainer = itemView.findViewById<LinearLayout>(R.id.featured_container)
        val videoContainer = itemView.findViewById<LinearLayout>(R.id.video_container)
        var embed: String = ""

        fun bind(data: Data) {
            println(data.title)
            println(data.featured_path)
            println(data.vimeo)
            nameView.text = data.title
            if (data.vimeo.isEmpty() && data.youtube.isEmpty() && data.featured_path.isNotEmpty()) {
                featuredContainer.visibility = View.VISIBLE
                videoContainer.visibility = View.INVISIBLE
                Picasso.with(context)
                        .load(data.featured_path)
                        .placeholder(R.drawable.loading_square)
                        .error(R.drawable.load_failed_square)
                        .into(featuredView)
            }
            if (data.featured_path.isEmpty() && (data.vimeo.isNotEmpty() || data.youtube.isNotEmpty())) {
                featuredContainer.visibility = View.INVISIBLE
                videoContainer.visibility = View.VISIBLE
                videoView.webViewClient = webViewClient
                videoView.settings.javaScriptEnabled
                var url = ""
                if (data.vimeo.isNotEmpty()) {
                    url = data.vimeo
                    getEmbed(url) { success ->
                        if (success) {
                            println(embed)
                            videoView.loadData(embed, "text/html; charset=utf-8", "UTF-8")
                        }
                    }
                } else if (data.youtube.isNotEmpty()) {
                    url = data.youtube
                }

            }
//            println("${data.title}: featured => ${data.featured_path}")
//            println("${data.title}: vimeo => ${data.vimeo}")
//            println("${data.title}: youbute => ${data.youtube}")
            itemView.setOnClickListener{itemClick(data)}
        }

        private fun getEmbed(uri: String, complete: CompletionHandler) {
            if (vimeoClient != null) {
                vimeoClient!!.fetchNetworkContent(uri, object : ModelCallback<Video>(Video::class.java) {
                    override fun success(t: Video?) {
                        //println(t)
                        embed = t!!.embed.html
                        //println(embed)
                        complete(true)
                    }

                    override fun failure(error: VimeoError?) {
                        //println(error!!.localizedMessage)
                        complete(false)
                    }
                })
            }
        }
    }

}