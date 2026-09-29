package com.example.ui

sealed interface ScreenDestination {
    data object Home : ScreenDestination
    data object CropRecommendation : ScreenDestination
    data object DiseaseDetection : ScreenDestination
    data object FertilizerGuide : ScreenDestination
    data object WeatherPrediction : ScreenDestination
    data object MarketAnalysis : ScreenDestination
    data object CropCompensation : ScreenDestination
    data object BenefitEligibility : ScreenDestination
    data object CombinedReport : ScreenDestination
    data object Profile : ScreenDestination
    data object AdminDashboard : ScreenDestination
    data object HelpGuide : ScreenDestination
}
