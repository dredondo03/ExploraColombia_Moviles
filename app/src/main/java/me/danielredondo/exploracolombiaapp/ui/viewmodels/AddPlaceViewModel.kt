package me.danielredondo.exploracolombiaapp.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class AddPlaceViewModel : ViewModel() {
    var placeName by mutableStateOf("")
    var department by mutableStateOf("")
    var city by mutableStateOf("")
    var description by mutableStateOf("")

    fun onPlaceNameChange(newValue: String) {
        placeName = newValue
    }

    fun onDepartmentChange(newValue: String) {
        department = newValue
    }

    fun onCityChange(newValue: String) {
        city = newValue
    }

    fun onDescriptionChange(newValue: String) {
        description = newValue
    }

    fun savePlace(onSuccess: () -> Unit) {
        // Aquí iría la lógica para guardar en Firebase Firestore o Realtime Database
        if (placeName.isNotBlank() && department.isNotBlank() && city.isNotBlank()) {
            // Simulación de guardado
            onSuccess()
        }
    }
}
