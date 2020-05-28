package com.example.meetplanner.network

import android.content.Context
import android.content.SharedPreferences

object Tokens {
    private var sharedPreferences: SharedPreferences? = null
    fun setup(context: Context) {
        sharedPreferences = context.getSharedPreferences("MeetPlanner.sharedpref", Context.MODE_PRIVATE)
    }
    var access: String?
        get() = sharedPreferences!!.getString("access_token", "")
        set(value) {
            with(sharedPreferences!!.edit()) {
                putString("access_token", value)
                commit()
            }
        }
    var refresh: String?
        get() = sharedPreferences!!.getString("refresh_token", "")
        set(value) {
            with(sharedPreferences!!.edit()) {
                putString("refresh_token", value)
                commit()
            }
        }
    fun clear() {
        sharedPreferences!!.edit().apply {
            remove("refresh_token")
            remove("access_token")
            apply()
        }
    }
}
