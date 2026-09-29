package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
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
fun CombinedReportScreen(
    viewModel: FarmingViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }
    val context = LocalContext.current

    val cropRecord by viewModel.latestCropPrediction.collectAsStateWithLifecycle()
    val diseaseRecord by viewModel.latestDiseaseDetection.collectAsStateWithLifecycle()
    val fertRecord by viewModel.latestFertilizer.collectAsStateWithLifecycle()
    val weatherRecord by viewModel.latestWeather.collectAsStateWithLifecycle()
    val marketRecord by viewModel.latestMarket.collectAsStateWithLifecycle()
    val compRecord by viewModel.latestCompensation.collectAsStateWithLifecycle()
    val eligRecord by viewModel.latestEligibility.collectAsStateWithLifecycle()

    fun copyReportToClipboard() {
        val summary = buildString {
            appendLine("=== SMARTFARM AI COMPREHENSIVE DOSSIER ===")
            appendLine("🌱 Crop Recommendation: ${cropRecord?.predictedCrop ?: "Rice"}")
            appendLine("🔬 Leaf Health: ${diseaseRecord?.detectedDisease ?: "Healthy"} (${diseaseRecord?.confidence ?: 90.0}%)")
            appendLine("🧪 Fertilizer Plan: ${fertRecord?.fertilizerName ?: "Urea & DAP Mix"} (Urea: ${fertRecord?.ureaKgPerAcre ?: 0.0} kg/acre, DAP: ${fertRecord?.dapKgPerAcre ?: 0.0} kg/acre)")
            appendLine("☁️ Weather Forecast: ${weatherRecord?.predictedWeather ?: "Sunny / Pleasant"}")
            appendLine("📊 Mandi Spot Rate: ${marketRecord?.cropName ?: "Rice"} in ${marketRecord?.state ?: "Karnataka"} @ ₹${marketRecord?.currentPrice ?: 32.0}/kg")
            appendLine("💰 Loss Claim Estimate: ₹${compRecord?.totalCompensation?.toLong() ?: 62500} for ${compRecord?.cropName ?: "Cotton"}")
            appendLine("🏛️ Govt Benefit Status: ${if (eligRecord?.isEligible == true) "ELIGIBLE" else "UNDER REVIEW"}")
            appendLine("===========================================")
        }
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText("SmartFarm Report", summary))
        Toast.makeText(context, "Full agricultural report copied to clipboard!", Toast.LENGTH_SHORT).show()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Comprehensive Agricultural Dossier", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("report_back_btn")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { copyReportToClipboard() }, modifier = Modifier.testTag("share_report_btn")) {
                        Icon(Icons.Default.Share, contentDescription = "Share Report")
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
                .testTag("combined_report_screen"),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ModuleHeroHeader(
                    iconEmoji = "📑",
                    title = "Consolidated Farm Audit",
                    subtitle = "Unified intelligence summary integrating predictions across all 7 farm decision modules."
                )
            }

            // Export Action Card
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Export Dossier", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                            Text("Copy full formatted audit report to share with agricultural officers", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f))
                        }
                        Button(
                            onClick = { copyReportToClipboard() },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            modifier = Modifier.testTag("copy_report_action_btn")
                        ) {
                            Text("Copy")
                        }
                    }
                }
            }

            // Module 1: Crop Recommendation Card
            item {
                ReportModuleCard(
                    emoji = "🌱",
                    moduleTitle = "1. Crop Recommendation",
                    primaryText = cropRecord?.predictedCrop ?: "Rice",
                    secondaryText = "Inputs: Temp ${cropRecord?.temperature ?: 25.0}°C | Humidity ${cropRecord?.humidity ?: 80.0}% | Soil Moisture ${cropRecord?.soilMoisture ?: 75.0}%",
                    badge = "Optimal Match"
                )
            }

            // Module 2: Disease Detection Card
            item {
                ReportModuleCard(
                    emoji = "🔬",
                    moduleTitle = "2. Foliar Pathology Detection",
                    primaryText = diseaseRecord?.detectedDisease ?: "Leaf Spot",
                    secondaryText = "Confidence: ${diseaseRecord?.confidence ?: 88.5}% | Rx: ${diseaseRecord?.recommendation ?: "Apply Mancozeb 75% WP @ 2g/L"}",
                    badge = "Diagnosis"
                )
            }

            // Module 3: Fertilizer Guidance Card
            item {
                ReportModuleCard(
                    emoji = "🧪",
                    moduleTitle = "3. Fertilizer Requirements",
                    primaryText = "${fertRecord?.fertilizerName ?: "Urea & DAP Mix"} for ${fertRecord?.cropName ?: "Wheat"}",
                    secondaryText = "Dosage: Urea: ${fertRecord?.ureaKgPerAcre ?: 35.0} kg/acre | DAP: ${fertRecord?.dapKgPerAcre ?: 25.0} kg/acre | MOP: ${fertRecord?.mopKgPerAcre ?: 15.0} kg/acre",
                    badge = "Agronomy Plan"
                )
            }

            // Module 4: Weather Forecast Card
            item {
                ReportModuleCard(
                    emoji = "☁️",
                    moduleTitle = "4. Microclimate Advisory",
                    primaryText = weatherRecord?.predictedWeather ?: "Sunny / Pleasant",
                    secondaryText = weatherRecord?.advisory ?: "Optimal conditions for crop harvesting and threshing operations.",
                    badge = "Field Window"
                )
            }

            // Module 5: Market Intelligence Card
            item {
                ReportModuleCard(
                    emoji = "📊",
                    moduleTitle = "5. Mandi Price Intelligence",
                    primaryText = "${marketRecord?.cropName ?: "Rice"} in ${marketRecord?.state ?: "Karnataka"} @ ₹${marketRecord?.currentPrice ?: 32.0}/kg",
                    secondaryText = "Trend: ${marketRecord?.trend ?: "Bullish (Upward)"} | Q1 Forecast: ₹${marketRecord?.month1Price ?: 32.5}/kg -> ₹${marketRecord?.month3Price ?: 33.8}/kg",
                    badge = "APMC Mandi"
                )
            }

            // Module 6: Crop Compensation Card
            item {
                ReportModuleCard(
                    emoji = "💰",
                    moduleTitle = "6. Disaster Compensation Claim",
                    primaryText = "₹${compRecord?.totalCompensation?.toLong() ?: 62500} Total Claim",
                    secondaryText = "Rate: ₹${compRecord?.compensationPerAcre?.toLong() ?: 12500}/acre | Cause: ${compRecord?.damageFactor ?: "Flood"} (${compRecord?.cropName ?: "Cotton"} in ${compRecord?.state ?: "Karnataka"})",
                    badge = "PMFBY Claim"
                )
            }

            // Module 7: Govt Benefit Eligibility Card
            item {
                ReportModuleCard(
                    emoji = "🏛️",
                    moduleTitle = "7. Government Scheme Eligibility",
                    primaryText = if (eligRecord?.isEligible == false) "NOT CURRENTLY ELIGIBLE" else "ELIGIBLE FOR FINANCIAL RELIEF",
                    secondaryText = "Scheme: ${eligRecord?.criteriaJson ?: "Pradhan Mantri Fasal Bima Yojana (PMFBY)"} | Confidence: ${eligRecord?.confidence ?: 91.2}%",
                    badge = if (eligRecord?.isEligible == false) "Review" else "Qualified"
                )
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

@Composable
fun ReportModuleCard(
    emoji: String,
    moduleTitle: String,
    primaryText: String,
    secondaryText: String,
    badge: String
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(emoji, fontSize = 22.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = moduleTitle,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
                StatusBadge(text = badge, isPositive = true)
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = primaryText,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = secondaryText,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
