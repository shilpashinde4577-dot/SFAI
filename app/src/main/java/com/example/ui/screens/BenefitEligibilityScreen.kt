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
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
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
fun BenefitEligibilityScreen(
    viewModel: FarmingViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val crops = listOf("Soybean", "Rice", "Wheat", "Cotton", "Maize", "Sugarcane", "Tomato", "Onion")
    val states = listOf("Maharashtra", "Karnataka", "Madhya Pradesh", "Gujarat", "Punjab", "Haryana", "Rajasthan", "Andhra Pradesh")
    val factors = listOf("Drought", "Flood", "Hailstorm", "Unseasonal Rain", "Pest/Animals", "Fire")
    val categories = listOf("Small & Marginal (< 5 Acres)", "Medium (5 - 10 Acres)", "Large (> 10 Acres)", "Tenant / Sharecropper")

    var selectedState by remember { mutableStateOf("Maharashtra") }
    var selectedCrop by remember { mutableStateOf("Soybean") }
    var selectedFactor by remember { mutableStateOf("Drought") }
    var acreageStr by remember { mutableStateOf("4.0") }
    var lossPctStr by remember { mutableStateOf("45.0") }
    var intimationHrsStr by remember { mutableStateOf("48") }
    var isInsured by remember { mutableStateOf("Yes") }
    var selectedCategory by remember { mutableStateOf("Small & Marginal (< 5 Acres)") }

    var stateExpanded by remember { mutableStateOf(false) }
    var cropExpanded by remember { mutableStateOf(false) }
    var factorExpanded by remember { mutableStateOf(false) }
    var catExpanded by remember { mutableStateOf(false) }

    val activeResult by viewModel.activeEligibilityResult.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Govt Benefit Eligibility", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("elig_back_btn")) {
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
                .testTag("benefit_eligibility_screen"),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ModuleHeroHeader(
                    iconEmoji = "🏛️",
                    title = "Govt Crop Loss Benefits",
                    subtitle = "Verify statutory PMFBY & SDRF relief criteria and get your exact local submission checklist."
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
                            text = "Claim Qualification Assessment",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        // State & Crop
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            ExposedDropdownMenuBox(
                                expanded = stateExpanded,
                                onExpandedChange = { stateExpanded = !stateExpanded },
                                modifier = Modifier.weight(1f)
                            ) {
                                OutlinedTextField(
                                    value = selectedState,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("State") },
                                    modifier = Modifier.menuAnchor().fillMaxWidth().testTag("elig_state_dropdown")
                                )
                                ExposedDropdownMenu(expanded = stateExpanded, onDismissRequest = { stateExpanded = false }) {
                                    states.forEach { s ->
                                        DropdownMenuItem(text = { Text(s) }, onClick = { selectedState = s; stateExpanded = false })
                                    }
                                }
                            }

                            ExposedDropdownMenuBox(
                                expanded = cropExpanded,
                                onExpandedChange = { cropExpanded = !cropExpanded },
                                modifier = Modifier.weight(1f)
                            ) {
                                OutlinedTextField(
                                    value = selectedCrop,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Crop") },
                                    modifier = Modifier.menuAnchor().fillMaxWidth().testTag("elig_crop_dropdown")
                                )
                                ExposedDropdownMenu(expanded = cropExpanded, onDismissRequest = { cropExpanded = false }) {
                                    crops.forEach { c ->
                                        DropdownMenuItem(text = { Text(c) }, onClick = { selectedCrop = c; cropExpanded = false })
                                    }
                                }
                            }
                        }

                        // Calamity & Acreage
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            ExposedDropdownMenuBox(
                                expanded = factorExpanded,
                                onExpandedChange = { factorExpanded = !factorExpanded },
                                modifier = Modifier.weight(1f)
                            ) {
                                OutlinedTextField(
                                    value = selectedFactor,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Calamity") },
                                    modifier = Modifier.menuAnchor().fillMaxWidth().testTag("elig_factor_dropdown")
                                )
                                ExposedDropdownMenu(expanded = factorExpanded, onDismissRequest = { factorExpanded = false }) {
                                    factors.forEach { f ->
                                        DropdownMenuItem(text = { Text(f) }, onClick = { selectedFactor = f; factorExpanded = false })
                                    }
                                }
                            }

                            OutlinedTextField(
                                value = acreageStr,
                                onValueChange = { acreageStr = it },
                                label = { Text("Acreage") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.weight(1f).testTag("elig_acreage_input")
                            )
                        }

                        // Loss % and Intimation Hours
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            OutlinedTextField(
                                value = lossPctStr,
                                onValueChange = { lossPctStr = it },
                                label = { Text("Loss % (≥33%)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.weight(1f).testTag("elig_loss_input")
                            )
                            OutlinedTextField(
                                value = intimationHrsStr,
                                onValueChange = { intimationHrsStr = it },
                                label = { Text("Intimation (Hrs)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f).testTag("elig_hours_input")
                            )
                        }

                        // Insured Switch
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Insured under PMFBY?", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                                Text("Active enrollment for current season", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Switch(
                                checked = isInsured == "Yes",
                                onCheckedChange = { isInsured = if (it) "Yes" else "No" },
                                modifier = Modifier.testTag("elig_insured_switch")
                            )
                        }

                        // Farmer Category Dropdown
                        ExposedDropdownMenuBox(
                            expanded = catExpanded,
                            onExpandedChange = { catExpanded = !catExpanded }
                        ) {
                            OutlinedTextField(
                                value = selectedCategory,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Farmer Category") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = catExpanded) },
                                modifier = Modifier.menuAnchor().fillMaxWidth().testTag("elig_category_dropdown")
                            )
                            ExposedDropdownMenu(expanded = catExpanded, onDismissRequest = { catExpanded = false }) {
                                categories.forEach { cat ->
                                    DropdownMenuItem(text = { Text(cat) }, onClick = { selectedCategory = cat; catExpanded = false })
                                }
                            }
                        }

                        Button(
                            onClick = {
                                val ac = acreageStr.toDoubleOrNull() ?: 4.0
                                val lp = lossPctStr.toDoubleOrNull() ?: 45.0
                                val ih = intimationHrsStr.toIntOrNull() ?: 48
                                viewModel.evaluateEligibility(
                                    selectedState, selectedCrop, selectedFactor, ac, lp, ih, isInsured, selectedCategory
                                )
                            },
                            modifier = Modifier.fillMaxWidth().height(50.dp).testTag("elig_evaluate_btn"),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.AccountBalance, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Evaluate Scheme Eligibility", fontWeight = FontWeight.Bold)
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
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, if (res.isEligible) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error),
                        modifier = Modifier.fillMaxWidth().testTag("elig_result_card")
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = if (res.isEligible) "ELIGIBLE FOR BENEFIT" else "NOT CURRENTLY ELIGIBLE",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = if (res.isEligible) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                                    )
                                    Text(
                                        text = res.schemeName,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                StatusBadge(
                                    text = "${res.confidence}% Conf.",
                                    isPositive = res.isEligible
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Statutory Criteria Audit:", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))

                            // Criteria Check Items
                            res.criteriaChecks.forEach { check ->
                                Card(
                                    shape = RoundedCornerShape(10.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (check.isMet) ForestGreenContainer.copy(alpha = 0.4f) else Color(0xFFFFEBEE)
                                    ),
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                                ) {
                                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            if (check.isMet) Icons.Default.Check else Icons.Default.Close,
                                            contentDescription = null,
                                            tint = if (check.isMet) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column {
                                            Text(check.title, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                                            Text(check.description, style = MaterialTheme.typography.bodySmall)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            Divider(color = CardBorder)
                            Spacer(modifier = Modifier.height(12.dp))

                            Text("📑 Mandatory Document Submission Checklist:", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))

                            res.documents.forEach { doc ->
                                Row(
                                    modifier = Modifier.padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(doc.icon, fontSize = 20.sp)
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(doc.title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                                        Text(doc.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            Divider(color = CardBorder)
                            Spacer(modifier = Modifier.height(12.dp))

                            Text("🏢 Nearest Government Submission Office:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(res.submissionOffice, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}
