package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.RequestQuote
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
import com.example.ui.components.StatusBadge
import com.example.ui.theme.CardBorder
import com.example.ui.theme.ForestGreenContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CropCompensationScreen(
    viewModel: FarmingViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val crops = listOf("Cotton", "Sugarcane", "Coffee", "Tomato", "Onion", "Potato", "Rice", "Wheat", "Maize", "Jute", "Millets")
    val states = listOf("Karnataka", "Maharashtra", "Punjab", "Haryana", "Andhra Pradesh", "Gujarat", "Tamil Nadu", "Uttar Pradesh", "Madhya Pradesh", "Rajasthan")
    val factors = listOf("Flood", "Drought", "Hailstorm", "Fire", "Pest/Animals", "Unseasonal Rain")

    var selectedState by remember { mutableStateOf("Karnataka") }
    var selectedCrop by remember { mutableStateOf("Cotton") }
    var selectedFactor by remember { mutableStateOf("Flood") }
    var acreageStr by remember { mutableStateOf("5.0") }

    var stateExpanded by remember { mutableStateOf(false) }
    var cropExpanded by remember { mutableStateOf(false) }
    var factorExpanded by remember { mutableStateOf(false) }

    val activeResult by viewModel.activeCompensationResult.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crop Compensation Claim", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("comp_back_btn")) {
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
                .testTag("crop_compensation_screen"),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ModuleHeroHeader(
                    iconEmoji = "💰",
                    title = "Crop Loss Compensation",
                    subtitle = "ML-backed calculation of disaster compensation payouts under PMFBY norms."
                )
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
                            text = "Loss Claim Parameters",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        // State
                        ExposedDropdownMenuBox(
                            expanded = stateExpanded,
                            onExpandedChange = { stateExpanded = !stateExpanded }
                        ) {
                            OutlinedTextField(
                                value = selectedState,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("State") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = stateExpanded) },
                                modifier = Modifier.menuAnchor().fillMaxWidth().testTag("comp_state_dropdown")
                            )
                            ExposedDropdownMenu(
                                expanded = stateExpanded,
                                onDismissRequest = { stateExpanded = false }
                            ) {
                                states.forEach { s ->
                                    DropdownMenuItem(text = { Text(s) }, onClick = { selectedState = s; stateExpanded = false })
                                }
                            }
                        }

                        // Crop
                        ExposedDropdownMenuBox(
                            expanded = cropExpanded,
                            onExpandedChange = { cropExpanded = !cropExpanded }
                        ) {
                            OutlinedTextField(
                                value = selectedCrop,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Damaged Crop") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = cropExpanded) },
                                modifier = Modifier.menuAnchor().fillMaxWidth().testTag("comp_crop_dropdown")
                            )
                            ExposedDropdownMenu(
                                expanded = cropExpanded,
                                onDismissRequest = { cropExpanded = false }
                            ) {
                                crops.forEach { c ->
                                    DropdownMenuItem(text = { Text(c) }, onClick = { selectedCrop = c; cropExpanded = false })
                                }
                            }
                        }

                        // Factor
                        ExposedDropdownMenuBox(
                            expanded = factorExpanded,
                            onExpandedChange = { factorExpanded = !factorExpanded }
                        ) {
                            OutlinedTextField(
                                value = selectedFactor,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Damage / Calamity Factor") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = factorExpanded) },
                                modifier = Modifier.menuAnchor().fillMaxWidth().testTag("comp_factor_dropdown")
                            )
                            ExposedDropdownMenu(
                                expanded = factorExpanded,
                                onDismissRequest = { factorExpanded = false }
                            ) {
                                factors.forEach { f ->
                                    DropdownMenuItem(text = { Text(f) }, onClick = { selectedFactor = f; factorExpanded = false })
                                }
                            }
                        }

                        // Acreage
                        OutlinedTextField(
                            value = acreageStr,
                            onValueChange = { acreageStr = it },
                            label = { Text("Affected Area (Acres)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("comp_acreage_input")
                        )

                        Button(
                            onClick = {
                                val ac = acreageStr.toDoubleOrNull() ?: 1.0
                                viewModel.calculateCompensation(selectedState, selectedCrop, selectedFactor, ac)
                            },
                            modifier = Modifier.fillMaxWidth().height(50.dp).testTag("comp_calculate_btn"),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.RequestQuote, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Compute Compensation Claim", fontWeight = FontWeight.Bold)
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
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary),
                        modifier = Modifier.fillMaxWidth().testTag("comp_result_card")
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Estimated Relief Settlement",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                                StatusBadge(
                                    text = "${res.damageFactor} Claim",
                                    isPositive = true
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Highlight Total Payout
                            Card(
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = ForestGreenContainer.copy(alpha = 0.5f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "Total Estimated Compensation",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "₹${res.totalClaimAmount.toLong()}",
                                        style = MaterialTheme.typography.headlineLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Rate: ₹${res.compensationPerAcre.toLong()} per acre across ${res.acreage} acres",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            Divider(color = CardBorder)
                            Spacer(modifier = Modifier.height(12.dp))

                            Text("📋 Actuarial Assessment Breakdown:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(res.assessmentBreakdown, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

                            Spacer(modifier = Modifier.height(12.dp))
                            Text("📑 Settlement Procedure:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(res.claimGuidelines, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}
