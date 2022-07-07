package com.example.mynote.edu.swu

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.mynote.R
import com.example.mynote.databinding.FgCollBinding

import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.liufeng.base.util.RecyclerUtils

class CollFragment:Fragment() {

    val TAG = "@@CollFragment"
    lateinit var helper: NoteDBHelper
    lateinit var adapter: CollAdapter
    var init=0
    private var toUpdate: Note? = null
    var arr = arrayListOf<Note>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "I'm OK1")
       //val view = inflater.inflate(R.layout.fg_coll,container,false)
        //init(view)
        //return view
        val binding = DataBindingUtil.inflate<FgCollBinding>(inflater, R.layout.fg_coll, container, false);
        binding.lifecycleOwner=this
        init(binding.getRoot())
        return binding.getRoot();


    }

    private fun init(view :View){
        //打开DB
        Log.d(TAG, "I'm OK2")
        helper = NoteDBHelper(this.requireContext(),"Note.db",1)
        //用于管理列表,for recycler
        adapter = CollAdapter()
        //下面这三行是要用adapter一定要写的

//        val view: View = requireActivity().layoutInflater.inflate(R.layout.fg_note, null)

        view?.apply {

            val recycler = findViewById<RecyclerView>(R.id.recycler)
            RecyclerUtils.initList(activity,recycler)
            recycler.adapter = adapter
            searchInDb()
            val srl = findViewById<SwipeRefreshLayout>(R.id.srl)
            srl.setColorSchemeResources(R.color.bg_topbar)
            srl.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
                Log.d(TAG, "coll shuaxin。。。")

                Log.d(TAG,"SIZE OF ARR ${arr.size}")

                Log.d(TAG,"SIZE OF ARR ${arr.size}")
                adapter.setNewData()//自清
                searchInDb()

                srl.isRefreshing = false
//有问题！！
            })
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== Activity.RESULT_OK){
            if (requestCode==10001){
                data?.let { saveInput(it) }
            }
        }
    }

    /**
     * 新增？
     */
    private fun saveInput(intent: Intent) {
        //取出字符串并转向saveInDb()中
        val mainOutline= intent.getStringExtra("OUTLINE").toString()
        val mainContent=intent.getStringExtra("CONTENT").toString()
        val time=intent.getStringExtra("TIME").toString()
        val item_id= intent.getStringExtra("item_id")?.toInt()
        val tag=intent.getStringExtra("Tag")?.toInt()
        val iscoll=intent.getIntExtra("coll",0)

        Log.d(TAG,"mainOutline as ${mainOutline} mainContent as $mainContent id as ${item_id},time as $time,tag as $tag")

        if (item_id != null) {

            if (tag != null) {
                if (iscoll != null) {

                    updateindb(mainContent,mainOutline,item_id,time,tag,iscoll)
                }
            }

        }
        else//SQLite还没有分配ID
        {
            if(init==1)//防止初始化自动增加内容
            {
                if (tag != null) {
                    saveInDb(mainContent,mainOutline,time,tag,iscoll)
                }
            }
        }
    }

    private fun saveInDb(text: String, outline: String, time: String, tag: Int, iscoll: Int?) {
        //往数据库存数据！！！

        Log.d(TAG, "get in edit=$text  outline=$outline")
        val db = helper.writableDatabase
        //编辑时间


        val item = iscoll?.let { Note(outline,text, time,tag, it) }
        val values = ContentValues().apply {
            if (item != null) {
                put(Note.COL_OUTLINE,item.outline)
            }
            if (item != null) {
                put(Note.COL_CONTENT, item.content)
            }
            if (item != null) {
                put(Note.COL_TIME, item.createTime)
            }
            if (item != null) {
                put(Note.COL_TAG, item.tag)
            }
            if (item != null) {
                put(Note.COL_coll,item.isColl)
            }
        }
        var rs = -1

        if (toUpdate != null) {
            if (item != null) {
                item.id = toUpdate?.id
            }
            Log.d(TAG, "UPDATE ID=$rs")
            rs = db.update(Note.TABLE, values, "id=?", arrayOf(toUpdate?.id.toString()))
            if (rs != -1) {
                toUpdate?.id?.let {
                    if (item != null) {
                        adapter.replaceItem(it, item)
                    }
                }
                toUpdate = null
            }
        } else {

            rs = db.insert(Note.TABLE, null, values).toInt()
            Log.d(TAG, "insert id =$rs")
            if (rs != -1) {
                if (item != null) {
                    item.id = rs
                }
                if (item != null) {
                    adapter.addItem(item)
                }
            }
        }
        // Toast.makeText(this, if (rs < 0) "保存失败" else "保存成功", Toast.LENGTH_LONG).show()

    }

    private fun updateindb(text: String, outline: String, item_id:Int,time:String,tag:Int,iscoll:Int) {
        //往数据库存数据！！！
        Log.d(TAG, "get in edit=$text  outline=$outline")
        val db = helper.writableDatabase
        val item = Note(outline, text,time,tag,iscoll)//这里标签也需要修改后期添加记得加上，时间就不需要了
        val values = ContentValues().apply {
            put(Note.COL_OUTLINE,item.outline)
            put(Note.COL_CONTENT, item.content)
            put(Note.COL_TIME, item.createTime)
            put(Note.COL_TAG, item.tag)
            put(Note.COL_coll,item.isColl)
        }
        var rs = -1
        //   Log.d(TAG,"这里这里$toUpdate")
        if (item_id != null) {
            item.id = item_id
            Log.d(TAG, "UPDATE ID=$rs")
            rs = db.update(Note.TABLE, values, "id=?", arrayOf(item_id.toString()))
            if (rs != -1) {
                item_id.let { adapter.replaceItem(it, item) }
                // toUpdate = null
            }
        } else {
            Log.d(TAG, "insert id =$rs")
            rs = db.insert(Note.TABLE, null, values).toInt()
            // Log.d(TAG, "insert id =$rs")
            if (rs != -1) {
                item.id = rs
                adapter.addItem(item)
            }
        }
        //  Toast.makeText(this, if (rs < 0) "保存失败" else "保存成功", Toast.LENGTH_LONG).show()

    }

    private fun setInputText(o_content: String,o_outline:String,item_id: Int?,tag: Int,coll:Int) {

        Log.d(TAG, "content=$o_content, outline=$o_outline")
        //findViewById<EditText>(R.id.ipt_text).setText(s)

//        Log.d(TAG, "outline as $o_outline content as $o_content")
        val intent = Intent(activity, EditActivity::class.java)
        val i = Intent(intent) //建立Intent

        i.putExtra("OLD_OUTLINE", o_outline)
        i.putExtra("OLD_CONTENT", o_content)
        i.putExtra("item_id",item_id.toString()) //传递两个参数
        i.putExtra("tag",tag.toString())
        i.putExtra("coll",coll.toString())

        Log.d(TAG, "send outline as $o_outline content as $o_content id as ${item_id.toString()}")
        startActivityForResult(i,10001)
    }
    @SuppressLint("Range")
    private fun searchInDb() {
        Log.d(TAG, "I'm OK3")

        val db = helper.readableDatabase//文件为只读
        val select="${Note.COL_coll} =1"
        Log.d(TAG,select)
        val cursor = db.query(Note.TABLE, null,select, null, null, null, "${Note.COL_ID} desc"
        )//使用存入ID来排序，其实后期可以改成按照change_time排
        Log.d(TAG, "I'm OK4")

        arr.removeAll(arr)
        Log.d(TAG, "I'm OK7 size--> "+arr.size)
        Log.d(TAG, "I'm OK5")
        if (cursor.moveToFirst()) {
            do {//遍历Cursor对象，取出数据并打印
                arr.add(
                    Note(//idx是角标，id
                        //这里需要加个判断才行,加了也不得行，所以加了这个函数上面那个注解
                        cursor.getString(cursor.getColumnIndex(Note.COL_OUTLINE)),//内容
                        cursor.getString(cursor.getColumnIndex(Note.COL_CONTENT)),//标题
                        cursor.getString(cursor.getColumnIndex(Note.COL_TIME)),//更新时间
                        cursor.getInt(cursor.getColumnIndex(Note.COL_coll)),//收藏否？
                        cursor.getInt(cursor.getColumnIndex(Note.COL_TAG))//标签

                    ).apply {
                        if (cursor.getColumnIndex(Note.COL_ID) >= 0) {
                            id = cursor.getInt(cursor.getColumnIndex(Note.COL_ID))
                        }

                        Log.d(TAG, "id ${Note.COL_ID},cont ${Note.COL_CONTENT},outline ${Note.COL_OUTLINE},tag ${Note.COL_TAG},collection as ${Note.COL_coll}")
                    }

                )
            } while (cursor.moveToNext())
        }
        Log.d(TAG, "I'm OK6 size--> "+arr.size)

        adapter.setData(arr)

        cursor.close()
    }


    inner class CollAdapter : RecyclerView.Adapter<QueryViewHolder>() {
        val cont = arrayListOf<Note>()//这个是getItemCount重要返还的数据大小的数据

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QueryViewHolder {
            //在实际开发种LayoutInflater这个类还是非常有用的，它的作用类似于findViewById()，不同点是LayoutInflater是用来找layout下xml布局文件，并且实例化！而findViewById()是找具体xml下的具体widget控件(如:Button,TextView等)。
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
            return QueryViewHolder(view).apply {
                outline=view.findViewById(R.id.outline)
                content = view.findViewById(R.id.content)
                time =view.findViewById(R.id.time)
                //这里后期可以增添一个标签
                // btnColl=view.findViewById(R.id.btn_coll)//这个也要传参，之后可以做成一个隐藏的界面
                btnUpdate = view.findViewById(R.id.content)
                btnDelete = view.findViewById(R.id.btn_delete)
            }
            //  val holder=TeacherViewHolder(view)EXP0320中的写法,
            //        return holder
            //下面是EXP0320中TeacherViewHolder中的内容，和我这个有区别在于在HOlder中就已经找好val值了而我这里是在OnCreat里才找的
            //val name=itemView.findViewById<TextView>(R.id.name)
            //         val desc=itemView.findViewById<TextView>(R.id.description)
            //         val img=itemView.findViewById<ImageView>(R.id.avater)
//这个item需要我们用inflate函数把msg_item动态的加载进main布局，
//并且返回了一个用来获取item里控件并且对其进行操作的View对象。
        }

        override fun onBindViewHolder(holder: QueryViewHolder, position: Int) {
            //束缚控件的位置
            holder.render(cont[position])
        }

        override fun getItemCount(): Int {
            return cont.size
            Log.d(TAG,"cont.size as ${cont.size}")
        }

        fun setData(arr: ArrayList<Note>) {
            cont.addAll(arr)
            Log.d(TAG,"cont as --> ${cont.size}")
            adapter.notifyDataSetChanged()
        }
fun setNewData(){

    cont.removeAll(cont)
    Log.d(TAG,"SetNewData cont as --> ${cont.size}")
    adapter.notifyItemRangeInserted(0,cont.size)
}
        fun replaceItem(id: Int, item: Note) {
            val idx = findIdx(id)
            if (idx >= 0) {
                Log.d(TAG, "idx=$idx,item=$item")
                cont.set(idx, item)
                notifyItemChanged(idx)
            }
        }

        fun addItem(item: Note) {
            cont.add(0, item)
            notifyItemInserted(0)
        }

        private fun findIdx(id: Int?): Int {
            var idx = -1
            cont.forEachIndexed() {index,todo->
                if (todo.id == id) {
                    idx = index
                }
            }
            /*  cont.forEachIndexed{ index,todo-?
            if(todo.id==id){
                idx=index
            }
        }
        return idx
    }*/
            return idx

        }

        fun itemDeleted(id: Int?) {
            val idx = findIdx(id)
            Log.d(TAG, "to idx id=${idx}")
            if (idx >= 0) {

                cont.removeAt(idx)
                notifyItemRemoved(idx)
                //  Log.d(TAG,"to idx1 id=${idx}")

                // Log.d(TAG,"to idx2 id=${idx}")
            }
        }
    }

    inner class QueryViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var id: TextView? = null
        var outline: TextView?=null
        var content: TextView? = null
        var time: TextView?=null
        var btnColl: TextView?=null
        var btnUpdate: TextView? = null
        var btnDelete: TextView? = null
        fun render(note: Note) {
            id?.text = note.id.toString()
            outline?.text=note.outline
            content?.text = note.content
            time?.text=note.createTime
            btnColl?.text= note.isColl.toString()

            btnDelete?.setOnClickListener {//点击删除按钮
                Log.d(TAG, "to delete id=${note.id}")
                val db = helper.writableDatabase

                db.delete(Note.TABLE, "id=?", arrayOf(note.id.toString()))

                adapter.itemDeleted(note.id)


            }
            btnUpdate?.setOnClickListener {//更改笔记内容
                Log.d(TAG, "to Update id=${note.id}")
                toUpdate = note
                ///获取outline和content参数
                Log.d(TAG, "最初=${toUpdate}")
                setInputText(note.content,note.outline,note.id,note.tag,note.isColl)

            }
        }
    }

}