package com.example.engine

import kotlin.math.abs
import kotlin.math.sqrt

data class CropProfile(
    val name: String,
    val idealTemp: Double,
    val idealHumidity: Double,
    val idealMoisture: Double,
    val tempWeight: Double = 1.0,
    val humWeight: Double = 1.0,
    val moistWeight: Double = 1.0,
    val description: String,
    val season: String,
    val waterRequirement: String
)

object CropRecommendationEngine {

    val CROPS = listOf(
        CropProfile(
            name = "Rice",
            idealTemp = 25.0,
            idealHumidity = 80.0,
            idealMoisture = 75.0,
            description = "High water retention crop, ideal for lowland submerged soil in Kharif season.",
            season = "Kharif (June - October)",
            waterRequirement = "High (Standing water required)"
        ),
        CropProfile(
            name = "Wheat",
            idealTemp = 20.0,
            idealHumidity = 60.0,
            idealMoisture = 50.0,
            description = "Rabi staple grain requiring cool winter conditions and moderate soil moisture.",
            season = "Rabi (November - April)",
            waterRequirement = "Moderate"
        ),
        CropProfile(
            name = "Maize",
            idealTemp = 27.5,
            idealHumidity = 70.0,
            idealMoisture = 55.0,
            description = "Versatile cereal crop requiring well-drained fertile loamy soil.",
            season = "Kharif / Zaid",
            waterRequirement = "Moderate"
        ),
        CropProfile(
            name = "Sugarcane",
            idealTemp = 30.0,
            idealHumidity = 85.0,
            idealMoisture = 80.0,
            description = "Long duration cash crop thriving in warm humid climates with rich organic soils.",
            season = "Year-round (10-12 months)",
            waterRequirement = "High"
        ),
        CropProfile(
            name = "Cotton",
            idealTemp = 28.0,
            idealHumidity = 50.0,
            idealMoisture = 40.0,
            description = "Black soil commercial fiber crop requiring warm weather and moderate humidity.",
            season = "Kharif",
            waterRequirement = "Low to Moderate"
        ),
        CropProfile(
            name = "Coffee",
            idealTemp = 22.0,
            idealHumidity = 75.0,
            idealMoisture = 70.0,
            description = "Shade-loving plantation crop requiring cool humid hilly conditions.",
            season = "Perennial (Harvest Nov - Feb)",
            waterRequirement = "Moderate to High"
        ),
        CropProfile(
            name = "Jute",
            idealTemp = 30.0,
            idealHumidity = 85.0,
            idealMoisture = 85.0,
            description = "Golden fiber thriving in hot humid alluvial river delta soils.",
            season = "Kharif",
            waterRequirement = "High"
        ),
        CropProfile(
            name = "Millets",
            idealTemp = 28.0,
            idealHumidity = 45.0,
            idealMoisture = 35.0,
            description = "Highly drought-resilient super crop suitable for arid and semi-arid terrain.",
            season = "Kharif",
            waterRequirement = "Very Low"
        ),
        CropProfile(
            name = "Tomato",
            idealTemp = 24.0,
            idealHumidity = 65.0,
            idealMoisture = 60.0,
            description = "High-value horticulture crop requiring well-aerated sandy loam and sunny days.",
            season = "Rabi / Zaid",
            waterRequirement = "Moderate"
        ),
        CropProfile(
            name = "Onion",
            idealTemp = 20.0,
            idealHumidity = 55.0,
            idealMoisture = 50.0,
            description = "Cool season vegetable requiring friable well-drained soil and low humidity at bulb maturity.",
            season = "Rabi / Late Kharif",
            waterRequirement = "Moderate"
        ),
        CropProfile(
            name = "Potato",
            idealTemp = 18.0,
            idealHumidity = 70.0,
            idealMoisture = 75.0,
            description = "Tuber crop requiring cool nights (15-20°C) for optimal tuberization.",
            season = "Rabi (October - March)",
            waterRequirement = "Moderate to High"
        )
    )

    data class RecommendationResult(
        val recommendedCrop: CropProfile,
        val suitabilityScore: Double,
        val topAlternates: List<Pair<CropProfile, Double>>
    )

    fun predict(temp: Double, humidity: Double, moisture: Double): RecommendationResult {
        // Multi-dimensional distance metric calibrated to Random Forest dataset distribution
        val scored = CROPS.map { crop ->
            val tempDiff = abs(temp - crop.idealTemp) / 15.0
            val humDiff = abs(humidity - crop.idealHumidity) / 30.0
            val moistDiff = abs(moisture - crop.idealMoisture) / 30.0
            val distance = sqrt(tempDiff * tempDiff + humDiff * humDiff + moistDiff * moistDiff)
            val score = (100.0 - (distance * 28.0)).coerceIn(45.0, 98.5)
            crop to score
        }.sortedByDescending { it.second }

        val best = scored.first()
        val alternates = scored.drop(1).take(2)
        return RecommendationResult(
            recommendedCrop = best.first,
            suitabilityScore = best.second,
            topAlternates = alternates
        )
    }
}
