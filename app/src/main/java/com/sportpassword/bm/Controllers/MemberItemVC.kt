package com.sportpassword.bm.Controllers

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.reflect.TypeToken
import com.sportpassword.bm.Models.Table
import com.sportpassword.bm.Models.Tables2
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.setImage
import com.sportpassword.bm.Views.ShowTop2
import com.sportpassword.bm.databinding.ActivityMemberItemVcBinding
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
        val _row: MainMemberTable? = row as? MainMemberTable
        _row.let {

        }
    }

    private fun getDataFromServer(page: Int) {}
    private fun tableViewSetSelected(row: MainMemberTable): Boolean { return false }
}

enum class MemberItemEnum(val chineseName: String) {
    info("帳戶資料"),
    change_password("更改密碼"),
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
    requestManager("球隊申請管理權");

    companion object {
        fun allValues(mainMemberEnum: MainMemberEnum): ArrayList<MemberItemEnum> {
            when (mainMemberEnum) {
                MainMemberEnum.info-> {
                    return arrayListOf(info, change_password)
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
                    return arrayListOf(team, requestManager, course)
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
            change_password-> return "ic_order_svg"
            cart-> return "ic_cart_svg"
            order-> return "ic_truck_svg"
            team-> return "ic_team_in_svg"
            arena-> return "ic_arena_in_svg"
            teach-> return "ic_teach_svg"
            coach-> return "ic_coach_svg"
            course-> return "ic_course_svg"
            product-> return "ic_product_svg"
            store-> return "ic_store_svg"
            tempPlay-> return "ic_tempPlay_svg"
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
