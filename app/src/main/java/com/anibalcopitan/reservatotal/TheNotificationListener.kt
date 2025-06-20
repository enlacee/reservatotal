package com.anibalcopitan.reservatotal

import android.app.Notification
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class TheNotificationListener : NotificationListenerService() {
    private val broadcastReceiver = MyReceiverBroadcast()

    /**
     * Crea al `broadcastReceiver` que escuchara despues
     * y sera llamado con el INTENT
     */
    override fun onCreate() {
        super.onCreate()
        val intentFilter = IntentFilter(MyReceiverBroadcast.ID_ACTION)
        registerReceiver(broadcastReceiver, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReceiver)
    }

    override fun onBind(intent: Intent): IBinder? {
        return super.onBind(intent)
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        Log.i("TheNotificationListener", "Connected");
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)

        //if (sbn.packageName.equals("com.whatsapp")) {
        if (sbn.packageName.equals("com.bcp.innovacxion.yapeapp")) {
            Log.i("debug", "== com.whatsapp  Yape! [fuera] == ");
            val message = sbn.notification?.extras?.getString(Notification.EXTRA_TEXT)
            if (
                !message.isNullOrEmpty() &&
                message.contains("te envió")
            ) {
                Log.i("debug", " == com.whatsapp  Yape! ==");
                // Llamar al BroadcastReceiver para manejar la notificación
                val intent = Intent(MyReceiverBroadcast.ID_ACTION)
                intent.putExtra("message", message)
                sendBroadcast(intent)
            }
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        // This method will be called whenever a notification is removed
        // Process the removed notification here
    }
}