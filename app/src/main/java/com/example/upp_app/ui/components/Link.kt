package com.example.upp_app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun Link(
    text: String = "Link",
    modifier: Modifier = Modifier,
    onClick: () -> Unit // Agrega un evento de clic
) {
    Text(
        text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline
                )
            ) {
                append(text)
            }
        },
        modifier = modifier.clickable { onClick() } // Hace el texto clickeable
    )
}

@Preview
@Composable
fun PreviewLink() {
    Link(text = "Ir a Registro", onClick = { /* Acción de prueba */ })
}