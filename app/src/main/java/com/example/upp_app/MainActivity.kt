package com.example.upp_app


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.upp_app.api.AuthManager
import com.example.upp_app.ui.screen.chatbot.ChatbotScreen
import com.example.upp_app.ui.screen.home.HomeScreen
import com.example.upp_app.ui.screen.login.LoginScreen
import com.example.upp_app.ui.screen.profile.ProfileScreen
import com.example.upp_app.ui.screen.register.RegisterScreen
import com.example.upp_app.ui.screen.settings.SettingsScreen
//import com.example.upp_app.ui.screen.splash.SplashScreen
import com.example.upp_app.ui.screen.verify.VerifyScreen
import com.example.upp_app.ui.screen.welcome.WelcomeScreen
import com.example.upp_app.ui.theme.ThemeManager
import com.example.upp_app.ui.theme.Upp_appTheme
import com.example.upp_app.ui.utils.ProvideActivity
import androidx.fragment.app.FragmentActivity

class MainActivity :  FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            //val context = LocalContext.current
            //val authManager = remember { AuthManager(context) }
            ProvideActivity(this) {
            Upp_appTheme {

                    val navController = rememberNavController()
                    val context = LocalContext.current
                    val authManager = remember { AuthManager(context) }
                    NavHost(navController = navController, startDestination = "welcome") {
                        //composable("splash") {
                        //SplashScreen(navController = navController)
                        //}
                        composable("welcome") {
                            WelcomeScreen(navController = navController)
                        }
                        composable("login") {
                            LoginScreen(
                                navController = navController,
                                authManager = authManager
                            )

                        }
                        composable("register") {
                            RegisterScreen(navController = navController)
                        }
                        composable("verify") {
                            VerifyScreen(navController = navController)
                        }
                        composable("home") {
                            HomeScreen(navController = navController)
                        }
                        composable("chatbot") {
                            ChatbotScreen(navController = navController)
                        }
                        composable("profile") {
                            ProfileScreen(navController = navController)
                        }
                        composable("settings") {
                            SettingsScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun AppPreview() {
    Upp_appTheme {
        ProfileScreen(navController = rememberNavController())
    }
}


