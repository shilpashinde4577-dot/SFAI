package com.example.engine

import kotlin.math.max
import kotlin.math.round

data class IdealNutrientProfile(
    val n: Double,
    val p: Double,
    val k: Double,
    val ph: Double
)

data class FertilizerCalculationResult(
    val cropName: String,
    val primaryFertilizerType: String,
    val ureaKgPerAcre: Double,
    val dapKgPerAcre: Double,
    val mopKgPerAcre: Double,
    val soilAmendment: String?,
    val recommendations: List<String>,
    val idealN: Double,
    val idealP: Double,
    val idealK: Double,
    val idealPh: Double
)

object FertilizerCalculatorEngine {

    val IDEAL_LEVELS = mapOf(
        "rice" to IdealNutrientProfile(50.0, 25.0, 25.0, 6.5),
        "wheat" to IdealNutrientProfile(60.0, 30.0, 20.0, 7.0),
        "maize" to IdealNutrientProfile(60.0, 30.0, 30.0, 6.5),
        "cotton" to IdealNutrientProfile(40.0, 20.0, 20.0, 6.0),
        "sugarcane" to IdealNutrientProfile(100.0, 40.0, 40.0, 6.0),
        "coffee" to IdealNutrientProfile(80.0, 30.0, 80.0, 5.5),
        "jute" to IdealNutrientProfile(30.0, 15.0, 15.0, 6.5),
        "millets" to IdealNutrientProfile(20.0, 10.0, 10.0, 7.0),
        "tomato" to IdealNutrientProfile(60.0, 40.0, 60.0, 6.5),
        "onion" to IdealNutrientProfile(40.0, 20.0, 40.0, 6.5),
        "potato" to IdealNutrientProfile(60.0, 40.0, 80.0, 6.0)
    )

    fun calculate(
        crop: String,
        n: Double,
        p: Double,
        k: Double,
        ph: Double
    ): FertilizerCalculationResult {
        val normalizedCrop = crop.trim().lowercase()
        val ideal = IDEAL_LEVELS[normalizedCrop] ?: IdealNutrientProfile(50.0, 25.0, 25.0, 6.5)

        val defN = max(0.0, ideal.n - n)
        val defP = max(0.0, ideal.p - p)
        val defK = max(0.0, ideal.k - k)

        // 1. DAP (18% N, 46% P)
        val dapKg = if (defP > 0) defP / 0.46 else 0.0
        val nFromDap = dapKg * 0.18

        // 2. Urea (46% N) - offset by N provided by DAP
        val remainingN = max(0.0, defN - nFromDap)
        val ureaKg = if (remainingN > 0) remainingN / 0.46 else 0.0

        // 3. MOP (60% K)
        val mopKg = if (defK > 0) defK / 0.60 else 0.0

        // Determine primary fertilizer mix
        val primaryType = when {
            ureaKg > 20 && dapKg > 15 -> "Urea & DAP Balanced Mix"
            ureaKg > 20 -> "Urea (Nitrogen Rich)"
            dapKg > 15 -> "DAP (Diammonium Phosphate)"
            mopKg > 15 -> "MOP (Muriate of Potash)"
            ph < 5.5 -> "Agricultural Lime (Calcium Carbonate)"
            ph > 7.5 -> "Gypsum (Calcium Sulphate)"
            else -> "NPK 10-26-26 Complex"
        }

        val recs = mutableListOf<String>()
        recs.add("Agronomic engine recommends $primaryType for optimal vegetative growth.")

        if (ureaKg > 0.5) {
            recs.add("Apply ${round(ureaKg * 10) / 10} kg of Urea per acre in split doses to satisfy Nitrogen demands.")
        }
        if (dapKg > 0.5) {
            recs.add("Apply ${round(dapKg * 10) / 10} kg of DAP per acre at basel sowing for root establishment.")
        }
        if (mopKg > 0.5) {
            recs.add("Apply ${round(mopKg * 10) / 10} kg of MOP per acre to improve drought resilience and disease resistance.")
        }

        var amendment: String? = null
        if (ph < 5.5) {
            amendment = "Soil is acidic (pH < 5.5). Apply 200 kg of Agricultural Lime per acre."
            recs.add(amendment)
        } else if (ph > 7.5) {
            amendment = "Soil is alkaline (pH > 7.5). Apply 100 kg of Gypsum per acre."
            recs.add(amendment)
        }

        return FertilizerCalculationResult(
            cropName = crop.replaceFirstChar { it.uppercase() },
            primaryFertilizerType = primaryType,
            ureaKgPerAcre = round(ureaKg * 10) / 10,
            dapKgPerAcre = round(dapKg * 10) / 10,
            mopKgPerAcre = round(mopKg * 10) / 10,
            soilAmendment = amendment,
            recommendations = recs,
            idealN = ideal.n,
            idealP = ideal.p,
            idealK = ideal.k,
            idealPh = ideal.ph
        )
    }
}
