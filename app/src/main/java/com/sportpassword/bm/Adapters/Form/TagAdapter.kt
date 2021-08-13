package com.sportpassword.bm.Adapters.Form

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TableLayout
import android.widget.TableRow
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.sportpassword.bm.Controllers.BaseActivity
import com.sportpassword.bm.Form.BaseForm
import com.sportpassword.bm.Form.FormItem.FormItem
import com.sportpassword.bm.Form.FormItem.TagFormItem
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.IndexPath
import com.sportpassword.bm.Utilities.quotientAndRemainder
import com.sportpassword.bm.Views.Tag
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.formitem_tag.*
import kotlinx.android.synthetic.main.tag.view.*
import org.jetbrains.anko.Orientation
import org.jetbrains.anko.backgroundColor

class TagAdapter1(sectionKey: String, rowKey: String, title: String, value: String, show: String, delegate: BaseActivity?=null): FormItemAdapter1(sectionKey, rowKey, title, value, show, delegate) {

    val labelWidth: Int = 50
    val labelHeight: Int = 30
    val horizonMergin: Int = 30
    val vericalMergin: Int = 16
    val column: Int = 3
    var row: Int = 0

    var tagLabels: ArrayList<Tag> = arrayListOf()

    override fun getLayout() = R.layout.formitem_tag

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        super.bind(viewHolder, position)
        val context: Context = viewHolder.title.context

        val attributes: Array<String> = show.split(",").toTypedArray()
        var count: Int = attributes.size

        val (q, r) = count.quotientAndRemainder(column)
        row = if (r > 0) { q + 1 } else { q }
        viewHolder.tag_container.removeAllViews()

        var tableRow: LinearLayout? = null
        for ((idx, attribute) in attributes.withIndex()) {
            var (_, columnCount) = idx.quotientAndRemainder(column)
            columnCount++

            //如果換下一行，則new 一個新的row
            if (columnCount == 1) {
                tableRow = LinearLayout(context)
                tableRow.orientation = LinearLayout.HORIZONTAL
                val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 180)
                tableRow.layoutParams = lp
                viewHolder.tag_container.addView(tableRow)
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

            tag.setOnClickListener {
                handleTap(it)
            }
            tagLabels.add(tag)

            if (attribute == value) {
                tag.isChecked = true
                tag.setSelectedStyle()
            }
        }
    }

    fun handleTap(view: View) {

        val tag: Tag = view as Tag
        val idx: Int = tag.tag as Int
        tag.isChecked = !tag.isChecked
        tag.setSelectedStyle()
        clearOtherTagSelected(tag)

        baseActivityDelegate?.setTag(sectionKey, rowKey, tag.key, tag.isChecked)
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

class TagAdapter(formItem: FormItem): FormItemAdapter(formItem) {

    val labelHeight: Int = 30
    val horizonMergin: Int = 8
    val vericalMergin: Int = 8
    val column: Int = 3
    var row: Int = 0

    var tagLabels: ArrayList<Tag> = arrayListOf()
    var tagDicts: ArrayList<HashMap<String, String>> = arrayListOf()

    override fun getLayout(): Int {
        return R.layout.formitem_tag
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.title.text = formItem.title
        val context: Context = viewHolder.title.context

        if (formItem.isRequired) {
            viewHolder.required.visibility = View.VISIBLE
        } else {
            viewHolder.required.visibility = View.INVISIBLE
        }

        var count = tagDicts.size
        val _formItem: TagFormItem = formItem as TagFormItem
        if (count == 0) {
            tagDicts = _formItem.tags
            count = tagDicts.size
        }
        val (q, r) = count.quotientAndRemainder(column)
        if (r > 0) { row = q + 1 } else  { row = q }

        viewHolder.tag_container.removeAllViews()
        var tableRow: LinearLayout? = null
        count = 0
        for (tagDict in tagDicts) {
            var (rowCount, columnCount) = count.quotientAndRemainder(column)
            rowCount++
            columnCount++
            if (columnCount == 1) {
                tableRow = LinearLayout(context)
                tableRow.orientation = LinearLayout.HORIZONTAL
                val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 180)
                tableRow.layoutParams = lp
//                tableRow.backgroundColor = ContextCompat.getColor(context, R.color.FBBLUE)
                viewHolder.tag_container.addView(tableRow)
            }

            val lp_tag = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            lp_tag.gravity = Gravity.CENTER
            lp_tag.weight = 1F

            for ((key, value) in tagDict) {
                val tag: Tag = Tag(context)
                tag.layoutParams = lp_tag
                tableRow!!.addView(tag)
                tag.key = key
                tag.value = value
                tag.tag = count
                tag.tag_view.text = value
                tag.setOnClickListener {
                    handleTap(it)
                }

                for (idx in _formItem.selected_idxs) {
                    if (count == idx) {
                        tag.isChecked = true
                        tag.setSelectedStyle()
                    }
                }
                tagLabels.add(tag)
                break
            }
            count = count + 1
        }
    }

    fun handleTap(view: View) {
        val tag: Tag = view as Tag
        //println(tag.tag)
        val _formItem: TagFormItem = formItem as TagFormItem
        val idx: Int = tag.tag as Int
        _formItem.selected_idxs = arrayListOf(idx)
        _formItem.value = tag.key
        _formItem.show = tag.value

        tag.isChecked = !tag.isChecked
        tag.setSelectedStyle()
        clearOtherTagSelected(tag)
        if (valueChangedDelegate != null) {
            valueChangedDelegate!!.tagChecked(tag.isChecked, this.formItem.name!!, tag.key, tag.value)
        }

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




















