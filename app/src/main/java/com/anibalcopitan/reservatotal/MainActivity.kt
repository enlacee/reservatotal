package com.anibalcopitan.reservatotal

import android.app.ActivityManager
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.anibalcopitan.reservatotal.data.phonenumberregistration.SharedPreferencesManager
import com.anibalcopitan.reservatotal.ui.theme.OkeyPay2Theme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        var sharedPreferencesManager = SharedPreferencesManager(this@MainActivity)
        Log.i("BOTON-REGISTRAR", " MainActivity.onCreate ")
        super.onCreate(savedInstanceState)

        setContent {
            OkeyPay2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginScreen()
                }

                // 01. Llamado para solicitar permisos
                requestNotificationPolicyAccess(this@MainActivity)

                // 02. LISTENER NOTIFICATION
                if (!isNotificationListenerEnabled(this@MainActivity)) {
                    requestNotificationAccess()
                }

                // 03. start app foreground
                // Verificar si el servicio en primer plano ya está en ejecución
                val intent = Intent(this, InstallationService::class.java)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (isServiceRunning(InstallationService::class.java)) {
                        // El servicio ya está en primer plano, no es necesario iniciar uno nuevo
                        Log.d("DEBUG", "El servicio ya está en primer plano")
                    } else {
                        startForegroundService(intent)
                    }
                } else {
                    startService(intent)
                }
            }
        }
    }

    // Función para verificar si un servicio está en ejecución
    private fun isServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    private fun requestNotificationAccess() {
        val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
        startActivity(intent)
    }

    private fun isNotificationListenerEnabled(context: Context): Boolean {
        val componentName = ComponentName(context, TheNotificationListener::class.java)
        val flat = Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
        return flat != null && flat.contains(componentName.flattenToString())
    }
}

// Llamado desde tu Activity o Fragment para solicitar permisos
fun requestNotificationPolicyAccess(context: Context) {
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    // Comprobar si ya se tienen los permisos para cambiar la configuración del modo "No molestar"
    if (!notificationManager.isNotificationPolicyAccessGranted) {
        // Abrir la pantalla de ajustes para permitir el acceso a la configuración del modo "No molestar"
        val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
        context.startActivity(intent)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginViewPreview() {
    OkeyPay2Theme {
        LoginScreen()
    }
}