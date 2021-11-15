package com.sportpassword.bm.Adapters

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.zxing.BarcodeFormat
import com.google.zxing.oned.Code128Writer
import com.sportpassword.bm.Controllers.List1CellDelegate
import com.sportpassword.bm.Data.OneRow
import com.sportpassword.bm.Data.OneSection
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.CELL_TYPE
import com.sportpassword.bm.Utilities.quotientAndRemainder
import com.sportpassword.bm.Views.Tag
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.cart_list_cell.view.*
import kotlinx.android.synthetic.main.formitem_more.view.*
import kotlinx.android.synthetic.main.formitem_number.view.*
import kotlinx.android.synthetic.main.formitem_number.view.title
import kotlinx.android.synthetic.main.formitem_radio.view.*
import kotlinx.android.synthetic.main.formitem_tag.view.*
import kotlinx.android.synthetic.main.tag.view.*
import org.jetbrains.anko.backgroundColor
import java.lang.IllegalArgumentException

class OneSectionAdapter(val context: Context, private val resource: Int, var delegate: List1CellDelegate, val others: HashMap<String, String>): RecyclerView.Adapter<OneSectionViewHolder>() {
    private var oneSections: ArrayList<OneSection> = arrayListOf()
    //lateinit var adapter: TeamSearchItemAdapter

    fun setOneSection(oneSections: ArrayList<OneSection>) {
        this.oneSections = oneSections
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OneSectionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = inflater.inflate(resource, parent, false)

        return OneSectionViewHolder(viewHolder)

    }

    override fun onBindViewHolder(holder: OneSectionViewHolder, position: Int) {

        val section: OneSection = oneSections[position]
        holder.titleLbl.text = section.title

        val adapter =
            OneItemAdapter(context, position, oneSections[position], delegate, others)
//            holder.recyclerView.setHasFixedSize(true)
        holder.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        holder.recyclerView.adapter = adapter

        var iconID: Int = 0
        if (section.isExpanded) {
            iconID = context.resources.getIdentifier("to_down", "drawable", context.packageName)
        } else {
            iconID = context.resources.getIdentifier("to_right", "drawable", context.packageName)
        }
        holder.greater.setImageResource(iconID)
        holder.greater.setOnClickListener {
            delegate.handleOneSectionExpanded(position)
        }
    }

    override fun getItemCount(): Int {
        return oneSections.size
    }
}

class OneSectionViewHolder(val viewHolder: View): RecyclerView.ViewHolder(viewHolder) {

    var titleLbl: TextView = viewHolder.findViewById(R.id.titleLbl)
    var greater: ImageView = viewHolder.findViewById(R.id.greater)
    var recyclerView: RecyclerView = viewHolder.findViewById(R.id.recyclerView)
}

class OneItemAdapter(val context: Context, private val sectionIdx: Int, private val oneSection: OneSection, var delegate: List1CellDelegate, val others: HashMap<String, String>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var oneRows: ArrayList<OneRow> = oneSection.items
    var rowIdx: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        //var viewHolder = inflater.inflate(R.layout.formitem_plain, parent, false)
        when (viewType) {
            CELL_TYPE.PLAIN.toInt() -> {
                return PlainViewHolder(inflater.inflate(R.layout.formitem_plain, parent, false))
            }
            CELL_TYPE.TEXTFIELD.toInt() -> {
                return TextFieldViewHolder(inflater.inflate(R.layout.formitem_textfield, parent, false))
            }
            CELL_TYPE.TAG.toInt() -> {
                return TagViewHolder(inflater.inflate(R.layout.formitem_tag, parent, false))
            }
            CELL_TYPE.NUMBER.toInt() -> {
                return NumberViewHolder(inflater.inflate(R.layout.formitem_number, parent, false))
            }
            CELL_TYPE.CART.toInt() -> {
                return CartViewHolder(inflater.inflate(R.layout.cart_list_cell, parent, false))
            }
            CELL_TYPE.RADIO.toInt() -> {
                return RadioViewHolder(inflater.inflate(R.layout.formitem_radio, parent, false))
            }
            CELL_TYPE.MORE.toInt() -> {
                return MoreViewHolder(inflater.inflate(R.layout.formitem_more, parent, false))
            }
            CELL_TYPE.BARCODE.toInt() -> {
                return BarcodeViewHolder(inflater.inflate(R.layout.formitem_barcode, parent, false))
            }
            CELL_TYPE.SEX.toInt() -> {
                return SexViewHolder(inflater.inflate(R.layout.formitem_sex, parent, false))
            }
            CELL_TYPE.PASSWORD.toInt() -> {
                return PasswordViewHolder(inflater.inflate(R.layout.formitem_password, parent, false))
            }
            CELL_TYPE.PRIVACY.toInt() -> {
                return PrivacyViewHolder(inflater.inflate(R.layout.formitem_privacy, parent, false))
            }
            else -> {
                return PlainViewHolder(inflater.inflate(R.layout.formitem_plain, parent, false))
            }
        }
    }

    override fun getItemViewType(position: Int): Int {

        val row: OneRow = oneRows[position]
        return when (row.cell) {
            "text" -> CELL_TYPE.PLAIN.toInt()
            "textField" -> CELL_TYPE.TEXTFIELD.toInt()
            "tag" -> CELL_TYPE.TAG.toInt()
            "number" -> CELL_TYPE.NUMBER.toInt()
            "cart" -> CELL_TYPE.CART.toInt()
            "radio" -> CELL_TYPE.RADIO.toInt()
            "more" -> CELL_TYPE.MORE.toInt()
            "barcode" -> CELL_TYPE.BARCODE.toInt()
            "sex" -> CELL_TYPE.SEX.toInt()
            "password" -> CELL_TYPE.PASSWORD.toInt()
            "privacy" -> CELL_TYPE.PRIVACY.toInt()
            else -> throw IllegalArgumentException("錯誤的格式" + position)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        this.rowIdx = position
        val row: OneRow = oneRows[position]

        if (holder is PlainViewHolder) {
            holder.title.text = row.title
            holder.show.text = row.show
            holder.show.backgroundColor = Color.TRANSPARENT
        } else if (holder is TextFieldViewHolder) {
            holder.title.text = row.title
            holder.prompt.visibility = View.INVISIBLE
            holder.value.setText(row.value)
            holder.value.hint = row.placeholder
            holder.value.inputType = row.keyboard.toSwift()

            holder.required.visibility = if (row.isRequired) { View.VISIBLE } else { View.INVISIBLE }

            holder.value.addTextChangedListener(object: TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    delegate.cellTextChanged(sectionIdx, position, p0.toString())
                }
            })

            holder.clear.setOnClickListener {
                delegate.cellClear(sectionIdx, position)
            }
        } else if (holder is TagViewHolder) {
            holder.title.text = row.title

            val tags: ArrayList<Tag> = holder.generateTag(context, row.value, row.show)
            for (tag in tags) {
                tag.setOnClickListener {
                    holder.handleTap(tag)
                    delegate.cellSetTag(this.sectionIdx, this.rowIdx, tag.value, tag.isChecked)
                }
            }
        } else if (holder is NumberViewHolder) {
            holder.title.text = row.title

            holder.init(row.value.toInt(), row.show)
            holder.plus.setOnClickListener {
                val number = holder.plusClick()
                //delegate with number
                delegate.cellNumberChanged(sectionIdx, position, number)
            }
            holder.minus.setOnClickListener {
                val number = holder.minusClick()
                delegate.cellNumberChanged(sectionIdx, position, number)
            }
        } else if (holder is CartViewHolder) {
            holder.title.text = row.title
            if (row.feature_path.isNotEmpty()) {
                Picasso.with(context)
                    .load(row.feature_path)
                    .placeholder(R.drawable.loading_square_120)
                    .error(R.drawable.loading_square_120)
                    .into(holder.featured)
            }
            holder.attribute.text = row.attribute
            holder.amount.text = row.amount
            holder.quantity.text = "數量：${row.quantity}"

            if (others.containsKey("product_icon_view")) {
                val b: Boolean = others["product_icon_view"].toBoolean()
                if (b) {
                    holder.iconView.visibility = View.VISIBLE
                } else {
                    holder.iconView.visibility = View.GONE
                }
            }
            if (holder.iconView.visibility == View.VISIBLE) {
                holder.editIcon.setOnClickListener {
                    delegate.cellEdit(sectionIdx, rowIdx)
                }

                holder.deleteIcon.setOnClickListener {
                    delegate.cellDelete(sectionIdx, rowIdx)
                }

                holder.refreshIcon.setOnClickListener {
                    delegate.cellRefresh()
                }
            }
        } else if (holder is RadioViewHolder) {

            val group = holder.init(context, row)
            group.setOnCheckedChangeListener { radioGroup, i ->
                delegate.cellRadioChanged(row.key, sectionIdx, position, i)
            }
        } else if (holder is MoreViewHolder) {
            holder.title.text = row.title
            holder.show.text = row.show
            holder.show.visibility = View.VISIBLE
            holder.prompt.visibility = View.INVISIBLE

            holder.required.visibility = if (row.isRequired) { View.VISIBLE } else { View.INVISIBLE }

            holder.viewHolder.setOnClickListener {
                delegate.cellMoreClick(sectionIdx, position)
            }

            if (row.isClear) {
                holder.clear.visibility = View.VISIBLE
                holder.clear.setOnClickListener {
                    delegate.cellClear(sectionIdx, position)
                }
            } else {
                holder.clear.visibility = View.GONE
            }
        } else if (holder is BarcodeViewHolder) {

            holder.title.text = row.title
            holder.barcode.setImageBitmap(
                holder.createBarcodeBitmap(
                    value = row.value,
                    barcodeColor = Color.BLACK,
                    backgroundColor = Color.WHITE,
                    width = 500,
                    height = 150
                )
            )
        } else if (holder is SexViewHolder) {
            holder.title.text = row.title
            holder.required.visibility = if (row.isRequired) { View.VISIBLE } else { View.INVISIBLE }
            holder.sex.setOnCheckedChangeListener { _, i ->
                val radio = holder.sex.findViewById<RadioButton>(i)
                val sex = radio.tag.toString()
                if (delegate != null) {
                    delegate.cellSexChanged(row.key, sectionIdx, position, sex)
                }
            }
        } else if (holder is PasswordViewHolder) {
            holder.title.text = row.title
            holder.prompt.visibility = View.INVISIBLE
            holder.value.setText(row.value)
            holder.value.hint = row.placeholder

            holder.required.visibility = if (row.isRequired) { View.VISIBLE } else { View.INVISIBLE }

            holder.value.addTextChangedListener(object: TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    delegate.cellTextChanged(sectionIdx, position, p0.toString())
                }
            })

            holder.clear.setOnClickListener {
                delegate.cellClear(sectionIdx, position)
            }
        } else if (holder is PrivacyViewHolder) {
            holder.checkBox.setText(row.show)
            holder.required.visibility = if (row.isRequired) { View.VISIBLE } else { View.INVISIBLE }

            holder.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                delegate.cellPrivacyChanged(sectionIdx, position, isChecked)
            }
        }
    }

    override fun getItemCount(): Int {
        if (oneSection.isExpanded) {
            return oneRows.size
        } else {
            return 0
        }
    }
}

//abstract class FormViewHolder(viewHolder: View): RecyclerView.ViewHolder(viewHolder) {
//    abstract fun bind(row: OneRow)
//}

class PlainViewHolder(val viewHolder: View): RecyclerView.ViewHolder(viewHolder) {

    val title: TextView = viewHolder.title
    val show: TextView = viewHolder.detail
}

class TextFieldViewHolder(val viewHolder: View): RecyclerView.ViewHolder(viewHolder) {

    val title: TextView = viewHolder.findViewById(R.id.title)
    val prompt: ImageView = viewHolder.findViewById(R.id.promptBtn)
    val value: EditText = viewHolder.findViewById(R.id.textField)

    val required: ImageView = viewHolder.findViewById(R.id.required)
    val clear: ImageView = viewHolder.findViewById(R.id.clear)
}

class TagViewHolder(val viewHolder: View): RecyclerView.ViewHolder(viewHolder) {

    val title: TextView = viewHolder.title
    val tag_container: TableLayout = viewHolder.tag_container
    val tagLabels: ArrayList<Tag> = arrayListOf()

    fun generateTag(context: Context, value: String, show: String): ArrayList<Tag> {
        val columnNum: Int = 3
        var rowNum: Int = 0

        val attributes: Array<String> = show.split(",").toTypedArray()
        val count: Int = attributes.size

        val (q, r) = count.quotientAndRemainder(columnNum)
        rowNum = if (r > 0) { q + 1 } else { q }
        tag_container.removeAllViews()

        var tableRow: LinearLayout? = null
        for ((idx, attribute) in attributes.withIndex()) {
            var (_, columnCount) = idx.quotientAndRemainder(columnNum)
            columnCount++

            //如果換下一行，則new 一個新的row
            if (columnCount == 1) {
                tableRow = LinearLayout(context)
                tableRow.orientation = LinearLayout.HORIZONTAL
                val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 180)
                tableRow.layoutParams = lp
                tag_container.addView(tableRow)
            }
            val lp_tag = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            lp_tag.gravity = Gravity.CENTER
            lp_tag.weight = 1F

            val tag: Tag = Tag(context)
            tag.layoutParams = lp_tag
            tableRow!!.addView(tag)
            tag.tag = idx
            tag.key = attribute
            tag.value = attribute
            tag.tag_view.text = attribute

            tagLabels.add(tag)

            if (attribute == value) {
                tag.isChecked = true
                tag.setSelectedStyle()
            }
        }

        return tagLabels
    }

    fun handleTap(view: View) {

        val tag: Tag = view as Tag
        val rowIdx: Int = tag.tag as Int
        tag.isChecked = !tag.isChecked
        tag.setSelectedStyle()
        clearOtherTagSelected(tag)
    }

    fun clearOtherTagSelected(selectedTag: Tag) {
        if (selectedTag.isChecked) {
            for (tagLabel in tagLabels) {
                if (tagLabel != selectedTag) {
                    tagLabel.isChecked = false
                    tagLabel.unSelectedStyle()
                }
            }
        }
    }
}

class NumberViewHolder(val viewHolder: View): RecyclerView.ViewHolder(viewHolder) {

    val title: TextView = viewHolder.title
    val minus: Button = viewHolder.minus
    val plus: Button = viewHolder.plus
    var numberLbl: TextView = viewHolder.numberLbl

    var value: Int = 1
    var show: String = ""
    var min: Int = 1
    var max: Int = 1
    var number: Int = value

    fun init(value: Int, show: String) {
        this.value = value
        this.show = show
        this.number = value

        val tmp1: Array<String> = show.split(",").toTypedArray()
        if (tmp1.size == 2) {
            min = tmp1[0].toInt()
            max = tmp1[1].toInt()
        }
        numberLbl.text = value.toString()
    }

    fun plusClick (): Int {

        if (!minus.isEnabled) {
            minus.isEnabled = true
        }
        number += 1
        if (number > max) {
            number = max
            plus.isEnabled = false
        }
        numberLbl.text = number.toString()

        return number
    }

    fun minusClick (): Int {

        if (!plus.isEnabled) {
            plus.isEnabled = true
        }
        number -= 1
        if (number < min) {
            number = min
            minus.isEnabled = false
        }
        numberLbl.text = number.toString()

        return number
    }
}

class CartViewHolder(val viewHolder: View): RecyclerView.ViewHolder(viewHolder) {

    val title: TextView = viewHolder.titleLbl
    val featured: ImageView = viewHolder.listFeatured
    val attribute: TextView = viewHolder.attributeLbl
    val amount: TextView = viewHolder.amountLbl
    val quantity: TextView = viewHolder.quantityLbl

    val iconView: RelativeLayout = viewHolder.findViewById(R.id.iconView)
    val editIcon: ImageView = viewHolder.findViewById(R.id.editIcon)
    val deleteIcon: ImageView = viewHolder.findViewById(R.id.deleteIcon)
    val refreshIcon: ImageView = viewHolder.findViewById(R.id.refreshIcon)
}

class RadioViewHolder(val viewHolder: View): RecyclerView.ViewHolder(viewHolder) {

    fun init(context: Context, row: OneRow): RadioGroup {

        val textColor: Int = ContextCompat.getColor(context, R.color.MY_WHITE)
        val checkedColor: Int = ContextCompat.getColor(context, R.color.MY_RED)

        val colorStateList: ColorStateList = ColorStateList(
            arrayOf(
                intArrayOf(-android.R.attr.state_enabled), //disabled
                intArrayOf(android.R.attr.state_enabled)   //enabled
            ), intArrayOf(
                textColor, //disabled
                textColor  //enabled
            )
        )

        viewHolder.radioContainer.removeAllViews()

        val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        lp.setMargins(18, 24, 0, 24)

        val group = RadioGroup(context)

        val radioButtons: ArrayList<RadioButton> = arrayListOf()

        val arr: Array<String> = row.show.split(",").toTypedArray()
        val titles: Array<String> = row.title.split(",").toTypedArray()
        for ((idx, value) in arr.withIndex()) {
            val title: String = titles[idx]
            val radioButton: RadioButton = RadioButton(context)
            radioButton.id = idx
            radioButton.text = title
            radioButton.buttonTintList = colorStateList
            radioButton.setTextColor(textColor)
            radioButton.textSize = 18F
            radioButton.layoutParams = lp

            val isChecked: Boolean = row.value == value ?: run {
                false
            }

            radioButton.isChecked = isChecked
            group.addView(radioButton)

            radioButtons.add(radioButton)
        }

        viewHolder.radioContainer.addView(group)

        return group
    }
}

class MoreViewHolder(val viewHolder: View): RecyclerView.ViewHolder(viewHolder) {
    val title: TextView = viewHolder.title
    val show: TextView = viewHolder.detail
    val prompt: ImageView = viewHolder.promptBtn

    val required: ImageView = viewHolder.findViewById(R.id.required)
    val clear: ImageView = viewHolder.clear
}

class BarcodeViewHolder(val viewHolder: View): RecyclerView.ViewHolder(viewHolder) {
    val title: TextView = viewHolder.title
    val barcode: ImageView = viewHolder.findViewById(R.id.barcode)

    fun createBarcodeBitmap(value: String, @ColorInt barcodeColor: Int, @ColorInt backgroundColor: Int, width: Int, height: Int): Bitmap {

        val bitMatrix = Code128Writer().encode(
            value, BarcodeFormat.CODE_128,width,height
        )
        val pixels = IntArray(bitMatrix.width * bitMatrix.height)

        for (y in 0 until bitMatrix.height) {
            val offset = y * bitMatrix.width
            for (x in 0 until bitMatrix.width) {
                pixels[offset + x] = if (bitMatrix.get(x, y)) barcodeColor else backgroundColor
            }
        }

        val bitmap = Bitmap.createBitmap(
            bitMatrix.width,
            bitMatrix.height,
            Bitmap.Config.ARGB_8888
        )

        bitmap.setPixels(
            pixels,
            0,
            bitMatrix.width,
            0,
            0,
            bitMatrix.width,
            bitMatrix.height
        )

        return bitmap
    }
}

class SexViewHolder(val viewHolder: View): RecyclerView.ViewHolder(viewHolder) {
    val title: TextView = viewHolder.findViewById(R.id.title)
    val required: ImageView = viewHolder.findViewById(R.id.required)

    val sex: RadioGroup = viewHolder.findViewById(R.id.sex)
}

class PasswordViewHolder(val viewHolder: View): RecyclerView.ViewHolder(viewHolder) {
    val title: TextView = viewHolder.findViewById(R.id.title)
    val prompt: ImageView = viewHolder.findViewById(R.id.promptBtn)
    val value: EditText = viewHolder.findViewById(R.id.textField)

    val required: ImageView = viewHolder.findViewById(R.id.required)
    val clear: ImageView = viewHolder.findViewById(R.id.clear)
}

class PrivacyViewHolder(val viewHolder: View): RecyclerView.ViewHolder(viewHolder) {
    val title: TextView = viewHolder.findViewById(R.id.title)
    val checkBox: CheckBox = viewHolder.findViewById(R.id.privacyBox)

    val required: ImageView = viewHolder.findViewById(R.id.required)
}