package com.test.landmarkremark.utils

import android.content.Context
import android.content.SharedPreferences
import com.test.landmarkremark.application

/**
 * The class is used to save session token throughout communication
 *
 * @author Nam Hoai Nguyen
 */
object SavedStore {

    private const val PREFS_KEY = "account-session"
    private val prefs: SharedPreferences by lazy { application!!.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE) }

    /**
     * Save string.
     *
     * @param key   the key
     * @param value the value
     * @return true, if successful
     */
    fun saveString(key: String?, value: String?): Boolean {
        val editor = prefs.edit()
        editor.putString(key, value)
        return editor.commit()
    }

    /**
     * Gets the string.
     *
     * @param key      the key
     * @param defValue the default value
     * @return the string
     */
    fun getString(key: String?, defValue: String?): String {
        return prefs.getString(key, defValue) ?: ""
    }

    /**
     * Save boolean.
     *
     * @param key   the key
     * @param value the value
     * @return true, if successful
     */
    fun saveBoolean(key: String?, value: Boolean): Boolean {
        val editor = prefs.edit()
        editor.putBoolean(key, value)
        return editor.commit()
    }

    /**
     * Gets the boolean.
     *
     * @param key      the key
     * @param defValue the default value
     * @return the boolean
     */
    fun getBoolean(key: String?, defValue: Boolean): Boolean {
        return prefs.getBoolean(key, defValue)
    }

    fun saveInt(key: String?, value: Int): Boolean {
        val editor = prefs.edit()
        editor.putInt(key, value)
        return editor.commit()
    }

    fun getInt(key: String?, defValue: Int): Int {
        return prefs.getInt(key, defValue)
    }

    fun saveLong(key: String?, value: Long): Boolean {
        val editor = prefs.edit()
        editor.putLong(key, value)
        return editor.commit()
    }

    fun getLong(key: String?, defValue: Long): Long {
        return prefs.getLong(key, defValue)
    }

    fun saveStringSet(key: String?, value: Set<String>): Boolean {
        val editor = prefs.edit()
        editor.putStringSet(key, value)
        return editor.commit()
    }

    fun getStringSet(key: String?, defValue: Set<String>): MutableSet<String>? {
        return prefs.getStringSet(key, defValue)
    }

    fun contains(key: String?): Boolean {
        return prefs.contains(key)
    }

    /**
     * Clear.
     *
     * @param objKey the obj key
     */
    fun clear(objKey: String?) {
        val editor = prefs.edit()
        editor.remove(objKey)
        editor.apply()
    }

    fun clearAllData() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }
}