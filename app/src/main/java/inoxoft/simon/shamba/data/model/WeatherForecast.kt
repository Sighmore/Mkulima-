package inoxoft.simon.shamba.data.model

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

data class WeatherForecast(
    val date: String,
    val temperature: Double,
    val humidity: Int,
    val description: String,
    val isOptimal: Boolean
) {
    fun getWeatherGradient(): Brush {
        return when (description.lowercase()) {
            "hot" -> Brush.verticalGradient(
                colors = listOf(
                    Color(0xFFFF5722),
                    Color(0xFFFF9800)
                )
            )
            "warm" -> Brush.verticalGradient(
                colors = listOf(
                    Color(0xFFFF9800),
                    Color(0xFFFFC107)
                )
            )
            "optimal" -> Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF4CAF50),
                    Color(0xFF8BC34A)
                )
            )
            "cool" -> Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF03A9F4),
                    Color(0xFF00BCD4)
                )
            )
            else -> Brush.verticalGradient( // Cold
                colors = listOf(
                    Color(0xFF2196F3),
                    Color(0xFF03A9F4)
                )
            )
        }
    }
} 