package com.sportpassword.bm.Form

import android.text.InputType
import com.sportpassword.bm.Controllers.BaseActivity
import com.sportpassword.bm.Form.FormItem.*
import com.sportpassword.bm.Utilities.*

class RegisterForm(delegate: BaseActivity): BaseForm(null, null, "", false, delegate) {

    override fun configureItems() {

        val section1 = SectionFormItem("登入資料")
        val emailItem = EmailFormItem(EMAIL_KEY, "EMail", "請輸入EMail", null, InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS, true)
        val passwordItem = PasswordFormItem(PASSWORD_KEY, "密碼", "請輸入密碼", null, true)
        val repasswordItem = PasswordFormItem(REPASSWORD_KEY, "密碼確認", "再次輸入密碼", null, true, null, true, passwordItem)

        val section2 = SectionFormItem("個人資料")
        val nameItem = TitleFormItem(NAME_KEY, "姓名", "請輸入真實姓名", null, true)
        val nicknameItem = NicknameFormItem(NICKNAME_KEY, "暱稱", "網路上使用", null, true)
        val dobItem = DateFormItem(DOB_KEY, "生日", "")
        val sexItem = SexFormItem("M", true)

        val section3 = SectionFormItem("聯絡資料")
        val mobileItem = MobileFormItem(MOBILE_KEY, "行動電話", "", null, InputType.TYPE_CLASS_PHONE, true)
        val telItem = TelFormItem(TEL_KEY, "市內電話", "", null, InputType.TYPE_CLASS_PHONE, false)
        val cityItem = CityFormItem(true, this.delegate)
        val areaItem = AreaFormItem(true, this.delegate)
        val roadItem = RoadFormItem(ROAD_KEY, "住址", "路街名、巷、弄、號", null, true)

        val section4 = SectionFormItem("社群資料")
        val fbItem = FBFormItem(FB_KEY, "FB", "請輸入FB網址", null)
        val lineItem = LineFormItem(LINE_KEY, "Line", "請輸入Line ID", null)

        val section5 = SectionFormItem("隱私權")
        val privacyItem = PrivacyFormItem(true, this.delegate)

        formItems = arrayListOf(
                section1,emailItem,passwordItem, repasswordItem,
                section2,nameItem,nicknameItem,dobItem, sexItem,
                section3,mobileItem,telItem,cityItem,areaItem,roadItem,
                section4,fbItem,lineItem,
                section5,privacyItem
        )
    }

}