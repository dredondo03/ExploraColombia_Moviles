package me.danielredondo.exploracolombiaapp

package me.danielredondo.exploracolombiaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import me.danielredondo.exploracolombiaapp.ui.elements.LoginScreen
import me.danielredondo.exploracolombiaapp.ui.elements.MainScreen
import me.danielredondo.exploracolombiaapp.ui.elements.RegisterScreen
import me.danielredondo.exploracolombiaapp.ui.theme.ExploraColombiaAppTheme

// Definición de rutas como objetos serializables (Type Safety)
@Serializable object LoginRoute
@Serializable object RegisterRoute
@Serializable object MainRoute

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExploraColombiaAppTheme {
                // El NavController gestiona el estado de la navegación
                val myNavController = rememberNavController()

                NavHost(
                    navController = myNavController,
                    startDestination = LoginRoute, // Referencia al objeto
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Pantalla de Login
                    composable<LoginRoute> {
                        LoginScreen(
                            onLoginSuccess = {
                                // Navega a Main y limpia el historial para que no pueda volver al Login
                                myNavController.navigate(MainRoute) {
                                    popUpTo(LoginRoute) { inclusive = true }
                                }
                            },
                            onNavigateToRegister = {
                                myNavController.navigate(RegisterRoute)
                            }
                        )
                    }

                    // Pantalla de Registro
                    composable<RegisterRoute> {
                        RegisterScreen(
                            onRegisterSuccess = {
                                myNavController.navigate(MainRoute) {
                                    popUpTo(LoginRoute) { inclusive = true }
                                }
                            },
                            onNavigateToLogin = {
                                // Usamos popBackStack si ya venimos del Login para evitar duplicar pantallas
                                if (!myNavController.popBackStack()) {
                                    myNavController.navigate(LoginRoute)
                                }
                            },
                            onBackClick = {
                                myNavController.popBackStack()
                            }
                        )
                    }

                    // Pantalla Principal (Main)
                    composable<MainRoute> {
                        MainScreen()
                    }
                }
            }
        }
    }
}