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
import me.danielredondo.exploracolombiaapp.ui.theme.ExploraColombiaAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExploraColombiaAppTheme {
                val myNavController = rememberNavController()

                NavHost(
                    navController = myNavController,
                    startDestination = "login",
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
