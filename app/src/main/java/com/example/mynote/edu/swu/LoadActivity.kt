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

class LoadActivity: AppCompatActivity(), RadioGroup.OnCheckedChangeListener
{
    //UI Objects
    val TAG="@@MainActivity"
    private var rg_tab_bar: RadioGroup? = null
    lateinit var fManager:FragmentManager

    private var fg1: NoteFragment? =null
    private  var fg2:QueryFragment? = null
    private  var fg3:CollFragment? = null
    private  var fg4:settingFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startWorker()

        fManager =supportFragmentManager
        var  rg_tab_bar=findViewById<RadioGroup?>(R.id.rg_tab_bar).setOnCheckedChangeListener(this)

        //获取第一个单选按钮，并设置其为选中状态
        var  rb_channel = findViewById<RadioButton>(R.id.rb_note);
        rb_channel.setChecked(true);

    }

    private fun startWorker() {
        val request = PeriodicWorkRequest.Builder(MyWorker::class.java,2 ,
            java.util.concurrent.TimeUnit.HOURS
        ).build()//周期需要大于2小时
        Toast.makeText( this , "Starting worker" , Toast.LENGTH_SHORT).show()
        WorkManager.getInstance( this ).enqueue(request)
    }


    override fun onCheckedChanged(group : RadioGroup?, checkedId: Int) {
        val fTransaction = fManager.beginTransaction()
        hideALLFragment(fTransaction)
        when (checkedId) {
            R.id.rb_note -> if (fg1 == null) {
                fg1 = NoteFragment()
                fTransaction.add(R.id.ly_content, fg1!!)
            } else {
                fTransaction.show(fg1!!)
            }
            R.id.rb_query -> if (fg2 == null) {
                fg2 = QueryFragment()
                fTransaction.add(R.id.ly_content, fg2!!)
            } else {
                fTransaction.show(fg2!!)
            }
            R.id.rb_coll -> if (fg3 == null) {
                fg3 = CollFragment()
                fTransaction.add(R.id.ly_content, fg3!!)
            } else {
                fTransaction.show(fg3!!)
            }
            R.id.rb_setting -> if (fg4 == null) {
                fg4 = settingFragment()
                fTransaction.add(R.id.ly_content, fg4!!)
            } else {
                fTransaction.show(fg4!!)
            }
        }
        fTransaction.commit()
    }

    private fun hideALLFragment(fTransaction: FragmentTransaction) {
        if(fg1!=null)fTransaction.hide(fg1!!)
        if(fg2!=null)fTransaction.hide(fg2!!)
        if(fg3!=null)fTransaction.hide(fg3!!)
        if(fg4!=null)fTransaction.hide(fg4!!)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(TAG, "I'm OK3")
        super.onActivityResult(requestCode, resultCode, data)
        if (getSupportFragmentManager()?.getFragments() != null && getSupportFragmentManager()?.getFragments()!!.size > 0) {

            val fragments: List<Fragment> = getSupportFragmentManager().getFragments()
            for (mFragment in fragments) {
                mFragment.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

}