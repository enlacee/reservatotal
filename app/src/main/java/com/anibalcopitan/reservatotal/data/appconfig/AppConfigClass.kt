package com.anibalcopitan.reservatotal.data.appconfig

import android.content.Context
import com.anibalcopitan.reservatotal.data.phonenumberregistration.SharedPreferencesManager

// Clase para representar la configuración
data class AppConfig(
    var id: String,
    var username: String,
    var password: String,
    var name: String,
    var subscriptionStartDate: String,
    var subscriptionDurationDays: String,
    var subscriptionPlan: String,
    var googleSheetUrl: String
)

// Builder para crear la configuración
private class AppConfigBuilder(private val sharedPreferencesManager: SharedPreferencesManager) {
    private var id: String = sharedPreferencesManager.getString(SharedPreferencesManager.KEY_ID, "")
    private var username: String = sharedPreferencesManager.getString(SharedPreferencesManager.KEY_USERNAME, "")
    private var password: String = sharedPreferencesManager.getString(SharedPreferencesManager.KEY_PASSWORD, "")
    private var name: String = sharedPreferencesManager.getString(SharedPreferencesManager.KEY_NAME, "")
    private var subscriptionStartDate: String = sharedPreferencesManager.getString(SharedPreferencesManager.KEY_SUBSCRIPTION_START_DATE, "")
    private var subscriptionDurationDays: String = sharedPreferencesManager.getString(SharedPreferencesManager.KEY_SUBSCRIPTION_DURATION_DAYS, "")
    private var subscriptionPlan: String = sharedPreferencesManager.getString(SharedPreferencesManager.KEY_SUBSCRIPTION_PLAN, "")
    private var googleSheetUrl: String = sharedPreferencesManager.getString(SharedPreferencesManager.KEY_GOOGLE_SHEET_URL, "")

    fun build(): AppConfig {
        return AppConfig(id, username, password, name, subscriptionStartDate, subscriptionDurationDays, subscriptionPlan, googleSheetUrl )
    }
}

class AppConfigClass(private val sharedPreferencesManager: SharedPreferencesManager) {

    private val appConfigBuilder = AppConfigBuilder(sharedPreferencesManager)

    fun saveAppConfig(appConfig: AppConfig) {
        sharedPreferencesManager.saveString(SharedPreferencesManager.KEY_ID, appConfig.id)
        sharedPreferencesManager.saveString(SharedPreferencesManager.KEY_USERNAME, appConfig.username)
        sharedPreferencesManager.saveString(SharedPreferencesManager.KEY_PASSWORD, appConfig.password)
        sharedPreferencesManager.saveString(SharedPreferencesManager.KEY_NAME, appConfig.name)
        sharedPreferencesManager.saveString(SharedPreferencesManager.KEY_SUBSCRIPTION_START_DATE, appConfig.subscriptionStartDate)
        sharedPreferencesManager.saveString(SharedPreferencesManager.KEY_SUBSCRIPTION_DURATION_DAYS, appConfig.subscriptionDurationDays)
        sharedPreferencesManager.saveString(SharedPreferencesManager.KEY_SUBSCRIPTION_PLAN, appConfig.subscriptionPlan)
        sharedPreferencesManager.saveString(SharedPreferencesManager.KEY_GOOGLE_SHEET_URL, appConfig.googleSheetUrl)
    }

    fun loadAppConfig(): AppConfig {
        return appConfigBuilder.build()
    }

    companion object {
        private var instance: AppConfigClass? = null

        fun getInstance(context: Context): AppConfigClass {
            if (instance == null) {
                val sharedPreferencesManager = SharedPreferencesManager(context)
                instance = AppConfigClass(sharedPreferencesManager)
            }
            return instance!!
        }
    }

}

