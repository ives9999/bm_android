package com.sportpassword.bm.Utilities

/**
 * Created by ivessun on 2018/2/2.
 */

const val CHANNEL = "bm"
var gSimulate: Boolean = false

// URL Constants
const val REMOTE_BASE_URL = "http://bm.sportpassword.com"
const val LOCALHOST_BASE_URL = "http://bm.sportpassword.localhost"
var BASE_URL = if (gSimulate) LOCALHOST_BASE_URL else REMOTE_BASE_URL
var URL_HOME = "${BASE_URL}/app/"
var URL_LIST = "${URL_HOME}%@"
var URL_SHOW = "${URL_HOME}%@/show/%@?device=app"
var URL_LOGIN = URL_HOME + "login"
var URL_FB_LOGIN = URL_HOME + "member/fb"
var URL_REGISTER = URL_HOME + "register"
var URL_MEMBER_UPDATE = URL_HOME + "member/update"
var URL_CITYS = URL_HOME + "citys"
var URL_ARENA_BY_CITY_ID = URL_HOME + "arena_by_city"
var URL_TEAM_UPDATE = URL_HOME + "team/update"
var URL_ONE = "${URL_HOME}%@/one"
var URL_TEAM = URL_HOME + "team/"
var URL_TEAM_TEMP_PLAY = URL_TEAM + "tempPlay/onoff"
var URL_TEAM_TEMP_PLAY_LIST = URL_TEAM + "tempPlay/list"
var URL_TEAM_PLUSONE = BASE_URL + "/team/tempPlay/plusOne/"