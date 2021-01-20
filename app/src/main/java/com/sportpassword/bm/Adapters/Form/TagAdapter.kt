package com.sportpassword.bm.Adapters.Form

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TableRow
import com.sportpassword.bm.Form.BaseForm
import com.sportpassword.bm.Form.FormItem.TagFormItem
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.IndexPath
import com.sportpassword.bm.Views.Tag
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.formitem_sex.*

class TagAdapter(form: BaseForm, idx: Int, indexPath: IndexPath, clearClick:(idx: Int)->Unit, promptClick:(idx: Int)->Unit): FormItemAdapter(form, idx, indexPath, clearClick, promptClick) {

    val labelHeight: Int = 30
    val horizonMergin: Int = 8
    val vericalMergin: Int = 8
    val column: Int = 3
    var row: Int = 0

    //var tagLabels: [Tag] = [Tag]()
    var tagDicts: ArrayList<HashMap<String, String>> = arrayListOf()

    override fun getLayout(): Int {
        return R.layout.formitem_tag
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        val formItem = form.formItems[position]
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
        }
        val q: Int = count / column
        val r: Int = count % column
        if (r > 0) { row = q + 1 } else  { row = q }

        count = 0
        for (tagDict in tagDicts) {
            for ((key, value) in tagDict) {
                val tag: Tag = Tag(context)
                val tableRow: TableRow = TableRow(context)
                tableRow.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                tableRow.addView(tag)
                viewHolder.containerView.addView(tableRow)
                tag.tag = count
                //tag.key = key
                //tag.value = value
                //tag.text = value

//                for (idx in _formItem.selected_idxs) {
//                    if (count == idx) {
//                        tag.selected = true
//                        tag.setSelectedStyle()
//                    }
//                }
    //            val gestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(handleTap(sender:)))
    //            tag.addGestureRecognizer(gestureRecognizer)
    //            tagLabels.append(tag)

                //tag.backgroundColor = UIColor.red
                //res = count.quotientAndRemainder(dividingBy: column)
                //print(res)
                //setMargin(block: tag, row_count: res.quotient + 1, column_count: res.remainder + 1)
                count = count + 1
                break
            }
        }


//        viewHolder.sex.setOnCheckedChangeListener{ _, i ->
//            if (valueChangedDelegate != null) {
//                val radio = viewHolder.sex.findViewById<RadioButton>(i)
//                valueChangedDelegate!!.sexChanged(radio.tag.toString())
//            }
//        }
    }
}