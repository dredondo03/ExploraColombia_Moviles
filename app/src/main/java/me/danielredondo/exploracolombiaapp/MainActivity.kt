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
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import me.danielredondo.exploracolombiaapp.ui.theme.ExploraColombiaAppTheme

// Definición de rutas como objetos serializables (Type Safety)
@Serializable object LoginRoute
@Serializable object RegisterRoute
@Serializable object MainRoute

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // iniciar instancia de firebase
        val auth = Firebase.auth
        
        // ver si esta logeado
        val currentUser = auth.currentUser
        
        // si no hay usuario logeado se manda al login
        val startRoute = if (currentUser != null) "main" else "login"

        enableEdgeToEdge()
        setContent {
            ExploraColombiaAppTheme {
                // El NavController gestiona el estado de la navegación
                val myNavController = rememberNavController()

                NavHost(
                    navController = myNavController,
                    startDestination = startRoute,
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Pantalla de Login
                    composable<LoginRoute> {
                        LoginScreen(
                            onLoginSuccess = {

                                myNavController.navigate("main") { //cambiar la ruta a main
                                    popUpTo("login") { inclusive = true } //limpieza
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
                                // si se registra se devuelve al login
                                myNavController.navigate("login") {
                                    popUpTo("register") { inclusive = true }
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
                    composable(route = "main") {
                        MainScreen(
                            onLogout = {
                                // cerrar sesion, token de autentificacion
                                auth.signOut()

                                myNavController.navigate("login") {
                                    popUpTo("main") { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}