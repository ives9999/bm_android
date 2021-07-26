package com.sportpassword.bm.Services

import com.sportpassword.bm.Utilities.*

object CartService: DataService() {

    override fun getOneURL(): String {
        return URL_ONE.format("cart")
    }

    override fun getListURL(): String {
        return URL_CART_LIST
    }

    override fun getUpdateURL(): String {
        return URL_CART_UPDATE
    }
}