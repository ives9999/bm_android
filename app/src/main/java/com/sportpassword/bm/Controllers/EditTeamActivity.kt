package com.sportpassword.bm.Controllers

import android.content.Intent
import android.os.Bundle
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.TeamService
import kotlinx.android.synthetic.main.activity_edit_team.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.sportpassword.bm.Views.ImagePicker
import java.io.File

class EditTeamActivity : BaseActivity(), ImagePicker {
    override val ACTION_CAMERA_REQUEST_CODE = 100
    override val ACTION_PHOTO_REQUEST_CODE = 200
    override val activity = this
    override val context = this
    lateinit override var imagePickerLayer: AlertDialog
    lateinit override var alertView: View
    override var currentPhotoPath = ""
    override var file: File? = null
    lateinit override var imageView: ImageView

    var teamToken: String = ""
    lateinit var that: EditTeamActivity

    private var originW: Int = 0
    private var originH: Int = 0
    private var originMarginTop = 0
    private var originMarginBottom = 0
    private lateinit var originScaleType: ImageView.ScaleType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_team)
        //println("oncreate")

        that = this
        imageView = teamedit_featured

        getImageViewParams()

        teamToken = intent.getStringExtra("token")
        //println(teamToken)
        if (teamToken.length > 0) {
            TeamService.getOne(this, "team", "name", teamToken) { success ->
                data = TeamService.data
                //println(data)
                setTeamData()
                println(data)
            }
        }

        initImagePicker(R.layout.image_picker_layer)
        teamedit_featured_container.onClick {
            showImagePickerLayer()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //println(requestCode)
        activityResult(requestCode, resultCode, data)
    }

    override fun setImage() {
        teamedit_text.visibility = View.INVISIBLE
        val layoutParams = teamedit_featured.layoutParams as LinearLayout.LayoutParams
        layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT
        layoutParams.height = LinearLayout.LayoutParams.MATCH_PARENT
        layoutParams.setMargins(0, 0, 0, 0)
        imageView.layoutParams = layoutParams
        super.setImage()
    }

    override fun removeImage() {
        teamedit_text.visibility = View.VISIBLE
        val layoutParams = teamedit_featured.layoutParams as LinearLayout.LayoutParams
        layoutParams.width = originW
        layoutParams.height = originH
        layoutParams.setMargins(0, originMarginTop, 0, originMarginBottom)
        imageView.layoutParams = layoutParams
        imageView.scaleType = originScaleType
        super.removeImage()
        closeImagePickerLayer()
    }

    fun getImageViewParams() {
        val l = teamedit_featured.layoutParams as LinearLayout.LayoutParams
        originW = l.width
        originH = l.height
        originScaleType = teamedit_featured.scaleType
        //val m = teamedit_featured.
        originMarginTop = l.topMargin
        originMarginBottom = l.bottomMargin
    }

    fun submit(view: View) {
        println("submit")
    }
}
