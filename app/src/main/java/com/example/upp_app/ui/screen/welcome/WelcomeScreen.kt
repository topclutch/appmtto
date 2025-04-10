package com.example.upp_app.ui.screen.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.upp_app.R
import com.example.upp_app.ui.theme.NeutralPrimary
import com.example.upp_app.ui.utils.localizedString

@Composable
fun WelcomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    // Create a gradient background
    val gradientColors = remember {
        listOf(
            Color(0xFFE0F7FA),
            Color(0xFFF3E5F5),
            Color(0xFFE8F5E9)
        )
    }

    // Define feature icons in a circle
    val featureIcons = listOf(
        R.drawable.ic_chat to Color(0xFFFFA07A),
        R.drawable.ic_person to Color(0xFFBA68C8),
        R.drawable.ic_settings to Color(0xFFFFEB3B),
        R.drawable.ic_calendar to Color(0xFF81C784),
        R.drawable.ic_notifications to Color(0xFF64B5F6),
        R.drawable.ic_language to Color(0xFFFFB74D),
        R.drawable.ic_security to Color(0xFFE57373),
        R.drawable.ic_feedback to Color(0xFF9575CD)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = gradientColors
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Circle of feature icons
            Box(
                modifier = Modifier
                    .size(280.dp)
                    .padding(bottom = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                // Center star icon
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.app_icon),
                        contentDescription = "App Logo",
                        modifier = Modifier.size(40.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                // Circle of icons
                featureIcons.forEachIndexed { index, (icon, color) ->
                    val angle = (index * (360f / featureIcons.size)).toDouble()
                    val radius = 120.dp
                    val x = (radius.value * kotlin.math.cos(Math.toRadians(angle))).toFloat()
                    val y = (radius.value * kotlin.math.sin(Math.toRadians(angle))).toFloat()

                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(color)
                            .align(Alignment.Center)
                            .offset(x = x.dp, y = y.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = icon),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // App name
            Text(
                text = "Salud Mental",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray,
                textAlign = TextAlign.Center
            )

            // Tagline with colored text
            //Row(
                //modifier = Modifier.padding(vertical = 8.dp),
                //horizontalArrangement = Arrangement.Center
            //) {
                //Text(
                    //text = "Delightful ",
                    //fontSize = 20.sp,
                    //fontWeight = FontWeight.Medium,
                    //color = Color.DarkGray
                //)
                //Text(
                    //text = "AI Assistant",
                    //fontSize = 20.sp,
                    //fontWeight = FontWeight.Medium,
                    //color = NeutralPrimary
                //)
            //}

            Spacer(modifier = Modifier.height(8.dp))

            // Start here text
            Text(
                text = "Start Here",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF5C6BC0),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Get Started button
            Button(
                onClick = { navController.navigate("login") },
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 32.dp)
            ) {
                Text(
                    text = "Start Here",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

