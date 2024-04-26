package com.sportpassword.bm.v2.arena.show

import com.sportpassword.bm.v2.base.IRepository

class Repository: IRepository<ShowDao> {

    //    override fun getOne() {
//
//    }
    override fun getRead(
        page: Int,
        perpage: Int,
        params: Map<String, String>?,
        callback: IRepository.IDaoCallback<ShowDao>
    ) {
        
    }
}