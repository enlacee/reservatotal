package com.anibalcopitan.reservatotal

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.anibalcopitan.reservatotal.data.phonenumberregistration.SharedPreferencesManager
import com.anibalcopitan.reservatotal.ui.theme.OkeyPay2Theme

class DashboardActivity : ComponentActivity() {
    private lateinit var sharedPreferencesManager: SharedPreferencesManager
    private lateinit var counterText: MutableState<Int>
    private var showDialog by mutableStateOf(false)
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == MyReceiverBroadcast.ID_SHOW_DIALOG) {
                showDialog = true
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferencesManager = SharedPreferencesManager(this@DashboardActivity)
        val n = sharedPreferencesManager.getCounter()

        // Registrar el brocast reciber
        val filter = IntentFilter(MyReceiverBroadcast.ID_SHOW_DIALOG)
        registerReceiver(receiver, filter)
        //
        setContent {
            OkeyPay2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DashboardScreenView(n)

                    if (showDialog) {
                        // Muestra el diálogo cuando showDialog es true
                        AlertDialog(
                            onDismissRequest = {
                                showDialog = false
                            },
                            title = { Text("Actualización de tu suscripción disponible") },
                            text = { Text("Has superado las 10 transacciones diarias permitidas en tu suscripción actual. Para actualizar tu suscripción, por favor, contáctanos a través de WhatsApp.") },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        showDialog = false
                                    }
                                ) {
                                    Text("Entiendo")
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        // Asegúrate de desregistrar el BroadcastReceiver cuando la actividad se destruye
        unregisterReceiver(receiver)
        super.onDestroy()
    }
}

@Composable
fun DashboardScreenView(counter: Int) {
    DashboardScreen(counter)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    OkeyPay2Theme {
        DashboardScreenView(0)
    }
}