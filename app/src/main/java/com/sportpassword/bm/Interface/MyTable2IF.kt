package com.sportpassword.bm.Interface

import com.sportpassword.bm.Controllers.BaseActivity
import com.sportpassword.bm.Controllers.MyTable2VC
import com.sportpassword.bm.Controllers.MyViewHolder2
import com.sportpassword.bm.Models.Table
import com.sportpassword.bm.Utilities.then

interface MyTable2IF {

    fun <T: MyViewHolder2<U>, U: Table> showTableView(tableView: MyTable2VC<T, U>, jsonString: String?): Boolean {

        if (jsonString != null) {
            val b: Boolean = tableView.parseJSON(jsonString)

            return ((!b && tableView.msg.isEmpty()) then { false }) ?: true
        }

        return false
    }
}