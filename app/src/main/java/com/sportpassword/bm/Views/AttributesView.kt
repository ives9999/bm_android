package com.sportpassword.bm.Views

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.sportpassword.bm.R
import com.sportpassword.bm.bm_new.data.dto.match.MatchSignUpDto
import com.sportpassword.bm.extensions.quotientAndRemainder
import org.jetbrains.anko.backgroundColor

class AttributesView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    LinearLayout(context, attrs, defStyleAttr) {

    val view: View = View.inflate(context, R.layout.attributes_view, this)

    //name: 屬性的中文名稱
    //alias: 屬性的別名，一般就是英文名稱
    //attribute: 是從資料庫撈出來的屬性值，如：{\"XS\",\"S\",\"M\",\"L\",\"XL\",\"2XL\",\"3XL\"}
    //selected: 已經選擇的屬性
    //clumn: 每列幾個欄，預設是3欄，列則是用欄跟屬性總數來計算出來的
    var alias: String = ""
    var name: String = ""
    var selected: String = ""

    var count: Int = 0
    var column: Int = 3
    var row: Int = 1

    var labelWidth: Int = 80
    var labelHeight: Int = 30
    var horizonMergin: Int = 30
    var verticalMergin: Int = 16

    private var listener: Listener? = null

    //var parent: UIView = UIView()
    var attributes: ArrayList<String> = arrayListOf()

    var tagLabels: ArrayList<Tag> = arrayListOf()

    init {
        orientation = VERTICAL
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.AttributesView, 0, 0)

            if (typedArray.hasValue(R.styleable.AttributesView_attributesViewName)) {

                typedArray.getString(R.styleable.AttributesView_attributesViewName)?.let { it1 ->
                    this.name = it1
                }

                typedArray.getString(R.styleable.AttributesView_attributesViewAlias)?.let { it1 ->
                    this.alias = it1
                }

                typedArray.getInt(R.styleable.AttributesView_attributesViewColumn, 3).let { it1 ->
                    this.column = it1
                }

                typedArray.getInt(R.styleable.AttributesView_attributesViewLabelWidth, 80)
                    .let { it1 ->
                        this.labelWidth = it1
                    }

                typedArray.getInt(R.styleable.AttributesView_attributesViewLabelHeight, 30)
                    .let { it1 ->
                        this.labelHeight = it1
                    }

                typedArray.getInt(R.styleable.AttributesView_attributesViewLabelHorizonMergin, 30)
                    .let { it1 ->
                        this.horizonMergin = it1
                    }

                typedArray.getInt(R.styleable.AttributesView_attributesViewLabelVerticalMergin, 16)
                    .let { it1 ->
                        this.verticalMergin = it1
                    }
            }
        }

//        this.setOnClickListener {
//            delegate?.iconPressed(iconStr)
//        }
    }

    fun setAttributes(
        gift: MatchSignUpDto.MatchGift.Product.ProductAttribute,
        selected: String = "",
        listener: Listener
    ) {
        this.selected = selected
        this.listener = listener
        name = gift.name
        alias = gift.alias
        val attributeList: List<String> = parseAttributes(gift.attribute)
        val count: Int = attributeList.size

        val (q, r) = count.quotientAndRemainder(column)
        row = if (r > 0) {
            q + 1
        } else {
            q
        }

        this.removeAllViews()

        var tableRow: LinearLayout? = null
        for (idx in attributeList.indices) {

            var (_, columnCount) = idx.quotientAndRemainder(column)
            columnCount++

            //如果換下一行，則new 一個新的row
            if (columnCount == 1) {
                tableRow = LinearLayout(context)
                tableRow.orientation = LinearLayout.HORIZONTAL
                val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 180)
                tableRow.layoutParams = lp
                this.addView(tableRow)

                tableRow.backgroundColor = R.color.MY_PURPLE
            }

            val tag: Tag = Tag(context)

            val lp_tag = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            lp_tag.gravity = Gravity.CENTER
            lp_tag.weight = 1F

            tag.layoutParams = lp_tag
            tableRow!!.addView(tag)

            val attributeString: String = attributeList[idx]
            tag.tag = idx
            tag.key = attributeString
            tag.value = attributeString

            tag.findViewById<TextView>(R.id.tag_view)?.let {
                it.text = attributeString
            }

            tagLabels.add(tag)

            tag.setOnClick()

            if (attributeString == selected) {
                tag.isChecked = true
                tag.setSelectedStyle()
            }
        }
    }

    private fun Tag.setOnClick() {
        setOnClickListener {
            if (selected == value) {
                selected = ""
                isChecked = false
            } else {
                selected = value
                isChecked = true
            }

            listener?.onTagClick(
                alias,
                if (isChecked) "{name:$name,alias:$alias,value:$selected}" else "",
            )

            tagLabels.forEach {
                if (selected != it.value) {
                    it.isChecked = false
                }
                it.setSelectedStyle()
            }
        }
    }

    //將"{\"XS\",\"S\",\"M\",\"L\",\"XL\",\"2XL\",\"3XL\"}"解析成["XS","S","M","L","XL","2XL","3XL"]
    private fun parseAttributes(attribute: String): List<String> {

        var res: List<String> = arrayListOf()
        var tmp = attribute.replace("{", "")
        tmp = tmp.replace("}", "")
        tmp = tmp.replace("\"", "")
        res = tmp.split(",")

        return res
    }

    interface Listener {
        fun onTagClick(alias: String, giftData: String)
    }
}
















