package me.danielredondo.exploracolombiaapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Home : Screen("home", "Inicio", Icons.Default.Home)
    object Explore : Screen("explore", "Explorar", Icons.Default.Search)
    object Favorites : Screen("favorites", "Favoritos", Icons.Default.Favorite)
    object Profile : Screen("profile", "Perfil", Icons.Default.Person)
}

@Composable
fun MainScreen(onLogout: () -> Unit) {
    val navController = rememberNavController()
    val items = listOf(
        Screen.Home,
        Screen.Explore,
        Screen.Favorites,
        Screen.Profile
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(screen.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFFE45D25),
                            selectedTextColor = Color(0xFFE45D25),
                            indicatorColor = Color(0xFFE45D25).copy(alpha = 0.1f)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { 
                val user = Firebase.auth.currentUser
                val userName = user?.displayName ?: "Usuario"
                PlaceholderScreen("Bienvenido, $userName") 
            }
            composable(Screen.Explore.route) { PlaceholderScreen("Pantalla de Explorar") }
            composable(Screen.Favorites.route) { PlaceholderScreen("Pantalla de Favoritos") }
            composable(Screen.Profile.route) { 
                ProfileScreen(onLogout = onLogout)
            }
        }
    }
}

@Composable
fun ProfileScreen(onLogout: () -> Unit) {
    val user = Firebase.auth.currentUser
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Perfil de Usuario", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Email: ${user?.email ?: "N/A"}", color = Color.Gray)
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = onLogout,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE45D25))
        ) {
            Text("Cerrar Sesión")
        }
    }
}

@Composable
fun PlaceholderScreen(text: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, style = MaterialTheme.typography.headlineMedium)
    }
}
