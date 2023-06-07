package com.sportpassword.bm.bm_new.data.dto.match
import com.google.gson.annotations.SerializedName

data class MatchSignUpDto(
    @SerializedName("match")
    val match: Match,
    @SerializedName("match_gifts")
    val matchGifts: List<MatchGift>,
    @SerializedName("match_group")
    val matchGroup: MatchGroup,
    @SerializedName("match_players")
    val matchPlayers: List<Any>,
    @SerializedName("order")
    val order: Any?, // null
    @SerializedName("product_token")
    val productToken: String // FhAxb6qWYnKuUAQoTp0GwEETr57IJLB
) {
    data class Match(
        @SerializedName("arena_id")
        val arenaId: Int, // 414
        @SerializedName("ball")
        val ball: String, // 台灣優迪紅標
        @SerializedName("channel")
        val channel: String, // bm
        @SerializedName("content")
        val content: String, // <p><strong>【羽球密碼】2023密碼盃羽球錦標賽競賽章程</strong></p><p>一、宗 旨：推廣全民羽球運動，提升羽球技術水準及聯絡情誼為目的。</p><p>二、主辦單位：<span style="color: rgb(44, 130, 201);"><strong>羽球密碼</strong></span>。</p><p>三、協辦單位：<span style="color: rgb(44, 130, 201);"><strong>藍色行動有限公司</strong></span>。</p><p>四、贊助單位：<span style="color: rgb(44, 130, 201);"><strong>李寧國際</strong></span>、<span style="color: rgb(44, 130, 201);"><strong>葉氏悟語體育用品店</strong></span>。</p><p>五、比賽時間：<span style="color: rgb(184, 49, 47);"><strong>2023-11-01(三) 08:00 ~ 2023-11-01(三) 18:00</strong></span></p><p>六、報名時間：<span style="color: rgb(209, 72, 65);"><strong>2023-07-01(六) 10:00 ~ 2023-08-31(五) 23:59</strong></span></p><p>七、比賽組別：</p><p>（一）個人雙打賽</p><p>&nbsp; &nbsp; 1.社會組男雙「雙人均需30歲(不含)以下，謝絕【體保生】、【甲組】報名。</p><p>&nbsp; &nbsp; 2.社會組女雙「雙人均需30歲(不含)以下，謝絕【體保生】、【甲組】報名。</p><p>&nbsp; &nbsp; 3.社會組混雙「雙人均需30歲(不含)以下，謝絕【體保生】、【甲組】報名。</p><p>&nbsp; &nbsp; 4.青年組男雙「雙人均需30歲(含)以上，謝絕【甲組】報名。</p><p>&nbsp; &nbsp; 5.青年組女雙「雙人均需30歲(含)以上，謝絕【甲組】報名。</p><p>&nbsp; &nbsp; 6.青年組混雙「雙人均需30歲(含)以上，謝絕【甲組】報名。</p><p>&nbsp; &nbsp; 7.青壯年組男雙「雙人均需45歲(含)以上。</p><p>&nbsp; &nbsp; 8.青壯年組女雙「雙人均需45歲(含)以上。</p><p>&nbsp; &nbsp; 9.青壯年組混雙「雙人均需45歲(含)以上。</p><p>八、隊數限制：每組限報<span style="color: rgb(184, 49, 47);"><strong>16</strong></span>隊。</p><p>九、比賽地點：<span style="color: rgb(184, 49, 47);"><strong>台南市南區南商運動中心</strong></span>。</p><p>十、報名辦法：請下載「<span style="color: rgb(184, 49, 47);"><strong>羽球密碼APP</strong></span>」做報名與繳費。</p><p>十一、報名費：每隊報名費<span style="color: rgb(226, 80, 65);"><strong>【</strong></span><strong><span style="color: rgb(184, 49, 47);">NT$</span></strong><span style="color: rgb(226, 80, 65);"><strong>1,400元】</strong></span></p><p>十二、贈品：本賽事贈送每位參賽者一件價值【640元】條碼T。</p><p>十三、賽事安排：</p><p>&nbsp; &nbsp; 1.預賽採<span style="color: rgb(184, 49, 47);"><strong>【四方形】</strong></span>排名賽，取前兩名進入決賽。</p><p>&nbsp; &nbsp; 2.預賽採壹局<span style="color: rgb(184, 49, 47);"><strong>【25分制】</strong></span>，無Deuce，13分交換場地。</p><p>&nbsp; &nbsp; 3.預賽採總積分計算，每組<span style="color: rgb(184, 49, 47);"><strong>【前2名】</strong></span>進入決賽，如果積分相同則比雙方勝負，勝者晉級決賽。</p><p>&nbsp; &nbsp; 4.決賽採【樹狀】單淘汰賽。</p><p>&nbsp; &nbsp; 5.決賽採壹局<span style="color: rgb(184, 49, 47);"><strong>【25分制】</strong></span>，無Deuce，13分交換場地，輸者遭淘汰。</p><p>&nbsp; &nbsp; 6.決賽取前2名，不打季軍賽。</p><p>十四、比賽辦法：</p><p>（一）本比賽採用中華民國羽球協會公佈之最新羽球規則。(依世界羽球聯盟BWF 新制所訂規則)。</p><p>（二）選手年紀以 112 年 減 出生年計算。例如:112-76=36。</p><p>（三）本賽事甲組資格認定以中華民國羽球協會公布之甲組球員名單為主。</p><p>（四）各隊抽籤排定後，不得請求更換人選。選手逾出賽時間 5 分鐘未出場 (以會場現場掛鐘為準)，以棄權論，棄權後之該項其他賽程不得再出賽， 成績不予計算，如遇賽程提前時，請依大會廣播出賽，如有特殊事故得依 賽程表時間出賽，並不宣判棄權;但如遇賽程延遲時，經大會廣播出賽二 分鐘內仍未到場比賽者，及宣判棄權，如連場則給予 5 分鐘休息，不得異議。</p><p>（五）參加比賽之球員，應攜帶具相片相關證件以備隨時查驗，如對比賽有意見者，請於列隊時提出，賽中、賽後恕不受理，逾比賽時間五分鐘未出賽者以棄權論。</p><p>（六）不服從裁判及裁判長之判決及不遵守大會規定者，得取消其比賽資格。</p><p>（七）比賽進行期間有關「界內、界外」的判決以裁判判決為主，不服判決者，得取消其比賽資格。</p><p>十五、比賽用球：比賽指定用球為【<span style="color: rgb(184, 49, 47);"><strong>台灣紅色優迪</strong></span>】或同等級之用球。</p><p>十六、請參與人員自行依需要投保人身險。</p><p>十七、聯絡資訊：</p><p>email：app@bluemobile.com.tw</p><p>手機：0911-299-994</p><p>十八、其他未規定事項由大會另行公佈實施。</p><p><br></p>
        @SerializedName("created_at")
        val createdAt: String, // 2023-04-13 22:11:26
        @SerializedName("created_id")
        val createdId: Int, // 1
        @SerializedName("id")
        val id: Int, // 6
        @SerializedName("match_end")
        val matchEnd: String, // 2023-11-02 17:00:00
        @SerializedName("match_start")
        val matchStart: String, // 2023-11-01 08:00:00
        @SerializedName("name")
        val name: String, // 2023羽球密碼盃比賽
        @SerializedName("signup_end")
        val signupEnd: String, // 2023-04-23 17:30:33
        @SerializedName("signup_start")
        val signupStart: String, // 2023-04-23 17:30:33
        @SerializedName("sort_order")
        val sortOrder: Int, // 1681395119
        @SerializedName("status")
        val status: String, // online
        @SerializedName("token")
        val token: String, // Hm0k2OvzOyZntkHvYikj1oEJR1BW5pD
        @SerializedName("updated_at")
        val updatedAt: String // 2023-05-17 22:01:17
    )

    data class MatchGift(
        @SerializedName("created_at")
        val createdAt: String, // 2023-05-07 11:58:06
        @SerializedName("id")
        val id: Int, // 1
        @SerializedName("match_id")
        val matchId: Int, // 6
        @SerializedName("product")
        val product: Product,
        @SerializedName("product_id")
        val productId: Int, // 1
        @SerializedName("sort_order")
        val sortOrder: Int, // 0
        @SerializedName("token")
        val token: String, // abcde
        @SerializedName("updated_at")
        val updatedAt: String // 2023-05-10 18:44:09
    ) {
        data class Product(
            @SerializedName("name")
            val name: String, // 2019羽球密碼帽T
            @SerializedName("product_attributes")
            val productAttributes: List<ProductAttribute>
        ) {
            data class ProductAttribute(
                @SerializedName("alias")
                val alias: String, // color
                @SerializedName("attribute")
                val attribute: String, // {"湖水綠","極致黑","經典白","太空灰"}
                @SerializedName("name")
                val name: String // 顏色
            )
        }
    }

    data class MatchGroup(
        @SerializedName("content")
        val content: String, // <p>雙人均需30歲(不含)以下，謝絕【體保生】、【甲組】報名</p>
        @SerializedName("created_at")
        val createdAt: String, // 2023-04-16 11:54:02
        @SerializedName("id")
        val id: Int, // 1
        @SerializedName("limit")
        val limit: Int, // 16
        @SerializedName("match_id")
        val matchId: Int, // 6
        @SerializedName("name")
        val name: String, // 社會組男雙
        @SerializedName("number")
        val number: Int, // 2
        @SerializedName("price")
        val price: Int, // 1400
        @SerializedName("product_price")
        val productPrice: ProductPrice,
        @SerializedName("product_price_id")
        val productPriceId: Int, // 35
        @SerializedName("sort_order")
        val sortOrder: Int, // 1681617242
        @SerializedName("token")
        val token: String, // EewXZ1TV8vbEX2k2LzWhOBLotiWt3VL
        @SerializedName("updated_at")
        val updatedAt: String // 2023-05-20 13:49:53
    ) {
        data class ProductPrice(
            @SerializedName("id")
            val id: Int, // 35
            @SerializedName("price_desc")
            val priceDesc: String, // 社會組男雙
            @SerializedName("price_dummy")
            val priceDummy: Any?, // null
            @SerializedName("price_member")
            val priceMember: Int, // 1400
            @SerializedName("price_nonmember")
            val priceNonmember: Any?, // null
            @SerializedName("price_title")
            val priceTitle: Any?, // null
            @SerializedName("price_title_alias")
            val priceTitleAlias: Any?, // null
            @SerializedName("product")
            val product: Product,
            @SerializedName("product_id")
            val productId: Int, // 37
            @SerializedName("shipping_fee")
            val shippingFee: Any?, // null
            @SerializedName("shipping_fee_desc")
            val shippingFeeDesc: Any?, // null
            @SerializedName("shipping_fee_unit")
            val shippingFeeUnit: Any?, // null
            @SerializedName("tax")
            val tax: Any? // null
        ) {
            data class Product(
                @SerializedName("alias")
                val alias: Any?, // null
                @SerializedName("channel")
                val channel: String, // bm
                @SerializedName("coin")
                val coin: Any?, // null
                @SerializedName("color")
                val color: Any?, // null
                @SerializedName("content")
                val content: String, // <p>羽球密碼盃比賽</p>
                @SerializedName("created_at")
                val createdAt: String, // 2023-05-16 23:39:28
                @SerializedName("created_id")
                val createdId: Int, // 1
                @SerializedName("gateway")
                val gateway: String, // credit_card,store_cvs
                @SerializedName("id")
                val id: Int, // 37
                @SerializedName("invoice_name")
                val invoiceName: String, // 羽球比賽
                @SerializedName("name")
                val name: String, // 羽球密碼盃比賽
                @SerializedName("order_max")
                val orderMax: Int, // 1
                @SerializedName("order_min")
                val orderMin: Int, // 1
                @SerializedName("pv")
                val pv: Int, // 4
                @SerializedName("shipping")
                val shipping: String, // direct
                @SerializedName("size")
                val size: Any?, // null
                @SerializedName("slug")
                val slug: String, // 羽球密碼盃比賽
                @SerializedName("sort_order")
                val sortOrder: Int, // 1684251568
                @SerializedName("status")
                val status: String, // online
                @SerializedName("token")
                val token: String, // FhAxb6qWYnKuUAQoTp0GwEETr57IJLB
                @SerializedName("type")
                val type: String, // match
                @SerializedName("unit")
                val unit: Any?, // null
                @SerializedName("updated_at")
                val updatedAt: String, // 2023-05-21 00:13:57
                @SerializedName("weight")
                val weight: Any? // null
            )
        }
    }
}