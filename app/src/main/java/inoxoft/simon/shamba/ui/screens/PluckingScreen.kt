package inoxoft.simon.shamba.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import inoxoft.simon.shamba.data.model.PluckingRecord
import inoxoft.simon.shamba.ui.viewmodel.PluckingViewModel

enum class TeaCompany(val displayName: String, val rate: Double) {
    KTDA_SASINI("KTDA/SASINI", 9.0),
    TET_CHEML("TET/CHEML", 8.0),
    OTHERS("Others", 7.0)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PluckingScreen(
        navController: NavController,
        viewModel: PluckingViewModel = viewModel()
) {
    var pluckerName by remember { mutableStateOf("") }
    var kilos by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var selectedCompany by remember { mutableStateOf(TeaCompany.KTDA_SASINI) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Plucking Record") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Input: Plucker Name
            OutlinedTextField(
                value = pluckerName,
                onValueChange = {
                    pluckerName = it
                    showError = false
                },
                label = { Text("Plucker Name") },
                modifier = Modifier.fillMaxWidth(),
                isError = showError && pluckerName.isBlank(),
                supportingText = {
                    if (showError && pluckerName.isBlank()) {
                        Text("Name is required")
                    }
                }
            )

            // Input: Kilos Plucked
            OutlinedTextField(
                value = kilos,
                onValueChange = {
                    if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) {
                        kilos = it
                        showError = false
                    }
                },
                label = { Text("Kilos Plucked") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                isError = showError && kilos.isBlank(),
                supportingText = {
                    if (showError && kilos.isBlank()) {
                        Text("Kilos value is required")
                    }
                }
            )

            // Company Selection
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Select Company",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TeaCompany.values().forEach { company ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedCompany == company,
                                onClick = { selectedCompany = company }
                            )
                            Text(
                                "${company.displayName}: Ksh ${"%.2f".format(company.rate)}",
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Total Amount Preview
            val totalAmount = kilos.toDoubleOrNull()?.let { it * selectedCompany.rate } ?: 0.0
            ElevatedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Total Amount",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        "Ksh ${"%.2f".format(totalAmount)}",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }

            // Save Record Button
            Button(
                onClick = {
                    when {
                        pluckerName.isBlank() || kilos.isBlank() -> {
                            showError = true
                        }
                        else -> {
                            val record = PluckingRecord(
                                pluckerName = pluckerName,
                                kilos = kilos.toDouble(),
                                rate = selectedCompany.rate,
                                amount = kilos.toDouble() * selectedCompany.rate,
                                id = TODO(),
                                date = TODO()
                            )
                            viewModel.addRecord(record)
                            navController.navigateUp()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text("Save Record")
            }
        }
    }
}
