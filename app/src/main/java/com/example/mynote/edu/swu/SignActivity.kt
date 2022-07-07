package com.example.mynote.edu.swu

import android.content.ContentValues
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.mynote.R

class SignActivity : AppCompatActivity() {
    lateinit var dbhelper: UserDBHelper
    val context = this


    val TAG = "@@SignActivity"

    //    var id: String? =null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_sign)
        val srl = findViewById<Button>(R.id.btn_ok)
        srl.setOnClickListener(){
            Log.d(TAG,"Chekck")
            check()
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun check() {

        val iptUsername = findViewById<EditText>(R.id.ipt_username).getText().toString()//获取输入框信息
        val iptPassword = findViewById<EditText>(R.id.ipt_password).getText().toString()
        val iptCheck = findViewById<EditText>(R.id.ipt_check).getText().toString()
        Log.d(TAG,"iptUsername $iptUsername  iptPassword$iptPassword   iptCheck$iptCheck")
            if(iptUsername!=null&&iptPassword!=null&&iptCheck!=null){
                if(iptCheck==iptPassword)
                {
                    adduser(iptUsername,iptPassword)
                }
                else{
                    Toast.makeText( this , "两次输入有差，请重新输入密码" , Toast.LENGTH_SHORT).show()
                }

            }
            else if(iptUsername!=null&&iptPassword==null){
                Toast.makeText( this , "请输入密码" , Toast.LENGTH_SHORT).show()
            }
            else if(iptUsername!=null&&iptPassword!=null&&iptCheck==null){
                Toast.makeText( this , "请再次输入确认密码" , Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText( this , "请将内容填写完整" , Toast.LENGTH_SHORT).show()
            }



    }

    private fun adduser(user: String, pass: String) {
        //往数据库存数据！！！
        Log.d(TAG, "get in edit=$user  outline=$pass")

        dbhelper=UserDBHelper(this,User.TABLE,1)
    val db = dbhelper.writableDatabase

    val values = ContentValues().apply {
        put(User.USER,"$user")
        put(User.PASSWORD, "$pass")
        put(User.SQL,1)
        Log.d(TAG,"user=${user}")
        Log.d(TAG,"user=${pass}")

    }
    db.insert("Use", null, values) // 插入一条数据
    db.close()
        startActivity(Intent(context, MainActivity::class.java))

}






}
