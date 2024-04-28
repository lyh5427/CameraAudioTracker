package com.yunho.king.Utils

import android.view.View

class SingleClickListener(
    private val interval: Int = 600,
    private var singleClick: (View) -> Unit
) : View.OnClickListener {

    companion object {
        private var lastestClickTime: Long = 0
    }

    override fun onClick(v: View?) {
        val clickTime = System.currentTimeMillis()
        if ((clickTime - lastestClickTime) < interval) {
            return
        }
        lastestClickTime = clickTime
        singleClick(v!!)
    }
}

fun View.singleClickListener(onSingleClick: (View) -> Unit) {
    val singleClick = SingleClickListener {
        onSingleClick
    }
    setOnClickListener(singleClick)
}