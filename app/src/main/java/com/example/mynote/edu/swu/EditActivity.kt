package com.example.mynote.edu.swu

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.mynote.R
import com.example.mynote.databinding.ActivityEditBinding
import com.example.mynote.databinding.ActivityMainBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class EditActivity : AppCompatActivity() {
 //   lateinit var helper: TagDBHelper

    @RequiresApi(Build.VERSION_CODES.O)
    val current = LocalDateTime.now()

    var state=0

    @RequiresApi(Build.VERSION_CODES.O)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    @RequiresApi(Build.VERSION_CODES.O)
    val formatted = current.format(formatter)

    val TAG = "@@EditActivity"

    //    var id: String? =null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "trans successfully")
        setContentView(R.layout.activity_edit)//update和init要区分开
       /* val binding= DataBindingUtil.setContentView<ActivityEditBinding>(this,R.layout.activity_edit)
        binding.lifecycleOwner=this*/

        init()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun init() {
        //这里是接收修改的内容
        val o_Outline = intent.getStringExtra("OLD_OUTLINE").toString()
        val o_Content = intent.getStringExtra("OLD_CONTENT").toString()

        val id = intent.getStringExtra("item_id")
        val tag = intent.getStringExtra("tag")
        val coll = intent.getStringExtra("coll")
        Log.d(TAG, "mainOutline as ${o_Outline} mainContent as $o_Content  id as $id tag as $tag")
        //Spinner
       // helper = TagDBHelper(this,"tag.db",1)
            //SpinnerSet()
        //readInDb()
        //----
        if (id != null) {
            Log.d(TAG, "id as ${id}")
            if (tag != null) {
                if (coll != null) {
                    setEditText(o_Outline, o_Content,tag,coll)
                }
            }
        } else {
            if (tag != null) {
                if (coll != null) {
                    setEditText(o_Outline, o_Content,tag,coll)
                }
            }
        }
        if (id != null) {
            initData(id)
        } else {
            initData(null)
        }

    }



    @RequiresApi(Build.VERSION_CODES.O)
    private fun setEditText(oOutline: String, oContent: String,tag: String,coll:String) {
        if (oOutline == "null") {
            val title = "Title"
            findViewById<EditText>(R.id.ipt_outline).setText(title)
        } else {
            findViewById<EditText>(R.id.ipt_outline).setText(oOutline)
        }
        if (oContent == "null") {
            val context = "context"
            findViewById<EditText>(R.id.ipt_content).setText(context)
        } else {
            findViewById<EditText>(R.id.ipt_content).setText(oContent)
        }
        if(coll=="1"){
            findViewById<ImageView>(R.id.iv_coll).setImageState(
                intArrayOf(
                    android.R.attr.state_activated,
                    android.R.attr.state_selected
                ), true)
            findViewById<TextView>(R.id.time).setText(formatted.toString())
            Log.d(TAG,"coll is ${coll}")
        }
        Log.d(TAG,"coll is ${coll}")
        findViewById<TextView>(R.id.time).setText(formatted.toString())

    }




    /*private fun SpinnerSet() {

        val tag = resources.getStringArray(R.array.tag)


        val spinner = findViewById<Spinner>(R.id.tag_select)
        if (spinner != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item, tag
            )
            spinner.adapter = adapter
            spinner.setBackgroundColor(0x0)
        }
        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                Toast.makeText(//下面的提醒
                    this@EditActivity,
                    getString(R.string.selected_item) + " " +
                            "" + tag[position], Toast.LENGTH_SHORT
                ).show()
                state=position
               //这里可以增加一个传参，tag[position]就是下拉框的选项
            }



            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

    }*/
    private fun initData(item_id: String?) {
        val ivColl = findViewById<ImageView>(R.id.iv_coll)
        ivColl.setOnClickListener { ivColl.isSelected = !ivColl.isSelected }

        findViewById<Button>(R.id.btn_save).setOnClickListener {
            val outline = findViewById<TextView>(R.id.ipt_outline).text.toString();
            val content = findViewById<TextView>(R.id.ipt_content).text.toString();

            Log.d(TAG, "outline as $outline content as $content")
            //回传数据
            val i = Intent() //建立Intent
            i.putExtra("OUTLINE", outline)
            i.putExtra("CONTENT", content)
            i.putExtra("TIME", formatted)
            i.putExtra("item_id", item_id) //传递两个参数
            i.putExtra("Tag",state.toString())
            i.putExtra("coll",if (ivColl.isSelected) 1 else 0)
            Log.d(
                TAG,
                "send outline as $outline content as $content id as $item_id time as $formatted tag as $state collection as ${ivColl.isSelected}"
            )
            setResult(RESULT_OK,i)
            finish()
        }
    }

}
