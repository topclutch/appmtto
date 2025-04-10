package com.example.upp_app.ui.screen.home


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.upp_app.ui.components.UserProfileMenu
import com.example.upp_app.ui.theme.InfoColor
import com.example.upp_app.ui.theme.NeutralPrimary
import com.example.upp_app.ui.theme.SuccessColor
import com.example.upp_app.ui.theme.WarningColor
import com.example.upp_app.ui.utils.localizedString
import com.example.upp_app.api.AuthManager
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.offset

import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.foundation.Canvas
import androidx.compose.ui.platform.LocalContext

data class MenuItem(
    val id: String,
    val title: String,
    val icon: Int,
    val route: String,
    val backgroundColor: Color = NeutralPrimary
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val menuItems = listOf(
        MenuItem(
            id = "chatbot",
            title = localizedString("chatbot"),
            icon = R.drawable.ic_chat,
            route = "chatbot",
            backgroundColor = InfoColor
        ),
        MenuItem(
            id = "profile",
            title = localizedString("profile"),
            icon = R.drawable.ic_person,
            route = "profile",
            backgroundColor = SuccessColor
        ),
        MenuItem(
            id = "settings",
            title = localizedString("settings"),
            icon = R.drawable.ic_settings,
            route = "settings",
            backgroundColor = NeutralPrimary
        ),
        MenuItem(
            id = "logout",
            title = localizedString("logout"),
            icon = R.drawable.ic_logout,
            route = "login",
            backgroundColor = WarningColor
        )
    )

    val context = LocalContext.current
    val authManager = remember { AuthManager(context) }
    val userName by remember { mutableStateOf(authManager.getUserName() ?: "User") }

    // Greeting text according to time
    val greeting = remember {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        when (hour) {
            in 5..11 -> "Good morning"
            in 12..17 -> "Good afternoon"
            in 18..23 -> "Good evening"
            else -> "Welcome"
        }
    }

    val calendar = Calendar.getInstance()
    val currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
    val dateFormat = SimpleDateFormat("dd", Locale.getDefault())
    val dayFormat = SimpleDateFormat("EEE", Locale.getDefault())

    val weekDays = remember {
        val days = mutableListOf<Pair<String, String>>()
        val cal = Calendar.getInstance()
        cal.firstDayOfWeek = Calendar.SUNDAY
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)

        for (i in 0..6) {
            val date = cal.time
            days.add(Pair(dayFormat.format(date), dateFormat.format(date)))
            cal.add(Calendar.DAY_OF_MONTH, 1)
        }
        days
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                currentRoute = "home",
                onItemSelected = { route ->
                    when (route) {
                        "home" -> {}
                        "explore" -> navController.navigate("chatbot")
                        "journey" -> navController.navigate("profile")
                        "trends" -> navController.navigate("settings")
                    }
                },
                onFabClicked = {
                    navController.navigate("chatbot")
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Top bar with icon, counter, and profile
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_wellcome),
                    contentDescription = "App Icon",
                    tint = Color.LightGray,
                    modifier = Modifier.size(24.dp)
                )

                Text(
                    text = "0",
                    color = Color.LightGray,
                    fontSize = 14.sp
                )

                UserProfileMenu(
                    navController = navController,
                    authManager = authManager,
                    onSignedOut = {
                        authManager.logout()
                        navController.navigate("login") {
                            popUpTo("home") { inclusive = true }
                        }
                    }
                )
            }

            // Greeting
            Text(
                text = "$greeting, $userName.",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
            )

            // Week days row
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 24.dp)
            ) {
                items(weekDays) { (day, date) ->
                    val isToday = weekDays.indexOf(Pair(day, date)) == currentDayOfWeek - 1
                    DayItem(
                        day = day,
                        date = date,
                        isSelected = isToday
                    )
                }
            }

            Divider()

            // Current day label
            Text(
                text = dayFormat.format(Date()).uppercase(),
                fontSize = 12.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            )

            // Activity cards
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
            ) {
                Text(
                    text = "Start day 1 of your plan",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE0CFFF)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_person),
                                contentDescription = null,
                                tint = Color(0xFF8B5CF6),
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 16.dp)
                        ) {
                            Text(
                                text = "Your Insights",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF4CAF50))
                                )

                                Spacer(modifier = Modifier.width(4.dp))

                                Text(
                                    text = "Stress Management",
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                            }
                        }

                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_right),
                            contentDescription = "View Insights",
                            tint = Color.Gray,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

                // Daily tracker card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF3F0FF)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Background with gradient and clouds
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            Color(0xFFF3F0FF),
                                            Color(0xFFEFF6FF)
                                        )
                                    )
                                )
                        )

                        // Decorative clouds
                        CloudShape(
                            modifier = Modifier
                                .size(80.dp)
                                .align(Alignment.TopEnd)
                                .offset(x = (-20).dp, y = 20.dp)
                        )

                        CloudShape(
                            modifier = Modifier
                                .size(60.dp)
                                .align(Alignment.TopStart)
                                .offset(x = 20.dp, y = 40.dp)
                        )

                        CloudShape(
                            modifier = Modifier
                                .size(70.dp)
                                .align(Alignment.CenterStart)
                                .offset(x = (-10).dp, y = (-30).dp)
                        )

                        // Clock icon in top right
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(16.dp)
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.8f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_clock),
                                contentDescription = "Time",
                                tint = Color(0xFF8B5CF6),
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        // Content
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Daily tracker label
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color.White.copy(alpha = 0.6f))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_sun),
                                    contentDescription = null,
                                    tint = Color(0xFF8B5CF6),
                                    modifier = Modifier.size(16.dp)
                                )

                                Spacer(modifier = Modifier.width(4.dp))

                                Text(
                                    text = "Daily Tracker",
                                    fontSize = 14.sp,
                                    color = Color(0xFF8B5CF6),
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Question
                            Text(
                                text = "How are you feeling today?",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            // Mood options - first row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                MoodOption(
                                    emoji = "😊",
                                    label = "Great",
                                    backgroundColor = Color(0xFFD1FAE5)
                                )

                                MoodOption(
                                    emoji = "🙂",
                                    label = "Good",
                                    backgroundColor = Color(0xFFE0F2FE)
                                )

                                MoodOption(
                                    emoji = "😐",
                                    label = "Okay",
                                    backgroundColor = Color(0xFFEDE9FE)
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Mood options - second row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                MoodOption(
                                    emoji = "😕",
                                    label = "Not Great",
                                    backgroundColor = Color(0xFFFEF3C7)
                                )

                                Spacer(modifier = Modifier.width(24.dp))

                                MoodOption(
                                    emoji = "😢",
                                    label = "Bad",
                                    backgroundColor = Color(0xFFFEE2E2)
                                )
                            }
                        }
                    }
                }
            }

            // Favorites section
            Text(
                text = "FAVORITES",
                fontSize = 12.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            )

            // Add some space at the bottom to ensure content isn't hidden by the bottom navigation
            Spacer(modifier = Modifier.height(80.dp))
        }
    }


@Composable
fun BottomNavigationBar(
    currentRoute: String,
    onItemSelected: (String) -> Unit,
    onFabClicked: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Home
            BottomNavItem(
                icon = R.drawable.ic_home,
                label = "Home",
                isSelected = currentRoute == "home",
                onClick = { onItemSelected("home") }
            )

            // Chat
            BottomNavItem(
                icon = R.drawable.ic_chat,
                label = "Chat",
                isSelected = currentRoute == "explore",
                onClick = { onItemSelected("explore") }
            )

            // FAB in the center
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color.Black)
                    .clickable { onFabClicked() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = "Add",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Profile
            BottomNavItem(
                icon = R.drawable.ic_person,
                label = "Profile",
                isSelected = currentRoute == "journey",
                onClick = { onItemSelected("journey") }
            )

            // Settings
            BottomNavItem(
                icon = R.drawable.ic_settings,
                label = "Settings",
                isSelected = currentRoute == "trends",
                onClick = { onItemSelected("trends") }
            )
        }
    }
}

@Composable
fun BottomNavItem(
    icon: Int,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = label,
            tint = if (isSelected) Color.Black else Color.Gray,
            modifier = Modifier.size(24.dp)
        )

        Text(
            text = label,
            fontSize = 12.sp,
            color = if (isSelected) Color.Black else Color.Gray
        )
    }
}

@Composable
fun DayItem(
    day: String,
    date: String,
    isSelected: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = day,
            fontSize = 12.sp,
            color = if (isSelected) Color.Black else Color.Gray
        )

        Text(
            text = date,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) Color.Black else Color.Gray
        )
    }
}

@Composable
fun Divider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Color(0xFFEEEEEE))
    )
}

@Composable
fun MoodOption(
    emoji: String,
    label: String,
    backgroundColor: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(backgroundColor)
                .clickable { /* Handle mood selection */ },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = emoji,
                fontSize = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Black
        )
    }
}

@Composable
fun CloudShape(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        val path = Path().apply {
            // Draw a cloud shape
            moveTo(width * 0.2f, height * 0.5f)
            cubicTo(
                width * 0.1f, height * 0.4f,
                width * 0.15f, height * 0.2f,
                width * 0.3f, height * 0.2f
            )
            cubicTo(
                width * 0.3f, height * 0.1f,
                width * 0.7f, height * 0.1f,
                width * 0.7f, height * 0.2f
            )
            cubicTo(
                width * 0.85f, height * 0.2f,
                width * 0.9f, height * 0.4f,
                width * 0.8f, height * 0.5f
            )
            cubicTo(
                width * 0.9f, height * 0.6f,
                width * 0.8f, height * 0.8f,
                width * 0.6f, height * 0.8f
            )
            cubicTo(
                width * 0.5f, height * 0.9f,
                width * 0.3f, height * 0.9f,
                width * 0.2f, height * 0.8f
            )
            cubicTo(
                width * 0.1f, height * 0.8f,
                width * 0.1f, height * 0.6f,
                width * 0.2f, height * 0.5f
            )
            close()
        }

        drawPath(
            path = path,
            color = Color.White.copy(alpha = 0.7f),
            style = Fill
        )
    }
}