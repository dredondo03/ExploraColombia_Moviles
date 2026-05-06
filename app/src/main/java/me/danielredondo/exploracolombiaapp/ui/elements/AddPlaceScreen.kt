package me.danielredondo.exploracolombiaapp.ui.elements

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import me.danielredondo.exploracolombiaapp.ui.viewmodels.AddPlaceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPlaceScreen(
    onBackClick: () -> Unit,
    addPlaceViewModel: AddPlaceViewModel = viewModel()
) {
    val context = LocalContext.current
    val primaryOrange = Color(0xFFE45D25)
    val secondaryOrange = Color(0xFFD1451B)
    val lightGrayBg = Color(0xFFF8F9FE)
    val inputBg = Color(0xFFE5E5EA)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Agregar Lugar",
                        color = Color(0xFF8B2D16),
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            tint = Color(0xFF8B2D16)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = lightGrayBg)
            )
        },
        containerColor = lightGrayBg
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            // Header Card con Gradiente
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(40.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(primaryOrange, secondaryOrange)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Comparte tu descubrimiento",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Ayuda a otros viajeros a encontrar los tesoros escondidos de nuestra tierra.",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Formulario
            Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                PlaceInputField(
                    label = "NOMBRE DEL LUGAR",
                    value = addPlaceViewModel.placeName,
                    onValueChange = { addPlaceViewModel.onPlaceNameChange(it) },
                    placeholder = "Ej: Cascada del Fin del Mundo",
                    inputBg = inputBg
                )

                PlaceInputField(
                    label = "DEPARTAMENTO",
                    value = addPlaceViewModel.department,
                    onValueChange = { addPlaceViewModel.onDepartmentChange(it) },
                    placeholder = "Ej: Putumayo",
                    inputBg = inputBg
                )

                PlaceInputField(
                    label = "CIUDAD",
                    value = addPlaceViewModel.city,
                    onValueChange = { addPlaceViewModel.onCityChange(it) },
                    placeholder = "Ej: Mocoa",
                    inputBg = inputBg
                )

                PlaceInputField(
                    label = "DESCRIPCIÓN",
                    value = addPlaceViewModel.description,
                    onValueChange = { addPlaceViewModel.onDescriptionChange(it) },
                    placeholder = "Cuéntanos por qué este lugar es especial...",
                    inputBg = inputBg,
                    singleLine = false,
                    modifier = Modifier.height(150.dp)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Botón Publicar
            Button(
                onClick = {
                    addPlaceViewModel.savePlace {
                        Toast.makeText(context, "¡Lugar publicado con éxito!", Toast.LENGTH_SHORT).show()
                        onBackClick()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(primaryOrange, secondaryOrange)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Publicar",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun PlaceInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    inputBg: Color,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
            modifier = Modifier.padding(start = 8.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp)),
            placeholder = { Text(placeholder, color = Color.Gray, fontSize = 14.sp) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = inputBg,
                unfocusedContainerColor = inputBg,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color(0xFFE45D25)
            ),
            singleLine = singleLine
        )
    }
}
