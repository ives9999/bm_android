package com.sportpassword.bm.v2.base

open class BaseRpository<T>: IRepository<T> {
    override fun getRead(
        page: Int,
        perpage: Int,
        params: Map<String, String>?,
        callback: IRepository.IDaoCallback<T>
    ) {

    }
}