package com.yunho.king.core.common

import android.content.Context

object AdMobUtil {
    private const val MAIN_BANNER_RES_NAME = "admob_unit_id_main_banner"
    private const val TEST_BANNER_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"

    fun getMainBannerUnitId(context: Context): String {
        val resId = context.resources.getIdentifier(
            MAIN_BANNER_RES_NAME,
            "string",
            context.packageName
        )
        return if (resId != 0) context.getString(resId) else TEST_BANNER_UNIT_ID
    }
}
