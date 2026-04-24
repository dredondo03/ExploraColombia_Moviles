package me.danielredondo.exploracolombiaapp

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest
import me.danielredondo.exploracolombiaapp.ui.theme.ExploraColombiaAppTheme


@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
   //inicia el firebase
    val auth = Firebase.auth
    val context = LocalContext.current
    //obtiene lo que esta pasando y ayuda a acceder a r o navegar

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var acceptedTerms by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    val primaryOrange = Color(0xFFE45D25)
    val lightGrayBg = Color(0xFFF8F9FE)
    val inputBg = Color(0xFFE5E5EA)

    Surface(
        modifier = modifier.fillMaxSize(),
        color = lightGrayBg
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Icono atrás y textos (Omitidos por brevedad en comentarios)
            IconButton(onClick = onBackClick, enabled = !isLoading) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = primaryOrange)
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Explorando Colombia", color = primaryOrange, fontSize = 22.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(32.dp))
            Text(text = "Crea tu cuenta", fontSize = 32.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(32.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                RegisterField(label = "NOMBRE COMPLETO", value = name, onValueChange = { name = it }, placeholder = "Tu nombre", leadingIcon = Icons.Default.Person, inputBg = inputBg, enabled = !isLoading)
                Spacer(modifier = Modifier.height(20.dp))
                RegisterField(label = "CORREO ELECTRÓNICO", value = email, onValueChange = { email = it }, placeholder = "hola@ejemplo.com", leadingIcon = Icons.Default.Email, inputBg = inputBg, enabled = !isLoading)
                Spacer(modifier = Modifier.height(20.dp))
                RegisterField(label = "CONTRASEÑA", value = password, onValueChange = { password = it }, placeholder = "........", leadingIcon = Icons.Default.Lock, inputBg = inputBg, isPassword = true, enabled = !isLoading)
                Spacer(modifier = Modifier.height(20.dp))
                RegisterField(label = "CONFIRMAR", value = confirmPassword, onValueChange = { confirmPassword = it }, placeholder = "........", leadingIcon = Icons.Default.Refresh, inputBg = inputBg, isPassword = true, enabled = !isLoading)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = acceptedTerms, onCheckedChange = { acceptedTerms = it }, colors = CheckboxDefaults.colors(checkedColor = primaryOrange), enabled = !isLoading)
                Text(text = "Acepto los términos y condiciones.", fontSize = 12.sp, color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty()) {
                        if (password == confirmPassword) {
                            if (acceptedTerms) {
                                isLoading = true

                                //registra el correo y la contra
                                auth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {

                                            //se pide el nombre para luego usarlo en el bienvenido
                                            val profileUpdates = userProfileChangeRequest {
                                                displayName = name
                                            }
                                            task.result.user?.updateProfile(profileUpdates)
                                                ?.addOnCompleteListener {
                                                    isLoading = false
                                                    Toast.makeText(context, "Registro exitoso. Inicia sesión.", Toast.LENGTH_SHORT).show()
                                                    //aqui hace un cerrar sesion rapido para volver al login y logearse de nuevo
                                                    auth.signOut() 
                                                    onRegisterSuccess()
                                                }
                                        } else {
                                            isLoading = false
                                            Toast.makeText(context, "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                                        }
                                    }
                            } else {
                                Toast.makeText(context, "Acepta los términos", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(context, "Contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(64.dp),
                shape = RoundedCornerShape(32.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Registrarse", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }

        }
    }
}

@Composable
fun RegisterField(label: String, value: String, onValueChange: (String) -> Unit, placeholder: String, leadingIcon: androidx.compose.ui.graphics.vector.ImageVector, inputBg: Color, modifier: Modifier = Modifier, isPassword: Boolean = false, enabled: Boolean = true) {
    Column(modifier = modifier) {
        Text(text = label, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth().height(56.dp).clip(RoundedCornerShape(28.dp)),
            placeholder = { Text(placeholder, color = Color.Gray) },
            leadingIcon = { Icon(leadingIcon, contentDescription = null, tint = Color.Gray) },
            visualTransformation = if (isPassword) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Text),
            colors = TextFieldDefaults.colors(focusedContainerColor = inputBg, unfocusedContainerColor = inputBg, disabledContainerColor = inputBg, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent),
            singleLine = true,
            enabled = enabled
        )
    }
}
