# 🌾 SmartFarm AI - Android Application

SmartFarm AI is an intelligent agricultural decision support platform built natively for Android using Kotlin and Jetpack Compose. Rewritten from the original Django system, it brings real-time machine learning inference, mathematical agronomic modeling, local offline Room database persistence, and government scheme eligibility auditing directly to farmers' devices.

---

## 🚀 Key Modules & Capabilities

1. **🌱 Crop Recommendation**:
   - Random forest multi-parameter agronomic matching based on temperature, humidity, and soil moisture sensor readings.
   - Outputs optimal crop, suitability match %, season, water requirements, and viable alternate crops.

2. **🔬 Leaf Disease Detection**:
   - Zero-permission Android Photo Picker (`ActivityResultContracts.PickVisualMedia`) for leaf photo uploads + 5 built-in specimen pathology classes (Healthy, Leaf Blight, Leaf Spot, Rust, Powdery Mildew).
   - Provides confidence scores, symptomatology, chemical fungicide prescriptions (Mancozeb, Copper Oxychloride, Propiconazole, Sulphur), and bio-fungicides.

3. **🧪 Fertilizer Guide & Agronomy Engine**:
   - Computes exact nutrient deficits against crop-specific ideal profiles (N, P, K, pH) across 11 major crops.
   - Calculates exact fertilizer application quantities in **kg per acre** for **Urea**, **DAP**, and **MOP**, plus agricultural lime or gypsum soil amendments.

4. **☁️ Weather Forecast**:
   - Microclimate sensor classifier predicting Sunny, Rainy, Cloudy, and Stormy conditions.
   - Provides field operation advisories, irrigation scheduling, and pesticide/fertilizer spraying windows.

5. **📊 Market Price Intelligence**:
   - Real-world APMC Mandi regional price modeling across Indian states with state-wise adjustment modifiers.
   - 3-month forward price forecast timeline, percent trend indicator (Bullish/Bearish/Stable), and trading insights.

6. **💰 Crop Loss Compensation**:
   - Disaster compensation calculation based on PMFBY Scale of Finance norms.
   - Computes base rate per acre and total payout in ₹ based on calamity factors (Flood, Drought, Hailstorm, Fire, Pest, Unseasonal Rain) and acreage.

7. **🏛️ Govt Crop Loss Benefits Eligibility (PMFBY / SDRF)**:
   - Audits statutory 33% minimum loss threshold, 72-hour notification deadline, and PMFBY/SDRF disaster relief criteria.
   - Produces a detailed compliance checklist, 7 mandatory submission documents, and guidance to the nearest District Agriculture Office (DAO) / CSC / KVK.

8. **📑 Comprehensive Agricultural Dossier**:
   - Unified audit report synthesizing findings from all 7 modules with one-tap clipboard copy and sharing.

9. **👨‍🌾 Farmer Profile & 🛡️ Admin Control Center**:
   - User profile management, regional jurisdiction, usage statistics, and local activity log feed.
   - Admin management panel with user filtering, active/suspended status toggling, and role management.

---

## 🛠️ Architecture & Tech Stack

- **Platform**: Android SDK 36, Kotlin 2.1.10, Jetpack Compose (Material 3).
- **Architecture**: MVVM (Model-View-ViewModel) + Clean Repository pattern.
- **Persistence**: SQLite with Android Room Database (`@Entity`, `@Dao`, `Flow<T>`) for complete offline persistence.
- **UI & UX**: Edge-to-Edge display, Material 3 dynamic color scheme, custom adaptive launcher icon, and TalkBack accessibility semantics.
