package com.sportpassword.bm.Adapters

import com.sportpassword.bm.Controllers.List1CellDelegate
import com.sportpassword.bm.R

class MemberLevelUpAdapter(list1CellDelegate: List1CellDelegate?):
    MyAdapter<MemberCoinViewHolder>(R.layout.levelup_cell, ::MemberCoinViewHolder, list1CellDelegate) {


}
