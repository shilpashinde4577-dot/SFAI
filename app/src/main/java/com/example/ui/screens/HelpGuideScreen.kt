package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.ScreenDestination
import com.example.ui.components.ModuleHeroHeader
import com.example.ui.components.SectionHeader
import com.example.ui.theme.CardBorder
import com.example.ui.theme.ForestGreenContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpGuideScreen(
    onBack: () -> Unit,
    onNavigate: (ScreenDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("SmartFarm AI Demo Guide", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("help_back_btn")) {
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
                .testTag("help_guide_screen"),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ModuleHeroHeader(
                    iconEmoji = "🌾",
                    title = "Interactive Demo Guide",
                    subtitle = "Reference values and agronomic benchmarks for demonstrating all 7 modules to your mentor."
                )
            }

            // 1. Crop Recommendation
            item {
                DemoSectionCard(
                    title = "1. Crop Recommendation",
                    description = "Predicts optimal crop planting based on temperature, humidity, and soil moisture sensor inputs.",
                    sampleData = "• Rice: 25.0°C | 80.0% Hum | 75.0% Moist\n• Wheat: 20.0°C | 60.0% Hum | 50.0% Moist\n• Maize: 27.5°C | 70.0% Hum | 55.0% Moist\n• Sugarcane: 30.0°C | 85.0% Hum | 80.0% Moist",
                    onOpen = { onNavigate(ScreenDestination.CropRecommendation) }
                )
            }

            // 2. Fertilizer Guide
            item {
                DemoSectionCard(
                    title = "2. Fertilizer Guide",
                    description = "Calculates exact Urea, DAP, and MOP in kg per acre + Lime/Gypsum soil pH amendments.",
                    sampleData = "• Needs Urea (Low N): N=10, P=50, K=50, pH=6.5\n• Needs DAP (Low P): N=60, P=5, K=50, pH=6.5\n• Needs MOP (Low K): N=60, P=50, K=8, pH=6.5\n• Needs Lime (Acidic): N=60, P=50, K=50, pH=4.5\n• Needs Gypsum (Alkaline): N=60, P=50, K=50, pH=8.5",
                    onOpen = { onNavigate(ScreenDestination.FertilizerGuide) }
                )
            }

            // 3. Leaf Disease Detection
            item {
                DemoSectionCard(
                    title = "3. Leaf Disease Detection",
                    description = "Diagnoses 5 foliage pathology classes from photographs with targeted chemical & bio-fungicides.",
                    sampleData = "• Healthy: Natural foliage, no chemical intervention needed\n• Leaf Blight: Mancozeb / Copper Oxychloride spray\n• Leaf Spot: Mancozeb 75% WP @ 2g/L\n• Rust: Propiconazole 25% EC @ 1ml/L\n• Powdery Mildew: Wettable Sulphur 80% WP",
                    onOpen = { onNavigate(ScreenDestination.DiseaseDetection) }
                )
            }

            // 4. Weather Prediction
            item {
                DemoSectionCard(
                    title = "4. Weather Prediction",
                    description = "Predicts Sunny, Rainy, Cloudy, or Stormy states with actionable agricultural advisories.",
                    sampleData = "• Sunny: 32.0°C | 35.0% Hum | 20.0% Moist\n• Rainy: 23.0°C | 92.0% Hum | 85.0% Moist\n• Cloudy: 26.0°C | 75.0% Hum | 50.0% Moist\n• Stormy: 21.0°C | 95.0% Hum | 90.0% Moist",
                    onOpen = { onNavigate(ScreenDestination.WeatherPrediction) }
                )
            }

            // 5. Market Analysis
            item {
                DemoSectionCard(
                    title = "5. Mandi Price Intelligence",
                    description = "Simulates regional Indian Mandi rates (₹/kg) across states with 3-month forecast trends.",
                    sampleData = "• Select 'Karnataka' and 'Maize' @ ₹21.0/kg spot price to view regional APMC benchmark comparisons and quarterly forecast trends.",
                    onOpen = { onNavigate(ScreenDestination.MarketAnalysis) }
                )
            }

            // 6. Crop Loss Compensation
            item {
                DemoSectionCard(
                    title = "6. Disaster Compensation Claim",
                    description = "Estimates relief settlement payouts per acre based on PMFBY Scale of Finance norms.",
                    sampleData = "• Select 'Flood' factor and 5.0 Acres in Karnataka for Cotton to generate an actuarial claim estimation of ₹62,500.",
                    onOpen = { onNavigate(ScreenDestination.CropCompensation) }
                )
            }

            // 7. Govt Benefit Eligibility
            item {
                DemoSectionCard(
                    title = "7. Govt Benefit Eligibility (PMFBY / SDRF)",
                    description = "Audits statutory 33% minimum loss and 72-hour intimation criteria with submission checklist.",
                    sampleData = "• Loss ≥ 33% mandatory threshold\n• Intimation within 72 hours\n• Insured under PMFBY or Severe Calamity (≥50% loss for SDRF relief)",
                    onOpen = { onNavigate(ScreenDestination.BenefitEligibility) }
                )
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

@Composable
fun DemoSectionCard(
    title: String,
    description: String,
    sampleData: String,
    onOpen: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(10.dp))

            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = sampleData,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(12.dp),
                    lineHeight = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            TextButton(onClick = onOpen, modifier = Modifier.align(Alignment.End)) {
                Text("Open Module")
                Spacer(modifier = Modifier.width(4.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
            }
        }
    }
}
