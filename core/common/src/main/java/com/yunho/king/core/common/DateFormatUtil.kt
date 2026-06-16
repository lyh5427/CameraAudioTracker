package com.yunho.king.core.common

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateFormatUtil {
    private val formatter = SimpleDateFormat("MM월 dd일 hh:mm:ss", Locale.getDefault())

    fun format(timeMillis: Long): String = formatter.format(Date(timeMillis))
}
