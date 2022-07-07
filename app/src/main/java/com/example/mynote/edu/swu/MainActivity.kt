package com.example.mynote.edu.swu


import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.*
import com.example.mynote.R
import com.example.mynote.databinding.ActivityMainBinding
import com.example.mynote.BR.MainBinding
import java.util.*


class MainActivity: AppCompatActivity(){

    //UI Objects
         val TAG="@@MainActivity"
    lateinit var dbHelper: UserDBHelper
    private var username:String?=null
    private var password:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load)

        val iptUsername=findViewById<EditText>(R.id.ipt_username)//获取输入框信息
        val iptPassword=findViewById<EditText>(R.id.ipt_password)

        val userNameWatcher = object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.i(TAG,"beforeTextChanged:$s")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.i(TAG,"onTextChanged:$s")
            }

            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG,"afterTextChanged:$s")
                username=s?.toString()

            }

        }
        val passwordWatcher = object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.i(TAG,"beforeTextChanged:$s")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.i(TAG,"onTextChanged:$s")
            }

            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG,"afterTextChanged:$s")
                password=s.toString()//将watch中获取的内容放入全局变量中
            }

        }

        iptUsername.addTextChangedListener(userNameWatcher)
        iptPassword.addTextChangedListener(passwordWatcher)
    }
    fun sign(view:View){
        val context = this
        startActivity(Intent(context, SignActivity::class.java))
    }
    fun onClick(view: View) {
        Log.d(TAG, "username:$username")
        Log.d(TAG, "password:$password")

        AlertDialog.Builder(this)
            .setTitle("请确认")
            .setMessage("您是否要登录？")
            .setNegativeButton("取消"){d,w->
                Toast.makeText(this,"已取消",Toast.LENGTH_LONG).show()
            }
            .setPositiveButton("确定"){d,w->
                tologin()
            }
            .show()
    }

    private fun tologin() {
        val iptUsername = findViewById<EditText>(R.id.ipt_username).getText().toString()//获取输入框信息
        val iptPassword = findViewById<EditText>(R.id.ipt_password).getText().toString()

        val process = findViewById<ProgressBar>(R.id.progress)
        process.visibility = View.VISIBLE
        process.progress = 0
        //TODO:请求网络  耗时
        val timer = Timer()
        val context = this
        Log.d(TAG,"username as $iptUsername iptPassword as $iptPassword")
       if (searchInDb(iptUsername,iptPassword)) {
        //if (iptUsername.equals("123")&&iptPassword.equals("123")) {
            val task = object : TimerTask() {

                override fun run() {//定时

                    Log.d(TAG, "progress=${process.progress}")
                    process.progress = process.progress + 1

                    if (process.progress == 10) {
                        timer.cancel()
                        /* runOnUiThread{//runnable
                          process.visibility=View.GONE

                      }*/
                        runOnUiThread {//runnable
                            process.visibility = View.GONE
                            startActivity(Intent(context, LoadActivity::class.java))
                        }
                    }
                }
            }
            timer.schedule(task, 0, 1000)//一秒钟执行一次fun run()


        }
        else{
            AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("用户名或密码输入有误")

                .setPositiveButton("确定"){d,w->
                    Toast.makeText(this,"已取消",Toast.LENGTH_LONG).show()
                }
                .show()
        }
    }
   private fun searchInDb(name:String,pass:String):Boolean{
        Log.d(TAG, "I'm OK3")
       //val dbHelper =UserDBHelper(this, "User.db", 2)
       dbHelper=UserDBHelper(this,User.TABLE,1)
        val db = dbHelper.readableDatabase//文件为只读
        val select = "${User.USER} = '" + name + "' and ${User.PASSWORD} = '" + pass + "'"
        Log.d(TAG, select)
        val cursor = db.query(
            User.TABLE, null, select, null, null, null, "${User.USER} desc"
        )//使用存入ID来排序，其实后期可以改成按照change_time排
        Log.d(TAG, "I'm OK4")

        if (cursor.moveToFirst()) {
            do {//遍历Cursor对象，取出数据并打印
                Log.d(TAG,"11")
                return true
            } while (cursor.moveToNext())
        }

        cursor.close()

       return false
    }

}