package com.yunho.king.data.local

interface LocalDataSource {
    suspend fun getAppAlim(): Boolean
    suspend fun setAppAlim(enabled: Boolean)

    suspend fun getFirstOpenApp(): Boolean
    suspend fun setFirstOpenApp(isFirst: Boolean)

    suspend fun getRemoveList(): Set<String>
    suspend fun setRemoveList(pkgs: Set<String>)
}

