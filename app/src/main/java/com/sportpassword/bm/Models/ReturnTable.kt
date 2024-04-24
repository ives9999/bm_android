package com.sportpassword.bm.Models

import com.sportpassword.bm.extensions.noSec

class ReturnTable: Table() {

    var order_id: Int = -1
    var sn_id: String = ""
    var return_code: String = ""
    var expire_at: String = ""
    var error_msg: String = ""

    var expire_at_show: String = ""

    override fun filterRow() {
        super.filterRow()

        if (sn_id == null) {
            sn_id = ""
        }

        if (return_code == null) {
            return_code = ""
        }

        if (expire_at == null) {
            expire_at = ""
        }

        if (error_msg == null) {
            error_msg = ""
        }

        if (expire_at.length > 0) {
            expire_at_show = expire_at.noSec()
        }

        if (created_at.length > 0) {
            created_at_show = created_at.noSec()
        }
    }
}