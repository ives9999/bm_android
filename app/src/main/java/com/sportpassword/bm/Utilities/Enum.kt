package com.sportpassword.bm.Utilities

import android.graphics.Color
import android.text.InputType

open class MYENUM<T>() {

//    fun all(allValues: ArrayList<T>): ArrayList<HashMap<String, Any?>> {
//        val res: ArrayList<HashMap<String, Any?>> = arrayListOf()
//        for (item in allValues) {
//            res.add(hashMapOf("key" to item.toString(), "value" to item))
//        }
//        return res
//    }
//
//    fun makeSelect(allValues: ArrayList<T>): ArrayList<HashMap<String, String>> {
//        val res: ArrayList<HashMap<String, String>> = arrayListOf()
//        for (item in allValues) {
//            res.add(hashMapOf("title" to item.value, "value" to item.toString()))
//        }
//        return res
//    }
}

enum class CELL_TYPE(val value: String) {
    PLAIN("plain"),
    TEXTFIELD("textField"),
    TAG("tag"),
    NUMBER("number"),
    CART("cart"),
    RADIO("radio"),
    MORE("more"),
    BARCODE("barcode"),
    SEX("sex"),
    PASSWORD("password"),
    PRIVACY("privacy"),
    SWITCH("switch"),
    CONTENT("content");

    fun toInt(): Int {
        when (this) {
            PLAIN -> return 0
            TEXTFIELD -> return 1
            TAG -> return 2
            NUMBER -> return 3
            CART -> return 4
            RADIO -> return 5
            MORE -> return 6
            BARCODE -> return 7
            SEX -> return 8
            PASSWORD -> return 9
            PRIVACY -> return 10
            SWITCH -> return 11
            CONTENT -> return 12
        }
    }

}

enum class MEMBER_COIN_IN_TYPE(val englishName: String, val chineseName: String) {
    buy("buy", "購買"),
    gift("gift", "贈品"),
    none("none", "無");

    companion object {

        fun enumFromString(value: String): MEMBER_COIN_IN_TYPE {
            when (value) {
                "buy" -> return MEMBER_COIN_IN_TYPE.buy
                "gift" -> return MEMBER_COIN_IN_TYPE.gift
                "none" -> return MEMBER_COIN_IN_TYPE.none
            }
            return MEMBER_COIN_IN_TYPE.none
        }
    }
}

enum class MEMBER_COIN_OUT_TYPE(val englishName: String, val chineseName: String) {
    product("product", "商品"),
    course("course", "課程"),
    none("none", "無");

    companion object {

        fun enumFromString(value: String): MEMBER_COIN_OUT_TYPE {
            when (value) {
                "product" -> return product
                "course" -> return course
                "none" -> return none
            }
            return none
        }
    }
}

enum class MYCOLOR(val value: Int) {
    //    danger("danger"), success("success"), primary("primary"), warning("warning"),
//    info("info"), gray("gray")
    primary(0x245580),
    warning(0xF1C40F),
    info(0x659be0),
    danger(0xc12e2a),
    success(0x419641),
    white(0xe1e1e1);


//    fun toString(): String {
//        when (this) {
//            danger -> return "danger"
//            success -> return "success"
//            primary -> return "primary"
//            warning -> return "warning"
//            info -> return "info"
//            gray -> return "gray"
//        }
//        return "success"
//    }

    fun toColor(): Int {
        val hexColor = String.format("#%06X", 0xFFFFFF and this.value)
        return Color.parseColor(hexColor)
    }

    companion object {

        val allValues: ArrayList<MYCOLOR> = arrayListOf(primary, warning, info, danger, success, white)

        fun from(value: String): MYCOLOR {
            when (value) {
                "danger" -> return danger
                "success" -> return success
                "primary" -> return primary
                "warning" -> return warning
                "info" -> return info
                "white" -> return white
            }
            return success
        }

        fun all(): ArrayList<HashMap<String, Any>> {
            val res: ArrayList<HashMap<String, Any>> = arrayListOf()
            for (item in allValues) {
                res.add(hashMapOf("key" to item.toString(), "value" to item, "color" to item.toColor()))
            }
            return res
        }
    }
}

enum class STATUS(val value: String) {
    online("上線"),
    offline("下線"),
    padding("草稿"),
    trash("垃圾桶"),
    delete("刪除");

    companion object {

        val allValues: ArrayList<STATUS> = arrayListOf(online, offline, padding, trash, delete)

        fun from(value: String): STATUS {
            when (value) {
                "online" -> return online
                "offline" -> return offline
                "padding" -> return padding
                "trash" -> return trash
                "delete" -> return delete
                else -> return online
            }
            return online
        }

        fun all(): ArrayList<HashMap<String, Any>> {
            val res: ArrayList<HashMap<String, Any>> = arrayListOf()
            for (item in allValues) {
                res.add(hashMapOf("key" to item.toString(), "value" to item, "ch" to item.value))
            }
            return res
        }
    }
}

enum class DEGREE(val value: String) {
    new("新手"), soso("普通"), high("高手");
    companion object {
        fun toString(value: DEGREE): String {
            when (value) {
                DEGREE.new -> return "new"
                DEGREE.soso -> return "soso"
                DEGREE.high -> return "high"
            }
        }
        fun fromChinese(findValue: String): DEGREE = DEGREE.values().first { it.value == findValue }

        fun fromEnglish(findValue: String): DEGREE {
            when (findValue) {
                "new" ->return new
                "soso" ->return soso
                "high" ->return high
            }
            return new
        }
        fun all(): Map<String, String> {
            return mapOf("new" to "新手", "soso" to "普通", "high" to "高手")
        }
    }
}

enum class SELECT_TIME_TYPE(val value: Int) {
    play_start(0), play_end(1);
}

enum class SELECT_DATE_TYPE(val value: Int) {
    start(0), end(1);
}

enum class TEXT_INPUT_TYPE(val value: String) {
    temp_play("臨打"),
    charge("收費標準"),
    content("詳細內容"),
    exp("經歷"),
    feat("比賽成績"),
    license("證照"),
    timetable_coach("課程說明")
}

enum class CYCLE_UNIT(val value: String) {
    month("月"),
    week("週");

    companion object {
        val allValues: ArrayList<CYCLE_UNIT> = arrayListOf(month, week)

        fun from(value: String): CYCLE_UNIT {
            when (value) {
                "month" -> return month
                "week" -> return week
            }
            return month
        }

        fun all(): ArrayList<HashMap<String, Any>> {
            val res: ArrayList<HashMap<String, Any>> = arrayListOf()
            for (item in allValues) {
                res.add(hashMapOf("key" to item.toString(), "value" to item))
            }
            return res
        }

        fun makeSelect(): ArrayList<HashMap<String, String>> {
            val res: ArrayList<HashMap<String, String>> = arrayListOf()
            for (item in allValues) {
                res.add(hashMapOf("title" to item.value, "value" to item.toString()))
            }
            return res
        }
    }
}

enum class COURSE_KIND(val value: String) {
    one("一次性"),
    cycle("週期性");

    companion object {

        val allValues: ArrayList<COURSE_KIND> = arrayListOf(one, cycle)

        fun from(value: String): COURSE_KIND {
            when (value) {
                "one" -> return one
                "cycle" -> return cycle
            }
            return cycle
        }

        fun all(): ArrayList<HashMap<String, Any>> {
            val res: ArrayList<HashMap<String, Any>> = arrayListOf()
            for (item in allValues) {
                res.add(hashMapOf("key" to item.toString(), "value" to item))
            }
            return res
        }

        fun makeSelect(): ArrayList<HashMap<String, String>> {
            val res: ArrayList<HashMap<String, String>> = arrayListOf()
            for (item in allValues) {
                res.add(hashMapOf("title" to item.value, "value" to item.toString()))
            }
            return res
        }
    }
}

enum class PRICE_UNIT(val value: String) {

    month("每月"),
    week("每週"),
    season("每季"),
    year("每年"),
    span("每期"),
    other("其他");

    companion object: MYENUM<PRICE_UNIT>() {

        val allValues: ArrayList<PRICE_UNIT> = arrayListOf(month, week, season, year, span, other)

        fun from(value: String): PRICE_UNIT {
            when (value) {
                "month" -> return month
                "week" -> return week
                "season" -> return season
                "year" -> return year
                "span" -> return span
                "other" -> return other
            }
            return month
        }

        fun all(): ArrayList<HashMap<String, Any>> {
            val res: ArrayList<HashMap<String, Any>> = arrayListOf()
            for (item in allValues) {
                res.add(hashMapOf("key" to item.toString(), "value" to item))
            }
            return res
        }

        fun makeSelect(): ArrayList<HashMap<String, String>> {
            val res: ArrayList<HashMap<String, String>> = arrayListOf()
            for (item in allValues) {
                res.add(hashMapOf("title" to item.value, "value" to item.toString()))
            }
            return res
        }
    }
}

enum class WEEKDAY(val value: Int) {
    mon(1),
    tue(2),
    wed(3),
    thu(4),
    fri(5),
    sat(6),
    sun(7);

    fun enumToShortString(): String {
        when (this) {
            mon -> return "一"
            tue -> return "二"
            wed -> return "三"
            thu -> return "四"
            fri -> return "五"
            sat -> return "六"
            sun -> return "日"
        }
    }

    companion object: MYENUM<WEEKDAY>() {

        val allValues: ArrayList<WEEKDAY> = arrayListOf(mon, tue, wed, thu, fri, sat, sun)

        fun from(value: Int): WEEKDAY {
            when (value) {
                1 -> return mon
                2 -> return tue
                3 -> return wed
                4 -> return thu
                5 -> return fri
                6 -> return sat
                7 -> return sun
            }
            return mon
        }

        fun intToString(value: Int): String {
            when (value) {
                1 -> return "星期一"
                2 -> return "星期二"
                3 -> return "星期三"
                4 -> return "星期四"
                5 -> return "星期五"
                6 -> return "星期六"
                7 -> return "星期日"
            }
            return "星期一"
        }

        fun enumToString(value: WEEKDAY): String {

            return WEEKDAY.intToString(value.value)
        }

        fun all(): ArrayList<HashMap<String, Any>> {
            val res: ArrayList<HashMap<String, Any>> = arrayListOf()
            for (item in allValues) {
                res.add(hashMapOf("key" to item.toString(), "value" to item))
            }
            return res
        }

        fun makeSelect(): ArrayList<HashMap<String, String>> {
            val res: ArrayList<HashMap<String, String>> = arrayListOf()
            for (item in allValues) {
                res.add(hashMapOf("title" to WEEKDAY.enumToString(item), "value" to item.value.toString()))
            }
            return res
        }
    }
}

enum class ORDER_PROCESS(val englishName: String, val chineseName: String) {
    normal("normal", "訂單成立"),
    gateway("gateway", "完成付款"),
    shipping("shipping", "出貨中"),
    store("store", "送達超商"),
    complete("complete", "訂單完成"),
    returning("returning", "商品退回中"),
    `return`("return", "商品已退回"),
    gateway_fail("gateway_fail", "付款失敗");

    fun toChineseString(): String {
        return chineseName
    }

    fun toEnglishString(): String {
        return englishName
    }

    companion object: MYENUM<ORDER_PROCESS>() {

        fun getRawValueFromString(value: String): String {
            return ORDER_PROCESS.valueOf(value).toChineseString()
        }
    }
}

enum class ALL_PROCESS(val englishName: String, val chineseName: String) {
    notexist("notexist", "沒有"),
    normal("normal", "訂單成立"),
    gateway_on("gateway_on", "付款中"),
    gateway_off("gateway_off", "完成付款，準備出貨"),
    shipping("shipping", "準備出貨"),
    logistic("logistic", "到達物流中心"),
    store("store", "到達便利商店"),
    complete("complete", "完成取貨"),
    returning("returning", "商品退回中"),
    `return`("return", "商品已退回"),
    gateway_fail("gateway_fail", "付款失敗");

    fun toChineseString(): String {
        return chineseName
    }

    fun toEnglishString(): String {
        return englishName
    }

    companion object: MYENUM<ALL_PROCESS>() {

        fun intToEnum(enumInt: Int): ALL_PROCESS {
            when (enumInt) {
                0-> return notexist
                1-> return normal
                2-> return gateway_on
                3-> return gateway_off
                4-> return shipping
                5-> return logistic
                6-> return store
                7-> return complete
                8-> return returning
                9-> return `return`
                10-> return gateway_fail
            }
            return normal
        }

        fun getRawValueFromString(value: String): String {
            return ALL_PROCESS.valueOf(value).toChineseString()
        }
    }
}

enum class PAYMENT_PROCESS(val englishName: String, val chineseName: String) {
    normal("normal", "未付款"),
    code("code", "取得付款代碼"),
    complete("complete", "完成付款");

    fun toChineseString(): String {
        return chineseName
    }

    fun toEnglishString(): String {
        return englishName
    }

    companion object: MYENUM<PAYMENT_PROCESS>() {

        fun getRawValueFromString(value: String): String {
            return PAYMENT_PROCESS.valueOf(value).toChineseString()
        }
    }
}

enum class GATEWAY(val englishName: String, val chineseName: String) {

    credit_card("credit_card", "信用卡"),
    store_cvs("store_cvs", "超商代碼"),
    store_barcode("store_barcode", "超商條碼"),
    store_pay_711("store_pay_711", "7-11超商取貨付款"),
    store_pay_family("store_pay_family", "全家超商取貨付款"),
    store_pay_hilife("store_pay_hilife", "萊爾富超商取貨付款"),
    store_pay_ok("store_pay_ok", "OK超商取貨付款"),
    ATM("ATM", "虛擬帳戶"),
    remit("remit", "匯款"),
    cash("cash", "現金"),
    coin("coin", "解碼點數");

    fun toChineseString(): String {
        return chineseName
    }

    fun toEnglishString(): String {
        return englishName
    }

    fun mapToShipping(): SHIPPING {
        return when(this) {
            store_pay_711-> SHIPPING.store_711
            store_pay_family->SHIPPING.store_family
            store_pay_hilife->SHIPPING.store_hilife
            store_pay_ok->SHIPPING.store_ok
            else->SHIPPING.direct
        }
    }

    fun enumToECPay(): String {
        return when(this) {
            store_pay_711-> "UNIMARTC2C"
            store_pay_family -> "FAMIC2C"
            store_pay_hilife -> return "HILIFEC2C"
            store_pay_ok -> return "OKMARTC2C"
            else -> "UNIMARTC2C"
        }
    }

    companion object: MYENUM<GATEWAY>() {

        fun getRawValueFromString(value: String): String {
            return GATEWAY.valueOf(value).toChineseString()
        }

        fun stringToEnum(str: String): GATEWAY {

            when(str) {

                "credit_card"-> return credit_card
                "store_cvs"-> return store_cvs
                "store_barcode"-> return store_barcode
                "store_pay_711"-> return store_pay_711
                "store_pay_family"-> return store_pay_family
                "store_pay_hilife"-> return store_pay_hilife
                "store_pay_ok"-> return store_pay_ok
                "ATM"-> return ATM
                "remit"-> return remit
                "cash"-> return cash
                "coin"-> return coin
            }
            return credit_card
        }
    }
}

enum class GATEWAY_PROCESS(val englishName: String, val chineseName: String) {

    normal("normal", "未付款"),
    code("code", "取得付款代碼"),
    complete("complete", "完成付款"),
    fail("fail", "付款失敗"),
    `return`("return", "完成退款");

    fun toChineseString(): String {
        return chineseName
    }

    fun toEnglishString(): String {
        return englishName
    }

    companion object: MYENUM<SHIPPING_PROCESS>() {

        fun getRawValueFromString(value: String): String {
            return GATEWAY_PROCESS.valueOf(value).toChineseString()
        }
    }
}

enum class SHIPPING(val englishName: String, val chineseName: String) {

    direct("direct", "宅配"),
    store_711("store_711", "7-11超商取貨"),
    store_family("store_family", "全家超商取貨"),
    store_hilife("store_hilife", "萊爾富超商取貨"),
    store_ok("store_ok", "OK超商取貨"),
    cash("cash", "面交");

    fun toChineseString(): String {
        return chineseName
    }

    fun toEnglishString(): String {
        return englishName
    }

    companion object: MYENUM<SHIPPING_PROCESS>() {

        fun getRawValueFromString(value: String): String {
            return SHIPPING.valueOf(value).toChineseString()
        }

        fun stringToEnum(str: String): SHIPPING {

            when(str) {

                "direct"-> return direct
                "store_711"-> return store_711
                "store_family"-> return store_family
                "store_hilife"-> return store_hilife
                "store_ok"-> return store_ok
                "cash"-> return cash
            }
            return direct
        }
    }
}

enum class SHIPPING_PROCESS(val englishName: String, val chineseName: String) {
    normal("normal", "準備中"),
    shipping("shipping", "已經出貨"),
    logistic("logistic", "已經送到物流中心"),
    store("store", "商品已到便利商店"),
    complete("complete", "已完成取貨"),
    `return`("back", "貨物退回");

    fun toChineseString(): String {
        return chineseName
    }

    fun toEnglishString(): String {
        return englishName
    }

    companion object: MYENUM<SHIPPING_PROCESS>() {

        fun getRawValueFromString(value: String): String {
            return SHIPPING_PROCESS.valueOf(value).toChineseString()
        }
    }
}

enum class KEYBOARD(val type: String) {

    default("default"),
    emailAddress("emailAddress"),
    numberPad("numberPad"),
    URL("URL"),
    password("password");

    override fun toString(): String {
        return type
    }

    fun toSwift(): Int {
        when(this) {
            default -> return InputType.TYPE_CLASS_TEXT
            emailAddress -> return InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            numberPad -> return InputType.TYPE_CLASS_NUMBER
            URL -> return InputType.TYPE_TEXT_VARIATION_URI
            password -> return InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
    }

    companion object: MYENUM<KEYBOARD>() {

        fun stringToSwift(str: String): Int {

            //val n = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

            when(str) {

                "default"-> return InputType.TYPE_CLASS_TEXT
                "emailAddress"-> return InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                "numberPad"-> return InputType.TYPE_CLASS_NUMBER
                "URL"-> return InputType.TYPE_TEXT_VARIATION_URI
                "password"-> return (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
            }
            return InputType.TYPE_CLASS_TEXT
        }
    }
}

enum class SIGNUP_STATUS(val type: String) {

    normal("報名"),
    standby("候補"),
    cancel("取消");

    override fun toString(): String {
        return type
    }

    companion object {
        fun toString(value: SIGNUP_STATUS): String {
            when (value) {
                normal -> return "normal"
                standby -> return "standby"
                cancel -> return "cancel"
            }
        }

        fun stringToSwift(str: String): SIGNUP_STATUS {

            when(str) {

                "normal"-> return normal
                "standby"-> return standby
                "cancel"-> return cancel
            }
            return normal
        }
    }
}

enum class MEMBER_SUBSCRIPTION_KIND(val englishName: String, val chineseName: String) {

    diamond("diamond", "鑽石"),
    white_gold("white_gold", "白金"),
    gold("gold", "金牌"),
    silver("silver", "銀牌"),
    copper("copper", "銅牌"),
    steal("steal", "鐵牌"),
    basic("basic", "基本");

    fun toChineseString(): String {
        return chineseName
    }

    fun toEnglishString(): String {
        return englishName
    }

    fun lottery(): Int {
        when (this) {
            basic-> return 0
            copper -> return 1
            silver -> return 2
            gold-> return 3
            white_gold-> return 4
            diamond-> return 5
            else -> return 0
        }
    }

    companion object: MYENUM<MEMBER_SUBSCRIPTION_KIND>() {

        fun getRawValueFromString(value: String): String {
            return SHIPPING.valueOf(value).toChineseString()
        }

        fun stringToEnum(str: String): MEMBER_SUBSCRIPTION_KIND {

            when(str) {
                "basic"-> return basic
                "steal"-> return steal
                "copper"-> return copper
                "silver"-> return silver
                "gold"-> return gold
                "white_gold"-> return white_gold
                "diamond"-> return diamond
            }
            return basic
        }
    }
}