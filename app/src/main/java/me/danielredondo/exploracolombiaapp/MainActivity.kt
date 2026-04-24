package me.danielredondo.exploracolombiaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import me.danielredondo.exploracolombiaapp.ui.theme.ExploraColombiaAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val auth = Firebase.auth
        // forzar el cierre de inicio de sesiones
        auth.signOut()
        
        val currentUser = auth.currentUser
        val startRoute = if (currentUser != null) "main" else "login"

        enableEdgeToEdge()
        setContent {
            ExploraColombiaAppTheme {
                val myNavController = rememberNavController()

                NavHost(
                    navController = myNavController,
                    startDestination = startRoute,
                    modifier = Modifier.fillMaxSize()
                ) {
                    composable(route = "login") {
                        LoginScreen(
                            onLoginSuccess = {
                                myNavController.navigate("main") {
                                    popUpTo("login") { inclusive = true }
                                }
                            },
                            onNavigateToRegister = {
                                myNavController.navigate("register")
                            }
                        )
                    }
                    composable(route = "register") {
                        RegisterScreen(
                            onRegisterSuccess = {
                                myNavController.navigate("main") {
                                    popUpTo("register") { inclusive = true }
                                    popUpTo("login") { inclusive = true }
                                }
                            },
                            onNavigateToLogin = {
                                myNavController.navigate("login")
                            },
                            onBackClick = {
                                myNavController.popBackStack()
                            }
                        )
                    }
                    composable(route = "main") {
                        MainScreen()
                    }
                }
            }
        }
    }
}
