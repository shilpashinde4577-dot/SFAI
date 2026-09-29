from django import forms

INDIAN_STATES = [
    ('', 'Select a state...'),
    ('Andhra Pradesh', 'Andhra Pradesh'),
    ('Arunachal Pradesh', 'Arunachal Pradesh'),
    ('Assam', 'Assam'),
    ('Bihar', 'Bihar'),
    ('Chhattisgarh', 'Chhattisgarh'),
    ('Goa', 'Goa'),
    ('Gujarat', 'Gujarat'),
    ('Haryana', 'Haryana'),
    ('Himachal Pradesh', 'Himachal Pradesh'),
    ('Jharkhand', 'Jharkhand'),
    ('Karnataka', 'Karnataka'),
    ('Kerala', 'Kerala'),
    ('Madhya Pradesh', 'Madhya Pradesh'),
    ('Maharashtra', 'Maharashtra'),
    ('Manipur', 'Manipur'),
    ('Meghalaya', 'Meghalaya'),
    ('Mizoram', 'Mizoram'),
    ('Nagaland', 'Nagaland'),
    ('Odisha', 'Odisha'),
    ('Punjab', 'Punjab'),
    ('Rajasthan', 'Rajasthan'),
    ('Sikkim', 'Sikkim'),
    ('Tamil Nadu', 'Tamil Nadu'),
    ('Telangana', 'Telangana'),
    ('Tripura', 'Tripura'),
    ('Uttar Pradesh', 'Uttar Pradesh'),
    ('Uttarakhand', 'Uttarakhand'),
    ('West Bengal', 'West Bengal'),
    ('Delhi', 'Delhi'),
]

CROP_CHOICES = [
    ('', 'Select a crop...'),
    ('Rice', 'Rice'),
    ('Wheat', 'Wheat'),
    ('Maize', 'Maize'),
    ('Cotton', 'Cotton'),
    ('Sugarcane', 'Sugarcane'),
    ('Coffee', 'Coffee'),
    ('Jute', 'Jute'),
    ('Millets', 'Millets'),
    ('Tomato', 'Tomato'),
    ('Onion', 'Onion'),
    ('Potato', 'Potato'),
]

DAMAGE_FACTORS = [
    ('', 'Select a factor...'),
    ('Flood', 'Flood / Excessive Rain'),
    ('Drought', 'Drought / No Water'),
    ('Pest/Animals', 'Pest / Animal Intervention'),
    ('Hailstorm', 'Hailstorm'),
    ('Fire', 'Fire'),
]


class CropPredictionForm(forms.Form):
    """Form for crop recommendation based on environmental parameters."""
    temperature = forms.FloatField(
        label='Temperature (°C)',
        min_value=-10,
        max_value=60,
        widget=forms.NumberInput(attrs={
            'class': 'form-input',
            'placeholder': 'e.g., 25.5',
            'step': '0.1',
            'id': 'temperature-input',
        })
    )
    humidity = forms.FloatField(
        label='Humidity (%)',
        min_value=0,
        max_value=100,
        widget=forms.NumberInput(attrs={
            'class': 'form-input',
            'placeholder': 'e.g., 70',
            'step': '0.1',
            'id': 'humidity-input',
        })
    )
    soil_moisture = forms.FloatField(
        label='Soil Moisture (%)',
        min_value=0,
        max_value=100,
        widget=forms.NumberInput(attrs={
            'class': 'form-input',
            'placeholder': 'e.g., 55',
            'step': '0.1',
            'id': 'soil-moisture-input',
        })
    )


class WeatherPredictionForm(forms.Form):
    """Form for weather prediction based on local environmental parameters."""
    temperature = forms.FloatField(
        label='Temperature (°C)',
        min_value=-10,
        max_value=60,
        widget=forms.NumberInput(attrs={
            'class': 'form-input',
            'placeholder': 'e.g., 25.5',
            'step': '0.1',
            'id': 'weather-temperature-input',
        })
    )
    humidity = forms.FloatField(
        label='Humidity (%)',
        min_value=0,
        max_value=100,
        widget=forms.NumberInput(attrs={
            'class': 'form-input',
            'placeholder': 'e.g., 70',
            'step': '0.1',
            'id': 'weather-humidity-input',
        })
    )
    soil_moisture = forms.FloatField(
        label='Soil Moisture (%)',
        min_value=0,
        max_value=100,
        widget=forms.NumberInput(attrs={
            'class': 'form-input',
            'placeholder': 'e.g., 55',
            'step': '0.1',
            'id': 'weather-soil-moisture-input',
        })
    )


class DiseaseDetectionForm(forms.Form):
    """Form for uploading leaf images for disease detection."""
    image = forms.ImageField(
        label='Upload Leaf Image',
        widget=forms.ClearableFileInput(attrs={
            'class': 'form-input file-input',
            'accept': 'image/*',
            'id': 'leaf-image-input',
        })
    )


class FertilizerForm(forms.Form):
    """Form for fertilizer recommendation."""
    FERT_CROP_CHOICES = [
        ('', 'Select a crop...'),
        ('rice', 'Rice'),
        ('wheat', 'Wheat'),
        ('maize', 'Maize'),
        ('cotton', 'Cotton'),
        ('sugarcane', 'Sugarcane'),
        ('coffee', 'Coffee'),
        ('jute', 'Jute'),
        ('millets', 'Millets'),
        ('tomato', 'Tomato'),
        ('onion', 'Onion'),
        ('potato', 'Potato'),
    ]

    crop_name = forms.ChoiceField(
        label='Crop Name',
        choices=FERT_CROP_CHOICES,
        widget=forms.Select(attrs={
            'class': 'form-input',
            'id': 'crop-name-input',
        })
    )
    nitrogen = forms.FloatField(
        label='Nitrogen (N) Level',
        min_value=0,
        max_value=200,
        widget=forms.NumberInput(attrs={
            'class': 'form-input',
            'placeholder': 'e.g., 40',
            'step': '0.1',
            'id': 'nitrogen-input',
        })
    )
    phosphorus = forms.FloatField(
        label='Phosphorus (P) Level',
        min_value=0,
        max_value=200,
        widget=forms.NumberInput(attrs={
            'class': 'form-input',
            'placeholder': 'e.g., 30',
            'step': '0.1',
            'id': 'phosphorus-input',
        })
    )
    potassium = forms.FloatField(
        label='Potassium (K) Level',
        min_value=0,
        max_value=200,
        widget=forms.NumberInput(attrs={
            'class': 'form-input',
            'placeholder': 'e.g., 35',
            'step': '0.1',
            'id': 'potassium-input',
        })
    )
    soil_ph = forms.FloatField(
        label='Soil pH',
        min_value=0,
        max_value=14,
        widget=forms.NumberInput(attrs={
            'class': 'form-input',
            'placeholder': 'e.g., 6.5',
            'step': '0.1',
            'id': 'soil-ph-input',
        })
    )


class MarketAnalysisForm(forms.Form):
    """Form for market price analysis and prediction."""
    crop_name = forms.ChoiceField(
        label='Crop Name',
        choices=CROP_CHOICES,
        widget=forms.Select(attrs={
            'class': 'form-input',
            'id': 'market-crop-input',
        })
    )
    state = forms.ChoiceField(
        label='Region / State',
        choices=INDIAN_STATES,
        widget=forms.Select(attrs={
            'class': 'form-input',
            'id': 'market-state-input',
        })
    )
    current_price = forms.FloatField(
        label='Current Price (₹/kg)',
        min_value=1,
        max_value=10000,
        widget=forms.NumberInput(attrs={
            'class': 'form-input',
            'placeholder': 'e.g., 45.0',
            'step': '0.5',
            'id': 'market-price-input',
        })
    )


class CropCompensationForm(forms.Form):
    """Form for crop loss compensation estimation."""
    state = forms.ChoiceField(
        label='Region / State',
        choices=INDIAN_STATES,
        widget=forms.Select(attrs={
            'class': 'form-input',
            'id': 'comp-state-input',
        })
    )
    crop_name = forms.ChoiceField(
        label='Crop Name',
        choices=CROP_CHOICES,
        widget=forms.Select(attrs={
            'class': 'form-input',
            'id': 'comp-crop-input',
        })
    )
    damage_factor = forms.ChoiceField(
        label='Damage Factor',
        choices=DAMAGE_FACTORS,
        widget=forms.Select(attrs={
            'class': 'form-input',
            'id': 'comp-factor-input',
        })
    )
    acreage = forms.FloatField(
        label='Total Acreage (acres)',
        min_value=0.1,
        max_value=10000,
        widget=forms.NumberInput(attrs={
            'class': 'form-input',
            'placeholder': 'e.g., 5.0',
            'step': '0.1',
            'id': 'comp-acreage-input',
        })
    )


class CropLossEligibilityForm(forms.Form):
    """Form for checking government crop loss benefit eligibility."""
    
    INSURANCE_CHOICES = [
        ('', 'Select Insurance Status...'),
        ('Yes', 'Yes — Enrolled under PMFBY / State Crop Insurance'),
        ('No', 'No — Not Enrolled in Insurance'),
    ]

    FARMER_CATEGORIES = [
        ('', 'Select Farmer Category...'),
        ('Small & Marginal (< 5 Acres)', 'Small & Marginal Farmer (< 5 Acres)'),
        ('Medium (5 - 10 Acres)', 'Medium Farmer (5 - 10 Acres)'),
        ('Large (> 10 Acres)', 'Large Farmer (> 10 Acres)'),
        ('Tenant / Sharecropper', 'Tenant Farmer / Sharecropper'),
    ]

    EXTENDED_DAMAGE_FACTORS = [
        ('', 'Select Cause of Damage...'),
        ('Flood', 'Flood / Excessive Rainfall'),
        ('Drought', 'Drought / Water Scarcity'),
        ('Pest/Animals', 'Pest Attack / Animal Damage'),
        ('Hailstorm', 'Hailstorm / Heavy Storm'),
        ('Unseasonal Rain', 'Unseasonal Rain / Cyclone'),
        ('Fire', 'Fire Outbreak'),
    ]

    INTIMATION_CHOICES = [
        ('', 'Select Reporting Intimation Period...'),
        (12, 'Within 12 Hours'),
        (24, 'Within 24 Hours'),
        (48, 'Within 48 Hours'),
        (72, 'Within 72 Hours (Standard Government Limit)'),
        (96, '96 Hours (Delayed Notification)'),
        (120, '120+ Hours (Late Intimation)'),
    ]

    state = forms.ChoiceField(
        label='Region / State',
        choices=INDIAN_STATES,
        widget=forms.Select(attrs={
            'class': 'form-input',
            'id': 'eligibility-state-input',
        })
    )

    crop_name = forms.ChoiceField(
        label='Crop Name',
        choices=CROP_CHOICES,
        widget=forms.Select(attrs={
            'class': 'form-input',
            'id': 'eligibility-crop-input',
        })
    )

    damage_factor = forms.ChoiceField(
        label='Cause of Crop Damage',
        choices=EXTENDED_DAMAGE_FACTORS,
        widget=forms.Select(attrs={
            'class': 'form-input',
            'id': 'eligibility-factor-input',
        })
    )

    acreage = forms.FloatField(
        label='Cultivated Land Size (in Acres)',
        min_value=0.1,
        max_value=10000,
        widget=forms.NumberInput(attrs={
            'class': 'form-input',
            'placeholder': 'e.g., 3.5',
            'step': '0.1',
            'id': 'eligibility-acreage-input',
        })
    )

    loss_percentage = forms.FloatField(
        label='Estimated Crop Loss Percentage (%)',
        min_value=5.0,
        max_value=100.0,
        widget=forms.NumberInput(attrs={
            'class': 'form-input',
            'placeholder': 'e.g., 65 (Govt minimum threshold is 33%)',
            'step': '1.0',
            'id': 'eligibility-loss-input',
        })
    )

    intimation_hours = forms.ChoiceField(
        label='Intimation / Damage Reporting Time',
        choices=INTIMATION_CHOICES,
        widget=forms.Select(attrs={
            'class': 'form-input',
            'id': 'eligibility-intimation-input',
        })
    )

    insured_status = forms.ChoiceField(
        label='Crop Insurance Enrollment (PMFBY / State Scheme)',
        choices=INSURANCE_CHOICES,
        widget=forms.Select(attrs={
            'class': 'form-input',
            'id': 'eligibility-insured-input',
        })
    )

    farmer_category = forms.ChoiceField(
        label='Farmer Landholding Category',
        choices=FARMER_CATEGORIES,
        widget=forms.Select(attrs={
            'class': 'form-input',
            'id': 'eligibility-category-input',
        })
    )


class AdminUserCreationForm(forms.Form):
    """Form for admins to create a new user (admin or regular)."""
    username = forms.CharField(label='Username', max_length=150, widget=forms.TextInput(attrs={'class': 'form-input', 'id': 'new-username'}))
    email = forms.EmailField(label='Email', widget=forms.EmailInput(attrs={'class': 'form-input', 'id': 'new-email'}))
    password = forms.CharField(label='Password', widget=forms.PasswordInput(attrs={'class': 'form-input', 'id': 'new-password'}))
    ROLE_CHOICES = [('admin', 'Admin'), ('user', 'User')]
    role = forms.ChoiceField(label='Role', choices=ROLE_CHOICES, widget=forms.Select(attrs={'class': 'form-input', 'id': 'new-role'}))
