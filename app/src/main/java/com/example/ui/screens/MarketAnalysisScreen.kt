package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
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
import com.example.ui.components.StatusBadge
import com.example.ui.theme.CardBorder
import com.example.ui.theme.ForestGreenContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketAnalysisScreen(
    viewModel: FarmingViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val crops = listOf("Rice", "Wheat", "Maize", "Cotton", "Sugarcane", "Coffee", "Jute", "Millets", "Tomato", "Onion", "Potato")
    val states = listOf(
        "Karnataka", "Maharashtra", "Punjab", "Haryana", "Andhra Pradesh",
        "Uttar Pradesh", "Tamil Nadu", "Gujarat", "West Bengal", "Bihar",
        "Madhya Pradesh", "Rajasthan", "Kerala", "Telangana", "Odisha", "Assam"
    )

    var selectedCrop by remember { mutableStateOf("Maize") }
    var selectedState by remember { mutableStateOf("Karnataka") }
    var priceStr by remember { mutableStateOf("21.0") }
    var cropExpanded by remember { mutableStateOf(false) }
    var stateExpanded by remember { mutableStateOf(false) }

    val activeResult by viewModel.activeMarketResult.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Market Price Intelligence", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("market_back_btn")) {
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
                .testTag("market_analysis_screen"),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ModuleHeroHeader(
                    iconEmoji = "📊",
                    title = "Market Intelligence",
                    subtitle = "Indian APMC Mandi regional price modeling and 3-month forecast engine."
                )
            }

            // Quick Demo Hint
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("💡", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Demo Tip: Select Karnataka + Maize @ ₹21.0/kg to see realistic APMC trend forecasts.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }

            // Form Card
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Text(
                            text = "Mandi Query Parameters",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        // Crop Dropdown
                        ExposedDropdownMenuBox(
                            expanded = cropExpanded,
                            onExpandedChange = { cropExpanded = !cropExpanded }
                        ) {
                            OutlinedTextField(
                                value = selectedCrop,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Commodity / Crop") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = cropExpanded) },
                                modifier = Modifier.menuAnchor().fillMaxWidth().testTag("market_crop_dropdown")
                            )
                            ExposedDropdownMenu(
                                expanded = cropExpanded,
                                onDismissRequest = { cropExpanded = false }
                            ) {
                                crops.forEach { crop ->
                                    DropdownMenuItem(
                                        text = { Text(crop) },
                                        onClick = { selectedCrop = crop; cropExpanded = false }
                                    )
                                }
                            }
                        }

                        // State Dropdown
                        ExposedDropdownMenuBox(
                            expanded = stateExpanded,
                            onExpandedChange = { stateExpanded = !stateExpanded }
                        ) {
                            OutlinedTextField(
                                value = selectedState,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Indian State / Region") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = stateExpanded) },
                                modifier = Modifier.menuAnchor().fillMaxWidth().testTag("market_state_dropdown")
                            )
                            ExposedDropdownMenu(
                                expanded = stateExpanded,
                                onDismissRequest = { stateExpanded = false }
                            ) {
                                states.forEach { state ->
                                    DropdownMenuItem(
                                        text = { Text(state) },
                                        onClick = { selectedState = state; stateExpanded = false }
                                    )
                                }
                            }
                        }

                        // Current Price Input
                        OutlinedTextField(
                            value = priceStr,
                            onValueChange = { priceStr = it },
                            label = { Text("Current Mandi Spot Price (₹/kg)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("market_price_input")
                        )

                        Button(
                            onClick = {
                                val p = priceStr.toDoubleOrNull() ?: 21.0
                                viewModel.analyzeMarket(selectedCrop, selectedState, p)
                            },
                            modifier = Modifier.fillMaxWidth().height(50.dp).testTag("market_analyze_btn"),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.ShowChart, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Generate Mandi Forecast", fontWeight = FontWeight.Bold)
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
                        modifier = Modifier.fillMaxWidth().testTag("market_result_card")
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "${res.crop} in ${res.state}",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = "Regional Benchmark: ₹${res.regionalBasePrice}/kg",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                StatusBadge(
                                    text = res.trend,
                                    isPositive = res.percentChange >= 0
                                )
                            }

                            Spacer(modifier = Modifier.height(18.dp))

                            Text(
                                text = "📈 7-Month Price Evolution & Forecast (₹/kg):",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            // Timeline Price Track
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                res.historicalPoints.forEach { pt ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("📅 ${pt.monthLabel} (Past)", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text("₹${pt.price}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                                    }
                                }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(ForestGreenContainer.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                        .padding(horizontal = 8.dp, vertical = 6.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("📍 ${res.currentPoint.monthLabel} (Current Spot)", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = ForestGreenContainer)
                                    Text("₹${res.currentPoint.price}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                }

                                res.forecastPoints.forEachIndexed { idx, pt ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("🔮 ${pt.monthLabel} (Month +${idx + 1} Forecast)", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.SemiBold)
                                        Text("₹${pt.price}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            Divider(color = CardBorder)
                            Spacer(modifier = Modifier.height(12.dp))

                            Text("💡 APMC Mandi Intelligence Advisory:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(res.mandiInsight, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}
