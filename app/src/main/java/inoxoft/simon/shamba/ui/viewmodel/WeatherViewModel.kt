package inoxoft.simon.shamba.ui.viewmodel

import android.app.Application
import android.location.Location
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import inoxoft.simon.shamba.data.api.WeatherApi
import inoxoft.simon.shamba.data.model.WeatherForecast
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.net.UnknownHostException

class WeatherViewModel(application: Application) : AndroidViewModel(application) {
    private val _weatherForecasts = MutableStateFlow<List<WeatherForecast>>(emptyList())
    val weatherForecasts: StateFlow<List<WeatherForecast>> = _weatherForecasts

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)

    private val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    private val weatherApi = Retrofit.Builder()
        .baseUrl("https://api.open-meteo.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
        .create(WeatherApi::class.java)

    fun fetchWeatherData() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                
                Log.d("WeatherViewModel", "Fetching weather data...")
                val location = getCurrentLocation()
                Log.d("WeatherViewModel", "Got location: ${location.latitude}, ${location.longitude}")
                
                val response = weatherApi.getWeatherForecast(
                    latitude = location.latitude,
                    longitude = location.longitude
                )
                
                Log.d("WeatherViewModel", "Got response: ${response.daily.time.size} days")

                val forecasts = response.daily.time.mapIndexed { index, date ->
                    val avgTemp = (response.daily.temperature_2m_max[index] + 
                                 response.daily.temperature_2m_min[index]) / 2
                    WeatherForecast(
                        date = SimpleDateFormat("EEE", Locale.getDefault())
                            .format(SimpleDateFormat("yyyy-MM-dd").parse(date)!!),
                        temperature = avgTemp,
                        humidity = response.daily.relative_humidity_2m_max[index],
                        description = getWeatherDescription(avgTemp),
                        isOptimal = isOptimalWeather(avgTemp, response.daily.relative_humidity_2m_max[index])
                    )
                }.take(7)
                
                Log.d("WeatherViewModel", "Processed forecasts: ${forecasts.size} items")
                _weatherForecasts.value = forecasts
                
            } catch (e: UnknownHostException) {
                Log.e("WeatherViewModel", "Network error: Unable to connect to server", e)
                _error.value = "Please check your internet connection"
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "Error fetching weather data", e)
                _error.value = "Error loading weather: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun getCurrentLocation(): Location {
        return try {
            fusedLocationClient
                .getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, CancellationTokenSource().token)
                .await()
        } catch (e: Exception) {
            Log.e("WeatherViewModel", "Error getting location", e)
            // Default location (you might want to handle this differently)
            Location("").apply {
                latitude = -1.2921 // Default to Nairobi
                longitude = 36.8219
            }
        }
    }

    private fun isOptimalWeather(temperature: Double, humidity: Int): Boolean {
        // Optimal conditions for tea growing
        return temperature in 20.0..25.0 && humidity in 70..85
    }

    private fun getWeatherDescription(temperature: Double): String {
        return when {
            temperature > 30 -> "Hot"
            temperature > 25 -> "Warm"
            temperature > 20 -> "Optimal"
            temperature > 15 -> "Cool"
            else -> "Cold"
        }
    }
} 