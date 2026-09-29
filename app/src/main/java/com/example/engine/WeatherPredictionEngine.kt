package com.example.engine

data class WeatherForecastResult(
    val condition: String,
    val description: String,
    val farmingAdvisory: String,
    val irrigationAdvice: String,
    val sprayingAdvice: String,
    val iconSymbol: String
)

object WeatherPredictionEngine {

    fun predict(temp: Double, humidity: Double, moisture: Double): WeatherForecastResult {
        return when {
            humidity >= 88.0 && moisture >= 80.0 && temp <= 22.0 -> {
                WeatherForecastResult(
                    condition = "Stormy",
                    description = "Severe convective atmospheric disturbance with high precipitation and squalls expected.",
                    farmingAdvisory = "Secure greenhouse covers, reinforce young sapling stakes, and clear field drainage ditches.",
                    irrigationAdvice = "Cease all irrigation immediately to prevent waterlogging and root hypoxia.",
                    sprayingAdvice = "Postpone chemical and foliar pesticide spraying due to high wash-off risk.",
                    iconSymbol = "⛈️"
                )
            }
            humidity >= 80.0 || (moisture >= 70.0 && humidity >= 68.0) -> {
                WeatherForecastResult(
                    condition = "Rainy",
                    description = "Humid air mass with significant cloud buildup and imminent rainfall.",
                    farmingAdvisory = "Favorable for paddy transplanting. Avoid harvesting standing grains until skies clear.",
                    irrigationAdvice = "Halt artificial irrigation; rely on natural precipitation.",
                    sprayingAdvice = "Do not spray foliar fertilizers or contact insecticides.",
                    iconSymbol = "🌧️"
                )
            }
            humidity in 60.0..80.0 -> {
                WeatherForecastResult(
                    condition = "Cloudy / Overcast",
                    description = "Moderate atmospheric humidity with broken stratocumulus cover and diffuse sunlight.",
                    farmingAdvisory = "Excellent conditions for field tilling, intercultural weeding, and general farm scouting.",
                    irrigationAdvice = "Apply light supplementary drip irrigation if soil moisture is under 50%.",
                    sprayingAdvice = "Ideal window for applying systemic fungicides or micronutrient foliar sprays.",
                    iconSymbol = "⛅"
                )
            }
            else -> {
                WeatherForecastResult(
                    condition = "Sunny / Pleasant",
                    description = "Clear skies with high solar radiation and low localized atmospheric moisture.",
                    farmingAdvisory = "Optimal conditions for crop harvesting, grain sun-drying, and threshing operations.",
                    irrigationAdvice = "Schedule evening drip or furrow irrigation to replace high evapotranspiration losses.",
                    sprayingAdvice = "Safe for morning spraying before temperatures exceed 30°C.",
                    iconSymbol = "☀️"
                )
            }
        }
    }
}
