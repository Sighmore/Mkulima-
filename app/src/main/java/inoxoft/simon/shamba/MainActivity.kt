package inoxoft.simon.shamba

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import inoxoft.simon.shamba.ui.screens.HomeScreen
import inoxoft.simon.shamba.ui.screens.PluckingScreen
import inoxoft.simon.shamba.ui.screens.RecordsScreen
import inoxoft.simon.shamba.ui.screens.WelcomeScreen
import inoxoft.simon.shamba.ui.theme.ShambaTheme
import inoxoft.simon.shamba.ui.viewmodel.PluckingViewModel

class MainActivity : ComponentActivity() {
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) ||
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Location permissions granted
            }
            else -> {
                // No location access - app will use default location
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Request location permissions
        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ))

        enableEdgeToEdge()
        setContent {
            ShambaTheme {
                MkulimaApp()
            }
        }
    }
}

@Composable
fun MkulimaApp() {
    val navController = rememberNavController()
    val viewModel: PluckingViewModel = viewModel()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "welcome",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("welcome") {
                WelcomeScreen(
                    onContinue = { name ->
                        navController.navigate("home/$name") {
                            popUpTo("welcome") { inclusive = true }
                        }
                    }
                )
            }
            composable(
                route = "home/{userName}",
                arguments = listOf(navArgument("userName") { type = NavType.StringType })
            ) { backStackEntry ->
                val userName = backStackEntry.arguments?.getString("userName") ?: ""
                HomeScreen(
                    navController = navController,
                    userName = userName
                )
            }
            composable("plucking") {
                PluckingScreen(
                    navController = navController,
                    viewModel = viewModel
                )
            }
            composable("records") {
                RecordsScreen(
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }
    }
}