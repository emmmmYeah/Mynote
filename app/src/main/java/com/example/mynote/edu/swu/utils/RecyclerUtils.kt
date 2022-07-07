package com.liufeng.base.util

import android.content.Context
import androidx.recyclerview.widget.*


/**
 *
 * Created by liufeng on 2021/11/3
 */
object RecyclerUtils {

    /**
     * 设置listView区分线
     *
     * @param context
     * @param recyclerView
     * @param height       高
     * @param color        颜色
     */
    fun initList(context: Context?, recyclerView: RecyclerView) {
//        recyclerView.addItemDecoration(
//            DividerItemDecoration(
//                context,
//                GridLayoutManager.VERTICAL
//            )
//        )
        val layoutManager = LinearLayoutManager(context)
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL)
        recyclerView.setLayoutManager(layoutManager)
    }

    fun initListHorizontal(context: Context?, recyclerView: RecyclerView) {
        recyclerView.setLayoutManager(LinearLayoutManager(context, RecyclerView.HORIZONTAL, false))
    }

    //GridView
    fun initGrid(recyclerView: RecyclerView) {
        val staggeredGridLayoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.setLayoutManager(staggeredGridLayoutManager)
    }

    //GridView
    fun initGrid(recyclerView: RecyclerView, number: Int) {
        val staggeredGridLayoutManager =
            StaggeredGridLayoutManager(number, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.setLayoutManager(staggeredGridLayoutManager)
    }

    //GridView
    fun initGrid(
        context: Context?,
        recyclerView: RecyclerView,
        number: Int
    ) {
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                StaggeredGridLayoutManager.VERTICAL
            )
        )
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                StaggeredGridLayoutManager.HORIZONTAL
            )
        )
        val staggeredGridLayoutManager =
            StaggeredGridLayoutManager(number, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.setLayoutManager(staggeredGridLayoutManager)
    }

    /**
     * 横向2行滑动 recyclerView
     *
     * @param context
     * @param recyclerView
     */
    fun initHorizontalGrid(context: Context?, recyclerView: RecyclerView) {
        val manager = GridLayoutManager(context, 2, RecyclerView.HORIZONTAL, false)
        recyclerView.setLayoutManager(manager)
    }

    /**
     * 横向滑动 recyclerView
     *
     * @param context
     * @param recyclerView
     * @param line         横向排列几行
     */
    fun initHorizontalGrid(context: Context?, recyclerView: RecyclerView, line: Int) {
        val manager = GridLayoutManager(context, line, RecyclerView.HORIZONTAL, false)
        recyclerView.setLayoutManager(manager)
    }

    //GridView
    fun initGridWithRefresh(context: Context?, recyclerView: RecyclerView, number: Int) {
        val layoutManager = GridLayoutManager(context, number)
        recyclerView.setLayoutManager(layoutManager)
    }
}