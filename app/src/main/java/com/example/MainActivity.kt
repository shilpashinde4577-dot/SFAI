package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.FarmingViewModel
import com.example.ui.ScreenDestination
import com.example.ui.screens.*
import com.example.ui.theme.SmartFarmTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartFarmTheme {
                SmartFarmApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmartFarmApp(viewModel: FarmingViewModel = viewModel()) {
    val isLoggedIn by viewModel.isLoggedIn.collectAsStateWithLifecycle()
    var currentScreen by remember { mutableStateOf<ScreenDestination>(ScreenDestination.Home) }

    if (!isLoggedIn) {
        AuthScreen(viewModel = viewModel)
    } else {
        Scaffold(
            contentWindowInsets = WindowInsets.safeDrawing,
            bottomBar = {
            NavigationBar(
                modifier = Modifier.testTag("bottom_nav_bar")
            ) {
                NavigationBarItem(
                    selected = currentScreen == ScreenDestination.Home,
                    onClick = { currentScreen = ScreenDestination.Home },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    modifier = Modifier.testTag("nav_item_home")
                )
                NavigationBarItem(
                    selected = currentScreen in listOf(
                        ScreenDestination.CropRecommendation,
                        ScreenDestination.DiseaseDetection,
                        ScreenDestination.FertilizerGuide,
                        ScreenDestination.WeatherPrediction,
                        ScreenDestination.MarketAnalysis,
                        ScreenDestination.CropCompensation,
                        ScreenDestination.BenefitEligibility
                    ),
                    onClick = { currentScreen = ScreenDestination.CropRecommendation },
                    icon = { Icon(Icons.Default.Agriculture, contentDescription = "Tools") },
                    label = { Text("Tools") },
                    modifier = Modifier.testTag("nav_item_tools")
                )
                NavigationBarItem(
                    selected = currentScreen == ScreenDestination.CombinedReport,
                    onClick = { currentScreen = ScreenDestination.CombinedReport },
                    icon = { Icon(Icons.Default.Description, contentDescription = "Report") },
                    label = { Text("Report") },
                    modifier = Modifier.testTag("nav_item_report")
                )
                NavigationBarItem(
                    selected = currentScreen == ScreenDestination.Profile,
                    onClick = { currentScreen = ScreenDestination.Profile },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile") },
                    modifier = Modifier.testTag("nav_item_profile")
                )
                NavigationBarItem(
                    selected = currentScreen == ScreenDestination.AdminDashboard,
                    onClick = { currentScreen = ScreenDestination.AdminDashboard },
                    icon = { Icon(Icons.Default.AdminPanelSettings, contentDescription = "Admin") },
                    label = { Text("Admin") },
                    modifier = Modifier.testTag("nav_item_admin")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                ScreenDestination.Home -> HomeScreen(
                    viewModel = viewModel,
                    onNavigate = { currentScreen = it }
                )
                ScreenDestination.CropRecommendation -> CropRecommendationScreen(
                    viewModel = viewModel,
                    onBack = { currentScreen = ScreenDestination.Home }
                )
                ScreenDestination.DiseaseDetection -> DiseaseDetectionScreen(
                    viewModel = viewModel,
                    onBack = { currentScreen = ScreenDestination.Home }
                )
                ScreenDestination.FertilizerGuide -> FertilizerGuideScreen(
                    viewModel = viewModel,
                    onBack = { currentScreen = ScreenDestination.Home }
                )
                ScreenDestination.WeatherPrediction -> WeatherPredictionScreen(
                    viewModel = viewModel,
                    onBack = { currentScreen = ScreenDestination.Home }
                )
                ScreenDestination.MarketAnalysis -> MarketAnalysisScreen(
                    viewModel = viewModel,
                    onBack = { currentScreen = ScreenDestination.Home }
                )
                ScreenDestination.CropCompensation -> CropCompensationScreen(
                    viewModel = viewModel,
                    onBack = { currentScreen = ScreenDestination.Home }
                )
                ScreenDestination.BenefitEligibility -> BenefitEligibilityScreen(
                    viewModel = viewModel,
                    onBack = { currentScreen = ScreenDestination.Home }
                )
                ScreenDestination.CombinedReport -> CombinedReportScreen(
                    viewModel = viewModel,
                    onBack = { currentScreen = ScreenDestination.Home }
                )
                ScreenDestination.Profile -> ProfileScreen(
                    viewModel = viewModel,
                    onBack = { currentScreen = ScreenDestination.Home }
                )
                ScreenDestination.AdminDashboard -> AdminDashboardScreen(
                    viewModel = viewModel,
                    onBack = { currentScreen = ScreenDestination.Home }
                )
                ScreenDestination.HelpGuide -> HelpGuideScreen(
                    onBack = { currentScreen = ScreenDestination.Home },
                    onNavigate = { currentScreen = it }
                )
            }
        }
    }
}
}
