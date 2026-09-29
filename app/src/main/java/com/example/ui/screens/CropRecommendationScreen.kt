package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Grass
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.FarmingViewModel
import com.example.ui.components.ModuleHeroHeader
import com.example.ui.components.PresetChip
import com.example.ui.components.StatusBadge
import com.example.ui.theme.CardBorder
import com.example.ui.theme.ForestGreenContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CropRecommendationScreen(
    viewModel: FarmingViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    var tempStr by remember { mutableStateOf("25.0") }
    var humStr by remember { mutableStateOf("80.0") }
    var moistStr by remember { mutableStateOf("75.0") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val activeResult by viewModel.activeCropResult.collectAsStateWithLifecycle()

    val demoPresets = listOf(
        Triple("Rice", Triple(25.0, 80.0, 75.0), "🌾"),
        Triple("Wheat", Triple(20.0, 60.0, 50.0), "🍞"),
        Triple("Maize", Triple(27.5, 70.0, 55.0), "🌽"),
        Triple("Sugarcane", Triple(30.0, 85.0, 80.0), "🎋"),
        Triple("Cotton", Triple(28.0, 50.0, 40.0), "☁️"),
        Triple("Tomato", Triple(24.0, 65.0, 60.0), "🍅")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crop Recommendation", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("crop_back_btn")) {
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
                .testTag("crop_recommendation_screen"),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ModuleHeroHeader(
                    iconEmoji = "🌱",
                    title = "Crop Recommendation",
                    subtitle = "Random Forest AI predicts the optimal crop for your current soil and climate."
                )
            }

            // Quick Demo Presets
            item {
                Text(
                    text = "Quick Demo Scenarios (One-tap)",
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
                                viewModel.recommendCrop(values.first, values.second, values.third)
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
                            text = "Environmental Sensor Inputs",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        OutlinedTextField(
                            value = tempStr,
                            onValueChange = { tempStr = it; errorMessage = null },
                            label = { Text("Temperature (°C)") },
                            placeholder = { Text("e.g. 25.0") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("crop_temp_input")
                        )

                        OutlinedTextField(
                            value = humStr,
                            onValueChange = { humStr = it; errorMessage = null },
                            label = { Text("Humidity (%)") },
                            placeholder = { Text("e.g. 80.0") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("crop_hum_input")
                        )

                        OutlinedTextField(
                            value = moistStr,
                            onValueChange = { moistStr = it; errorMessage = null },
                            label = { Text("Soil Moisture (%)") },
                            placeholder = { Text("e.g. 75.0") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("crop_moist_input")
                        )

                        if (errorMessage != null) {
                            Text(
                                text = errorMessage!!,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        Button(
                            onClick = {
                                val t = tempStr.toDoubleOrNull()
                                val h = humStr.toDoubleOrNull()
                                val m = moistStr.toDoubleOrNull()
                                if (t == null || h == null || m == null) {
                                    errorMessage = "Please enter valid numeric values for all three fields."
                                } else {
                                    viewModel.recommendCrop(t, h, m)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("crop_predict_btn"),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Grass, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Recommend Optimal Crop", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Result Display Card
            if (activeResult != null) {
                val res = activeResult!!
                item {
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = ForestGreenContainer.copy(alpha = 0.4f)),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary),
                        modifier = Modifier.fillMaxWidth().testTag("crop_result_card")
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "AI Recommendation",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                                StatusBadge(
                                    text = "${res.suitabilityScore.toInt()}% Match",
                                    isPositive = true
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = res.recommendedCrop.name,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = res.recommendedCrop.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.height(16.dp))
                            Divider(color = CardBorder)
                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text("Season", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text(res.recommendedCrop.season, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                                }
                                Column {
                                    Text("Water Requirement", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text(res.recommendedCrop.waterRequirement, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                                }
                            }

                            if (res.topAlternates.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(14.dp))
                                Text(
                                    text = "Viable Alternate Crops:",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    res.topAlternates.forEach { (altCrop, altScore) ->
                                        AssistChip(
                                            onClick = {},
                                            label = { Text("${altCrop.name} (${altScore.toInt()}%)") }
                                        )
                                    }
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
