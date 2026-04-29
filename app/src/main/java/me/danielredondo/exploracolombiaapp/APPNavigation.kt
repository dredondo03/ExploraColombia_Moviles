package me.danielredondo.exploracolombiaapp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import me.danielredondo.exploracolombiaapp.LoginScreen
import me.danielredondo.exploracolombiaapp.ui.elements.MainScreen
import me.danielredondo.exploracolombiaapp.ui.elements.RegisterScreen
import me.danielredondo.exploracolombiaapp.ui.elements.AddPlaceScreen

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    val auth = Firebase.auth
    val startDestination = if (auth.currentUser == null) "login" else "main"

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(route = "login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate("register")
                }
            )
        }
        composable(route = "register") {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate("login")
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable(route = "main") {
            MainScreen(
                onLogout = {
                    auth.signOut()
                    navController.navigate("login") {
                        popUpTo("main") { inclusive = true }
                    }
                },
                onNavigateToAddPlace = {
                    navController.navigate("add_place")
                }
            )
        }
        composable(route = "add_place") {
            AddPlaceScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
