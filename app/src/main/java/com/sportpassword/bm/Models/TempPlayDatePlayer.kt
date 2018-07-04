package com.sportpassword.bm.Models

class TempPlayDatePlayer(val success:Boolean,val rows:ArrayList<Row_TempPlayDatePlayer>) {

}
class Row_TempPlayDatePlayer(val updated_at:String,val play_date:String,val token:String,val id:Int,val mobile:String,val name:String,val created_at:String,val team_id:Int,val member_id:Int){}
