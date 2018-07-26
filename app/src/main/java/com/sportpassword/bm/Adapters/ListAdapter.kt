package com.sportpassword.bm.Adapters

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.sportpassword.bm.Models.SuperData
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.*
import com.squareup.picasso.Picasso

/**
 * Created by ives on 2018/2/23.
 */
class ListAdapter(val context: Context, val iden: String="team", val screenWidth: Int=0, val itemClick: (SuperData) -> Unit): RecyclerView.Adapter<ListAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.tab_list_item, parent, false)
        return ViewHolder(view, iden, screenWidth, itemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(lists[position])
    }

    var lists: ArrayList<SuperData> = arrayListOf()
        get() = field
        set(value) {
            field = value
        }

    override fun getItemCount(): Int {
        return lists.size
    }

    inner class ViewHolder(itemView: View, val iden: String="team", val screenWidth: Int=0, val itemClick: (SuperData) -> Unit): RecyclerView.ViewHolder(itemView) {
        val featuredView = itemView.findViewById<ImageView>(R.id.listFeatured)
        val nameView = itemView.findViewById<TextView>(R.id.listTitleTxt)
        val cityView = itemView.findViewById<TextView>(R.id.listCityTxt)
        val arenaView = itemView.findViewById<TextView>(R.id.listArenaTxt)
        val ballView = itemView.findViewById<TextView>(R.id.listBallTxt)
        val dayView = itemView.findViewById<TextView>(R.id.listDayTxt)
        val intervalView = itemView.findViewById<TextView>(R.id.listIntervalTxt)
        //val videoView = itemView.findViewById<WebView>(R.id.listVideo)

        fun bind(superData: SuperData) {
            if (iden == "team") {
                if (superData.data.containsKey(CITY_KEY)) {
                    //println(superData.superData["city"]!!["show"])
                    cityView.text = superData.data[CITY_KEY]!!["show"] as String
                }
                if (superData.data.containsKey(TEAM_ARENA_KEY)) {
                    //println(superData.superData["arena"]!!["show"])
                    arenaView.text = superData.data[TEAM_ARENA_KEY]!!["show"] as String
                }
                if (superData.data.containsKey(TEAM_BALL_KEY)) {
                    ballView.text = superData.data[TEAM_BALL_KEY]!!["show"] as String
                }
                if (superData.data.containsKey(TEAM_DAYS_KEY)) {
                    //println(superData.superData["arena"]!!["show"])
                    dayView.text = superData.data[TEAM_DAYS_KEY]!!["show"] as String
                }
                if (superData.data.containsKey(TEAM_INTERVAL_KEY)) {
                    //println(superData.superData["arena"]!!["show"])
                    intervalView.text = superData.data[TEAM_INTERVAL_KEY]!!["show"] as String
                }
            } else if (iden == "coach") {
                if (superData.data.containsKey(CITY_KEY)) {
                    //println(superData.superData["city"]!!["show"])
                    cityView.text = superData.data[CITY_KEY]!!["show"] as String
                }
                if (superData.data.containsKey(MOBILE_KEY)) {
                    //println(superData.superData["arena"]!!["show"])
                    arenaView.text = superData.data[MOBILE_KEY]!!["show"] as String
                }
                if (superData.data.containsKey(COACH_SENIORITY_KEY)) {
                    ballView.text = "年資: " + superData.data[COACH_SENIORITY_KEY]!!["show"] as String
                }
                if (superData.data.containsKey(LINE_KEY)) {
                    //println(superData.superData["arena"]!!["show"])
                    dayView.text = "line id: " + superData.data[LINE_KEY]!!["show"] as String
                }
            }
            if (superData.vimeo.isEmpty() && superData.youtube.isEmpty()) {
                nameView.visibility = View.VISIBLE
                featuredView.visibility = View.VISIBLE
                //videoView.visibility = View.INVISIBLE
                nameView.text = superData.title
                if (superData.featured_path.isNotEmpty()) {
                    Picasso.with(context)
                            .load(superData.featured_path)
                            .placeholder(R.drawable.loading_square)
                            .error(R.drawable.load_failed_square)
                            .into(featuredView)
                } else {
                    featuredView.setImageResource(R.drawable.loading_square)
                }
            } else if (superData.featured_path.isEmpty() && superData.youtube.isNotEmpty()) {
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

//                videoView.visibility = View.VISIBLE
//                videoView.settings.javaScriptEnabled = true
//                videoView.webChromeClient = WebChromeClient()
                if (superData.youtube.isNotEmpty()) {
                    var width: Int = if (screenWidth == 0) 320 else screenWidth
                    var height: Int = height(width)
                    width += 1

                    val html =
                            "<html><body style=\"margin:0;padding:0;\"><iframe type=\"text/html5\" width=\"" +
                                    width +
                                    "\" height=\"" +
                                    height +
                                    "\" src=\"https://www.youtube.com/embed/" +
                                    superData.youtube +
                                    "\" frameborder=\"0\" allow=\"autoplay; encrypted-media\" allowfullscreen></iframe></body></html>"
                    //println(html)
//                    videoView.loadData(html, "text/html; charset=utf-8", "UTF-8")
                }

            }
//            println("${superData.title}: featured => ${superData.featured_path}")
//            println("${superData.title}: vimeo => ${superData.vimeo}")
//            println("${superData.title}: youbute => ${superData.youtube}")
            itemView.setOnClickListener{itemClick(superData)}
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