package com.sportpassword.bm.Models

open class DataTable<T: Table> {
    var _meta: MetaTable = MetaTable()
    var rows: ArrayList<T>? = null
}