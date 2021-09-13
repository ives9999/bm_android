package com.sportpassword.bm.Controllers

import android.os.Bundle
import android.widget.Toast
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.gson.JsonParseException
import com.sportpassword.bm.Models.Table
import com.sportpassword.bm.Models.TeachTable
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.TeachService
import com.sportpassword.bm.Utilities.jsonToModel
import kotlinx.android.synthetic.main.activity_show_course_vc.*
import kotlin.reflect.full.memberProperties
import kotlinx.android.synthetic.main.activity_show_teach_vc.*
import kotlinx.android.synthetic.main.activity_show_teach_vc.refresh
import com.google.android.youtube.player.YouTubePlayerView

class ShowTeachVC : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {

    var myTable: TeachTable? = null
    val api_key = "AIzaSyCR_6_3cnKPtamzWcXtwqjedFCcKYD3zPI"

    var youTubePlayer: YouTubePlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        setContentView(R.layout.activity_show_teach_vc)
        dataService = TeachService

        refreshLayout = refresh
        setRefreshListener()

        initAdapter()

        super.onCreate(savedInstanceState)

        tableRowKeys = mutableListOf("pv","created_at_show")
        tableRows = hashMapOf(
            "pv" to hashMapOf( "icon" to "pv","title" to "瀏覽數","content" to ""),
            "created_at_show" to hashMapOf( "icon" to "calendar","title" to "建立日期","content" to "")
        )
        youtube.initialize(api_key, this)

        refresh()
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
            youTubeInitializationResult.getErrorDialog(this, REQUEST_CODE).show()
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

    override fun genericTable() {
        //println(dataService.jsonString)
        try {
            table = jsonToModel<TeachTable>(dataService.jsonString)
        } catch (e: JsonParseException) {
            warning(e.localizedMessage!!)
            //println(e.localizedMessage)
        }
        if (table != null) {
            myTable = table as TeachTable
            myTable!!.filterRow()
        } else {
            warning("解析伺服器所傳的字串失敗，請洽管理員")
        }
    }

    override fun setData() {

        if (myTable != null) {
            setMainData(myTable!!)
        }
    }

    override fun setMainData(table: Table) {
        for (key in tableRowKeys) {
            val kc = table::class
            kc.memberProperties.forEach {
                if (key == it.name) {
                    val value = it.getter.call(table).toString()
                    tableRows[key]!!["content"] = value
                }
            }
        }

        //youtube.settings.javaScriptEnabled = true
        //youtube.loadUrl("https://www.youtube.com/embed/" + myTable!!.youtube)

        if (youTubePlayer != null) {
            youTubePlayer!!.loadVideo(myTable!!.youtube)
            youTubePlayer!!.play()
        }

        val items = generateMainItem()
        adapter.update(items)
    }
}




















