
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import android.net.Uri
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

// Powered by Anibal Copitan | Soporte: reservatotal.anibalcopitan.com o WhatsApp.
@Composable
fun FooterText(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Spacer(modifier = Modifier.height(10.dp))
    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
        Text(text = "Creado por: Anibal Copitan", fontSize = 10.sp, color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://anibalcopitan.com"))
                context.startActivity(intent)
            }
        )
    }
    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
        Text(text = "Soporte web",
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable {
                // Crea el Intent para abrir el navegador
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://reservatotal.anibalcopitan.com"))
                context.startActivity(intent) // Lanza el Intent
            }
        )
        Text(text = " | ", fontSize = 10.sp)
        Text(text = "+51970142637", fontSize = 10.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable {
                // Crea el Intent para abrir el navegador
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/+51970142637"))
                context.startActivity(intent) // Lanza el Intent
            })
    }
}
