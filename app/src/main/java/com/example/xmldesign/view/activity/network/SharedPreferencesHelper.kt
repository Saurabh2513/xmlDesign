package com.example.xmldesign.view.activity.network

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesHelper(context: Context) {

    private val preferences: SharedPreferences =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun saveEmail(email: String) {
        val editor = preferences.edit()
        val emails = getEmails().toMutableList()
        emails.add(email)
        editor.putStringSet("emails", emails.toSet())
        editor.apply()
    }

    fun getEmails(): Set<String> {
        return preferences.getStringSet("emails", emptySet()) ?: emptySet()
    }

    fun clearEmails() {
        val editor = preferences.edit()
        editor.clear()
        editor.apply()
    }
}
