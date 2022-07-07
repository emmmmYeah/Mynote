package com.example.mynote.edu.swu

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.nfc.Tag
import android.util.Log
import com.example.mynote.edu.swu.Note.Companion.TABLE

class UserDBHelper(val context: Context, name:String, version:Int)
    : SQLiteOpenHelper(context,name,null,version) {
    val TAG="@@UserDBHelper"
    //输入创建数据库表TABLE的命令，联合TODO数据集进行创建，剩下的就去全靠mainactivity
    private val createDB = "CREATE TABLE ${User.TABLE}(" +
            "${User.USER} integer PRIMARY KEY autoincrement," +
            "${User.PASSWORD} text,"+
            "${User.SQL} int)"


    override fun onCreate(db: SQLiteDatabase?) {
        Log.d(TAG,"DB onCreate ... ")
        if (db != null) {
            db.execSQL(createDB)
        }

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldversion: Int, newversion: Int) {
        TODO("Not yet implemented")
    }
}