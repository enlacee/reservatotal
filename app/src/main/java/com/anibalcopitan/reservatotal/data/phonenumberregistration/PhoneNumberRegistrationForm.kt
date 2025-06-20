package com.anibalcopitan.reservatotal.data.phonenumberregistration


import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.anibalcopitan.reservatotal.MyReceiverBroadcast
import com.anibalcopitan.reservatotal.ui.theme.Shapes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneNumberRegistrationForm(sharedPreferencesManager: SharedPreferencesManager) {

    val formData = sharedPreferencesManager.getFormData()
    var contactNumber1 by remember { mutableStateOf(formData.phoneNumber1) }
    var contactNumber2 by remember { mutableStateOf(formData.phoneNumber2) }
    val context = LocalContext.current // Get the current context

    /*
    * Validation input phone
    * */
    fun isValidNumber(number: String): Boolean {
        // return false
        return number.length > 8 && number.length <= 12
    }

    Column {
        /*
        Text(
            text = "Contactos a notificar mediante SMS",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        OutlinedTextField(
            value = contactNumber1,
            onValueChange = { contactNumber1 = it },
            label = { Text("Celular 1") },
            modifier = Modifier.fillMaxWidth(),
        )

        OutlinedTextField(
            value = contactNumber2,
            onValueChange = { contactNumber2 = it },
            label = { Text("Celular 2") },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                sharedPreferencesManager.saveFormData(contactNumber1, contactNumber2)
                Toast.makeText(context, "Datos guardados!", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 1.dp),
            shape = Shapes.large
        ) {
            Text(text = "GUARDAR")
        }

        // Adding Button test
        Spacer(modifier = Modifier.height(18.dp))
        */
        buttonTest(sharedPreferencesManager)
    }
}

@Composable
fun buttonTest(sharedPreferencesManager: SharedPreferencesManager) {
    // button test
    val formData = sharedPreferencesManager.getFormDataFlag()
    var flag by remember { mutableStateOf(formData.flag) }
    val context = LocalContext.current // Get the current context

    if (flag) {
        Button(
            onClick = {

            /**
             * Comentando permissos de SMS v10
             */
            /*
                // 01. pedir permisos de SMS
                if (ContextCompat.checkSelfPermission(
                        context as Activity,
                        android.Manifest.permission.SEND_SMS
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    // Permission already granted, handle sending SMS
                } else {
                    // Permission not granted, request it
                    ActivityCompat.requestPermissions(
                        context as Activity,
                        arrayOf(android.Manifest.permission.SEND_SMS),
                        1123456789
                    )
                }
            */

                // 02. show modal
                val alertDialogBuilder = android.app.AlertDialog.Builder(context)
                var message: String = "Yape! PEPE LUCHO RIOS PRUEBA te envió un pago por S/ 1"
                alertDialogBuilder.setTitle("Notificación de prueba")
                alertDialogBuilder.setMessage("Esta es una notificación de prueba. " + message)
                alertDialogBuilder.setPositiveButton("Sí") { dialog, which ->
                    // Aquí puedes colocar el código para realizar la operación.
                    realizeOperationX(context, message)
                }
                alertDialogBuilder.show()
                sharedPreferencesManager.saveFormDataFlag(false)
                flag = false
            },
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 1.dp),
            shape = Shapes.large,
        ) {
            Text(text = "REALIZAR UNA PRUEBA DE PAGO YAPE")
        }
    }
}

/*
* Call to broadcast task (notification)
* */
fun realizeOperationX(context: Context, message: String) {
    val intent = Intent(MyReceiverBroadcast.ID_ACTION)
    intent.putExtra(MyReceiverBroadcast.KEY_NAME_MESSAGE, message)
    context.sendBroadcast(intent)
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    val context = LocalContext.current
    val sharedPreferencesManager = SharedPreferencesManager(context)
    PhoneNumberRegistrationForm(sharedPreferencesManager)
}