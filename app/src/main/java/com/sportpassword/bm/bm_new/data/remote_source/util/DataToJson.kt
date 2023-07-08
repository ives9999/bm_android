package com.sportpassword.bm.bm_new.data.remote_source.util

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser

fun <T> dataToJsonObject(obj: T): JsonObject {
    return JsonParser.parseString(Gson().toJson(obj)).asJsonObject
}

fun <T> dataToJsonArray(obj: T): JsonArray {
    return JsonParser.parseString(Gson().toJson(obj)).asJsonArray
}