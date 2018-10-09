package com.sportpassword.bm.Controllers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.TextInputEditText
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.widget.SwipeRefreshLayout
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.TeamService
import kotlinx.android.synthetic.main.activity_edit_team.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.sportpassword.bm.Models.Arena
import com.sportpassword.bm.Models.City
import com.sportpassword.bm.Models.Team
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.Views.ImagePicker
import com.sportpassword.bm.member
import org.jetbrains.anko.contentView
import org.jetbrains.anko.inputMethodManager
import org.jetbrains.anko.toast
import java.io.File
import kotlinx.android.synthetic.main.mask.*

class EditTeamActivity : BaseActivity(), ImagePicker {

    override val ACTION_CAMERA_REQUEST_CODE = 100
    override val ACTION_PHOTO_REQUEST_CODE = 200
    val SELECT_REQUEST_CODE = 1
    override val activity = this
    override val context = this
    lateinit override var imagePickerLayer: AlertDialog
    lateinit override var alertView: View
    override var currentPhotoPath = ""
    override var filePath: String = ""
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
    var action: String = "INSERT"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_team)

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.button, menu)
        return true
    }
    override fun refresh() {
        super.refresh()
        if (teamToken.length > 0) {
            Loading.show(mask)
            action = "UPDATE"
            TeamService.getOne(this, "team", "name", teamToken) { success ->
                if (success) {
                    model.data = TeamService.data
                    //println(model.data)
                    //setTeamData()
                    //println(model.data)
                    dataToField(inputV)

                    teamedit_name.setSelection(teamedit_name.length())
                    closeRefresh()
                    val title: String = if (action == "UPDATE") "更新球隊" else "新增球隊"
                    setMyTitle(title)
                }
                Loading.hide(mask)
            }
        } else {
            setMyTitle("新增球隊")
            //model.runTestData()
            dataToField(inputV)
            teamedit_name.setSelection(teamedit_name.length())
        }
    }

    override fun setImage(newFile: File?, url: String?) {
        teamedit_text.visibility = View.INVISIBLE
        val layoutParams = teamedit_featured.layoutParams as LinearLayout.LayoutParams
        layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT
        layoutParams.height = LinearLayout.LayoutParams.MATCH_PARENT
        layoutParams.setMargins(0, 0, 0, 0)
        imageView.layoutParams = layoutParams
        isFeaturedChange = true
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
        isFeaturedChange = true
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
        hideKeyboard()
        var params: MutableMap<String, Any> = mutableMapOf()
        var isPass: Boolean = true
        fieldToData(inputV)
        val name: String = model.data[TEAM_NAME_KEY]!!["value"] as String
        if (name.length == 0) {
            isPass = false
            Alert.show(context, "提示", "請填寫隊名")
        }
        val mobile: String = model.data[TEAM_MOBILE_KEY]!!["value"] as String
        if (mobile.length == 0) {
            isPass = false
            Alert.show(context, "警告", "請填寫電話")
        }

        if (isPass) {
            params = model.makeSubmitArr()
            if (params.count() == 0 && !isFeaturedChange) {
                Alert.show(context, "提示", "沒有修改任何資料或圖片")
            } else {
                Loading.show(mask)
                if (params.count() == 0) {
                    if (model.data[TEAM_ID_KEY]!!["value"] as Int > 0) {
                        val id: Int = model.data[TEAM_ID_KEY]!!["value"] as Int
                        params[TEAM_ID_KEY] = id
                    } else {
                        params[TEAM_CREATED_ID_KEY] = member.id
                    }
                }
                val created_id = model.data[TEAM_CREATED_ID_KEY]!!["value"] as Int
                if (created_id < 0) {
                    params[TEAM_CREATED_ID_KEY] = member.id
                }

                TeamService.update(context, "team", params, filePath) { success ->
                    Loading.hide(mask)
                    if (success) {
                        if (TeamService.success) {
                            val id: Int = TeamService.id
                            model.data[TEAM_ID_KEY]!!["value"] = id
                            model.data[TEAM_ID_KEY]!!["show"] = id
                            Alert.update(this, this.action, {
                                if (file != null) {
                                    file!!.delete()
                                }
                                val teamUpdate = Intent(NOTIF_TEAM_UPDATE)
                                LocalBroadcastManager.getInstance(this).sendBroadcast(teamUpdate)
                                finish()
                            })
                        } else {
                            Alert.show(context, "錯誤", TeamService.msg)
                        }
                    } else {
                        Alert.show(context, "錯誤", TeamService.msg)
                    }

                }

            }
        }
    }

    // filter the not text input field
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
        //println(model.data)
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
                            var tmp1: Int = tmp as Int
                            val tmp2: String = if (tmp1 < 0) "" else tmp1.toString()
                            v.setText(tmp2)
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
                        if (newValue.length > 0) {
                            val oldValue: Any = value["value"] as Any
                            val vtype = value["vtype"] as String
                            _fieldToData(oldValue, newValue, vtype, key)
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
                val id = v.resources.getResourceName(v.id)
                //println(id)
                val feature = _idRegex(id)
                if (feature != null) {
                    val clear_id = "clearbutton_${feature}"
                    val resId = resources.getIdentifier(clear_id, "id", packageName)
                    if (resId != 0) {
                        val clear_button = findViewById<ImageButton>(resId)
                        clear_button.setOnClickListener() { view ->
                            v.setText("")
                        }
                    }
                }
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
    private fun _idRegex(id: String): String? {
        var res: String? = null
        val  regex = "([^_]*)_(.*)".toRegex()
        val matches = regex.find(id)
        if (matches != null && matches.groupValues.size > 2) {
            val groups = matches.groupValues
            res = groups[2]
        }
        return res
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
        hideKeyboard()
        fieldToData(inputV)
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
            var value = ""
            if (row.isNotEmpty()) {
                value = row["time"] as String
            }
            intent.putExtra("value", value)
        } else if (key == TEAM_DEGREE_KEY) {
            val value: ArrayList<String> = model.data[key]!!["sender"] as ArrayList<String>
            intent.putExtra("value", value)
        } else if (key == TEAM_CITY_KEY) {
            val value: Int = model.data[key]!!["sender"] as Int
            val city = City(value, "")
            val citys: ArrayList<City> = arrayListOf(city)
            intent.putParcelableArrayListExtra("citys", citys)
        } else if (key == TEAM_ARENA_KEY) {
            val city_id: Int = model.data[TEAM_CITY_KEY]!!["value"] as Int
            if (city_id == 0) {
                Alert.show(this, "警告", "請先選擇區域")
            } else {
                val tmp: MutableMap<String, Int> = model.data[key]!!["sender"] as MutableMap<String, Int>
                val city_id: Int = tmp["city_id"]!!
                val arena_id: Int = tmp["arena_id"]!!
                intent.putExtra("city_id", city_id)
                intent.putExtra("arena_id", arena_id)
            }
        } else {
            val value: String = model.data[key]!!["value"] as String
            intent.putExtra("value", value)
        }
        startActivityForResult(intent, SELECT_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //println(data)
        when (requestCode) {
            ACTION_PHOTO_REQUEST_CODE -> {
                //println(data!!.data)
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
                    } else if (key == TEAM_PLAY_START_KEY || key == TEAM_PLAY_END_KEY) {
                        val time: String = data!!.getStringExtra("time") + ":00"
                        if (key == TEAM_PLAY_START_KEY) {
                            model.updatePlayStartTime(time)
                        } else {
                            model.updatePlayEndTime(time)
                        }
                    } else if (key == TEAM_DEGREE_KEY) {
                        val degrees: ArrayList<String> = data!!.getStringArrayExtra("degree").toCollection(ArrayList<String>())
                        model.updateDegree(degrees)
                    } else if (key == TEAM_CITY_KEY) {
                        val citys: ArrayList<City> = data!!.getParcelableArrayListExtra("citys")
//                        val id: Int = data!!.getIntExtra("id", model.data[TEAM_CITY_KEY]!!["value"] as Int)
//                        val name: String = data!!.getStringExtra("name")
                        if (citys.size > 0) {
                            val city = citys[0]
                            model.updateCity(city)
                        }
                    } else if (key == TEAM_ARENA_KEY) {
                        val id: Int = data!!.getIntExtra("id", model.data[TEAM_ARENA_KEY]!!["value"] as Int)
                        val name: String = data!!.getStringExtra("name")
                        val arena = Arena(id, name)
                        model.updateArena(arena)
                    } else {
                        val content: String = data!!.getStringExtra("res")
                        if (key == TEAM_TEMP_CONTENT_KEY) {
                            model.updateTempContent(content)
                        } else if (key == TEAM_CHARGE_KEY) {
                            model.updateCharge(content)
                        } else if (key == TEAM_CONTENT_KEY) {
                            model.updateContent(content)
                        }
                    }
                    model.data[key]!!["change"] = true
                    dataToField(inputV)
                }
            }
            else -> {
                activity.toast("請重新選擇")
            }
        }
    }
}














