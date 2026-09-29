# 🌾 SmartFarmAI Demo Guide

This document provides a comprehensive guide for demonstrating the **SmartFarmAI** application. It includes the exact values you can enter to showcase each module effectively to your mentor.

---

## 1. 🚜 Crop Recommendation
Predicts the best crop to plant based on environmental conditions.

**Supported Crops:** Rice, Wheat, Maize, Cotton, Sugarcane, Coffee, Jute, Millets.

| Goal Crop | Temperature (°C) | Humidity (%) | Soil Moisture (%) |
| :--- | :--- | :--- | :--- |
| **Rice** | 25.0 | 80.0 | 75.0 |
| **Wheat** | 20.0 | 60.0 | 50.0 |
| **Maize** | 27.5 | 70.0 | 55.0 |
| **Sugarcane** | 30.0 | 85.0 | 80.0 |
| **Cotton** | 28.0 | 50.0 | 40.0 |
| **Coffee** | 22.0 | 75.0 | 70.0 |
| **Jute** | 30.0 | 85.0 | 85.0 |
| **Millets** | 28.0 | 45.0 | 35.0 |
| **Tomato** | 24.0 | 65.0 | 60.0 |
| **Onion** | 20.0 | 55.0 | 50.0 |
| **Potato** | 18.0 | 70.0 | 75.0 |

---

## 2. 🧪 Fertilizer Guide
Calculates exact fertilizer quantities (kg/acre) based on nutrient deficits.

**Demo Scenarios:**

| Goal Recommendation | N (Nitrogen) | P (Phosphorus) | K (Potassium) | pH |
| :--- | :--- | :--- | :--- | :--- |
| **Needs Urea** (Low N) | 10.0 | 50.0 | 50.0 | 6.5 |
| **Needs DAP** (Low P) | 60.0 | 5.0 | 50.0 | 6.5 |
| **Needs MOP** (Low K) | 60.0 | 50.0 | 8.0 | 6.5 |
| **Needs Lime** (Acidic) | 60.0 | 50.0 | 50.0 | **4.5** |
| **Needs Gypsum** (Alkaline) | 60.0 | 50.0 | 50.0 | **8.5** |

---

## 3. 🛡️ Disease Detection
Identifies plant diseases from leaf images using a CNN model.

**Supported Categories:**
1.  **Healthy**: No intervention needed.
2.  **Leaf Blight**: Requires copper-based fungicide.
3.  **Leaf Spot**: Requires Mancozeb fungicide.
4.  **Rust**: Requires Propiconazole.
5.  **Powdery Mildew**: Requires Sulphur-based fungicide.

*Demo Note: Upload a clear image of a leaf. If the ML model is not fully loaded, the system will provide a simulated result with a realistic confidence score.*

---

## 4. ☁️ Weather Prediction
Predicts local weather conditions based on current sensors.

**Supported Outcomes:** Sunny, Cloudy, Rainy, Stormy.

| Goal Weather | Temperature (°C) | Humidity (%) | Soil Moisture (%) |
| :--- | :--- | :--- | :--- |
| **Sunny** | 32.0 | 35.0 | 20.0 |
| **Rainy** | 23.0 | 92.0 | 85.0 |
| **Cloudy** | 26.0 | 75.0 | 50.0 |
| **Stormy** | 21.0 | 95.0 | 90.0 |

---

## 5. 📊 Market Analysis
Analyzes Indian Mandi prices and predicts future 3-month trends.

*   **Regional Pricing**: Automatically adjusts base prices for states like Karnataka, Maharashtra, Punjab, etc.
*   **Realistic Maize Price**: Set to fluctuate around **₹19–₹25/kg** as per Indian market reality.
*   **Demo Tip**: Select **Karnataka** and **Maize**, then enter **21.0** as current price to see a realistic trend chart.

---

## 6. 💰 Crop Compensation
Estimates government compensation for crop loss using ML.

*   **Inputs**: State, Crop, Damage Factor (Flood, Drought, Pest, etc.), and Acreage.
*   **Logic**: The ML model predicts a "Compensation Rate per Acre" which is then multiplied by your total acreage.
*   **Demo Tip**: Select **Flood** and **5.0 Acres** to see a significant compensation estimate.

---

### 🚀 Summary for Demo
- **Fertilizers** are now in **kg per acre** with specific names (Urea, DAP, MOP).
- **Market Prices** are strictly **Indian Mandi Rates** (₹/kg), not international values.
- **Recommendations** now show "Ideal Levels" for comparison.
