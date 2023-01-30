package com.sportpassword.bm.Controllers

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.gson.JsonParseException
import com.sportpassword.bm.Adapters.ShowAdapter
import com.sportpassword.bm.Models.Table
import com.sportpassword.bm.Models.TeachTable
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.TeachService
import com.sportpassword.bm.Utilities.jsonToModel
import kotlin.reflect.full.memberProperties
import com.sportpassword.bm.Data.ShowRow
import com.sportpassword.bm.Services.DataService
import com.sportpassword.bm.Utilities.Loading
import com.sportpassword.bm.Utilities.LoadingAnimation
import com.sportpassword.bm.Utilities.hideKeyboard
import com.sportpassword.bm.databinding.ActivityShowTeachVcBinding
import com.sportpassword.bm.member

class ShowTeachVC : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {

    private lateinit var binding: ActivityShowTeachVcBinding
    private lateinit var view: ViewGroup

    var dataService: DataService = DataService()
    var refreshLayout: SwipeRefreshLayout? = null
    lateinit var refreshListener: SwipeRefreshLayout.OnRefreshListener
//    var tableRowKeys:MutableList<String> = mutableListOf()
//    var tableRows: HashMap<String, HashMap<String,String>> = hashMapOf()
//    lateinit var adapter: GroupAdapter<GroupieViewHolder>
    val showRows: ArrayList<ShowRow> = arrayListOf()

    lateinit var showAdapter: ShowAdapter

    var token: String? = null    // course token
    var table: Table? = null
    var myTable: TeachTable? = null
    var isLike: Boolean = false
    var likeCount: Int = 0

    var apiKey = ""

    var youTubePlayer: YouTubePlayer? = null

    //loading
    lateinit var loadingAnimation: LoadingAnimation

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityShowTeachVcBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)

        dataService = TeachService
        refreshLayout = binding.refresh
        setRefreshListener()

        showAdapter = ShowAdapter(this)
        binding.tableView.adapter = showAdapter

//        initAdapter()

        if (intent.hasExtra("token")) {
            token = intent.getStringExtra("token")!!
        }

//        tableRowKeys = mutableListOf("pv","created_at_show")
//        tableRows = hashMapOf(
//            "pv" to hashMapOf( "icon" to "pv","title" to "瀏覽數","content" to ""),
//            "created_at_show" to hashMapOf( "icon" to "calendar","title" to "建立日期","content" to "")
//        )
        apiKey = getString(R.string.youtube_api_key)
        binding.youtube.initialize(apiKey, this)

        loadingAnimation = LoadingAnimation(this)

        init()
        refresh()
    }

    fun init() {
        showRows.addAll(arrayListOf(
            ShowRow("pv", "pv", "瀏覽數"),
            ShowRow("created_at_show", "calendar", "建立日期")
        ))
    }

//    fun initAdapter() {
//        adapter = GroupAdapter()

//        val items = generateMainItem()
//        adapter.addAll(items)
//        tableView.adapter = adapter
//    }

    fun refresh() {

        showRows.clear()
        if (token != null) {
            runOnUiThread {
                loadingAnimation.start()
            }
            val params: HashMap<String, String> = hashMapOf("token" to token!!, "member_token" to member.token!!)
            dataService.getOne(this, params) { success ->
                if (success) {
                    genericTable()
                    if (table != null) {
                        table!!.filterRow()

                        if (table!!.name.isNotEmpty()) {
                            runOnUiThread {
                                binding.topViewInclude.topTitleLbl.text = table!!.name
                            }
                            //setMyTitle(table!!.name)
                        } else if (table!!.title.isNotEmpty()) {
                            runOnUiThread {
                                binding.topViewInclude.topTitleLbl.text = table!!.title
                            }
                            //setMyTitle(table!!.title)
                        }

                        setData()
                        showAdapter.rows = showRows
                        runOnUiThread {
                            showAdapter.notifyDataSetChanged()
                        }

                        isLike = table!!.like
                        likeCount = table!!.like_count

                        setLike()

                    }
                }
                runOnUiThread {
                    closeRefresh()
                    loadingAnimation.stop()
                }
            }
        }
    }

//    fun generateMainItem(): ArrayList<Item> {
//
//        val items: ArrayList<Item> = arrayListOf()
//        var icon = ""
//        var title = ""
//        var content = ""
//        var isPressed: Boolean = false
//        for (key in tableRowKeys) {
//            if (tableRows.containsKey(key)) {
//                val row = tableRows[key]!!
//                if (row.containsKey("icon")) {
//                    icon = row["icon"]!!
//                }
//                if (row.containsKey("title")) {
//                    title = row["title"]!!
//                }
//                if (row.containsKey("content")) {
//                    content = row["content"]!!
//                }
//                if (row.containsKey("isPressed")) {
//                    isPressed = row["isPressed"]!!.toBoolean()
//                }
//                if (icon.length > 0 && title.length > 0) {
//                    val iconCell = IconCell(this, icon, title, content, isPressed)
//                    //iconCell.delegate = this
//                    items.add(iconCell)
//                }
//            }
//        }
//
//        return items
//    }

    fun genericTable() {
        //println(dataService.jsonString)
        try {
            table = jsonToModel<TeachTable>(dataService.jsonString)
        } catch (e: JsonParseException) {
            //warning(e.localizedMessage!!)
            //println(e.localizedMessage)
        }
        if (table != null) {
            myTable = table as TeachTable
            myTable!!.filterRow()
        } else {
            //warning("解析伺服器所傳的字串失敗，請洽管理員")
        }
    }

    fun setData() {

        if (myTable != null) {
            setMainData(myTable!!)
        }
    }

    fun setMainData(table: Table) {
        for (showRow in showRows) {
            val key: String = showRow.key
            val kc = table::class
            kc.memberProperties.forEach {
                if (key == it.name) {
                    val value = it.getter.call(table).toString()
                    showRow.show = value
                    //tableRows[key]!!["content"] = value
                }
            }
        }

        if (youTubePlayer != null) {
            youTubePlayer!!.cueVideo(myTable!!.youtube)
            //youTubePlayer!!.loadVideo(myTable!!.youtube)
            //youTubePlayer!!.pause()
        }

//        val items = generateMainItem()
//        adapter.update(items)
    }

    fun setLike() {
        //likeButton.initStatus(isLike, table!.like_count)
        //val _likeButton = view as Button

        setIcon()
        setCount()
    }

    private fun setIcon() {
        var res: Int = R.drawable.like_show
        if (isLike) {
            res = R.drawable.like_show1
        }
        binding.likeButton.setCompoundDrawablesWithIntrinsicBounds(res, 0, 0, 0)
    }

    private fun setCount() {
        if (table!!.like) {
            if (isLike) {
                likeCount = table!!.like_count
            } else {
                likeCount = table!!.like_count - 1
            }
        } else {
            if (isLike) {
                likeCount = table!!.like_count + 1
            } else {
                likeCount = table!!.like_count
            }
        }

        binding.likeButton.text = "${likeCount.toString()}人"
    }

    fun likeButtonPressed(view: View) {

        if (!member.isLoggedIn) {
            val loginIntent: Intent = Intent(this, LoginVC::class.java)
            this.startActivity(loginIntent)
        } else {
            if (table != null) {
                isLike = !isLike
                setLike()
                dataService.like(this, table!!.token, table!!.id)
            } else {
                //warning("沒有取得內容資料值，請稍後再試或洽管理員")
            }
        }
    }

    fun setRefreshListener() {
        if (refreshLayout != null) {
            refreshListener = SwipeRefreshLayout.OnRefreshListener {
                refresh()
            }
            refreshLayout!!.setOnRefreshListener(refreshListener)
        }
    }

    fun closeRefresh() {
        refreshLayout?.isRefreshing = false
    }

    fun prev(view: View) {
        hideKeyboard()
        finish()
    }

    override fun onInitializationSuccess(
        provider: YouTubePlayer.Provider?,
        youTubePlayer: YouTubePlayer?,
        wasRestored: Boolean
    ) {
        youTubePlayer?.setPlayerStateChangeListener(playerStateChangeListener)
        youTubePlayer?.setPlaybackEventListener(playbackEventListener)

        this.youTubePlayer = youTubePlayer

//        if (!wasRestored) {
//            youTubePlayer?.cuePlaylist("")
//        }
    }

    override fun onInitializationFailure(
        provider: YouTubePlayer.Provider?,
        youTubeInitializationResult: YouTubeInitializationResult?
    ) {
        val REQUEST_CODE = 0

        if (youTubeInitializationResult?.isUserRecoverableError == true) {
            runOnUiThread {
                youTubeInitializationResult.getErrorDialog(this, REQUEST_CODE).show()
            }
        } else {
            val errorMessage = "youtube player 初始化發生錯誤 ($youTubeInitializationResult)"
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        }
    }

    private val playbackEventListener = object: YouTubePlayer.PlaybackEventListener {
        override fun onSeekTo(p0: Int) {

        }

        override fun onBuffering(p0: Boolean) {

        }

        override fun onPlaying() {
            Toast.makeText(this@ShowTeachVC, "影片播放正常", Toast.LENGTH_SHORT).show()
        }

        override fun onStopped() {
            Toast.makeText(this@ShowTeachVC, "影片停止", Toast.LENGTH_SHORT).show()
        }

        override fun onPaused() {
            Toast.makeText(this@ShowTeachVC, "影片暫停", Toast.LENGTH_SHORT).show()
        }
    }

    private val playerStateChangeListener = object: YouTubePlayer.PlayerStateChangeListener {
        override fun onAdStarted() {
            Toast.makeText(this@ShowTeachVC, "按下廣告", Toast.LENGTH_SHORT).show()
        }

        override fun onLoading() {

        }

        override fun onVideoStarted() {
            Toast.makeText(this@ShowTeachVC, "影片開始播放", Toast.LENGTH_SHORT).show()
        }

        override fun onLoaded(p0: String?) {

        }

        override fun onVideoEnded() {
            Toast.makeText(this@ShowTeachVC, "影片結束播放", Toast.LENGTH_SHORT).show()
        }

        override fun onError(p0: YouTubePlayer.ErrorReason?) {

        }
    }
}




















