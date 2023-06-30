package com.sportpassword.bm.Controllers

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.reflect.TypeToken
import com.sportpassword.bm.Models.Table
import com.sportpassword.bm.Models.Tables2
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.EMAIL_VALIDATE
import com.sportpassword.bm.Utilities.MOBILE_VALIDATE
import com.sportpassword.bm.Utilities.setImage
import com.sportpassword.bm.Views.ShowTop2
import com.sportpassword.bm.bm_new.ui.match.team_list.MatchTeamListActivity
import com.sportpassword.bm.databinding.ActivityMemberItemVcBinding
import com.sportpassword.bm.member
import timber.log.Timber
import java.lang.reflect.Type

class MemberItemVC : BaseActivity() {

    private lateinit var binding: ActivityMemberItemVcBinding
    var mainMemberEnum: MainMemberEnum = MainMemberEnum.info

    var showTop2: ShowTop2? = null
    var recyclerView: RecyclerView? = null
    private val tableType: Type = object : TypeToken<Tables2<MainMemberTable>>() {}.type
    lateinit var tableView: MyTable2VC<MemberItemViewHolder, MainMemberTable, MemberItemVC>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMemberItemVcBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if (intent.hasExtra("item")) {
            val item: String = intent.getStringExtra("item")!!
            mainMemberEnum = MainMemberEnum.chineseGetEnum(item)
        }

        findViewById<ShowTop2>(R.id.top) ?. let {
            showTop2 = it
            it.setTitle(mainMemberEnum.chineseName)
            it.showPrev(true)
        }

        findViewById<RecyclerView>(R.id.list_container) ?. let {
            recyclerView = it
            tableView = MyTable2VC(
                it,
                null,
                R.layout.main_member_cell,
                ::MemberItemViewHolder,
                tableType,
                this::tableViewSetSelected,
                this::getDataFromServer,
                this
            )

            val enums: ArrayList<MemberItemEnum> = MemberItemEnum.allValues(mainMemberEnum)
            for (myEnum in enums) {
                tableView.rows.add(
                    MainMemberTable(
                        myEnum.chineseName,
                        myEnum.getIcon()
                    )
                )
            }
            tableView.setItems()
            //tableView.notifyDataSetChanged()
        }
    }

    override fun cellClick(row: Table) {
        val _row: MainMemberTable = row as MainMemberTable
        _row.let {
            val memberItemEnum: MemberItemEnum? = MemberItemEnum(it.title)
            if (memberItemEnum != null) {
                to(mainMemberEnum, memberItemEnum)
            }

        }
    }

    private fun to(mainMemberEnum: MainMemberEnum, memberItemEnum: MemberItemEnum) {
        if (mainMemberEnum == MainMemberEnum.info) {
            if (memberItemEnum == MemberItemEnum.info) {
                toRegister()
            } else if (memberItemEnum == MemberItemEnum.change_password) {
                toUpdatePassword()
            } else if (memberItemEnum == MemberItemEnum.email_validate) {
                toValidate("email")
            } else if (memberItemEnum == MemberItemEnum.mobile_validate) {
                toValidate("mobile")
            }
        } else if (mainMemberEnum == MainMemberEnum.order) {
            if (memberItemEnum == MemberItemEnum.cart) {
                toMemberCartList("member")
            } else if (memberItemEnum == MemberItemEnum.order) {
                toMemberOrderList()
            }
        } else if (mainMemberEnum == MainMemberEnum.like) {
            if (memberItemEnum == MemberItemEnum.team) {
                this.toTeam(null, true, true, true)
            } else if (memberItemEnum == MemberItemEnum.arena) {
                this.toArena(true, true, true)
            } else if (memberItemEnum == MemberItemEnum.teach) {
                toTeach(true)
            } else if (memberItemEnum == MemberItemEnum.coach) {
                toCoach(true)
            } else if (memberItemEnum == MemberItemEnum.course) {
                this.toCourse(true, true, true)
            } else if (memberItemEnum == MemberItemEnum.product) {
                toProduct(true)
            } else if (memberItemEnum == MemberItemEnum.store) {
                toStore(true)
            }
        } else if (mainMemberEnum == MainMemberEnum.join) {
            if (memberItemEnum == MemberItemEnum.team) {
                toMemberTeamList()
            } else if (memberItemEnum == MemberItemEnum.tempPlay) {
                //toMemberSignuplist("temp")
            } else if (memberItemEnum == MemberItemEnum.course) {
                //toMemberSignuplist("course")
            }
        } else if (mainMemberEnum == MainMemberEnum.manager) {
            if (memberItemEnum == MemberItemEnum.team) {
                this.toManager("team")
            } else if (memberItemEnum == MemberItemEnum.requestManager) {
                toRequestManagerTeam()
            } else if (memberItemEnum == MemberItemEnum.course) {
                this.toManager("course")
            } else if (memberItemEnum == MemberItemEnum.match) {
                Timber.d("賽事-報名隊伍列表")
                Intent(this, MatchTeamListActivity::class.java).apply {
                    startActivity(this)
                }
            }
        }
    }

    private fun getDataFromServer(page: Int) {}
    private fun tableViewSetSelected(row: MainMemberTable): Boolean { return false }
}

enum class MemberItemEnum(val chineseName: String) {
    info("帳戶資料"),
    change_password("更改密碼"),
    email_validate("EMail認證"),
    mobile_validate("手機認證"),
    cart("購物車"),
    order("訂單查詢"),
    team("球隊"),
    arena("球館"),
    teach("教學"),
    coach("教練"),
    course("課程"),
    product("商品"),
    store("體育用品店"),
    tempPlay("臨打"),
    match("賽事"),
    requestManager("球隊申請管理權");

    companion object {

        operator fun invoke(rawValue: String) = MemberItemEnum.values().find { it.chineseName == rawValue }
        fun allValues(mainMemberEnum: MainMemberEnum): ArrayList<MemberItemEnum> {
            when (mainMemberEnum) {
                MainMemberEnum.info-> {
                    val arr: ArrayList<MemberItemEnum> = arrayListOf(info, change_password)
                    val validate: Int = member.validate
                    if (validate and EMAIL_VALIDATE <= 0) {
                        arr.add(email_validate)
                    }
                    if (validate and MOBILE_VALIDATE <= 0) {
                        arr.add(mobile_validate)
                    }
                    return arr
                }
                MainMemberEnum.order-> {
                    return arrayListOf(cart, order)
                }
                MainMemberEnum.like-> {
                    return arrayListOf(team, arena, teach, coach, course, product, store)
                }
                MainMemberEnum.join-> {
                    return arrayListOf(team, tempPlay, course)
                }
                MainMemberEnum.manager-> {
                    return arrayListOf(team, requestManager, course, match)
                }
                else-> {
                    return arrayListOf(info, change_password)
                }
            }
        }

//        fun toEnum(rawValue: String): MemberItemEnum? {
//            val thisEnum: MemberItemEnum = MemberItemEnum(rawValue: rawValue)
//
//            {
//                return thisEnum
//            } else {
//                return nil
//            }
//        }
    }

    fun getIcon(): String {
        when (this) {
            info-> return "ic_info_svg"
            email_validate-> return "ic_validate_svg"
            mobile_validate-> return "ic_validate_svg"
            change_password-> return "ic_order_svg"
            cart-> return "ic_cart_svg"
            order-> return "ic_truck_svg"
            team-> return "ic_team_on_svg"
            arena-> return "ic_arena_on_svg"
            teach-> return "ic_teach_svg"
            coach-> return "ic_coach_svg"
            course-> return "ic_course_on_svg"
            product-> return "ic_product_svg"
            store-> return "ic_store_svg"
            tempPlay-> return "ic_tempplay_svg"
            match-> return "ic_ball_svg"
            requestManager-> return "ic_request_manager_svg"
        }
    }
}

class MemberItemViewHolder(
    context: Context,
    viewHolder: View,
    delegate: MemberItemVC
): MyViewHolder2<MainMemberTable, MemberItemVC>(context, viewHolder, delegate) {

    var iconIV: ImageView? = null
    var textTV: TextView? = null
    var greaterIV: ImageView? = null

    init {
        viewHolder.findViewById<ImageView>(R.id.iconIV) ?. let {
            iconIV = it
        }

        viewHolder.findViewById<TextView>(R.id.textTV) ?. let {
            textTV = it
        }

        viewHolder.findViewById<ImageView>(R.id.greaterIV) ?. let {
            it.visibility = View.GONE
        }
    }

    override fun bind(row: MainMemberTable, idx: Int) {
        super.bind(row, idx)

        iconIV?.setImage(row.icon)
        textTV?.text = row.title
    }
}
