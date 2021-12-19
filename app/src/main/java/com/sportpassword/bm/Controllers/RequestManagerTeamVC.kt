package com.sportpassword.bm.Controllers

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.gson.JsonParseException
import com.sportpassword.bm.Models.MemberTable
import com.sportpassword.bm.Models.SuccessTable
import com.sportpassword.bm.Models.TeamTable
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.MemberService
import com.sportpassword.bm.Services.TeamService
import com.sportpassword.bm.Utilities.Loading
import com.sportpassword.bm.Utilities.jsonToModel
import com.sportpassword.bm.member
import kotlinx.android.synthetic.main.activity_request_manager_team_vc.*
import kotlinx.android.synthetic.main.mask.*
import java.io.File


class RequestManagerTeamVC : BaseActivity() {

    lateinit var teamNameTF: EditText
    lateinit var managerTokenTF: EditText

    lateinit var managerNicknameLbl: TextView
    lateinit var managerEMailLbl: TextView
    lateinit var managerMobileLbl: TextView

    lateinit var line1: View
    lateinit var line2: View
    lateinit var line3: View

    lateinit var managerContainer: RelativeLayout
    lateinit var imageContainer: RelativeLayout
    lateinit var footer: LinearLayout

    var managerTable: MemberTable? = null

    var manager_type_token: String = ""
    var teamImagePath1: String = ""
    var teamImagePath2: String = ""

    var teamImageIdx: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_manager_team_vc)

        dataService = TeamService
        able_type = "team"

        setMyTitle("申請球隊管理權")

        teamNameTF = findViewById(R.id.team_nameTF)
        managerTokenTF = findViewById(R.id.manager_tokenTF)

        teamNameTF.setText("早安羽球隊")
        managerTokenTF.setText("bbeq9v41HVRBOgPNEA9pmAEH6abNZPs")

        managerNicknameLbl = findViewById(R.id.managerNicknameLbl)
        managerEMailLbl = findViewById(R.id.managerEMailLbl)
        managerMobileLbl = findViewById(R.id.managerMobileLbl)

        val clear1: ImageView = findViewById(R.id.clear1)
        clear1.setOnClickListener {
            teamNameTF.setText("")
        }

        val clear2: ImageView = findViewById(R.id.clear2)
        clear2.setOnClickListener {
            managerTokenTF.setText("")
        }

        val isNameExistBtn: Button = findViewById(R.id.isNameExistBtn)
        isNameExistBtn.setOnClickListener {
            isNameExist(it)
        }

        val validateMemberBtn: Button = findViewById(R.id.validateMemberBtn)
        validateMemberBtn.setOnClickListener {
            getMemberOne(it)
        }

        val teamImageView1: RelativeLayout = findViewById(R.id.team_image_container1)
        val teamImageView2: RelativeLayout = findViewById(R.id.team_image_container2)

        initImagePicker(R.layout.image_picker_layer)
        teamImageView1.setOnClickListener {
            imageView = edit_featured1
            teamImageIdx = 1
            showImagePickerLayer()
        }
        teamImageView2.setOnClickListener {
            imageView = edit_featured2
            teamImageIdx = 2
            showImagePickerLayer()
        }

        line1 = findViewById(R.id.line1)
        line2 = findViewById(R.id.line2)
        line3 = findViewById(R.id.line3)

        managerContainer = findViewById(R.id.managerContainer)
        imageContainer = findViewById(R.id.imageContainer)
        footer = findViewById(R.id.footer)

        line1.visibility = View.INVISIBLE
        line2.visibility = View.INVISIBLE
        line3.visibility = View.INVISIBLE

        managerContainer.visibility = View.INVISIBLE
        imageContainer.visibility = View.INVISIBLE
        footer.visibility = View.INVISIBLE

        init()
    }

    override fun init() {
        isPrevIconShow = true
        super.init()
    }

    fun isNameExist(view: View) {

        val team_name: String = teamNameTF.text.toString()
        if (team_name.isEmpty()) {
            warning("請填球隊名稱")
        } else {

            Loading.show(mask)
            dataService.isNameExist(this, team_name) { success ->

                if (success) {
                    try {
                        val successTable: SuccessTable? = jsonToModel<SuccessTable>(dataService.jsonString)
                        if (successTable != null) {
                            if (successTable.success) {
                                manager_type_token = successTable.token
                                runOnUiThread {
                                    line1.visibility = View.VISIBLE
                                    managerContainer.visibility = View.VISIBLE
                                }
                            } else {
                                runOnUiThread {
                                    warning("球隊名稱錯誤，系統無此球隊，須完全符合系統幫您建立的球隊名稱")
                                }
                            }
                        }
                    } catch (e: JsonParseException) {
                        warning(e.localizedMessage!!)
                        //println(e.localizedMessage)
                    }
                }
                runOnUiThread {
                    closeRefresh()
                    Loading.hide(mask)
                }
            }
        }
    }

    fun getMemberOne(view: View) {

        val manager_token: String = managerTokenTF.text.toString()
        if (manager_token.isEmpty()) {
            warning("請填管理者金鑰")
        } else {

            Loading.show(mask)
            MemberService.getOne(this, hashMapOf("token" to manager_token)) { success ->

                if (success) {
                    try {
                        //println(MemberService.jsonString)
                        val successTable: SuccessTable? = jsonToModel<SuccessTable>(MemberService.jsonString)
                        if (successTable != null) {
                            if (successTable.success) {
                                managerTable = jsonToModel<MemberTable>(MemberService.jsonString)
                                if (managerTable != null) {
                                    runOnUiThread {
                                        managerNicknameLbl.text = managerTable!!.nickname
                                        managerEMailLbl.text = managerTable!!.email
                                        managerMobileLbl.text = managerTable!!.mobile

                                        line2.visibility = View.VISIBLE
                                        line3.visibility = View.VISIBLE
                                        imageContainer.visibility = View.VISIBLE
                                        footer.visibility = View.VISIBLE
                                    }
                                }
                            } else {
                                runOnUiThread {
                                    warning("管理員金鑰錯誤，系統無此會員!!")
                                }
                            }
                        }
                    } catch (e: JsonParseException) {
                        warning(e.localizedMessage!!)
                        //println(e.localizedMessage)
                    }
                }
                runOnUiThread {
                    closeRefresh()
                    Loading.hide(mask)
                }
            }
        }
    }

    override fun makeTempEmptyFile(): File? {

        val dir = File(activity.getExternalFilesDir(null)!!.absolutePath)
        //val dir = File("" + Environment.getExternalStorageDirectory() + "/Android/data" + activity.applicationContext.packageName + "Files")
        if (!dir.exists()) {
            if (!dir.mkdir()) {
                return null
            }
        }

        var mediaFile: File? = null
        if (teamImageIdx == 1) {
            teamImagePath1 = dir.path + File.separator + "temp1.jpg"
            mediaFile = File(teamImagePath1)
        } else {
            teamImagePath2 = dir.path + File.separator + "temp2.jpg"
            mediaFile = File(teamImagePath2)
        }
        //val mediaFile = File(filePath)
        return mediaFile
    }

    fun submitButtonPressed(view: View) {

        msg = ""

        if (teamNameTF.text.isEmpty()) {
            msg += "沒有填寫球隊名稱\n"
        }

        if (manager_type_token.isEmpty()) {
            msg += "沒有填寫管理者金鑰\n"
        }

        //manager_type is team
        params["manager_type"] = able_type
        //manager_type_token is team token
        params["manager_type_token"] = manager_type_token
        //manager_token is manager member token
        params["manager_token"] = managerTokenTF.text.toString()
        //member_token is post member token, if self post manager_token = member_token
        params["member_token"] = member.token!!

        params["do"] = "update"

        val images: ArrayList<String> = arrayListOf()

        if (teamImagePath1.isNotEmpty()) {
            images.add(teamImagePath1)
        } else {
            msg += "請上傳第一張圖片\n"
        }
        if (teamImagePath2.isNotEmpty()) {
            images.add(teamImagePath2)
        } else {
            msg += "請上傳第二張圖片\n"
        }

        if (msg.isNotEmpty()) {
            warning(msg)
            return
        }

        Loading.show(mask)
        dataService.requestManager(this, params, images) { success ->

            if (success) {
                try {
                    val successTable: SuccessTable? = jsonToModel<SuccessTable>(dataService.jsonString)
                    if (successTable != null) {
                        if (successTable.success) {
                            runOnUiThread {
                                info("已經送出審核資料，我們會盡快通過審核", "", "確定") {
                                    setResult(Activity.RESULT_OK, intent)
                                    finish()
                                }
                            }
                        } else {
                            runOnUiThread {
                                warning(successTable.msg)
                            }
                        }
                    }
                } catch (e: JsonParseException) {
                    warning(e.localizedMessage!!)
                    //println(e.localizedMessage)
                }
            }
        }

    }

    fun cancelButtonPressed(view: View) {
        prev()
    }
}