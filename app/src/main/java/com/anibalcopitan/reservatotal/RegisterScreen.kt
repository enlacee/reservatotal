package com.anibalcopitan.reservatotal

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.anibalcopitan.reservatotal.data.appconfig.AppConfigClass
import com.anibalcopitan.reservatotal.ui.theme.OkeyPay2Theme
import com.anibalcopitan.reservatotal.ui.theme.Shapes
import org.json.JSONObject


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterScreenScreenPreview() {
    OkeyPay2Theme {
        RegisterScreen(
            LocalContext.current,
            object : DialogCallback {
                override fun closeDialog() { }
            },
            onSaveClicked = { theUsername, thePassword ->
                Log.d("theUsername", theUsername) // Imprimir en la consola
                Log.d("thePassword", thePassword) // Imprimir en la consola
            }
        )
    }
}


interface DialogCallback {
    fun closeDialog()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    context: Context,
    dialogCallback: DialogCallback,
    onSaveClicked: (String, String) -> Unit
) {
    var registerData by remember { mutableStateOf(RegisterData()) }
    var isProcessing by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        HeaderText()
        Spacer(modifier = Modifier.height(18.dp))
        OutlinedTextField(
            value = registerData.name,
            onValueChange = { data -> registerData = registerData.copy(name = data) },
            label = { Text(text = "Nombre") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = registerData.email,
            onValueChange = { data -> registerData = registerData.copy(email = data) },
            label = { Text(text = "Correo") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = registerData.password,
            onValueChange = { data -> registerData = registerData.copy(password = data) },
            label = { Text(text = "Crear Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (isProcessing) {
                CircularProgressIndicator()

            } else {
                ButtonRegister(context) {

                    if (formValidation(registerData, context)) {
                        Log.i("BOTON-REGISTRAR", "==PASO VALIDACION FORM REGISTRO==")
                        Log.i("BOTON-REGISTRAR", registerData.email)
                        isProcessing = true
                        val params: MutableMap<String?, String?> = HashMap()
                        params["op"] = "register"
                        params["email"] = registerData.email
                        params["password"] = registerData.password
                        params["name"] = registerData.name
                        // clear data
//                        registerData = RegisterData()

                        val parameters = JSONObject(params as Map<*, *>?)
                        var url = BuildConfig.VITE_API_BASE_URL_APP

                        val request = JsonObjectRequest(
                            Request.Method.POST,  // Método POST
                            url,                 // URL del servidor
                            parameters,          // Datos JSON a enviar
                            { response ->
                                var theResponse = response;
                                Log.i("BOTON-REGISTRAR", "response OK vollley")
                                isProcessing = false

                                // Manejar la respuesta del servidor
                                if (theResponse.has("message")) {
                                    Toast.makeText(context, theResponse.getString("message"), Toast.LENGTH_SHORT)
                                        .show()
                                }

                                // is okey
                                if (
                                    theResponse.has("status") &&
                                    theResponse.getString("status").equals("ok")
                                ) {
                                    // save data
                                    // val thedata = theResponse.getJSONArray("data")
                                    val dataObject = theResponse.getJSONObject("data")
                                    val id = dataObject.getString("id")

                                    // Save Data (prefs)
                                    val appConfigClass = AppConfigClass.getInstance(context)
                                    var appConfig = appConfigClass.loadAppConfig()
                                    appConfig.username = params["email"].toString()
                                    appConfig.password = params["password"].toString()
                                    //
                                    appConfig.id = dataObject.get("id")?.toString() ?: ""
                                    appConfig.name = dataObject.get("name")?.toString() ?: ""
                                    appConfig.subscriptionStartDate = dataObject.get("subscriptionStartDate")?.toString() ?: ""
                                    appConfig.subscriptionDurationDays = dataObject.get("subscriptionDurationDays")?.toString() ?: ""
                                    appConfig.subscriptionPlan = dataObject.get("subscriptionPlan")?.toString() ?: ""
                                    appConfig.googleSheetUrl = dataObject.get("googleSheetUrl")?.toString() ?: ""
                                    //
                                    appConfigClass.saveAppConfig(appConfig)


                                    //Log.i("BOTON-REGISTRAR", thedata.getString("email"))
//                                    Log.i("BOTON-REGISTRAR thedata pass", "pass")
//                                    Log.i("BOTON-REGISTRAR thedata id= ", id)
//                                    var theemail = params["email"];
//                                    Log.i("BOTON-REGISTRAR thedata params=email ", theemail.toString())
//                                    Log.i("BOTON-REGISTRAR ", "REGISTER: appConfig = " + appConfig.toString())


                                    // Llamar al callback para cerrar el diálogo/modal
                                    /*
                                    * Llamar a los callbacks
                                    * */
                                    onSaveClicked(params["email"].toString(), params["password"].toString())
                                    dialogCallback.closeDialog()


                                    /*
                                    * - aqui guardar data en temporal android , para usarlo despues
                                    * en toda la APP
                                    *
                                    * - Guardar la contrasenia para usarlo localmente.
                                    * - SI SE INSTALA ya si necesitaras las credenciales
                                    * */
                                }
                            },
                            { error ->
                                Log.i("BOTON-REGISTRAR", "Error: $error")
                                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()
                                isProcessing = false
                            }
                        )
                        request.retryPolicy = DefaultRetryPolicy(
                            20000, // 20 segundos espera
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                        )

                        // Agrega la solicitud a la cola de solicitudes de Volley
                        // Volley.newRequestQueue(context).add(request)
                        MySingleton.getInstance(context).addToRequestQueue(request)

                        Log.i("BOTON-REGISTRAR", "Enpezo a correr VOLLEY")

                        // Limpiar datos del formulario
                        // registerData = RegisterData()
                    }
                }
            }

        }
    }





}


/*
* validation of form
* */
fun formValidation(registerData: RegisterData, context: Context): Boolean {
    if (registerData.isEmpty()) {
        Toast.makeText(context, "Llene todos los campos", Toast.LENGTH_SHORT).show()
        return false
    }

    if (!registerData.isEmailValid()) {
        Toast.makeText(context, "El correo no es válido", Toast.LENGTH_SHORT).show()
        return false
    }

    if (!registerData.isPasswordValid()) {
        Toast.makeText(context, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
        return false
    }

    return true
}

@Composable
private fun HeaderText() {
//    Text(text = "Bienvenid@,", fontWeight = FontWeight.Bold, fontSize = 32.sp)
//    Spacer(modifier = Modifier.height(2.dp))
    Text(text = "Regístrese para crear una cuenta", fontWeight = FontWeight.Bold, fontSize = 18.sp)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UsernameTextField() {
    var username by remember { mutableStateOf("") }

    OutlinedTextField(
        value = username,
        onValueChange = { username = it },
        label = { Text(text = "Nombre") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EmailTextField() {
    var email by remember { mutableStateOf("") }

    OutlinedTextField(
        value = email,
        onValueChange = { email = it },
        label = { Text(text = "Correo") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PasswordTextField() {
    var password by remember { mutableStateOf("") }

    OutlinedTextField(
        value = password,
        onValueChange = { password = it },
        label = { Text(text = "Crear Contraseña") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
    )
}

@Composable
private fun ButtonRegister(context : Context, onClick: () -> Unit) {
    Button(
//        onClick = { /*TODO*/ },
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 1.dp),
        shape = Shapes.large
    ) {
        Text("REGISTRAR")
    }
}


data class RegisterData(
    var name: String = "", // "android"
    var email: String = "", // android@pprios.com
    var password: String = "", // android
) {
    fun isEmpty(): Boolean {
        return name.isEmpty() && email.isEmpty() && password.isEmpty()
    }

    fun isEmailValid(): Boolean {
        var email: String = email
        val regexPattern = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+$")
        return regexPattern.matches(email)
    }

    fun isPasswordValid(): Boolean {
        return password.length >= 6
    }
}
