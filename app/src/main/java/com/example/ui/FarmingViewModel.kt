package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import com.example.engine.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FarmingViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repo = FarmingRepository(db.farmingDao())

    // Active logged-in user profile & Auth state
    private val _currentUser = MutableStateFlow<UserProfileRecord?>(
        UserProfileRecord(
            username = "shilpa_farmer",
            fullName = "Shilpa Shinde",
            email = "shilpashinde4577@gmail.com",
            phoneNumber = "+91 98765 43210",
            state = "Maharashtra",
            password = "password123",
            role = "admin",
            status = "active"
        )
    )
    val currentUser: StateFlow<UserProfileRecord?> = _currentUser.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(true)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    // Persistent Room Flows
    val latestCropPrediction = repo.latestCropPrediction.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    val allCropPredictions = repo.allCropPredictions.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val latestDiseaseDetection = repo.latestDiseaseDetection.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    val allDiseaseDetections = repo.allDiseaseDetections.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val latestFertilizer = repo.latestFertilizerRecommendation.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    val allFertilizers = repo.allFertilizerRecommendations.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val latestWeather = repo.latestWeatherPrediction.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    val allWeather = repo.allWeatherPredictions.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val latestMarket = repo.latestMarketAnalysis.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    val allMarkets = repo.allMarketAnalyses.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val latestCompensation = repo.latestCropCompensation.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    val allCompensations = repo.allCropCompensations.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val latestEligibility = repo.latestCropEligibility.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    val allEligibilities = repo.allCropEligibilities.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allProfiles = repo.allUserProfiles.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val userCount = repo.userCount.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1)
    val activityLogs = repo.recentActivityLogs.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active execution results for instant UI feedback
    private val _activeCropResult = MutableStateFlow<CropRecommendationEngine.RecommendationResult?>(null)
    val activeCropResult = _activeCropResult.asStateFlow()

    private val _activeDiseaseResult = MutableStateFlow<DiseaseDetectionEngine.DetectionResult?>(null)
    val activeDiseaseResult = _activeDiseaseResult.asStateFlow()

    private val _activeFertilizerResult = MutableStateFlow<FertilizerCalculationResult?>(null)
    val activeFertilizerResult = _activeFertilizerResult.asStateFlow()

    private val _activeWeatherResult = MutableStateFlow<WeatherForecastResult?>(null)
    val activeWeatherResult = _activeWeatherResult.asStateFlow()

    private val _activeMarketResult = MutableStateFlow<MarketForecastResult?>(null)
    val activeMarketResult = _activeMarketResult.asStateFlow()

    private val _activeCompensationResult = MutableStateFlow<CompensationClaimResult?>(null)
    val activeCompensationResult = _activeCompensationResult.asStateFlow()

    private val _activeEligibilityResult = MutableStateFlow<GovtEligibilityResult?>(null)
    val activeEligibilityResult = _activeEligibilityResult.asStateFlow()

    init {
        // Initial setup and seed demo defaults if empty
        viewModelScope.launch {
            _currentUser.value?.let { repo.saveProfile(it) }
            repo.saveProfile(
                UserProfileRecord(
                    username = "rajesh_patil",
                    fullName = "Rajesh Patil",
                    email = "rajesh.patil@example.com",
                    phoneNumber = "+91 91234 56789",
                    state = "Karnataka",
                    password = "password123",
                    role = "user",
                    status = "active"
                )
            )
            repo.saveProfile(
                UserProfileRecord(
                    username = "ramesh_kumar",
                    fullName = "Ramesh Kumar",
                    email = "ramesh.k@example.com",
                    phoneNumber = "+91 94567 89012",
                    state = "Punjab",
                    password = "password123",
                    role = "user",
                    status = "active"
                )
            )

            // Seed initial activity log
            repo.logActivity(_currentUser.value?.username ?: "farmer", "App Initialized", "SmartFarm AI platform loaded")

            // Seed initial default predictions if first launch
            val defaultCrop = CropRecommendationEngine.predict(25.0, 80.0, 75.0)
            _activeCropResult.value = defaultCrop
            repo.saveCropPrediction(
                CropPredictionRecord(
                    temperature = 25.0,
                    humidity = 80.0,
                    soilMoisture = 75.0,
                    predictedCrop = defaultCrop.recommendedCrop.name
                )
            )

            val defaultDisease = DiseaseDetectionEngine.diagnose("Leaf Spot", 88.5)
            _activeDiseaseResult.value = defaultDisease
            repo.saveDiseaseDetection(
                DiseaseDetectionRecord(
                    detectedDisease = defaultDisease.disease.name,
                    confidence = defaultDisease.confidence,
                    description = defaultDisease.disease.description,
                    recommendation = defaultDisease.disease.chemicalTreatment
                )
            )

            val defaultFert = FertilizerCalculatorEngine.calculate("Wheat", 50.0, 25.0, 20.0, 6.5)
            _activeFertilizerResult.value = defaultFert
            repo.saveFertilizerRecommendation(
                FertilizerRecommendationRecord(
                    cropName = defaultFert.cropName,
                    nitrogen = 50.0,
                    phosphorus = 25.0,
                    potassium = 20.0,
                    soilPh = 6.5,
                    fertilizerName = defaultFert.primaryFertilizerType,
                    ureaKgPerAcre = defaultFert.ureaKgPerAcre,
                    dapKgPerAcre = defaultFert.dapKgPerAcre,
                    mopKgPerAcre = defaultFert.mopKgPerAcre,
                    soilAmendment = defaultFert.soilAmendment,
                    fullAdvice = defaultFert.recommendations.joinToString(" ")
                )
            )

            val defaultWeather = WeatherPredictionEngine.predict(26.0, 65.0, 50.0)
            _activeWeatherResult.value = defaultWeather
            repo.saveWeatherPrediction(
                WeatherPredictionRecord(
                    temperature = 26.0,
                    humidity = 65.0,
                    soilMoisture = 50.0,
                    predictedWeather = defaultWeather.condition,
                    advisory = defaultWeather.farmingAdvisory
                )
            )

            val defaultMarket = MarketIntelligenceEngine.analyze("Rice", "Karnataka", 32.0)
            _activeMarketResult.value = defaultMarket
            repo.saveMarketAnalysis(
                MarketAnalysisRecord(
                    cropName = "Rice",
                    state = "Karnataka",
                    currentPrice = 32.0,
                    month1Price = defaultMarket.forecastPoints[0].price,
                    month2Price = defaultMarket.forecastPoints[1].price,
                    month3Price = defaultMarket.forecastPoints[2].price,
                    trend = defaultMarket.trend
                )
            )

            val defaultComp = CompensationCalculatorEngine.calculate("Karnataka", "Cotton", "Flood", 5.0)
            _activeCompensationResult.value = defaultComp
            repo.saveCropCompensation(
                CropCompensationRecord(
                    state = "Karnataka",
                    cropName = "Cotton",
                    damageFactor = "Flood",
                    acreage = 5.0,
                    compensationPerAcre = defaultComp.compensationPerAcre,
                    totalCompensation = defaultComp.totalClaimAmount
                )
            )

            val defaultElig = GovtBenefitEligibilityEngine.evaluate("Maharashtra", "Soybean", "Drought", 4.0, 45.0, 48, "Yes", "Small/Marginal")
            _activeEligibilityResult.value = defaultElig
            repo.saveCropEligibility(
                CropEligibilityRecord(
                    state = "Maharashtra",
                    cropName = "Soybean",
                    damageFactor = "Drought",
                    acreage = 4.0,
                    lossPercentage = 45.0,
                    intimationHours = 48,
                    insuredStatus = "Yes",
                    farmerCategory = "Small/Marginal",
                    isEligible = defaultElig.isEligible,
                    confidence = defaultElig.confidence,
                    criteriaJson = defaultElig.schemeName
                )
            )
        }
    }

    // 1. Crop Recommendation
    fun recommendCrop(temp: Double, humidity: Double, moisture: Double) {
        viewModelScope.launch {
            val res = CropRecommendationEngine.predict(temp, humidity, moisture)
            _activeCropResult.value = res
            repo.saveCropPrediction(
                CropPredictionRecord(
                    temperature = temp,
                    humidity = humidity,
                    soilMoisture = moisture,
                    predictedCrop = res.recommendedCrop.name
                )
            )
            repo.logActivity(_currentUser.value?.username ?: "farmer", "Crop Recommendation", "Recommended: ${res.recommendedCrop.name}")
        }
    }

    // 2. Disease Detection
    fun diagnoseDisease(diseaseName: String? = null, customConfidence: Double? = null, imageUri: String? = null) {
        viewModelScope.launch {
            val res = DiseaseDetectionEngine.diagnose(diseaseName, customConfidence)
            _activeDiseaseResult.value = res
            repo.saveDiseaseDetection(
                DiseaseDetectionRecord(
                    detectedDisease = res.disease.name,
                    confidence = res.confidence,
                    description = res.disease.description,
                    recommendation = res.disease.chemicalTreatment,
                    imageUri = imageUri
                )
            )
            repo.logActivity(_currentUser.value?.username ?: "farmer", "Disease Detection", "Detected: ${res.disease.name} (${res.confidence}%)")
        }
    }

    // 3. Fertilizer Calculation
    fun calculateFertilizer(crop: String, n: Double, p: Double, k: Double, ph: Double) {
        viewModelScope.launch {
            val res = FertilizerCalculatorEngine.calculate(crop, n, p, k, ph)
            _activeFertilizerResult.value = res
            repo.saveFertilizerRecommendation(
                FertilizerRecommendationRecord(
                    cropName = res.cropName,
                    nitrogen = n,
                    phosphorus = p,
                    potassium = k,
                    soilPh = ph,
                    fertilizerName = res.primaryFertilizerType,
                    ureaKgPerAcre = res.ureaKgPerAcre,
                    dapKgPerAcre = res.dapKgPerAcre,
                    mopKgPerAcre = res.mopKgPerAcre,
                    soilAmendment = res.soilAmendment,
                    fullAdvice = res.recommendations.joinToString(" ")
                )
            )
            repo.logActivity(_currentUser.value?.username ?: "farmer", "Fertilizer Guide", "Calculated dosage for ${res.cropName}")
        }
    }

    // 4. Weather Prediction
    fun predictWeather(temp: Double, humidity: Double, moisture: Double) {
        viewModelScope.launch {
            val res = WeatherPredictionEngine.predict(temp, humidity, moisture)
            _activeWeatherResult.value = res
            repo.saveWeatherPrediction(
                WeatherPredictionRecord(
                    temperature = temp,
                    humidity = humidity,
                    soilMoisture = moisture,
                    predictedWeather = res.condition,
                    advisory = res.farmingAdvisory
                )
            )
            repo.logActivity(_currentUser.value?.username ?: "farmer", "Weather Forecast", "Forecast: ${res.condition}")
        }
    }

    // 5. Market Analysis
    fun analyzeMarket(crop: String, state: String, currentPrice: Double) {
        viewModelScope.launch {
            val res = MarketIntelligenceEngine.analyze(crop, state, currentPrice)
            _activeMarketResult.value = res
            repo.saveMarketAnalysis(
                MarketAnalysisRecord(
                    cropName = crop,
                    state = state,
                    currentPrice = currentPrice,
                    month1Price = res.forecastPoints[0].price,
                    month2Price = res.forecastPoints[1].price,
                    month3Price = res.forecastPoints[2].price,
                    trend = res.trend
                )
            )
            repo.logActivity(_currentUser.value?.username ?: "farmer", "Market Intelligence", "Analyzed $crop in $state (₹$currentPrice/kg)")
        }
    }

    // 6. Crop Compensation
    fun calculateCompensation(state: String, crop: String, damageFactor: String, acreage: Double) {
        viewModelScope.launch {
            val res = CompensationCalculatorEngine.calculate(state, crop, damageFactor, acreage)
            _activeCompensationResult.value = res
            repo.saveCropCompensation(
                CropCompensationRecord(
                    state = state,
                    cropName = crop,
                    damageFactor = damageFactor,
                    acreage = acreage,
                    compensationPerAcre = res.compensationPerAcre,
                    totalCompensation = res.totalClaimAmount
                )
            )
            repo.logActivity(_currentUser.value?.username ?: "farmer", "Compensation Claim", "Claim ₹${res.totalClaimAmount.toLong()} for $crop")
        }
    }

    // 7. Govt Benefit Eligibility
    fun evaluateEligibility(
        state: String,
        crop: String,
        damageFactor: String,
        acreage: Double,
        lossPct: Double,
        intimationHours: Int,
        insuredStatus: String,
        farmerCategory: String
    ) {
        viewModelScope.launch {
            val res = GovtBenefitEligibilityEngine.evaluate(
                state, crop, damageFactor, acreage, lossPct, intimationHours, insuredStatus, farmerCategory
            )
            _activeEligibilityResult.value = res
            repo.saveCropEligibility(
                CropEligibilityRecord(
                    state = state,
                    cropName = crop,
                    damageFactor = damageFactor,
                    acreage = acreage,
                    lossPercentage = lossPct,
                    intimationHours = intimationHours,
                    insuredStatus = insuredStatus,
                    farmerCategory = farmerCategory,
                    isEligible = res.isEligible,
                    confidence = res.confidence,
                    criteriaJson = res.schemeName
                )
            )
            val statusText = if (res.isEligible) "Eligible" else "Not Eligible"
            repo.logActivity(_currentUser.value?.username ?: "farmer", "Govt Scheme Check", "Result: $statusText for $crop")
        }
    }

    // User Profile Actions
    fun updateProfile(fullName: String, phone: String, state: String) {
        viewModelScope.launch {
            val current = _currentUser.value ?: return@launch
            val updated = current.copy(
                fullName = fullName,
                phoneNumber = phone,
                state = state
            )
            _currentUser.value = updated
            repo.saveProfile(updated)
            repo.logActivity(updated.username, "Profile Updated", "Personal settings saved")
        }
    }

    // Authentication: Logout
    fun logout() {
        viewModelScope.launch {
            val username = _currentUser.value?.username ?: "farmer"
            repo.logActivity(username, "User Logged Out", "Session ended")
            _currentUser.value = null
            _isLoggedIn.value = false
        }
    }

    // Authentication: Login
    fun login(usernameOrEmail: String, pass: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val user = repo.findUser(usernameOrEmail.trim())
            if (user == null) {
                onResult(false, "User not found. Please register a new farmer account.")
                return@launch
            }
            if (user.status == "banned" || user.status == "suspended") {
                onResult(false, "This account is suspended. Please contact administrator.")
                return@launch
            }
            if (user.password != pass) {
                onResult(false, "Incorrect password. Please try again.")
                return@launch
            }
            _currentUser.value = user
            _isLoggedIn.value = true
            repo.logActivity(user.username, "User Logged In", "Welcome back, ${user.fullName}")
            onResult(true, "Welcome back, ${user.fullName}!")
        }
    }

    // Authentication: Register
    fun register(
        username: String,
        fullName: String,
        email: String,
        phoneNumber: String,
        state: String,
        pass: String,
        onResult: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            val trimmedUsername = username.trim().lowercase()
            val existing = repo.getProfile(trimmedUsername)
            if (existing != null) {
                onResult(false, "Username '@$trimmedUsername' already exists. Please choose another.")
                return@launch
            }
            val existingEmail = repo.findUser(email.trim())
            if (existingEmail != null) {
                onResult(false, "Email '${email.trim()}' is already registered.")
                return@launch
            }

            val newProfile = UserProfileRecord(
                username = trimmedUsername,
                fullName = fullName.trim(),
                email = email.trim(),
                phoneNumber = phoneNumber.trim(),
                state = state.trim(),
                password = pass,
                role = "user",
                status = "active"
            )
            repo.saveProfile(newProfile)
            _currentUser.value = newProfile
            _isLoggedIn.value = true
            repo.logActivity(newProfile.username, "New Farmer Registered", "Account registered in ${newProfile.state}")
            onResult(true, "Registration successful! Welcome to SmartFarm AI.")
        }
    }

    // Admin Panel Actions
    fun toggleUserStatus(targetUsername: String, currentStatus: String) {
        viewModelScope.launch {
            val target = repo.getProfile(targetUsername) ?: return@launch
            val newStatus = if (currentStatus == "active") "banned" else "active"
            val updated = target.copy(status = newStatus)
            repo.saveProfile(updated)
            repo.logActivity(_currentUser.value?.username ?: "admin", "Admin Status Toggle", "Set $targetUsername to ${newStatus.uppercase()}")
        }
    }

    fun toggleUserRole(targetUsername: String, currentRole: String) {
        viewModelScope.launch {
            val target = repo.getProfile(targetUsername) ?: return@launch
            val newRole = if (currentRole == "admin") "user" else "admin"
            val updated = target.copy(role = newRole)
            repo.saveProfile(updated)
            repo.logActivity(_currentUser.value?.username ?: "admin", "Admin Role Toggle", "Set $targetUsername role to ${newRole.uppercase()}")
        }
    }
}
