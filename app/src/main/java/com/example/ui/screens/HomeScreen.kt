package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.FarmingViewModel
import com.example.ui.ScreenDestination
import com.example.ui.components.SectionHeader
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*

data class ToolItem(
    val title: String,
    val subtitle: String,
    val emoji: String,
    val destination: ScreenDestination,
    val tag: String,
    val badge: String = "ML Model"
)

@Composable
fun HomeScreen(
    viewModel: FarmingViewModel,
    onNavigate: (ScreenDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val latestCrop by viewModel.latestCropPrediction.collectAsStateWithLifecycle()
    val latestDisease by viewModel.latestDiseaseDetection.collectAsStateWithLifecycle()
    val latestWeather by viewModel.latestWeather.collectAsStateWithLifecycle()

    val tools = listOf(
        ToolItem("Crop Recommendation", "AI matched to temperature, humidity & soil moisture", "🌱", ScreenDestination.CropRecommendation, "tool_crop"),
        ToolItem("Disease Detection", "Visual analysis of leaf foliage with treatment plans", "🔬", ScreenDestination.DiseaseDetection, "tool_disease"),
        ToolItem("Fertilizer Guide", "Exact kg/acre quantities of Urea, DAP, MOP & lime/gypsum", "🧪", ScreenDestination.FertilizerGuide, "tool_fertilizer"),
        ToolItem("Weather Forecast", "Local agro-meteorology and field activity advisories", "☁️", ScreenDestination.WeatherPrediction, "tool_weather"),
        ToolItem("Market Intelligence", "Indian Mandi regional price benchmarks & 3-month trends", "📊", ScreenDestination.MarketAnalysis, "tool_market"),
        ToolItem("Crop Compensation", "PMFBY compensation estimation based on damage factors", "💰", ScreenDestination.CropCompensation, "tool_compensation"),
        ToolItem("Govt Benefit Eligibility", "Check PMFBY/SDRF scheme criteria & required documents", "🏛️", ScreenDestination.BenefitEligibility, "tool_eligibility")
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Welcome Card
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier.fillMaxWidth().testTag("hero_banner")
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(ForestGreen40, Color(0xFF00390B))
                            )
                        )
                        .padding(24.dp)
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "🌾", fontSize = 22.sp)
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "SmartFarm AI",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Namaste, ${currentUser?.fullName ?: "Farmer"}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.White.copy(alpha = 0.85f)
                                    )
                                }
                            }
                            StatusBadge(
                                text = (currentUser?.role ?: "user").uppercase(),
                                isPositive = true,
                                modifier = Modifier
                            )
                        }

                        Spacer(modifier = Modifier.height(18.dp))
                        Text(
                            text = "Harness the power of Machine Learning for smarter, resilient agriculture.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White.copy(alpha = 0.95f),
                            lineHeight = 22.sp
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Button(
                                onClick = { onNavigate(ScreenDestination.CropRecommendation) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White,
                                    contentColor = ForestGreen40
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.testTag("get_started_btn")
                            ) {
                                Text("Get Started", fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Start", modifier = Modifier.size(16.dp))
                            }

                            OutlinedButton(
                                onClick = { onNavigate(ScreenDestination.CombinedReport) },
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.6f)),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.testTag("view_report_btn")
                            ) {
                                Text("Full Report")
                            }
                        }
                    }
                }
            }
        }

        // Quick Agricultural Health Overview
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth().testTag("quick_stats_card")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "🌱 Crop", style = MaterialTheme.typography.labelMedium)
                        Text(
                            text = latestCrop?.predictedCrop ?: "Rice",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Divider(modifier = Modifier.height(32.dp).width(1.dp))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "🔬 Health", style = MaterialTheme.typography.labelMedium)
                        Text(
                            text = latestDisease?.detectedDisease ?: "Leaf Spot",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                    Divider(modifier = Modifier.height(32.dp).width(1.dp))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "☁️ Weather", style = MaterialTheme.typography.labelMedium)
                        Text(
                            text = latestWeather?.predictedWeather?.substringBefore(" ") ?: "Sunny",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = AmberGold40
                        )
                    }
                }
            }
        }

        // Tools Section Header
        item {
            SectionHeader(
                title = "Agricultural Intelligence Tools",
                subtitle = "7 specialized ML and agronomy decision modules"
            )
        }

        // List of All 7 Tools
        items(tools.size) { index ->
            val tool = tools[index]
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigate(tool.destination) }
                    .testTag(tool.tag)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(ForestGreenContainer.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = tool.emoji, fontSize = 24.sp)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = tool.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = tool.subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Open ${tool.title}",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Quick Help & Demo Guide Banner
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigate(ScreenDestination.HelpGuide) }
                    .testTag("help_banner_card")
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "💡", fontSize = 28.sp)
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Interactive Demo Guide",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        Text(
                            text = "Tap to load verified demo sensor values & showcase all modules",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.85f)
                        )
                    }
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Open Demo Guide",
                        tint = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
