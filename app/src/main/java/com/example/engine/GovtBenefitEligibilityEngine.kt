package com.example.engine

data class EligibilityCriteriaCheck(
    val title: String,
    val isMet: Boolean,
    val description: String
)

data class DocumentRequirement(
    val title: String,
    val description: String,
    val icon: String
)

data class GovtEligibilityResult(
    val isEligible: Boolean,
    val confidence: Double,
    val criteriaChecks: List<EligibilityCriteriaCheck>,
    val documents: List<DocumentRequirement>,
    val submissionOffice: String,
    val schemeName: String
)

object GovtBenefitEligibilityEngine {

    val REQUIRED_DOCS = listOf(
        DocumentRequirement(
            title = "Aadhaar Card Copy",
            description = "Self-attested photocopy of Aadhaar linked with active mobile number.",
            icon = "🆔"
        ),
        DocumentRequirement(
            title = "Land Ownership / Tenancy Document",
            description = "7/12 Extract, Khasra/Khatauni, or registered Sharecropper Tenancy Affidavit.",
            icon = "📑"
        ),
        DocumentRequirement(
            title = "Bank Passbook Copy (DBT Enabled)",
            description = "Clear copy of active Bank Passbook showing Account No., Branch Name & IFSC Code.",
            icon = "💳"
        ),
        DocumentRequirement(
            title = "Sowing & Crop Loss Certificate",
            description = "Certificate signed by Village Revenue Officer (VRO) / Gram Sevak / Sarpanch.",
            icon = "🌾"
        ),
        DocumentRequirement(
            title = "Geo-Tagged Photographs of Field",
            description = "2 printed photographs showing field crop damage with date & GPS coordinates stamped.",
            icon = "📸"
        ),
        DocumentRequirement(
            title = "PMFBY / Calamity Claim Form",
            description = "Duly filled and signed Form-A Intimation Application for crop loss relief.",
            icon = "📝"
        ),
        DocumentRequirement(
            title = "Intimation Reference Slip",
            description = "Acknowledgement slip or SMS reference of loss reported within 72 hours.",
            icon = "⏳"
        )
    )

    const val SUBMISSION_OFFICE = "Nearest District Agriculture Office (DAO), Block Development Office (BDO), or Common Service Center (CSC) / Krishi Vigyan Kendra (KVK)."

    fun evaluate(
        state: String,
        crop: String,
        damageFactor: String,
        acreage: Double,
        lossPercentage: Double,
        intimationHours: Int,
        insuredStatus: String,
        farmerCategory: String
    ): GovtEligibilityResult {
        val criteria = mutableListOf<EligibilityCriteriaCheck>()

        // 1. Loss percentage (Govt threshold >= 33%)
        val lossMet = lossPercentage >= 33.0
        criteria.add(
            EligibilityCriteriaCheck(
                title = "Minimum Loss Threshold (≥33%)",
                isMet = lossMet,
                description = if (lossMet) {
                    "Reported loss of ${lossPercentage.toInt()}% satisfies the mandatory 33% threshold under PMFBY and SDRF norms."
                } else {
                    "Reported loss of ${lossPercentage.toInt()}% is below the mandatory 33% statutory minimum required for financial relief."
                }
            )
        )

        // 2. Intimation timeline (≤ 72 hours)
        val timeMet = intimationHours <= 72
        criteria.add(
            EligibilityCriteriaCheck(
                title = "72-Hour Notification Deadline",
                isMet = timeMet,
                description = if (timeMet) {
                    "Loss reported within $intimationHours hours complies with the 72-hour cutoff for localized calamity intimation."
                } else {
                    "Reporting time of $intimationHours hours exceeds the 72-hour statutory window for localized calamity surveys."
                }
            )
        )

        // 3. Scheme enrollment & Disaster status
        val isInsured = insuredStatus.equals("Yes", ignoreCase = true)
        val isSevereDisaster = lossPercentage >= 50.0 && listOf("Flood", "Drought", "Hailstorm", "Unseasonal Rain").contains(damageFactor)
        val schemeMet = isInsured || isSevereDisaster

        criteria.add(
            EligibilityCriteriaCheck(
                title = "Scheme Enrollment / Calamity Provision",
                isMet = schemeMet,
                description = when {
                    isInsured -> "Active enrollment under Pradhan Mantri Fasal Bima Yojana (PMFBY) confirmed."
                    isSevereDisaster -> "Uninsured loss qualifies under State Disaster Response Fund (SDRF) special calamity provisions (${damageFactor} ≥ 50% loss)."
                    else -> "Crop is uninsured and does not meet emergency SDRF catastrophic relief criteria."
                }
            )
        )

        val isEligible = lossMet && timeMet && schemeMet
        val scheme = if (isInsured) "Pradhan Mantri Fasal Bima Yojana (PMFBY)" else "State Disaster Response Fund (SDRF)"

        // Confidence score computation
        val conf = when {
            isEligible && lossPercentage >= 50.0 && intimationHours <= 48 -> 94.5
            isEligible -> 89.0
            !lossMet && !timeMet -> 96.0
            else -> 84.0
        }

        return GovtEligibilityResult(
            isEligible = isEligible,
            confidence = conf,
            criteriaChecks = criteria,
            documents = REQUIRED_DOCS,
            submissionOffice = SUBMISSION_OFFICE,
            schemeName = scheme
        )
    }
}
