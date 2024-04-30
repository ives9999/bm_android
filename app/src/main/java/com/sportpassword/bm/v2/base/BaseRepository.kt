package com.sportpassword.bm.v2.base

open class BaseRepository<T>: IRepository<T> {
    override suspend fun getRead(
        page: Int,
        perpage: Int,
        otherParams: Map<String, String>?,
        callback: IRepository.IDaoCallback<T>
    ) {

    }

    override fun getOne(token: String, callback: IRepository.IDaoCallback<T>) {

    }
}