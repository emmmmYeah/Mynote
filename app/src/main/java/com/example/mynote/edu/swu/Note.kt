package com.example.mynote.edu.swu

data class Note(
    val outline:String,
    val content: String,
    val createTime: String,
    val tag:Int,
    val isColl: Int
){
    companion object{
        val TABLE="Note"
        val COL_ID="id"
        val COL_OUTLINE="outline"
        val COL_CONTENT="content"
        val COL_TIME="createTime"
        val COL_TAG="tag"
        val COL_coll="isColl"

    }
    var id:Int?=null

}
