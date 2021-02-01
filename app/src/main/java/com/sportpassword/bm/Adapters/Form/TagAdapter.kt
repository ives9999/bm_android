package com.sportpassword.bm.Adapters.Form

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TableLayout
import android.widget.TableRow
import com.sportpassword.bm.Form.BaseForm
import com.sportpassword.bm.Form.FormItem.FormItem
import com.sportpassword.bm.Form.FormItem.TagFormItem
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.IndexPath
import com.sportpassword.bm.Utilities.quotientAndRemainder
import com.sportpassword.bm.Views.Tag
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.formitem_tag.*
import kotlinx.android.synthetic.main.tag.view.*

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

    override fun bind(viewHolder: ViewHolder, position: Int) {
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
        var tableRow: TableRow? = null
        count = 0
        for (tagDict in tagDicts) {
            var (rowCount, columnCount) = count.quotientAndRemainder(column)
            rowCount++
            columnCount++
            if (columnCount == 1) {
                tableRow = TableRow(context)
                tableRow.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                viewHolder.tag_container.addView(tableRow)
            }

            for ((key, value) in tagDict) {
                val tag: Tag = Tag(context)
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




















