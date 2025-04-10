package com.example.upp_app.ui.screen.verify
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.upp_app.R
import com.example.upp_app.ui.components.Link
import com.example.upp_app.ui.components.OtpInputField
import com.example.upp_app.ui.components.PrimaryButton
import com.example.upp_app.ui.theme.Background
import com.example.upp_app.ui.theme.MediumTextColor
import com.example.upp_app.ui.theme.NeutralAccent

@Composable
fun VerifyScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
            .systemBarsPadding()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Botón de retroceso
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.Start)
                .padding(top = 8.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_back),
                contentDescription = "Back",
                tint = NeutralAccent
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Título
        Text(
            text = "Verify Code",
            style = MaterialTheme.typography.headlineMedium,
            color = NeutralAccent
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Descripción
        Text(
            text = "Please enter the code we sent to email example@gmail.com",
            style = MaterialTheme.typography.bodyMedium,
            color = MediumTextColor,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Campo para ingresar el código OTP
        OtpInputField(
            otpLength = 4,
            onOtpComplete = { /* Implementar verificación del código */ }
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Enlace para reenviar el código
        Text(
            text = "Didn't receive OTP?",
            style = MaterialTheme.typography.bodyMedium,
            color = MediumTextColor
        )

        Spacer(modifier = Modifier.height(8.dp))

        Link(text = "Resend code") { /* Implementar reenvío del código */ }

        Spacer(modifier = Modifier.height(48.dp))

        // Botón para verificar
        PrimaryButton(
            onClick = { navController.navigate("home") },
            text = "Verify",
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}
