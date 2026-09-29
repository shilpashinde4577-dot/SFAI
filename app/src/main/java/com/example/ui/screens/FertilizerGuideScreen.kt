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
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun FertilizerGuideScreen(
    viewModel: FarmingViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val crops = listOf("Wheat", "Rice", "Maize", "Cotton", "Sugarcane", "Coffee", "Jute", "Millets", "Tomato", "Onion", "Potato")
    var selectedCrop by remember { mutableStateOf("Wheat") }
    var cropDropdownExpanded by remember { mutableStateOf(false) }

    var nStr by remember { mutableStateOf("50.0") }
    var pStr by remember { mutableStateOf("25.0") }
    var kStr by remember { mutableStateOf("20.0") }
    var phStr by remember { mutableStateOf("6.5") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val activeResult by viewModel.activeFertilizerResult.collectAsStateWithLifecycle()

    val demoScenarios = listOf(
        Triple("Needs Urea (Low N)", listOf(10.0, 50.0, 50.0, 6.5), "🧪"),
        Triple("Needs DAP (Low P)", listOf(60.0, 5.0, 50.0, 6.5), "🌱"),
        Triple("Needs MOP (Low K)", listOf(60.0, 50.0, 8.0, 6.5), "🌾"),
        Triple("Needs Lime (Acidic)", listOf(60.0, 50.0, 50.0, 4.5), "🍋"),
        Triple("Needs Gypsum (Alkaline)", listOf(60.0, 50.0, 50.0, 8.5), "🧂")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Fertilizer Guide", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("fert_back_btn")) {
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
                .testTag("fertilizer_guide_screen"),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ModuleHeroHeader(
                    iconEmoji = "🧪",
                    title = "Fertilizer Guide",
                    subtitle = "Agronomy calculation engine computes exact Urea, DAP, and MOP in kg/acre for your soil deficits."
                )
            }

            // Demo Scenario Presets
            item {
                Text(
                    text = "Demo Soil Scenarios (One-tap)",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(demoScenarios) { (label, vals, icon) ->
                        PresetChip(
                            label = "$icon $label",
                            onClick = {
                                nStr = vals[0].toString()
                                pStr = vals[1].toString()
                                kStr = vals[2].toString()
                                phStr = vals[3].toString()
                                viewModel.calculateFertilizer(selectedCrop, vals[0], vals[1], vals[2], vals[3])
                            }
                        )
                    }
                }
            }

            // Soil Nutrient Form
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Text(
                            text = "Soil Testing Nutrient Inputs",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        // Crop Selector Dropdown
                        ExposedDropdownMenuBox(
                            expanded = cropDropdownExpanded,
                            onExpandedChange = { cropDropdownExpanded = !cropDropdownExpanded }
                        ) {
                            OutlinedTextField(
                                value = selectedCrop,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Target Crop") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = cropDropdownExpanded) },
                                modifier = Modifier.menuAnchor().fillMaxWidth().testTag("fert_crop_dropdown")
                            )
                            ExposedDropdownMenu(
                                expanded = cropDropdownExpanded,
                                onDismissRequest = { cropDropdownExpanded = false }
                            ) {
                                crops.forEach { crop ->
                                    DropdownMenuItem(
                                        text = { Text(crop) },
                                        onClick = {
                                            selectedCrop = crop
                                            cropDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            OutlinedTextField(
                                value = nStr,
                                onValueChange = { nStr = it; errorMessage = null },
                                label = { Text("Nitrogen (N)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.weight(1f).testTag("fert_n_input")
                            )
                            OutlinedTextField(
                                value = pStr,
                                onValueChange = { pStr = it; errorMessage = null },
                                label = { Text("Phosphorus (P)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.weight(1f).testTag("fert_p_input")
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            OutlinedTextField(
                                value = kStr,
                                onValueChange = { kStr = it; errorMessage = null },
                                label = { Text("Potassium (K)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.weight(1f).testTag("fert_k_input")
                            )
                            OutlinedTextField(
                                value = phStr,
                                onValueChange = { phStr = it; errorMessage = null },
                                label = { Text("Soil pH") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.weight(1f).testTag("fert_ph_input")
                            )
                        }

                        if (errorMessage != null) {
                            Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                        }

                        Button(
                            onClick = {
                                val n = nStr.toDoubleOrNull()
                                val p = pStr.toDoubleOrNull()
                                val k = kStr.toDoubleOrNull()
                                val ph = phStr.toDoubleOrNull()
                                if (n == null || p == null || k == null || ph == null) {
                                    errorMessage = "Please enter valid numeric values for all soil parameters."
                                } else {
                                    viewModel.calculateFertilizer(selectedCrop, n, p, k, ph)
                                }
                            },
                            modifier = Modifier.fillMaxWidth().height(50.dp).testTag("fert_calculate_btn"),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Science, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Calculate Exact Fertilizer Quantities", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Results Section
            if (activeResult != null) {
                val res = activeResult!!
                item {
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary),
                        modifier = Modifier.fillMaxWidth().testTag("fert_result_card")
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                text = "Agronomy Prescription for ${res.cropName}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Recommended: ${res.primaryFertilizerType}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Fertilizer Doses Grid
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = ForestGreenContainer.copy(alpha = 0.4f)),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("Urea", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                                        Text("${res.ureaKgPerAcre} kg", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                        Text("per acre", style = MaterialTheme.typography.labelSmall)
                                    }
                                }

                                Card(
                                    colors = CardDefaults.cardColors(containerColor = ForestGreenContainer.copy(alpha = 0.4f)),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("DAP", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                                        Text("${res.dapKgPerAcre} kg", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                        Text("per acre", style = MaterialTheme.typography.labelSmall)
                                    }
                                }

                                Card(
                                    colors = CardDefaults.cardColors(containerColor = ForestGreenContainer.copy(alpha = 0.4f)),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("MOP", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                                        Text("${res.mopKgPerAcre} kg", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                        Text("per acre", style = MaterialTheme.typography.labelSmall)
                                    }
                                }
                            }

                            if (res.soilAmendment != null) {
                                Spacer(modifier = Modifier.height(14.dp))
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text("⚠️ Soil pH Adjustment Needed", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                                        Text(res.soilAmendment, style = MaterialTheme.typography.bodySmall)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            Divider(color = CardBorder)
                            Spacer(modifier = Modifier.height(12.dp))

                            Text("Detailed Recommendations:", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(6.dp))
                            res.recommendations.forEach { rec ->
                                Row(modifier = Modifier.padding(vertical = 2.dp)) {
                                    Text("• ", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                                    Text(rec, style = MaterialTheme.typography.bodySmall)
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
