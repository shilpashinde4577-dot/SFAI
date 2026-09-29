package com.example.engine

import kotlin.math.round

data class CompensationClaimResult(
    val state: String,
    val crop: String,
    val damageFactor: String,
    val acreage: Double,
    val compensationPerAcre: Double,
    val totalClaimAmount: Double,
    val assessmentBreakdown: String,
    val claimGuidelines: String
)

object CompensationCalculatorEngine {

    val DAMAGE_MULTIPLIERS = mapOf(
        "Flood" to 1.25,
        "Hailstorm" to 1.20,
        "Fire" to 1.15,
        "Drought" to 1.05,
        "Pest/Animals" to 0.85,
        "Unseasonal Rain" to 1.10
    )

    val CROP_VALUE_TIERS = mapOf(
        "Cotton" to 18500.0,
        "Sugarcane" to 22000.0,
        "Coffee" to 25000.0,
        "Tomato" to 19500.0,
        "Onion" to 17500.0,
        "Potato" to 18000.0,
        "Rice" to 14500.0,
        "Wheat" to 13500.0,
        "Maize" to 12500.0,
        "Jute" to 14000.0,
        "Millets" to 10500.0
    )

    fun calculate(
        state: String,
        crop: String,
        damageFactor: String,
        acreage: Double
    ): CompensationClaimResult {
        val baseTier = CROP_VALUE_TIERS[crop] ?: 14000.0
        val factorMult = DAMAGE_MULTIPLIERS[damageFactor] ?: 1.0
        val stateAdj = when (state) {
            "Maharashtra", "Karnataka", "Punjab", "Haryana", "Gujarat" -> 1.08
            "Kerala", "Tamil Nadu", "Andhra Pradesh", "Telangana" -> 1.05
            else -> 1.00
        }

        val ratePerAcre = round((baseTier * factorMult * stateAdj) / 100.0) * 100.0
        val total = round(ratePerAcre * acreage)

        val breakdown = "Base Scale of Finance for $crop: ₹${baseTier.toLong()} | Damage Multiplier ($damageFactor): x$factorMult | Regional State Norm ($state): x$stateAdj"

        val guidelines = "Submit Joint Loss Assessment survey report signed by the Village Revenue Inspector and Agricultural Officer within 15 days of notification to expedite Direct Benefit Transfer (DBT) claim settlement."

        return CompensationClaimResult(
            state = state,
            crop = crop,
            damageFactor = damageFactor,
            acreage = acreage,
            compensationPerAcre = ratePerAcre,
            totalClaimAmount = total,
            assessmentBreakdown = breakdown,
            claimGuidelines = guidelines
        )
    }
}
