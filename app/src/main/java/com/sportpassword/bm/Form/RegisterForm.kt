package com.sportpassword.bm.Form

import android.text.InputType
import com.sportpassword.bm.Form.FormItem.*
import com.sportpassword.bm.Utilities.*

class RegisterForm: BaseForm() {

    override fun configureItems() {

        val section1 = SectionFormItem("登入資料")
        val emailItem = TextFieldFormItem(EMAIL_KEY, "EMail", "請輸入EMail", null, true)
        val passwordItem = PasswordFormItem(PASSWORD_KEY, "密碼", "請輸入密碼", null, true)
        val repasswordItem = PasswordFormItem(REPASSWORD_KEY, "密碼確認", "再次輸入密碼", null, true)

        val section2 = SectionFormItem("個人資料")
        val nameItem = TextFieldFormItem(NAME_KEY, "姓名", "請輸入真實姓名", null, true)
        val nicknameItem = TextFieldFormItem(NICKNAME_KEY, "暱稱", "網路上使用", null, true)
        val dobItem = DateFormItem(DOB_KEY, "生日", "")
        val sexItem = SexFormItem(SEX_KEY, "M", true)

        val section3 = SectionFormItem("聯絡資料")
        val mobileItem = TextFieldFormItem(MOBILE_KEY, "行動電話", "", null, true)
        val telItem = TextFieldFormItem(TEL_KEY, "市內電話", "", null, false)
        val cityItem = CityFormItem(true)
        val section4 = SectionFormItem("社群資料")
        val section5 = SectionFormItem("隱私權")

        formItems = arrayListOf(
                section1,emailItem,passwordItem, repasswordItem,
                section2,nameItem,nicknameItem,dobItem, sexItem,
                section3,mobileItem,telItem,cityItem,
                section4,
                section5
        )
    }
}