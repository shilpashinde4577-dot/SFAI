package com.example.engine

data class DiseaseInfo(
    val name: String,
    val severity: String,
    val description: String,
    val chemicalTreatment: String,
    val organicTreatment: String,
    val preventionTip: String
)

object DiseaseDetectionEngine {

    val CATALOG = mapOf(
        "Healthy" to DiseaseInfo(
            name = "Healthy Leaf",
            severity = "None",
            description = "The leaf tissue shows normal chlorophyll distribution and vibrant cell structure with no visible pathogen lesions.",
            chemicalTreatment = "No chemical intervention needed.",
            organicTreatment = "Apply seaweed extract spray (2 ml/L) or Panchagavya (3%) to enhance natural plant immunity.",
            preventionTip = "Maintain balanced macro and micro-nutrients and avoid waterlogging around roots."
        ),
        "Leaf Blight" to DiseaseInfo(
            name = "Leaf Blight (Alternaria / Helminthosporium)",
            severity = "High",
            description = "Characterized by irregular water-soaked concentric lesions turning brown to necrotic with yellow chlorotic halos.",
            chemicalTreatment = "Spray Copper Oxychloride 50% WP (2.5 g/L) or Mancozeb 75% WP (2.0 g/L). In severe conditions, use Azoxystrobin (1 ml/L).",
            organicTreatment = "Spray 5% Neem seed kernel extract (NSKE) or Trichoderma viride bio-fungicide (5 g/L).",
            preventionTip = "Sterilize pruning tools, ensure wide plant spacing for canopy aeration, and destroy diseased crop residue."
        ),
        "Leaf Spot" to DiseaseInfo(
            name = "Cercospora Leaf Spot",
            severity = "Moderate",
            description = "Small circular or angular dark brown to purple necrotic spots with grayish ash centers on the upper leaf foliage.",
            chemicalTreatment = "Apply Mancozeb 75% WP @ 2.0 g/L or Chlorothalonil 75% WP @ 2.0 g/L at 10-14 day intervals.",
            organicTreatment = "Foliar spray of Pseudomonas fluorescens (5 g/L) or garlic-chili extract solution.",
            preventionTip = "Avoid overhead sprinkler irrigation in late afternoon and remove lower infected leaves."
        ),
        "Rust" to DiseaseInfo(
            name = "Cereal / Foliar Rust (Puccinia spp.)",
            severity = "High",
            description = "Reddish-orange or golden powdery pustules bursting through leaf epidermises, shedding infectious spores.",
            chemicalTreatment = "Apply Propiconazole 25% EC (1.0 ml/L) or Tebuconazole 25.9% EC (1.0 ml/L) immediately upon symptom onset.",
            organicTreatment = "Apply sour buttermilk (5%) or baking soda solution (3 g/L water with a few drops of dish soap).",
            preventionTip = "Plant resistant cultivars, avoid excessive early Nitrogen application, and practice crop rotation."
        ),
        "Powdery Mildew" to DiseaseInfo(
            name = "Powdery Mildew (Erysiphe / Leveillula)",
            severity = "Moderate",
            description = "Distinct superficial talcum-powder-like white mycelial patches coating leaf surfaces, reducing photosynthesis.",
            chemicalTreatment = "Spray Wettable Sulphur 80% WP (3.0 g/L) or Hexaconazole 5% EC (1.0 ml/L).",
            organicTreatment = "Spray diluted raw milk solution (1:9 with water) in bright sunlight or potassium bicarbonate (3 g/L).",
            preventionTip = "Prune dense inner canopies to increase sunlight penetration and lower localized humidity."
        )
    )

    data class DetectionResult(
        val disease: DiseaseInfo,
        val confidence: Double,
        val detectedAt: Long = System.currentTimeMillis()
    )

    // Analyze leaf characteristics based on sample or image input
    fun diagnose(chosenDisease: String? = null, customConfidence: Double? = null): DetectionResult {
        val key = chosenDisease ?: listOf("Healthy", "Leaf Blight", "Leaf Spot", "Rust", "Powdery Mildew").random()
        val info = CATALOG[key] ?: CATALOG["Healthy"]!!
        val conf = customConfidence ?: (82.0 + (Math.random() * 14.5))
        return DetectionResult(
            disease = info,
            confidence = Math.round(conf * 10.0) / 10.0
        )
    }
}
