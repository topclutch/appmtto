package com.example.upp_app.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.upp_app.R
import com.example.upp_app.ui.theme.NeutralPrimary
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.text.font.FontWeight
@Composable
fun PrimaryButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    isNavigationArrowVisible: Boolean = false,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = Color.Black, // Cambiado a negro
        contentColor = Color.White // Para que el texto siga siendo visible
    ),
    shadowColor: Color = Color.Transparent,
    cornerRadius: Int = 12,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(cornerRadius.dp),
        colors = colors,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        enabled = enabled
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
        )
        if (isNavigationArrowVisible) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_right),
                contentDescription = "Navigate",
                modifier = Modifier.padding(start = 8.dp),
                tint = Color.White // Asegurar que el icono sea visible en el fondo negro
            )
        }
    }
}
