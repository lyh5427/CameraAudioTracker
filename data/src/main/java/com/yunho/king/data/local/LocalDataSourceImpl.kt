package com.yunho.king.data.local

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "king_prefs")

@Singleton
class LocalDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : LocalDataSource {

    override suspend fun getAppAlim(): Boolean =
        context.dataStore.data.map { it[Keys.APP_ALIM] ?: true }.first()

    override suspend fun setAppAlim(enabled: Boolean) {
        context.dataStore.edit { it[Keys.APP_ALIM] = enabled }
    }

    override suspend fun getFirstOpenApp(): Boolean =
        context.dataStore.data.map { it[Keys.FIRST_OPEN_APP] ?: true }.first()

    override suspend fun setFirstOpenApp(isFirst: Boolean) {
        context.dataStore.edit { it[Keys.FIRST_OPEN_APP] = isFirst }
    }

    override suspend fun getRemoveList(): Set<String> =
        context.dataStore.data.map { prefs ->
            prefs[Keys.REMOVE_LIST] ?: defaultRemoveList()
        }.first()

    override suspend fun setRemoveList(pkgs: Set<String>) {
        context.dataStore.edit { it[Keys.REMOVE_LIST] = pkgs }
    }

    private fun defaultRemoveList(): Set<String> = setOf("com.sec.android.app.launcher")

    private object Keys {
        val APP_ALIM: Preferences.Key<Boolean> = booleanPreferencesKey("appAlim")
        val FIRST_OPEN_APP: Preferences.Key<Boolean> = booleanPreferencesKey("firstPermissionCheck")
        val REMOVE_LIST: Preferences.Key<Set<String>> = stringSetPreferencesKey("removeList")
    }
}

