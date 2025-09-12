package com.happyhope.bubbletea.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * 사용자 설정 관리를 위한 DataStore
 */
class UserPreferencesDataStore(private val context: Context) {
    
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
            name = "user_preferences"
        )
        
        private val USER_NAME = stringPreferencesKey("user_name")
        private val FAVORITE_CATEGORY = stringPreferencesKey("favorite_category")
        private val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
        private val DARK_MODE_ENABLED = booleanPreferencesKey("dark_mode_enabled")
    }
    
    val userName: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[USER_NAME] ?: ""
    }
    
    val favoriteCategory: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[FAVORITE_CATEGORY] ?: ""
    }
    
    val notificationsEnabled: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[NOTIFICATIONS_ENABLED] ?: true
    }
    
    val darkModeEnabled: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[DARK_MODE_ENABLED] ?: false
    }
    
    suspend fun setUserName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_NAME] = name
        }
    }
    
    suspend fun setFavoriteCategory(category: String) {
        context.dataStore.edit { preferences ->
            preferences[FAVORITE_CATEGORY] = category
        }
    }
    
    suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[NOTIFICATIONS_ENABLED] = enabled
        }
    }
    
    suspend fun setDarkModeEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DARK_MODE_ENABLED] = enabled
        }
    }
}