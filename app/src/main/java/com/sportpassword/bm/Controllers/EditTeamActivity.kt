package com.sportpassword.bm.Controllers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.TextInputEditText
import android.support.v4.widget.SwipeRefreshLayout
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
import android.widget.TextView
import com.sportpassword.bm.Models.Team
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.Views.ImagePicker
import com.sportpassword.bm.member
import org.jetbrains.anko.contentView
import org.jetbrains.anko.toast
import java.io.File

class EditTeamActivity : BaseActivity(), ImagePicker, View.OnFocusChangeListener {

    override val ACTION_CAMERA_REQUEST_CODE = 100
    override val ACTION_PHOTO_REQUEST_CODE = 200
    val SELECT_REQUEST_CODE = 1
    override val activity = this
    override val context = this
    lateinit override var imagePickerLayer: AlertDialog
    lateinit override var alertView: View
    override var currentPhotoPath = ""
    lateinit override var filePath: String
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
    private var isFeaturedChange: Boolean = false

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

        initImagePicker(R.layout.image_picker_layer)
        teamedit_featured_container.onClick {
            showImagePickerLayer()
        }

        val allV = getAllChildrenBFS(teamedit_layout)
        inputV = filterInputField(allV)

        textFieldDidEndEditing(inputV)

        refreshLayout = contentView!!.findViewById<SwipeRefreshLayout>(R.id.teamedit_refresh)
        //println(refreshLayout)
        setRefreshListener()
        refresh()
    }
    override fun refresh() {
        super.refresh()
        if (teamToken.length > 0) {
            TeamService.getOne(this, "team", "name", teamToken) { success ->
                model.data = TeamService.data
                //println(data)
                //setTeamData()
                //println(model.data)
                dataToField(inputV)
                closeRefresh()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //println(requestCode)
        when (requestCode) {
            ACTION_PHOTO_REQUEST_CODE -> {
                dealPhoto(requestCode, resultCode, data)
            }
            ACTION_CAMERA_REQUEST_CODE -> {
                dealCamera(requestCode, resultCode, data)
            }
            SELECT_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val key = data!!.getStringExtra("key")
                    if (key == TEAM_DAYS_KEY) {
                        val days1: ArrayList<Day> = data!!.getParcelableArrayListExtra("days")
                        val days: ArrayList<Int> = arrayListOf()
                        for (i in 0..days1.size - 1) {
                            val d: Day = days1[i]
                            days.add(d.day)
                        }
                        model.updateDays(days)
                        dataToField(inputV)
                    }
                }
            }
            else -> {
                activity.toast("請重新選擇")
            }
        }
    }

    override fun setImage(newFile: File?, url: String?) {
        teamedit_text.visibility = View.INVISIBLE
        val layoutParams = teamedit_featured.layoutParams as LinearLayout.LayoutParams
        layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT
        layoutParams.height = LinearLayout.LayoutParams.MATCH_PARENT
        layoutParams.setMargins(0, 0, 0, 0)
        imageView.layoutParams = layoutParams
        super.setImage(newFile, url)
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
        var params: MutableMap<String, Any> = mutableMapOf()
        var isPass: Boolean = true
        fieldToData(inputV)
        val name: String = model.data[TEAM_NAME_KEY]!!["value"] as String
        if (name.length == 0) {
            isPass = false
            Alert.show(context, "提示", "請填寫隊名")
        }
//        val mobile: String = model.data[TEAM_MOBILE_KEY]!!["value"] as String
//        if (mobile.length == 0) {
//            isPass = false
//            Alert.show(context, "警告", "請填寫電話")
//        }

        if (isPass) {
            params = model.makeSubmitArr()
            println(params)
            if (params.count() == 0 && !isFeaturedChange) {
                Alert.show(context, "提示", "沒有修改任何資料或圖片")
            } else {
                if (params.count() == 0) {
                    params[TEAM_CREATED_ID_KEY] = member.id
                    if (model.data[TEAM_ID_KEY]!!["value"] as Int > 0) {
                        val id: Int = model.data[TEAM_ID_KEY]!!["value"] as Int
                        params[TEAM_ID_KEY] = id
                    }
                }

                TeamService.update(context, "team", params, filePath) { success ->
                    /*
                    if (success) {
                        if (TeamService.success) {
                            val id: Int = TeamService.id
                            model.data[TEAM_ID_KEY]!!["value"] = id
                            model.data[TEAM_ID_KEY]!!["show"] = id
                            Alert.show(context, "成功", "新增 / 修改球隊成功")
                        } else {
                            Alert.show(context, "錯誤", TeamService.msg)
                        }
                    } else {
                        Alert.show(context, "錯誤", "新增 / 修改球隊失敗，伺服器無法新增成功，請稍後再試")
                    }
                    */
                }

            }
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
            val tag = v.tag
            for ((key, value) in model.data) {
                if (key == tag) {
                    res.add(v)
                }
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
            } else if (v is TextView) {
                for ((key, value) in model.data) {
                    if (tag == key) {
                        val tmp: String = value["show"] as String
                        v.text = tmp
                    }
                }
            }
        }
        val featured: String = model.data[TEAM_FEATURED_KEY]!!["value"] as String
        if (featured.length > 0) {
            setImage(null, featured)
        }
    }
    private fun fieldToData(inputV: List<View>) {
        for (i in 0..inputV.size-1) {
            val v = inputV.get(i)
            val tag = v.tag
            if (v is EditText) {
                for ((key, value) in model.data) {
                    if (tag == key) {
                        val newValue: String = v.text.toString()
                        val oldValue:Any = value["value"] as Any
                        val vtype = value["vtype"] as String
                        _fieldToData(oldValue, newValue, vtype, key)
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
            } else if (v is TextView) {
                val tmp = v.parent
                var l: ViewGroup? = null
                if (tmp is LinearLayout) {
                    l = tmp as LinearLayout
                } else if (tmp is ConstraintLayout) {
                    l = tmp as ConstraintLayout
                }
                if (l != null) {
                    l.onClick {
                        val key: String = l.tag.toString()
                        prepare(key)
                    }
                }
            }
        }
    }
    private fun setTextField(field: EditText) {
        val tag = field.tag
        val newValue = field.text.toString()
        //println(tag)
        //println(field.text)

        for ((key, value) in model.data) {
            if (key == tag) {
                val oldValue: Any = value["value"] as Any
                val vtype: String = value["vtype"] as String
                _fieldToData(oldValue, newValue, vtype, key)
            }
        }
    }
    private fun _fieldToData(_oldValue: Any, _newValue: String, vtype: String, key: String) {
        var oldValue: Any = _oldValue
        if (vtype == "String") {
            oldValue = oldValue as String
            val newValue = _newValue
            if (oldValue != newValue) {
                model.data[key]!!["value"] = newValue
                model.data[key]!!["change"] = true
            }
        } else if (vtype == "Int") {
            oldValue = oldValue as Int
            val newValue: Int = _newValue.toInt()
            if (oldValue != newValue) {
                model.data[key]!!["value"] = newValue
                model.data[key]!!["change"] = true
            }
        } else if (vtype == "Bool") {
            oldValue = oldValue as Boolean
            val newValue: Boolean = _newValue.toBoolean()
            if (oldValue != newValue) {
                model.data[key]!!["value"] = newValue
                model.data[key]!!["change"] = true
            }
        }
    }

    private fun prepare(key: String) {
        val intent = Intent(this@EditTeamActivity, EditTeamItemActivity::class.java)
        intent.putExtra("key", key)
        if (key == TEAM_DAYS_KEY) {
            val value: MutableList<Int> = model.data[key]!!["value"] as MutableList<Int>
            if (value.size > 0) {
                val days: IntArray = IntArray(value.size)
                for (i in 0..days.size-1) {
                    days.set(i, value.get(i))
                }
                intent.putExtra("value", days)
            }
        } else if (key == TEAM_PLAY_START_KEY || key == TEAM_PLAY_END_KEY) {
            val row: MutableMap<String, Any> = model.data[key]!!["sender"] as MutableMap<String, Any>
            val value: String = row["time"] as String
            intent.putExtra("value", value)
        }
        startActivityForResult(intent, SELECT_REQUEST_CODE)
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (!hasFocus) {
            setTextField(v!! as EditText)
        }
    }
}














