package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.FarmingViewModel
import com.example.ui.components.ModuleHeroHeader
import com.example.ui.components.PresetChip
import com.example.ui.theme.CardBorder
import com.example.ui.theme.ForestGreenContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherPredictionScreen(
    viewModel: FarmingViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    var tempStr by remember { mutableStateOf("26.0") }
    var humStr by remember { mutableStateOf("65.0") }
    var moistStr by remember { mutableStateOf("50.0") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val activeResult by viewModel.activeWeatherResult.collectAsStateWithLifecycle()

    val demoPresets = listOf(
        Triple("Sunny", Triple(32.0, 35.0, 20.0), "☀️"),
        Triple("Rainy", Triple(23.0, 92.0, 85.0), "🌧️"),
        Triple("Cloudy", Triple(26.0, 75.0, 50.0), "⛅"),
        Triple("Stormy", Triple(21.0, 95.0, 90.0), "⛈️")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Weather Forecast", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("weather_back_btn")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .testTag("weather_prediction_screen"),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ModuleHeroHeader(
                    iconEmoji = "☁️",
                    title = "Weather Prediction",
                    subtitle = "Sensor-based microclimate forecasting provides field operations and irrigation guidance."
                )
            }

            // Quick Demo Presets
            item {
                Text(
                    text = "Demo Weather Scenarios (One-tap)",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(demoPresets) { (name, values, icon) ->
                        PresetChip(
                            label = "$icon $name",
                            onClick = {
                                tempStr = values.first.toString()
                                humStr = values.second.toString()
                                moistStr = values.third.toString()
                                viewModel.predictWeather(values.first, values.second, values.third)
                            }
                        )
                    }
                }
            }

            // Input Form Card
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Text(
                            text = "Microclimate Sensor Readings",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        OutlinedTextField(
                            value = tempStr,
                            onValueChange = { tempStr = it; errorMessage = null },
                            label = { Text("Ambient Temperature (°C)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("weather_temp_input")
                        )

                        OutlinedTextField(
                            value = humStr,
                            onValueChange = { humStr = it; errorMessage = null },
                            label = { Text("Relative Humidity (%)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("weather_hum_input")
                        )

                        OutlinedTextField(
                            value = moistStr,
                            onValueChange = { moistStr = it; errorMessage = null },
                            label = { Text("Soil Moisture (%)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("weather_moist_input")
                        )

                        if (errorMessage != null) {
                            Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                        }

                        Button(
                            onClick = {
                                val t = tempStr.toDoubleOrNull()
                                val h = humStr.toDoubleOrNull()
                                val m = moistStr.toDoubleOrNull()
                                if (t == null || h == null || m == null) {
                                    errorMessage = "Please enter valid numeric values for all three fields."
                                } else {
                                    viewModel.predictWeather(t, h, m)
                                }
                            },
                            modifier = Modifier.fillMaxWidth().height(50.dp).testTag("weather_predict_btn"),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Cloud, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Forecast Weather & Advisory", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Results Display
            if (activeResult != null) {
                val res = activeResult!!
                item {
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = ForestGreenContainer.copy(alpha = 0.35f)),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary),
                        modifier = Modifier.fillMaxWidth().testTag("weather_result_card")
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.surface),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = res.iconSymbol, fontSize = 32.sp)
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text(
                                        text = "Forecast Outcome",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = res.condition,
                                        style = MaterialTheme.typography.headlineMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))
                            Text(
                                text = res.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.height(16.dp))
                            Divider(color = CardBorder)
                            Spacer(modifier = Modifier.height(14.dp))

                            // Actionable Advisory Card
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text(
                                        text = "🚜 Field Operations Advisory",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(text = res.farmingAdvisory, style = MaterialTheme.typography.bodySmall)

                                    Divider(color = CardBorder.copy(alpha = 0.5f))

                                    Text(
                                        text = "💧 Irrigation Guidance",
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(text = res.irrigationAdvice, style = MaterialTheme.typography.bodySmall)

                                    Divider(color = CardBorder.copy(alpha = 0.5f))

                                    Text(
                                        text = "🧴 Spraying Window",
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(text = res.sprayingAdvice, style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}
