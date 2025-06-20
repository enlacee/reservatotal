package com.anibalcopitan.reservatotal.data.phonenumberregistration

import android.content.Context
import android.content.SharedPreferences
import com.anibalcopitan.reservatotal.BuildConfig

class SharedPreferencesManager(context: Context) {

    companion object {
        private const val KEY_MY_PREFS = "${BuildConfig.APPLICATION_ID}.MyPrefs"
        private const val KEY_PHONE_NUMBER_1 = "phoneNumber1"
        private const val KEY_PHONE_NUMBER_2 = "phoneNumber2"
        private const val KEY_FLAG = "flag"

        // v2
        const val KEY_ID = "id"
        const val KEY_USERNAME = "username"
        const val KEY_PASSWORD = "password"
        const val KEY_NAME = "name"
        const val KEY_SUBSCRIPTION_START_DATE = "subscription_start_date"
        const val KEY_SUBSCRIPTION_DURATION_DAYS = "subscription_duration_days"
        const val KEY_SUBSCRIPTION_PLAN = "subscription_plan"
        const val KEY_GOOGLE_SHEET_URL = "google_sheet_url"
//        const val KEY_COUNTER = "counter"
        const val KEY_COUNTER = "counter"
    }
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(KEY_MY_PREFS, Context.MODE_PRIVATE)

    fun saveFormData(phoneNumber1: String, phoneNumber2: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_PHONE_NUMBER_1, phoneNumber1)
        editor.putString(KEY_PHONE_NUMBER_2, phoneNumber2)

        editor.apply()
    }

    fun getFormData(): FormModel {
        val phoneNumber1 = sharedPreferences.getString(KEY_PHONE_NUMBER_1, "") ?: ""
        val phoneNumber2 = sharedPreferences.getString(KEY_PHONE_NUMBER_2, "") ?: ""
        return FormModel(phoneNumber1, phoneNumber2)
    }

    fun getPhoneNumbers(): Array<String> {
        val phoneNumber1 = sharedPreferences.getString(KEY_PHONE_NUMBER_1, "") ?: ""
        val phoneNumber2 = sharedPreferences.getString(KEY_PHONE_NUMBER_2, "") ?: ""

        // Crear y retornar un array con los números de teléfono obtenidos
        return arrayOf(phoneNumber1, phoneNumber2)
    }

    /**
     * save data flag
     */
    fun saveFormDataFlag(flag:Boolean){
        val editor = sharedPreferences.edit()
        editor.putBoolean(KEY_FLAG, flag)

        editor.apply()
    }
    fun getFormDataFlag(): FormModelFlag {
        val flag = sharedPreferences.getBoolean(KEY_FLAG, true) ?: true
        return FormModelFlag(flag)
    }

    data class FormModel(val phoneNumber1: String, val phoneNumber2: String)
    data class FormModelFlag(val flag: Boolean)


    //    extra functionality
    fun getString(keyString: String, valueDefault: String): String {
        return sharedPreferences.getString(keyString, valueDefault) ?: ""
    }

    fun saveString(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    /*
    * Counter
    * */
    fun getCounter(): Int {
        return sharedPreferences.getInt(KEY_COUNTER, 0)
    }

    fun setCounter(counter: Int) {
        sharedPreferences.edit().putInt(KEY_COUNTER, counter).apply()
    }
    fun registerCounterChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    fun unregisterCounterChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
    }
}


