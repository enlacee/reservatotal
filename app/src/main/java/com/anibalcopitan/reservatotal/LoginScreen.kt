package com.anibalcopitan.reservatotal

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.anibalcopitan.reservatotal.data.appconfig.AppConfigClass
import com.anibalcopitan.reservatotal.data.phonenumberregistration.SharedPreferencesManager

import com.anibalcopitan.reservatotal.ui.theme.Shapes
import com.anibalcopitan.reservatotal.ui.theme.OkeyPay2Theme
import org.json.JSONObject
import FooterText

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenScreenPreview() {
    OkeyPay2Theme {
        LoginScreen()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen() {
    Log.i("BOTON-REGISTRAR", "=== START LoginScreen === " )
    val context = LocalContext.current
    val sharedPreferencesManager = SharedPreferencesManager(context)
    val openDialog = remember { mutableStateOf(false) }
    var credentials by remember { mutableStateOf(
        Credentials(
            sharedPreferencesManager.getString(SharedPreferencesManager.KEY_USERNAME, ""),
            sharedPreferencesManager.getString(SharedPreferencesManager.KEY_PASSWORD, "")
        )
    ) }
    val focusManager = LocalFocusManager.current
    var isProcessing by remember { mutableStateOf(false) }

    Log.i("BOTON-REGISTRAR", "[usernamexxx ] " + sharedPreferencesManager.getString(SharedPreferencesManager.KEY_USERNAME, "") )
    Log.i("BOTON-REGISTRAR", "[passxxx ] " + sharedPreferencesManager.getString(SharedPreferencesManager.KEY_PASSWORD, "") )

    Column(modifier = Modifier.padding(16.dp)) {
        HeaderText()
        Spacer(modifier = Modifier.height(28.dp))
        //PhoneNumberTextField()
        OutlinedTextField(
            value = credentials.login,
            onValueChange = { data -> credentials = credentials.copy(login = data) },
            label = { Text(text = "Correo") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
        )
        //
        Spacer(modifier = Modifier.height(4.dp))
        //PasswordTextField()
        OutlinedTextField(
            value = credentials.pwd,
            onValueChange = { data -> credentials = credentials.copy(pwd = data) },
            label = { Text(text = "Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
        )
        //
        Spacer(modifier = Modifier.height(24.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (isProcessing) {
                CircularProgressIndicator()
            } else {
                ButtonLogin(context) {
                    if (formValidation(credentials, context)) {
                        isProcessing = true
                        val params: MutableMap<String?, String?> = HashMap()
                        params["op"] = "login"
                        params["email"] = credentials.login
                        params["password"] = credentials.pwd

                        val parameters = JSONObject(params as Map<*, *>?)
                        var url = BuildConfig.VITE_API_BASE_URL_APP

                        val request = JsonObjectRequest(
                            Request.Method.POST,
                            url,
                            parameters,
                            { response ->
                                var theResponse = response;
                                Log.i("BOTON-REGISTRAR", "response Login OK vollley")

                                // Manejar la respuesta del servidor
                                if (theResponse.has("message")) {
                                    Toast.makeText(context, theResponse.getString("message"), Toast.LENGTH_SHORT).show()
                                }

                                // is okey
                                if (
                                    theResponse.has("status") &&
                                    theResponse.getString("status").equals("ok") &&
                                    theResponse.has("data")
                                ) {
                                    val dataObject = theResponse.getJSONObject("data")
                                    // Save Data (prefs)
                                    val appConfigClass = AppConfigClass.getInstance(context)
                                    var appConfig = appConfigClass.loadAppConfig()
                                    appConfig.username = credentials.login
                                    appConfig.password = credentials.pwd
                                    appConfig.id = dataObject.get("id")?.toString() ?: ""
                                    appConfig.name = dataObject.get("name")?.toString() ?: ""
                                    appConfig.subscriptionStartDate = dataObject.get("subscriptionStartDate")?.toString() ?: ""
                                    appConfig.subscriptionDurationDays = dataObject.get("subscriptionDurationDays")?.toString() ?: ""
                                    appConfig.subscriptionPlan = dataObject.get("subscriptionPlan")?.toString() ?: ""
                                    appConfig.googleSheetUrl = dataObject.get("googleSheetUrl")?.toString() ?: ""
                                    appConfigClass.saveAppConfig(appConfig)
                                    Log.i("BOTON-REGISTRAR", "nueva username " + appConfig.username)
                                    Log.i("BOTON-REGISTRAR", "nueva id " + appConfig.id)
                                    Log.i("BOTON-REGISTRAR", "nueva URL " + appConfig.googleSheetUrl)
                                    Log.i("BOTON-REGISTRAR ", "LOGIN: appConfig = " + appConfig.toString())
                                    // Llamar al callback para cerrar el diálogo/modal
                                    /*
                                    * Llamar a los callbacks
                                    * */
//                                    onSaveClicked(params["email"].toString(), params["password"].toString())
//                                    dialogCallback.closeDialog()
                                    openDashboardActivity(context)
                                }

                                isProcessing = false
                            },
                            { error ->
                                Log.i("BOTON-REGISTRAR", "Error: $error")
                                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()
                                isProcessing = false
                            }
                        )
                        request.retryPolicy = DefaultRetryPolicy(
                            10000, // 10 segundos espera
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                        )
                        MySingleton.getInstance(context).addToRequestQueue(request)

                        // 00. authentication login [consulta api]

                        // 01. Save Data (prefs) (Si falla igual guardar las credenciales ingresadas)
                        val appConfigClass = AppConfigClass.getInstance(context)
                        var appConfig = appConfigClass.loadAppConfig()
                        appConfig.username = credentials.login
                        appConfig.password = credentials.pwd
                        appConfigClass.saveAppConfig(appConfig)
                        Log.i("BOTON-REGISTRAR", "=== ButtonLogin === ")
                        Log.i("BOTON-REGISTRAR", "CONFIG username " + appConfig.username)
                        Log.i("BOTON-REGISTRAR", "CONFIG password " + appConfig.password)

                        // 02. abrir activity Dashboard
//                        openDashboardActivity(context)
//                        result = true
                    }

                }
            }
        }
        Spacer(modifier = Modifier.height(44.dp))
        ButtonToRegister { openDialog.value = true }
        FooterText();
    }

    // Open Modal
    if (openDialog.value) {
        Dialog(onDismissRequest = { openDialog.value = false }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(size = 0.dp)
            ){
                /*
                * Open Forn Register
                * */
                RegisterScreen(
                    context,
                    object : DialogCallback {
                        override fun closeDialog() {
                            openDialog.value = false
                            openDashboardActivity(context) // open dashboard FAST
                        }
                    },
                    onSaveClicked = { theUsername, thePassword ->
                        credentials.login = theUsername
                        credentials.pwd = thePassword
                        //Log.d("Formulario1", "Nombre: $nombre, Apellido: $apellido") // Imprimir en la consola
                    }
                )
            }
        }
    }
}

/*
* Open DashboardActivity
*
* return void
* */
fun openDashboardActivity(context: Context){
    context.startActivity(Intent(context, DashboardActivity::class.java))
    (context as Activity).finish()
}

data class Credentials(
    var login: String = "",
    var pwd: String = "",
) {
    fun isEmpty(): Boolean {
        return login.isEmpty() && pwd.isEmpty()
    }

    fun isEmailValid(): Boolean {
        var email: String = login
        val regexPattern = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+$")
        return regexPattern.matches(email)
    }

    fun isPasswordValid(): Boolean {
        return pwd.length >= 6
    }
}

/*
* validation of form
* */
fun formValidation(credentials: Credentials, context: Context): Boolean {
    if (credentials.isEmpty()) {
        Toast.makeText(context, "Llene todos los campos", Toast.LENGTH_SHORT).show()
        return false
    }

    if (!credentials.isEmailValid()) {
        Toast.makeText(context, "El correo no es válido", Toast.LENGTH_SHORT).show()
        return false
    }

    if (!credentials.isPasswordValid()) {
        Toast.makeText(context, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
        return false
    }

    return true
}

@Composable
private fun HeaderText() {
    Text(text = stringResource(id = R.string.app_name), fontWeight = FontWeight.Bold, fontSize = 32.sp)
    Spacer(modifier = Modifier.height(2.dp))
    Text(text = "Inicia sesión para continuar", fontWeight = FontWeight.Bold, fontSize = 26.sp)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PhoneNumberTextField() {
    var phoneNumber by remember { mutableStateOf("") }

    OutlinedTextField(
        value = phoneNumber,
        onValueChange = { phoneNumber = it },
        label = { Text(text = "Celular") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PasswordTextField() {
    var password by remember { mutableStateOf("") }

    OutlinedTextField(
        value = password,
        onValueChange = { password = it },
        label = { Text(text = "Contraseña") },
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
    )
}

@Composable
private fun ButtonLogin(mContext : Context, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 1.dp),
        shape = Shapes.large
    ) {
        Text("INICIAR SESIÓN")
    }
}

@Composable
private fun ButtonToRegister(onClick: () -> Unit) {
    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
        Text("¿No tienes una cuenta? ")
        Text("Regístrate ",
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.clickable(onClick = onClick)
        )
    }
}