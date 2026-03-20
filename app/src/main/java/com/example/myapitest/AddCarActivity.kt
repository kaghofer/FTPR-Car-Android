package com.example.myapitest

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapitest.models.Car
import com.example.myapitest.models.Place
import com.example.myapitest.ui.theme.CarLocationAppTheme

class AddCarActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CarLocationAppTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Adicionar Carro") },
                            navigationIcon = {
                                IconButton(onClick = { finish() }) {
                                    Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    AddCarScreen(
                        modifier = Modifier.padding(innerPadding),
                        onSuccess = { finish() }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCarScreen(
    modifier: Modifier = Modifier,
    viewModel: CarViewModel = viewModel(),
    onSuccess: () -> Unit
) {
    val context = LocalContext.current
    var carId by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var licence by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var latitude by remember { mutableStateOf("") }
    var longitude by remember { mutableStateOf("") }
    var isSaving by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = carId,
            onValueChange = { carId = it },
            label = { Text("ID do Carro") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nome do Carro") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = year,
            onValueChange = { year = it },
            label = { Text("Ano") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = licence,
            onValueChange = { licence = it },
            label = { Text("Placa") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = imageUrl,
            onValueChange = { imageUrl = it },
            label = { Text("URL da Imagem") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = latitude,
                onValueChange = { latitude = it },
                label = { Text("Latitude") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            OutlinedTextField(
                value = longitude,
                onValueChange = { longitude = it },
                label = { Text("Longitude") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val newCar = Car(
                    id = carId,
                    name = name,
                    year = year,
                    licence = licence,
                    imageUrl = imageUrl,
                    place = Place(
                        lat = latitude.toDoubleOrNull(),
                        long = longitude.toDoubleOrNull()
                    )
                )
                isSaving = true
                viewModel.saveCar(newCar) { errorMessage ->
                    isSaving = false
                    if (errorMessage == null) {
                        Toast.makeText(context, "Carro salvo com sucesso!", Toast.LENGTH_SHORT).show()
                        onSuccess()
                    } else {
                        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = carId.isNotBlank() && name.isNotBlank() && !isSaving
        ) {
            if (isSaving) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            } else {
                Text("Salvar Carro")
            }
        }
    }
}
