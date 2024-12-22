package inoxoft.simon.shamba.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.util.*
import androidx.lifecycle.viewmodel.compose.viewModel
import inoxoft.simon.shamba.ui.components.TeaPriceCard
import inoxoft.simon.shamba.ui.components.WeatherForecastSection
import inoxoft.simon.shamba.ui.viewmodel.WeatherViewModel
import inoxoft.simon.shamba.ui.viewmodel.PluckingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    userName: String,
    weatherViewModel: WeatherViewModel = viewModel(),
    pluckingViewModel: PluckingViewModel = viewModel()
) {
    val weatherForecasts by weatherViewModel.weatherForecasts.collectAsState(initial = emptyList())
    val isLoading by weatherViewModel.isLoading.collectAsState()
    val error by weatherViewModel.error.collectAsState()
    val todayRecords by pluckingViewModel.todayRecords.collectAsState(initial = emptyList())

    // Fetch data when screen is created
    LaunchedEffect(Unit) {
        weatherViewModel.fetchWeatherData()
        pluckingViewModel.getTodayRecords()
    }

    // Calculate totals
    val totalKilos = todayRecords.sumOf { it.kilos }
    val totalPluckers = todayRecords.distinctBy { it.pluckerName }.size

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "Good ${getGreeting()}",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            userName,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Summary Cards Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Total Kilos Card
                ElevatedCard(
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            String.format("%.1f", totalKilos),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Total Kilos",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                // Total Pluckers Card
                ElevatedCard(
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "$totalPluckers",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Pluckers Today",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            // Tea Price Card
            TeaPriceCard(
                currentPrice = 65.50, // Replace with actual price
                previousPrice = 62.30  // Replace with previous price
            )

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ElevatedButton(
                    onClick = { navController.navigate("plucking") },
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("New Record")
                }

                ElevatedButton(
                    onClick = { navController.navigate("records") },
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Icon(Icons.Default.List, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("View All")
                }
            }

            // Weather Forecast Section
            when {
                isLoading -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Loading weather forecast...")
                    }
                }
                error != null -> {
                    Text(
                        text = "Error loading weather: $error",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                weatherForecasts.isEmpty() -> {
                    Text(
                        text = "No weather data available",
                        modifier = Modifier.padding(16.dp)
                    )
                }
                else -> {
                    WeatherForecastSection(weatherForecasts)
                }
            }
        }
    }
}

private fun getGreeting(): String {
    return when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
        in 0..11 -> "Morning"
        in 12..15 -> "Afternoon"
        in 16..20 -> "Evening"
        else -> "Night"
    }
} 