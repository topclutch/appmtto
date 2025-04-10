package com.example.upp_app.ui.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.upp_app.R
import com.example.upp_app.ui.theme.DarkTextColor

@Composable
fun SocialLoginRow(
    modifier: Modifier = Modifier,
    onAppleClick: () -> Unit,
    onGoogleClick: () -> Unit,
    onFacebookClick: () -> Unit
) {
    androidx.compose.foundation.layout.Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Or sign in with",
            color = DarkTextColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SocialButton(
                iconResId = R.drawable.ic_apple,
                contentDescription = "Sign in with Apple",
                onClick = onAppleClick
            )

            Spacer(modifier = Modifier.width(24.dp))

            SocialButton(
                iconResId = R.drawable.ic_google,
                contentDescription = "Sign in with Google",
                onClick = onGoogleClick
            )

            Spacer(modifier = Modifier.width(24.dp))

            SocialButton(
                iconResId = R.drawable.ic_facebook,
                contentDescription = "Sign in with Facebook",
                onClick = onFacebookClick
            )
        }
    }
}

@Composable
fun SocialButton(
    iconResId: Int,
    contentDescription: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(Color.White)
            .border(1.dp, Color.LightGray, CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = iconResId),
            contentDescription = contentDescription,
            modifier = Modifier.size(24.dp)
        )
    }
}
