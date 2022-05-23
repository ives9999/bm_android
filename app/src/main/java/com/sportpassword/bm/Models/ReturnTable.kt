package com.sportpassword.bm.Models

class ReturnTable: Table() {

    var order_id: Int = -1
    var sn_id: String = ""
    var return_code: String = ""
    var expire_at: String = ""
    var error_msg: String = ""

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
    }
}