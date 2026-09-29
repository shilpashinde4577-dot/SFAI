package com.example.engine

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.round

data class MandiPricePoint(
    val monthLabel: String,
    val price: Double,
    val isForecast: Boolean = false
)

data class MarketForecastResult(
    val crop: String,
    val state: String,
    val currentPrice: Double,
    val regionalBasePrice: Double,
    val historicalPoints: List<MandiPricePoint>,
    val currentPoint: MandiPricePoint,
    val forecastPoints: List<MandiPricePoint>,
    val trend: String, // "Bullish (Upward)", "Bearish (Downward)", "Stable"
    val percentChange: Double,
    val mandiInsight: String
)

object MarketIntelligenceEngine {

    val BASE_PRICES = mapOf(
        "Rice" to 32.0,
        "Wheat" to 24.5,
        "Maize" to 21.0,
        "Cotton" to 65.0,
        "Sugarcane" to 3.5,
        "Coffee" to 210.0,
        "Jute" to 55.0,
        "Millets" to 28.0,
        "Tomato" to 25.0,
        "Onion" to 20.0,
        "Potato" to 18.0
    )

    val STATE_MODIFIERS = mapOf(
        "Karnataka" to 1.05,
        "Maharashtra" to 1.10,
        "Punjab" to 0.95,
        "Haryana" to 0.96,
        "Andhra Pradesh" to 1.02,
        "Uttar Pradesh" to 0.94,
        "Tamil Nadu" to 1.08,
        "Gujarat" to 1.03,
        "West Bengal" to 1.00,
        "Bihar" to 0.92,
        "Madhya Pradesh" to 0.98,
        "Rajasthan" to 1.04,
        "Kerala" to 1.15,
        "Telangana" to 1.02,
        "Odisha" to 0.96,
        "Assam" to 0.98
    )

    fun analyze(crop: String, state: String, currentPrice: Double): MarketForecastResult {
        val base = BASE_PRICES[crop] ?: 25.0
        val mod = STATE_MODIFIERS[state] ?: 1.0
        val regionalBase = round(base * mod * 100) / 100

        val cal = Calendar.getInstance()
        val monthFmt = SimpleDateFormat("MMM yyyy", Locale.US)

        // Past 3 months
        val past = mutableListOf<MandiPricePoint>()
        for (i in 3 downTo 1) {
            val c = Calendar.getInstance().apply { add(Calendar.MONTH, -i) }
            val variance = 1.0 + (((i * 17) % 11 - 5) / 100.0) // deterministic pseudo-fluctuation ±5%
            val p = round(currentPrice * variance * 10) / 10
            past.add(MandiPricePoint(monthFmt.format(c.time), p, isForecast = false))
        }

        val currentPoint = MandiPricePoint(monthFmt.format(cal.time), currentPrice, isForecast = false)

        // Future 3 months
        val future = mutableListOf<MandiPricePoint>()
        var running = currentPrice
        val stateFactor = (mod - 1.0) * 0.5
        for (i in 1..3) {
            val c = Calendar.getInstance().apply { add(Calendar.MONTH, i) }
            val seasonalRate = 1.0 + (0.015 * i) + (stateFactor * 0.01)
            running = round((running * seasonalRate) * 10) / 10
            future.add(MandiPricePoint(monthFmt.format(c.time), running, isForecast = true))
        }

        val finalFuture = future.last().price
        val changePct = round(((finalFuture - currentPrice) / currentPrice) * 1000) / 10
        val trend = when {
            changePct > 1.5 -> "Bullish (Upward)"
            changePct < -1.5 -> "Bearish (Downward)"
            else -> "Stable Market"
        }

        val insight = when {
            changePct > 2.0 -> "Expected mandi arrivals will tighten over the next quarter. Holding inventory or staggered selling may maximize net returns."
            changePct < -2.0 -> "Peak harvest arrivals from neighboring production hubs will exert downward pressure on spot rates. Forward contracts advised."
            else -> "Market demand and supply indicators remain balanced in $state APMC mandis. Regular dispatch schedule recommended."
        }

        return MarketForecastResult(
            crop = crop,
            state = state,
            currentPrice = currentPrice,
            regionalBasePrice = regionalBase,
            historicalPoints = past,
            currentPoint = currentPoint,
            forecastPoints = future,
            trend = trend,
            percentChange = changePct,
            mandiInsight = insight
        )
    }
}
