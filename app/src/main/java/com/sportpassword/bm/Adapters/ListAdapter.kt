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
import com.squareup.picasso.Picasso

/**
 * Created by ives on 2018/2/23.
 */
class ListAdapter(val context: Context, val itemClick: (Data) -> Unit): RecyclerView.Adapter<ListAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.tab_list_item, parent, false)
        return ViewHolder(view, itemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.bind(lists[position])
    }

    var lists: ArrayList<Data> = arrayListOf()
        get() = field
        set(value) {
            field = value
        }

    override fun getItemCount(): Int {
        return lists.size
    }

    inner class ViewHolder(itemView: View, val itemClick: (Data) -> Unit): RecyclerView.ViewHolder(itemView) {
        val nameView = itemView.findViewById<TextView>(R.id.listTitleTxt)
        val featuredView = itemView.findViewById<ImageView>(R.id.listFeatured)
        val webViewClient = WebViewClient()
        val videoView = itemView.findViewById<WebView>(R.id.listVideo)
        val featuredContainer = itemView.findViewById<LinearLayout>(R.id.featured_container)
        val videoContainer = itemView.findViewById<LinearLayout>(R.id.video_container)

        fun bind(data: Data) {
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
                var url = ""
                if (data.vimeo.isNotEmpty()) {
                    url = data.vimeo
                } else if (data.youtube.isNotEmpty()) {
                    url = data.youtube
                }
                videoView.webViewClient = webViewClient
                videoView.settings.javaScriptEnabled


                val yourData = "<div id='made-in-ny'></div>\n" +
                        "\n" +
                        "<script src='https://player.vimeo.com/api/player.js'></script>\n" +
                        "<script>\n" +
                        "    var options = {\n" +
                        "        id: 257623723,\n" +
                        "        width: 540,\n" +
                        "        loop: true\n" +
                        "    };\n" +
                        "\n" +
                        "    var player = new Vimeo.Player('made-in-ny', options);\n" +
                        "\n" +
                        "    player.setVolume(0);\n" +
                        "\n" +
                        "    player.on('play', function() {\n" +
                        "        console.log('played the video!');\n" +
                        "    });\n" +
                        "</script>"
                //videoView.loadUrl(url)
                videoView.loadData(yourData, "text/html; charset=utf-8", "UTF-8")

            }
            println("${data.title}: featured => ${data.featured_path}")
            println("${data.title}: vimeo => ${data.vimeo}")
            println("${data.title}: youbute => ${data.youtube}")
            itemView.setOnClickListener{itemClick(data)}
        }
    }

}