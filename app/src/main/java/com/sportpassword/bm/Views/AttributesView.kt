package com.sportpassword.bm.Views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.sportpassword.bm.R
import com.sportpassword.bm.extensions.quotientAndRemainder

class AttributesView@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0):
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

    //var parent: UIView = UIView()
    var attributes: ArrayList<String> = arrayListOf()

    var tagLabels: ArrayList<Tag> = arrayListOf()

    init {
        attrs ?. let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.AttributesView, 0, 0)

            if (typedArray.hasValue(R.styleable.AttributesView_attributesViewName)) {

                typedArray.getString(R.styleable.AttributesView_attributesViewName) ?. let { it1 ->
                    this.name = it1
                }

                typedArray.getString(R.styleable.AttributesView_attributesViewAlias) ?. let { it1 ->
                    this.alias = it1
                }

                typedArray.getInt(R.styleable.AttributesView_attributesViewColumn, 3).let { it1 ->
                    this.column = it1
                }

                typedArray.getInt(R.styleable.AttributesView_attributesViewLabelWidth, 80).let { it1 ->
                    this.labelWidth = it1
                }

                typedArray.getInt(R.styleable.AttributesView_attributesViewLabelHeight, 30)
                    .let { it1 ->
                        this.labelHeight = it1
                    }

                typedArray.getInt(R.styleable.AttributesView_attributesViewLabelHorizonMergin, 30) . let { it1 ->
                    this.horizonMergin = it1
                }

                typedArray.getInt(R.styleable.AttributesView_attributesViewLabelVerticalMergin, 16) . let { it1 ->
                    this.verticalMergin = it1
                }
            }
        }

//        this.setOnClickListener {
//            delegate?.iconPressed(iconStr)
//        }
    }

    fun setAttributes(attribute: String) {
        val attributeList: List<String> = parseAttributes(attribute)
        for (idx in attributeList.indices) {
            val attributeString: String = attributeList[idx]
            val tag: Tag = Tag(context)
            this.addView(tag)

            val (quotient, remainder) = idx.quotientAndRemainder(column)
            //商是第幾列row，餘數是第幾排column
            val leftPadding: Int = remainder * (labelWidth + horizonMergin)
            val topPadding: Int = quotient * (labelHeight + verticalMergin)
        }
    }

    //將"{\"XS\",\"S\",\"M\",\"L\",\"XL\",\"2XL\",\"3XL\"}"解析成["XS","S","M","L","XL","2XL","3XL"]
    private fun parseAttributes(attribute: String): List<String> {

        var res: List<String> = arrayListOf()
        var tmp = attribute.replace("{", "")
        tmp = tmp.replace("}", "")
        tmp = tmp.replace( "\"", "")
        res = tmp.split(",")

        return res
    }
}
















