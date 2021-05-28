package com.sportpassword.bm.Adapters

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.IndexPath
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.search_row_item.*
import org.jetbrains.anko.sdk27.coroutines.onCheckedChange

interface SearchItemDelegate {
    fun remove(indexPath: IndexPath)
}

class SearchItem(val title: String, val detail: String, val keyword: String, val switch: Boolean, val section: Int, val row: Int, val inputK:(k: String)->Unit, val switched:(idx: Int, b: Boolean)->Unit): Item() {

    var delegate: SearchItemDelegate? = null

    override fun getLayout() = R.layout.search_row_item

    override fun bind(viewHolder: com.xwray.groupie.kotlinandroidextensions.ViewHolder, position: Int) {

        viewHolder.row_title.text = title
        viewHolder.row_detail.text = detail
        val indexPath = IndexPath(section, row)

        if ((section == 0 && position == 1) || (section == -1 && position == 0)) {
            val keywordView = viewHolder.keywordTxt
            viewHolder.row_detail.visibility = View.INVISIBLE
            viewHolder.greater.visibility = View.INVISIBLE
            viewHolder.clearBtn.visibility = View.VISIBLE
            keywordView.visibility = View.VISIBLE
            if (keyword.length > 0) {
                keywordView.setText(keyword)
            }
//            keywordView.textChangedListener {
//                inputK(this, true)
//            }
            keywordView.addTextChangedListener(object: TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    inputK(p0.toString())
                }

            })
        } else {
            viewHolder.row_detail.visibility = View.VISIBLE
            viewHolder.greater.visibility = View.VISIBLE
            viewHolder.keywordTxt.visibility = View.INVISIBLE
            viewHolder.clearBtn.visibility = View.VISIBLE
        }

        val switchView = viewHolder.search_switch
        if (switch) {
            switchView.visibility = View.VISIBLE
            viewHolder.row_detail.visibility = View.INVISIBLE
            viewHolder.greater.visibility = View.INVISIBLE
            viewHolder.keywordTxt.visibility = View.INVISIBLE
            viewHolder.clearBtn.visibility = View.INVISIBLE
            switchView.setOnCheckedChangeListener { compoundButton, b ->
                switched(position, b)
            }
//            switchView.onCheckedChange{ view, b ->
//                switched(position, b)
//            }
        } else {
            switchView.visibility = View.INVISIBLE
        }

        viewHolder.clearBtn.setOnClickListener {
            if ((section == 0 && position == 1) || (section == -1 && position == 0)) {
                viewHolder.keywordTxt.setText("")
            } else {
                if (delegate != null) {
                    viewHolder.row_detail.text = "不限"
                    delegate!!.remove(indexPath)
                }
            }
        }

    }

}