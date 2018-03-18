package com.sportpassword.bm.Controllers

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.TeamService
import kotlinx.android.synthetic.main.activity_edit_team.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import android.support.v7.app.AlertDialog
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import com.sportpassword.bm.Models.Team
import com.sportpassword.bm.Utilities.Alert
import com.sportpassword.bm.Utilities.TEAM_MOBILE_KEY
import com.sportpassword.bm.Utilities.TEAM_NAME_KEY
import com.sportpassword.bm.Views.ImagePicker
import java.io.File

class EditTeamActivity : BaseActivity(), ImagePicker, View.OnFocusChangeListener {

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
    private lateinit var inputV: List<View>

    val model: Team = Team(0, "", "", "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_team)
        //println("oncreate")

        that = this
        imageView = teamedit_featured
        model.dataReset()
        //println(model.data)

        getImageViewParams()

        teamToken = intent.getStringExtra("token")
        //println(teamToken)
        if (teamToken.length > 0) {
            TeamService.getOne(this, "team", "name", teamToken) { success ->
                model.data = TeamService.data
                //println(data)
                //setTeamData()
                println(model.data)
                dataToField(inputV)
            }
        }

        initImagePicker(R.layout.image_picker_layer)
        teamedit_featured_container.onClick {
            showImagePickerLayer()
        }

        val allV = getAllChildrenBFS(teamedit_layout)
        inputV = filterInputField(allV)

        textFieldDidEndEditing(inputV)
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
        var isPass: Boolean = true
        fieldToData(inputV)
        val name: String = model.data[TEAM_NAME_KEY]!!["value"] as String
        if (name.length == 0) {
            isPass = false
            Alert.show(context, "警告", "請填寫隊名")
        }
//        val mobile: String = model.data[TEAM_MOBILE_KEY]!!["value"] as String
//        if (mobile.length == 0) {
//            isPass = false
//            Alert.show(context, "警告", "請填寫電話")
//        }

        if (isPass) {

        }
    }

    private fun getAllChildrenBFS(v: View): List<View> {
        var visited: ArrayList<View> = arrayListOf()
        var unvisited: ArrayList<View> = arrayListOf()
        unvisited.add(v)

        while (!unvisited.isEmpty()) {
            val child = unvisited.removeAt(0)
            visited.add(child)
            if (child !is ViewGroup) continue
            val group = child as ViewGroup
            val childCount = group.childCount
            for (i in 0..childCount-1) unvisited.add(group.getChildAt(i))
        }

        return visited
    }
    private fun filterInputField(allV: List<View>): List<View> {
        var res: ArrayList<View> = arrayListOf()
        for (i in 0..allV.size-1) {
            val v = allV.get(i)
            if (v is EditText) {
                res.add(v)
            }
        }
        return res
    }
    private fun dataToField(inputV: List<View>) {
        for (i in 0..inputV.size-1) {
            val v = inputV.get(i)
            val tag = v.tag
            if (v is EditText) {
                for ((key, value) in model.data) {
                    if (tag == key) {
                        val tmp: Any = value["value"] as Any
                        val vtype = value["vtype"] as String
                        if (vtype == "String") {
                            v.setText(tmp.toString())
                        } else if (vtype == "Int") {
                            v.setText(tmp.toString())
                        } else if (vtype == "Bool") {
                            v.setText(tmp.toString())
                        }
                    }
                }
            }
        }
    }
    private fun fieldToData(inputV: List<View>) {
        for (i in 0..inputV.size-1) {
            val v = inputV.get(i)
            val tag = v.tag
            if (v is EditText) {
                for ((key, value) in model.data) {
                    if (tag == key) {
                        val tmp = v.text.toString()
                        val vtype = value["vtype"] as String
                        if (vtype == "String") {
                            model.data[key]!!["value"] = tmp
                        } else if (vtype == "Int") {
                            model.data[key]!!["value"] = tmp.toInt()
                        } else if (vtype == "Bool") {
                            model.data[key]!!["value"] = tmp.toBoolean()
                        }
                    }
                }
            }

        }
    }

    private fun textFieldDidEndEditing(inputV: List<View>) {
        for (i in 0..inputV.size-1) {
            val v = inputV.get(i)
            if (v is EditText) {
                v.setOnFocusChangeListener(this)
            }
        }
    }
    private fun setTextField(field: EditText) {
        val tag = field.tag
        val text = field.text.toString()
        //println(tag)
        //println(field.text)

        for ((key, value) in model.data) {
            if (key == tag) {
                val oldValue: Any = value["value"] as Any
                val vtype: String = value["vtype"] as String
                if (vtype == "String") {
                    if (oldValue as String != text) {
                        model.data[key]!!["value"] = text
                        model.data[key]!!["change"] = true
                    }
                } else if (vtype == "Int") {
                    val newValue = text.toInt()
                    if (oldValue as Int != newValue) {
                        model.data[key]!!["value"] = newValue
                        model.data[key]!!["change"] = true
                    }
                } else if (vtype == "Bool") {
                    val newValue = text.toBoolean()
                    if (oldValue as Boolean != newValue) {
                        model.data[key]!!["value"] = newValue
                        model.data[key]!!["change"] = true
                    }
                }
            }
        }
    }
    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (!hasFocus) {
            setTextField(v!! as EditText)
        }
    }
}














