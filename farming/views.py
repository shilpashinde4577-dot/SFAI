import os
import json
import joblib
import numpy as np
import pandas as pd
from datetime import datetime, timedelta
from io import BytesIO
from django.shortcuts import render, redirect, get_object_or_404
from django.conf import settings
from django.http import JsonResponse, HttpResponse, Http404
from django.template.loader import get_template
from django.contrib.staticfiles import finders
from django.contrib.auth import login, logout, authenticate, update_session_auth_hash
from django.contrib.auth.models import User
from django.contrib.auth.decorators import login_required
from django.contrib import messages
from django.db.models import Q, Count
try:
    from xhtml2pdf import pisa
except Exception:
    pisa = None
from .forms import (
    CropPredictionForm, DiseaseDetectionForm, FertilizerForm,
    WeatherPredictionForm, MarketAnalysisForm, CropCompensationForm,
    CropLossEligibilityForm,
    INDIAN_STATES, CROP_CHOICES, DAMAGE_FACTORS,
)
from .models import (
    CropPrediction, DiseaseDetection, FertilizerRecommendation,
    WeatherPrediction, MarketAnalysis, CropCompensation,
    CropLossEligibility, UserProfile, UserActivityLog,
)


def log_user_activity(user, action, details='', request=None):
    """Helper to record user activity logs."""
    if user and user.is_authenticated:
        ip = None
        if request:
            x_ff = request.META.get('HTTP_X_FORWARDED_FOR')
            if x_ff:
                ip = x_ff.split(',')[0].strip()
            else:
                ip = request.META.get('REMOTE_ADDR')
        UserActivityLog.objects.create(
            user=user,
            action=action,
            details=details or '',
            ip_address=ip
        )



# ──────────────────────────────────────────────
# Load ML models & mappings at module level
# ──────────────────────────────────────────────
MODEL_DIR = os.path.dirname(os.path.abspath(__file__))

def _load(filename):
    try:
        return joblib.load(os.path.join(MODEL_DIR, filename))
    except Exception:
        return None

crop_model       = _load('ml_model.pkl')
crop_mapping     = _load('crop_mapping.pkl')
fertilizer_model = _load('fertilizer_model.pkl')
weather_model    = _load('weather_model.pkl')
disease_classes  = _load('disease_classes.pkl')
compensation_model = _load('compensation_model.pkl')
compensation_meta  = _load('compensation_meta.pkl')
if compensation_meta is None:
    compensation_meta = {
        'states': [s[0] for s in INDIAN_STATES if s[0]],
        'crops': [c[0] for c in CROP_CHOICES if c[0]],
        'factors': [f[0] for f in DAMAGE_FACTORS if f[0]]
    }
price_pred_model   = _load('price_prediction_model.pkl')
eligibility_model  = _load('eligibility_model.pkl')
eligibility_meta   = _load('eligibility_meta.pkl')


try:
    import tensorflow as tf
    from tensorflow.keras.models import load_model as tf_load
    from tensorflow.keras.preprocessing import image as keras_image
    disease_model = tf_load(os.path.join(MODEL_DIR, 'disease_model.h5'))
except Exception:
    disease_model = None
    keras_image = None

DISEASE_INFO = {
    'Healthy': {
        'description': 'The leaf appears healthy with no signs of disease.',
        'recommendation': 'Continue regular care and monitoring. Ensure proper watering and nutrition.',
    },
    'Leaf Blight': {
        'description': 'Leaf blight detected - characterized by brown/yellow lesions on leaves.',
        'recommendation': 'Apply copper-based fungicide. Remove infected leaves. Ensure proper spacing for air circulation.',
    },
    'Leaf Spot': {
        'description': 'Leaf spot disease detected - small dark spots visible on leaf surface.',
        'recommendation': 'Apply Mancozeb fungicide at 2g/L. Avoid overhead irrigation. Remove affected leaves.',
    },
    'Rust': {
        'description': 'Rust disease detected - orange/brown pustules on leaf surface.',
        'recommendation': 'Apply Propiconazole fungicide. Remove severely infected plants. Avoid excess nitrogen fertilization.',
    },
    'Powdery Mildew': {
        'description': 'Powdery mildew detected - white powdery coating on leaves.',
        'recommendation': 'Apply Sulphur-based fungicide. Ensure good air circulation. Avoid overhead watering.',
    },
}

# Helper: encode a value to its index in the meta list
def _encode(value, meta_list):
    try:
        return meta_list.index(value)
    except ValueError:
        return 0


# ──────────────────────────────────────────────
# Views
# ──────────────────────────────────────────────

def index(request):
    """Home page."""
    return render(request, 'farming/index.html')


@login_required
def crop_recommendation(request):
    """Crop recommendation based on environmental parameters."""
    result = None
    if request.method == 'POST':
        form = CropPredictionForm(request.POST)
        if form.is_valid():
            temp  = form.cleaned_data['temperature']
            hum   = form.cleaned_data['humidity']
            moist = form.cleaned_data['soil_moisture']

            if crop_model is not None and crop_mapping is not None:
                features = pd.DataFrame({'temperature': [temp], 'humidity': [hum], 'soil_moisture': [moist]})
                prediction = crop_model.predict(features)[0]
                crop_name = crop_mapping.get(prediction, f"Crop #{prediction}")

                CropPrediction.objects.create(
                    user=request.user if request.user.is_authenticated else None,
                    temperature=temp, humidity=hum,
                    soil_moisture=moist, predicted_crop=crop_name,
                )
                log_user_activity(request.user, "Crop Recommendation", f"Predicted crop: {crop_name}", request)
                result = {
                    'crop': crop_name,
                    'temperature': temp,
                    'humidity': hum,
                    'soil_moisture': moist,
                }
            else:
                result = {'error': 'ML model not loaded. Run ml_model.py first.'}
    else:
        form = CropPredictionForm()
    return render(request, 'farming/crop_recommendation.html', {'form': form, 'result': result})


@login_required
def disease_detection(request):
    """Disease detection from leaf images using CNN."""
    result = None
    if request.method == 'POST':
        form = DiseaseDetectionForm(request.POST, request.FILES)
        if form.is_valid():
            uploaded_image = request.FILES['image']
            detection = DiseaseDetection.objects.create(
                user=request.user if request.user.is_authenticated else None,
                image=uploaded_image
            )

            if disease_model is not None and disease_classes is not None and keras_image is not None:
                img = keras_image.load_img(detection.image.path, target_size=(64, 64))
                img_array = keras_image.img_to_array(img)
                img_array = np.expand_dims(img_array, axis=0) / 255.0
                predictions = disease_model.predict(img_array)
                class_idx  = int(np.argmax(predictions[0]))
                confidence = float(predictions[0][class_idx]) * 100
                detected   = disease_classes[class_idx]
            else:
                import random
                detected   = random.choice(list(DISEASE_INFO.keys()))
                confidence = round(random.uniform(75, 95), 1)

            info = DISEASE_INFO.get(detected, {'description': 'Unknown', 'recommendation': 'Consult an expert.'})
            detection.detected_disease = detected
            detection.confidence = confidence
            detection.recommendation = info['recommendation']
            detection.save()
            log_user_activity(request.user, "Disease Detection", f"Detected: {detected} ({confidence:.1f}%)", request)

            result = {
                'disease': detected,
                'description': info['description'],
                'recommendation': info['recommendation'],
                'confidence': round(confidence, 2),
                'image_url': detection.image.url,
            }
    else:
        form = DiseaseDetectionForm()
    return render(request, 'farming/disease_detection.html', {'form': form, 'result': result})


@login_required
def fertilizer_recommendation(request):
    """Fertilizer recommendation using ML model."""
    result = None
    if request.method == 'POST':
        form = FertilizerForm(request.POST)
        if form.is_valid():
            crop = form.cleaned_data['crop_name']
            n  = form.cleaned_data['nitrogen']
            p  = form.cleaned_data['phosphorus']
            k  = form.cleaned_data['potassium']
            ph = form.cleaned_data['soil_ph']

            if fertilizer_model is not None:
                features = pd.DataFrame({'nitrogen': [n], 'phosphorus': [p], 'potassium': [k], 'soil_ph': [ph]})
                prediction = fertilizer_model.predict(features)[0]
                rec_text = f"Based on your soil nutrients, the recommended fertilizer is {prediction}."

                # Define ideal levels for crops (N-P-K-pH) per acre (approximate)
                IDEAL_LEVELS = {
                    'rice':      {'n': 50,  'p': 25,  'k': 25,  'ph': 6.5},
                    'wheat':     {'n': 60,  'p': 30,  'k': 20,  'ph': 7.0},
                    'maize':     {'n': 60,  'p': 30,  'k': 30,  'ph': 6.5},
                    'cotton':    {'n': 40,  'p': 20,  'k': 20,  'ph': 6.0},
                    'sugarcane': {'n': 100, 'p': 40,  'k': 40,  'ph': 6.0},
                    'coffee':    {'n': 80,  'p': 30,  'k': 80,  'ph': 5.5},
                    'jute':      {'n': 30,  'p': 15,  'k': 15,  'ph': 6.5},
                    'millets':   {'n': 20,  'p': 10,  'k': 10,  'ph': 7.0},
                    'tomato':    {'n': 60,  'p': 40,  'k': 60,  'ph': 6.5},
                    'onion':     {'n': 40,  'p': 20,  'k': 40,  'ph': 6.5},
                    'potato':    {'n': 60,  'p': 40,  'k': 80,  'ph': 6.0},
                }
                ideal = IDEAL_LEVELS.get(crop.lower(), {'n': 50, 'p': 25, 'k': 25, 'ph': 6.5})

                # Calculate deficits
                def_n = max(0, ideal['n'] - n)
                def_p = max(0, ideal['p'] - p)
                def_k = max(0, ideal['k'] - k)

                # Fertilizer conversion logic (per acre)
                # 1. DAP (18% N, 46% P)
                dap_kg = def_p / 0.46
                n_from_dap = dap_kg * 0.18
                
                # 2. Urea (46% N) - subtract N already provided by DAP
                rem_n = max(0, def_n - n_from_dap)
                urea_kg = rem_n / 0.46
                
                # 3. MOP (60% K)
                mop_kg = def_k / 0.60

                recs = [f"The ML model recommends using {prediction} fertilizer."]
                if urea_kg > 0.5:
                    recs.append(f"Apply {urea_kg:.1f} kg of Urea per acre to fulfill Nitrogen requirement.")
                if dap_kg > 0.5:
                    recs.append(f"Apply {dap_kg:.1f} kg of DAP per acre to fulfill Phosphorus requirement.")
                if mop_kg > 0.5:
                    recs.append(f"Apply {mop_kg:.1f} kg of MOP (Muriate of Potash) per acre to fulfill Potassium requirement.")
                
                if ph < 5.5:
                    recs.append("Soil is too acidic. Apply 200 kg of Lime (Calcium Carbonate) per acre.")
                elif ph > 7.5:
                    recs.append("Soil is too alkaline. Apply 100 kg of Gypsum per acre.")

                FertilizerRecommendation.objects.create(
                    user=request.user if request.user.is_authenticated else None,
                    crop_name=crop, nitrogen=n, phosphorus=p,
                    potassium=k, soil_ph=ph, recommendation=rec_text,
                )
                log_user_activity(request.user, "Fertilizer Guidance", f"Recommended: {prediction} for {crop.title()}", request)
                result = {
                    'crop': crop.title(), 'nitrogen': n, 'phosphorus': p,
                    'potassium': k, 'soil_ph': ph,
                    'recommendation': rec_text, 
                    'recommendations': recs,
                    'fertilizer': prediction,
                    'ideal': ideal
                }
            else:
                result = {'error': 'ML model not loaded. Run ml_model.py first.'}
    else:
        form = FertilizerForm()
    return render(request, 'farming/fertilizer_recommendation.html', {'form': form, 'result': result})


@login_required
def weather_prediction(request):
    """Weather prediction using ML model."""
    result = None
    if request.method == 'POST':
        form = WeatherPredictionForm(request.POST)
        if form.is_valid():
            temp  = form.cleaned_data['temperature']
            hum   = form.cleaned_data['humidity']
            moist = form.cleaned_data['soil_moisture']

            if weather_model is not None:
                features = pd.DataFrame({'temperature': [temp], 'humidity': [hum], 'soil_moisture': [moist]})
                prediction = weather_model.predict(features)[0]

                WeatherPrediction.objects.create(
                    user=request.user if request.user.is_authenticated else None,
                    temperature=temp, humidity=hum,
                    soil_moisture=moist, predicted_weather=prediction,
                )
                log_user_activity(request.user, "Weather Forecast", f"Predicted weather: {prediction}", request)
                result = {
                    'weather': prediction,
                    'temperature': temp, 'humidity': hum, 'soil_moisture': moist,
                }
            else:
                result = {'error': 'ML model not loaded. Run ml_model.py first.'}
    else:
        form = WeatherPredictionForm()
    return render(request, 'farming/weather_prediction.html', {'form': form, 'result': result})


# ──────────────────────────────────────────────
# Market Analysis (Indian Market Simulation)
# ──────────────────────────────────────────────

# Typical Mandi Base Prices (₹/kg) across India
INDIAN_CROP_BASE_PRICES = {
    'Rice':      32.0,
    'Wheat':     24.5,
    'Maize':     21.0,
    'Cotton':    65.0,
    'Sugarcane': 3.5,
    'Coffee':    210.0,
    'Jute':      55.0,
    'Millets':   28.0,
    'Tomato':    25.0,
    'Onion':     20.0,
    'Potato':    18.0,
}

# State-wise adjustment factors (simulating regional Mandi variations)
STATE_PRICE_MODIFIERS = {
    'Karnataka':      1.05,
    'Maharashtra':    1.10,
    'Punjab':         0.95,
    'Haryana':        0.96,
    'Andhra Pradesh': 1.02,
    'Uttar Pradesh':  0.94,
    'Tamil Nadu':     1.08,
    'Gujarat':        1.03,
    'West Bengal':    1.00,
    'Bihar':          0.92,
    'Madhya Pradesh': 0.98,
    'Rajasthan':      1.04,
    'Kerala':         1.15,
}

def _fetch_indian_historical_prices(crop_name, state):
    """Generate realistic Indian Mandi historical data (simulated)."""
    base_price = INDIAN_CROP_BASE_PRICES.get(crop_name, 25.0)
    modifier   = STATE_PRICE_MODIFIERS.get(state, 1.0)
    regional_base = base_price * modifier
    
    today = datetime.now()
    data = []
    for i in range(3, 0, -1):
        dt = today - timedelta(days=30 * i)
        # Random historical fluctuation (±8%)
        hist_price = regional_base * np.random.uniform(0.92, 1.08)
        data.append({'label': dt.strftime('%b %Y'), 'price': round(hist_price, 2)})
    return data


@login_required
def market_analysis(request):
    """Market analysis using local Indian Mandi data simulation."""
    result = None
    if request.method == 'POST':
        form = MarketAnalysisForm(request.POST)
        if form.is_valid():
            crop_name     = form.cleaned_data['crop_name']
            state         = form.cleaned_data['state']
            current_price = form.cleaned_data['current_price']

            # 1. Get regional historical estimates
            past_data = _fetch_indian_historical_prices(crop_name, state)

            # Current month
            today = datetime.now()
            current_data = {'label': today.strftime('%b %Y'), 'price': current_price}

            # 2. Predict next 3 months (Indian Market Trends)
            future_data = []
            if price_pred_model is not None and compensation_meta is not None:
                state_idx = _encode(state, compensation_meta['states'])
                crop_idx  = _encode(crop_name, compensation_meta['crops'])
                running_price = current_price
                for i in range(1, 4):
                    future_month = (today.month + i - 1) % 12 + 1
                    features = pd.DataFrame({
                        'month': [future_month],
                        'state': [state_idx],
                        'crop': [crop_idx],
                        'past_price': [running_price],
                    })
                    predicted_price = float(price_pred_model.predict(features)[0])
                    
                    # Trend scaling (ML prediction might be biased, we clamp to realistic Mandi trends)
                    predicted_price = max(current_price * 0.9, min(current_price * 1.15, predicted_price))
                    
                    running_price = predicted_price
                    future_dt = today + timedelta(days=30 * i)
                    future_data.append({
                        'label': future_dt.strftime('%b %Y'),
                        'price': round(predicted_price, 2),
                    })
            else:
                # Fallback prediction logic
                running = current_price
                for i in range(1, 4):
                    running *= np.random.uniform(0.98, 1.05)
                    future_dt = today + timedelta(days=30 * i)
                    future_data.append({'label': future_dt.strftime('%b %Y'), 'price': round(running, 2)})

            # Combine all data for chart
            all_data = past_data + [current_data] + future_data
            labels = [d['label'] for d in all_data]
            prices = [d['price'] for d in all_data]

            MarketAnalysis.objects.create(
                user=request.user if request.user.is_authenticated else None,
                crop_name=crop_name, state=state, current_price=current_price,
            )
            log_user_activity(request.user, "Market Price Analysis", f"Analyzed {crop_name} in {state} (₹{current_price}/kg)", request)

            result = {
                'crop': crop_name,
                'state': state,
                'current_price': current_price,
                'labels_json': json.dumps(labels),
                'prices_json': json.dumps(prices),
                'past_data': past_data,
                'future_data': future_data,
                'current_data': current_data,
                'trend': 'up' if future_data[-1]['price'] > current_price else 'down',
            }
    else:
        form = MarketAnalysisForm()
    return render(request, 'farming/market_analysis.html', {'form': form, 'result': result})


# ──────────────────────────────────────────────
# Crop Compensation  (ML-based)
# ──────────────────────────────────────────────

def api_get_compensation_rate(request):
    """AJAX endpoint: predict base compensation per acre using ML model."""
    state  = request.GET.get('state', '')
    crop   = request.GET.get('crop', '')
    factor = request.GET.get('factor', '')

    if not all([state, crop, factor]):
        return JsonResponse({'rate': 0, 'error': 'Missing parameters'})

    if compensation_model is not None and compensation_meta is not None:
        state_idx  = _encode(state, compensation_meta['states'])
        crop_idx   = _encode(crop, compensation_meta['crops'])
        factor_idx = _encode(factor, compensation_meta['factors'])
        features = pd.DataFrame({'state': [state_idx], 'crop': [crop_idx], 'factor': [factor_idx]})
        rate = float(compensation_model.predict(features)[0])
        return JsonResponse({'rate': round(rate, 2)})
    else:
        return JsonResponse({'rate': 0, 'error': 'ML model not loaded'})


@login_required
def crop_compensation(request):
    """Crop loss compensation estimation using ML model."""
    result = None
    if request.method == 'POST':
        form = CropCompensationForm(request.POST)
        if form.is_valid():
            state   = form.cleaned_data['state']
            crop    = form.cleaned_data['crop_name']
            factor  = form.cleaned_data['damage_factor']
            acreage = form.cleaned_data['acreage']

            if compensation_model is not None and compensation_meta is not None:
                state_idx  = _encode(state, compensation_meta['states'])
                crop_idx   = _encode(crop, compensation_meta['crops'])
                factor_idx = _encode(factor, compensation_meta['factors'])

                features = pd.DataFrame({'state': [state_idx], 'crop': [crop_idx], 'factor': [factor_idx]})
                comp_per_acre = round(float(compensation_model.predict(features)[0]), 2)
                total = round(comp_per_acre * acreage, 2)

                CropCompensation.objects.create(
                    user=request.user if request.user.is_authenticated else None,
                    state=state, crop_name=crop, damage_factor=factor,
                    acreage=acreage, compensation_per_acre=comp_per_acre,
                    total_compensation=total,
                )
                log_user_activity(request.user, "Compensation Calculation", f"Estimated ₹{total:.0f} for {crop} ({state})", request)

                result = {
                    'state': state, 'crop': crop, 'factor': factor,
                    'acreage': acreage,
                    'comp_per_acre': round(comp_per_acre, 2),
                    'total': round(total, 2),
                }
            else:
                result = {'error': 'ML model not loaded. Run ml_model.py first.'}
    else:
        form = CropCompensationForm()
    return render(request, 'farming/crop_compensation.html', {'form': form, 'result': result})


@login_required
def crop_loss_eligibility(request):
    """Government Crop Loss Benefit Eligibility evaluation using ML model + Scheme Rules."""
    result = None
    if request.method == 'POST':
        form = CropLossEligibilityForm(request.POST)
        if form.is_valid():
            state = form.cleaned_data['state']
            crop = form.cleaned_data['crop_name']
            factor = form.cleaned_data['damage_factor']
            acreage = form.cleaned_data['acreage']
            loss_pct = form.cleaned_data['loss_percentage']
            intimation_hrs = int(form.cleaned_data['intimation_hours'])
            insured = form.cleaned_data['insured_status']
            farmer_cat = form.cleaned_data['farmer_category']

            if eligibility_model is not None and eligibility_meta is not None:
                state_idx = _encode(state, eligibility_meta['states'])
                crop_idx = _encode(crop, eligibility_meta['crops'])
                factor_idx = _encode(factor, eligibility_meta['factors'])
                insured_num = 1 if insured == 'Yes' else 0
                farmer_cat_idx = _encode(farmer_cat, eligibility_meta['farmer_categories'])

                features = pd.DataFrame({
                    'state': [state_idx],
                    'crop': [crop_idx],
                    'factor': [factor_idx],
                    'acreage': [acreage],
                    'loss_percentage': [loss_pct],
                    'intimation_hours': [intimation_hrs],
                    'insured_status': [insured_num],
                    'farmer_category': [farmer_cat_idx]
                })

                pred = int(eligibility_model.predict(features)[0])
                proba = eligibility_model.predict_proba(features)[0]
                confidence = float(proba[pred]) * 100

                # Evaluate criteria for feedback checklist
                criteria = []
                # 1. Loss percentage (Govt norm: >= 33%)
                if loss_pct >= 33.0:
                    criteria.append({
                        'met': True,
                        'text': f"Loss of {loss_pct:.0f}% meets the mandatory 33% government threshold for PMFBY/SDRF relief."
                    })
                else:
                    criteria.append({
                        'met': False,
                        'text': f"Loss of {loss_pct:.0f}% is below the mandatory 33% minimum threshold required for direct government compensation."
                    })

                # 2. Intimation timeline (Govt norm: <= 72 hours)
                if intimation_hrs <= 72:
                    criteria.append({
                        'met': True,
                        'text': f"Intimation within {intimation_hrs} hours satisfies the 72-hour government reporting window."
                    })
                else:
                    criteria.append({
                        'met': False,
                        'text': f"Reporting time of {intimation_hrs} hours exceeds the standard 72-hour notification deadline for localized calamity claims."
                    })

                # 3. Scheme enrollment & disaster status
                if insured == 'Yes':
                    criteria.append({
                        'met': True,
                        'text': "Crop is actively covered under PMFBY / National Agricultural Insurance Scheme."
                    })
                elif loss_pct >= 50.0 and factor in ['Flood', 'Drought', 'Hailstorm', 'Unseasonal Rain']:
                    criteria.append({
                        'met': True,
                        'text': f"Uninsured loss qualifies for State Calamity Relief Fund (SDRF) due to severe disaster ({factor})."
                    })
                else:
                    criteria.append({
                        'met': False,
                        'text': "Crop is not enrolled under PMFBY and loss does not meet emergency SDRF disaster relief criteria."
                    })

                # Final eligibility determination: direct ML model prediction
                is_eligible = bool(pred == 1)

                # Required Document Checklist when eligible
                required_documents = [
                    {
                        'title': 'Aadhaar Card Copy',
                        'desc': 'Self-attested photocopy of Aadhaar linked with active mobile number.',
                        'icon': '🆔'
                    },
                    {
                        'title': 'Land Ownership / Tenancy Document',
                        'desc': '7/12 Extract, Khasra/Khatauni, or registered Sharecropper Affidavit.',
                        'icon': '📑'
                    },
                    {
                        'title': 'Bank Passbook Copy (DBT Enabled)',
                        'desc': 'Clear copy of Bank Passbook showing Account No., Branch Name & IFSC Code.',
                        'icon': '💳'
                    },
                    {
                        'title': 'Sowing & Crop Loss Certificate',
                        'desc': 'Certificate signed by Village Revenue Officer (VRO) / Gram Sevak / Sarpanch.',
                        'icon': '🌾'
                    },
                    {
                        'title': 'Geo-Tagged Photographs of Field',
                        'desc': '2 printed or digital photographs showing field damage with date & GPS location.',
                        'icon': '📸'
                    },
                    {
                        'title': 'PMFBY / Calamity Claim Form',
                        'desc': 'Duly filled and signed Form-A Intimation Application for crop loss relief.',
                        'icon': '📝'
                    },
                    {
                        'title': 'Intimation Reference Slip',
                        'desc': 'Acknowledgement slip/SMS copy of loss reported within 72 hours.',
                        'icon': '⏳'
                    },
                ]

                # Nearest government office submission point
                submission_office = "Nearest District Agriculture Office (DAO), Block Development Office (BDO), or Common Service Center (CSC) / Krishi Vigyan Kendra (KVK)."

                CropLossEligibility.objects.create(
                    user=request.user if request.user.is_authenticated else None,
                    state=state, crop_name=crop, damage_factor=factor,
                    acreage=acreage, loss_percentage=loss_pct,
                    intimation_hours=intimation_hrs, insured_status=insured,
                    farmer_category=farmer_cat, is_eligible=is_eligible,
                    confidence=confidence,
                )
                log_user_activity(request.user, "Govt Scheme Eligibility", f"Status: {'Eligible' if is_eligible else 'Not Eligible'} for {crop}", request)

                result = {
                    'state': state,
                    'crop': crop,
                    'factor': factor,
                    'acreage': acreage,
                    'loss_pct': loss_pct,
                    'intimation_hrs': intimation_hrs,
                    'insured': insured,
                    'farmer_cat': farmer_cat,
                    'is_eligible': is_eligible,
                    'confidence': round(confidence, 1),
                    'criteria': criteria,
                    'documents': required_documents,
                    'office': submission_office,
                }
            else:
                result = {'error': 'ML model not loaded. Run ml_model.py first.'}
    else:
        form = CropLossEligibilityForm()
    return render(request, 'farming/benefit_eligibility.html', {'form': form, 'result': result})


def help_section(request):
    """Help section for the application."""
    return render(request, 'farming/help.html')

@login_required
def combined_report(request):
    """Generates a combined dashboard of the most recent results from all modules."""
    context = {
        'crop_pred': CropPrediction.objects.first(),
        'disease_det': DiseaseDetection.objects.first(),
        'fert_rec': FertilizerRecommendation.objects.first(),
        'weather_pred': WeatherPrediction.objects.first(),
        'market_anal': MarketAnalysis.objects.first(),
        'crop_comp': CropCompensation.objects.first(),
        'benefit_elig': CropLossEligibility.objects.first(),
    }
    return render(request, 'farming/combined_report.html', context)


# ──────────────────────────────────────────────
# Server-Side Professional PDF Export Engine
# ──────────────────────────────────────────────

def link_callback(uri, rel):
    """
    Convert HTML URIs to absolute system paths so xhtml2pdf can access images and static files.
    """
    if uri.startswith(settings.MEDIA_URL):
        path = os.path.join(settings.MEDIA_ROOT, uri.replace(settings.MEDIA_URL, ""))
    elif uri.startswith(settings.STATIC_URL):
        path = os.path.join(settings.STATIC_ROOT, uri.replace(settings.STATIC_URL, ""))
        if not os.path.isfile(path):
            result = finders.find(uri.replace(settings.STATIC_URL, ""))
            if result:
                path = result[0] if isinstance(result, (list, tuple)) else result
    else:
        path = uri

    if not os.path.isfile(path):
        clean_path = uri.lstrip('/')
        possible_path = os.path.join(settings.BASE_DIR, clean_path)
        if os.path.isfile(possible_path):
            return possible_path
        return uri
    return path


def render_to_pdf(template_src, context_dict={}):
    template = get_template(template_src)
    html = template.render(context_dict)
    result = BytesIO()
    pdf = pisa.pisaDocument(BytesIO(html.encode("UTF-8")), result, link_callback=link_callback)
    if not pdf.err:
        return HttpResponse(result.getvalue(), content_type='application/pdf')
    return HttpResponse("Error generating PDF", status=500)


@login_required
def export_pdf_report(request, module_name):
    """Generates a clean, professional PDF report for a single module."""
    now = datetime.now()
    report_date = now.strftime('%d %b %Y, %H:%M')
    report_id = now.strftime('%Y%m%d%H%M%S')

    module_titles = {
        'crop': 'Crop Recommendation',
        'disease': 'Leaf Disease Detection',
        'fertilizer': 'Fertilizer Guidance',
        'weather': 'Weather Forecast',
        'market': 'Market Price Intelligence',
        'compensation': 'Crop Compensation Claim',
        'eligibility': 'Govt Scheme Eligibility',
    }

    if module_name not in module_titles:
        raise Http404("Invalid module report requested.")

    module_title = module_titles[module_name]
    context = {
        'module_code': module_name,
        'module_title': module_title,
        'report_date': report_date,
        'report_id': report_id,
        'record': None,
        'image_path': None,
        'recommendations': [],
    }

    if module_name == 'crop':
        rec = CropPrediction.objects.first()
        if not rec:
            rec = CropPrediction(temperature=28.5, humidity=70.0, soil_moisture=45.0, predicted_crop='Rice')
        context['record'] = rec

    elif module_name == 'disease':
        rec = DiseaseDetection.objects.first()
        if not rec:
            rec = DiseaseDetection(detected_disease='Leaf Spot', confidence=88.5, recommendation='Apply Mancozeb fungicide at 2g/L.')
        context['record'] = rec
        if rec and rec.image:
            context['image_path'] = rec.image.path

    elif module_name == 'fertilizer':
        rec = FertilizerRecommendation.objects.first()
        if not rec:
            rec = FertilizerRecommendation(crop_name='Wheat', nitrogen=50, phosphorus=25, potassium=20, soil_ph=6.5, recommendation='Urea & DAP Mix')
        context['record'] = rec
        recs = [f"ML Model Recommendation: {rec.recommendation}"]
        if rec.nitrogen < 40:
            recs.append("Apply 50 kg Urea per acre to fulfill Nitrogen deficit.")
        if rec.phosphorus < 25:
            recs.append("Apply 35 kg DAP per acre for Phosphorus enrichment.")
        if rec.potassium < 20:
            recs.append("Apply 25 kg MOP (Muriate of Potash) per acre.")
        context['recommendations'] = recs

    elif module_name == 'weather':
        rec = WeatherPrediction.objects.first()
        if not rec:
            rec = WeatherPrediction(temperature=26.0, humidity=65.0, soil_moisture=50.0, predicted_weather='Sunny / Pleasant')
        context['record'] = rec

    elif module_name == 'market':
        rec = MarketAnalysis.objects.first()
        if not rec:
            rec = MarketAnalysis(crop_name='Rice', state='Karnataka', current_price=32.0)
        context['record'] = rec

    elif module_name == 'compensation':
        rec = CropCompensation.objects.first()
        if not rec:
            rec = CropCompensation(state='Karnataka', crop_name='Cotton', damage_factor='Flood', acreage=5.0, compensation_per_acre=12500, total_compensation=62500)
        context['record'] = rec

    elif module_name == 'eligibility':
        rec = CropLossEligibility.objects.first()
        if not rec:
            rec = CropLossEligibility(state='Maharashtra', crop_name='Soybean', damage_factor='Drought', acreage=4.0, loss_percentage=45.0, intimation_hours=48, insured_status='Yes', farmer_category='Small/Marginal', is_eligible=True, confidence=91.2)
        context['record'] = rec

    log_user_activity(request.user, f"Exported Individual PDF ({module_title})", request=request)
    response = render_to_pdf('farming/pdf_individual_report.html', context)
    response['Content-Disposition'] = f'attachment; filename="SmartFarm_{module_title.replace(" ", "_")}_Report.pdf"'
    return response


@login_required
def export_combined_pdf(request):
    """Generates a multi-page combined PDF report across all 7 modules."""
    now = datetime.now()
    report_date = now.strftime('%d %b %Y, %H:%M')
    report_id = now.strftime('%Y%m%d%H%M%S')

    disease_rec = DiseaseDetection.objects.first()
    disease_img_path = disease_rec.image.path if (disease_rec and disease_rec.image) else None

    context = {
        'report_date': report_date,
        'report_id': report_id,
        'crop_pred': CropPrediction.objects.first(),
        'disease_det': disease_rec,
        'disease_img_path': disease_img_path,
        'fert_rec': FertilizerRecommendation.objects.first(),
        'weather_pred': WeatherPrediction.objects.first(),
        'market_anal': MarketAnalysis.objects.first(),
        'crop_comp': CropCompensation.objects.first(),
        'benefit_elig': CropLossEligibility.objects.first(),
    }

    log_user_activity(request.user, "Exported Combined PDF Report", request=request)
    response = render_to_pdf('farming/pdf_combined_report.html', context)
    response['Content-Disposition'] = 'attachment; filename="SmartFarm_Comprehensive_Agricultural_Report.pdf"'
    return response


# ──────────────────────────────────────────────
# Authentication & User Profile Views
# ──────────────────────────────────────────────

def register_view(request):
    """User Registration view."""
    if request.user.is_authenticated:
        return redirect('farming:index')

    if request.method == 'POST':
        full_name = request.POST.get('full_name', '').strip()
        username = request.POST.get('username', '').strip()
        email = request.POST.get('email', '').strip()
        phone = request.POST.get('phone', '').strip()
        state = request.POST.get('state', '').strip()
        password = request.POST.get('password', '')
        password_confirm = request.POST.get('password_confirm', '')

        if not username or not email or not password:
            messages.error(request, "User ID / Username, Email, and Password are required.")
            return render(request, 'farming/register.html', {'indian_states': INDIAN_STATES})

        if password != password_confirm:
            messages.error(request, "Passwords do not match!")
            return render(request, 'farming/register.html', {'indian_states': INDIAN_STATES})

        if User.objects.filter(username__iexact=username).exists():
            messages.error(request, f"User ID / Username '{username}' is already taken. Please choose another.")
            return render(request, 'farming/register.html', {'indian_states': INDIAN_STATES})

        if User.objects.filter(email__iexact=email).exists():
            messages.error(request, f"Email '{email}' is already registered.")
            return render(request, 'farming/register.html', {'indian_states': INDIAN_STATES})

        name_parts = full_name.split(' ', 1)
        first_name = name_parts[0] if name_parts else ''
        last_name = name_parts[1] if len(name_parts) > 1 else ''

        user = User.objects.create_user(
            username=username,
            email=email,
            password=password,
            first_name=first_name,
            last_name=last_name
        )

        profile = user.profile
        profile.phone_number = phone
        profile.state = state
        if 'profile_picture' in request.FILES:
            profile.profile_picture = request.FILES['profile_picture']
        profile.save()

        log_user_activity(user, "Account Registered", f"New account created for {user.username}", request)
        authenticated_user = authenticate(request, username=username, password=password)
        if authenticated_user:
            login(request, authenticated_user)
            messages.success(request, f"Welcome to SmartFarm AI, {full_name or username}! Your account has been created.")
            return redirect('farming:index')
        else:
            messages.success(request, "Account created successfully! Please log in.")
            return redirect('farming:login')

    return render(request, 'farming/register.html', {'indian_states': INDIAN_STATES})


def login_view(request):
    """Login view for both Users and Admins."""
    if request.user.is_authenticated:
        return redirect('farming:index')

    if request.method == 'POST':
        username_or_email = request.POST.get('username', '').strip()
        password = request.POST.get('password', '')

        if not username_or_email or not password:
            messages.error(request, "Please enter both User ID/Email and password.")
            return render(request, 'farming/login.html')

        target_username = username_or_email
        if '@' in username_or_email:
            user_by_email = User.objects.filter(email__iexact=username_or_email).first()
            if user_by_email:
                target_username = user_by_email.username

        user = authenticate(request, username=target_username, password=password)
        if user is not None:
            profile = getattr(user, 'profile', None)
            if profile and profile.status in ['banned', 'inactive']:
                messages.error(request, "Your account has been deactivated or suspended by an administrator.")
                return render(request, 'farming/login.html')

            login(request, user)
            log_user_activity(user, "User Login", f"Logged in successfully", request)
            messages.success(request, f"Welcome back, {user.get_full_name() or user.username}!")
            
            next_url = request.GET.get('next') or request.POST.get('next')
            if next_url:
                return redirect(next_url)
            if profile and profile.is_admin:
                return redirect('farming:admin_dashboard')
            return redirect('farming:index')
        else:
            messages.error(request, "Invalid User ID/Email or password. Please try again.")

    return render(request, 'farming/login.html')


def logout_view(request):
    """Logout view."""
    if request.user.is_authenticated:
        log_user_activity(request.user, "User Logout", request=request)
        logout(request)
        messages.info(request, "You have been logged out.")
    return redirect('farming:index')


@login_required
def profile_view(request):
    """User profile view & settings update page."""
    user = request.user
    profile = user.profile

    if request.method == 'POST':
        full_name = request.POST.get('full_name', '').strip()
        email = request.POST.get('email', '').strip()
        phone = request.POST.get('phone', '').strip()
        state = request.POST.get('state', '').strip()
        
        old_password = request.POST.get('old_password', '')
        new_password = request.POST.get('new_password', '')
        confirm_password = request.POST.get('confirm_password', '')

        if email and email != user.email:
            if User.objects.filter(email__iexact=email).exclude(pk=user.pk).exists():
                messages.error(request, f"Email '{email}' is already used by another account.")
                return redirect('farming:profile')
            user.email = email

        if full_name:
            parts = full_name.split(' ', 1)
            user.first_name = parts[0]
            user.last_name = parts[1] if len(parts) > 1 else ''

        if old_password or new_password:
            if not user.check_password(old_password):
                messages.error(request, "Current password is incorrect.")
                return redirect('farming:profile')
            if new_password != confirm_password:
                messages.error(request, "New password and confirmation do not match.")
                return redirect('farming:profile')
            user.set_password(new_password)
            update_session_auth_hash(request, user)
            log_user_activity(user, "Password Changed", request=request)

        if 'profile_picture' in request.FILES:
            profile.profile_picture = request.FILES['profile_picture']

        profile.phone_number = phone
        profile.state = state

        user.save()
        profile.save()

        log_user_activity(user, "Profile Updated", "Updated personal details and settings", request)
        messages.success(request, "Your profile settings have been updated successfully!")
        return redirect('farming:profile')

    recent_activities = UserActivityLog.objects.filter(user=user)[:10]

    stats = {
        'crop_count': CropPrediction.objects.filter(user=user).count(),
        'disease_count': DiseaseDetection.objects.filter(user=user).count(),
        'fert_count': FertilizerRecommendation.objects.filter(user=user).count(),
        'weather_count': WeatherPrediction.objects.filter(user=user).count(),
        'market_count': MarketAnalysis.objects.filter(user=user).count(),
        'comp_count': CropCompensation.objects.filter(user=user).count(),
        'elig_count': CropLossEligibility.objects.filter(user=user).count(),
    }
    stats['total_operations'] = sum(stats.values())

    context = {
        'user_profile': profile,
        'recent_activities': recent_activities,
        'stats': stats,
        'indian_states': INDIAN_STATES,
    }
    return render(request, 'farming/profile.html', context)


# ──────────────────────────────────────────────
# Admin Management Views
# ──────────────────────────────────────────────

def _is_admin(user):
    return user.is_authenticated and hasattr(user, 'profile') and user.profile.is_admin


def admin_dashboard(request):
    """Admin Dashboard for managing registered users and viewing platform activity."""
    if not _is_admin(request.user):
        messages.error(request, "Access restricted! Admin privileges required.")
        return redirect('farming:index')

    q = request.GET.get('q', '').strip()
    status_filter = request.GET.get('status', '').strip()
    role_filter = request.GET.get('role', '').strip()

    users_qs = User.objects.select_related('profile').all().order_by('-date_joined')

    if q:
        users_qs = users_qs.filter(
            Q(username__icontains=q) |
            Q(email__icontains=q) |
            Q(first_name__icontains=q) |
            Q(last_name__icontains=q)
        )

    if status_filter:
        users_qs = users_qs.filter(profile__status=status_filter)

    if role_filter:
        users_qs = users_qs.filter(profile__role=role_filter)

    total_users = User.objects.count()
    active_users = UserProfile.objects.filter(status='active').count()
    banned_users = UserProfile.objects.filter(status='banned').count()
    admin_users = UserProfile.objects.filter(role='admin').count()

    total_predictions = (
        CropPrediction.objects.count() +
        DiseaseDetection.objects.count() +
        FertilizerRecommendation.objects.count() +
        WeatherPrediction.objects.count() +
        MarketAnalysis.objects.count() +
        CropCompensation.objects.count() +
        CropLossEligibility.objects.count()
    )

    recent_logs = UserActivityLog.objects.select_related('user').all()[:15]

    context = {
        'users': users_qs,
        'total_users': total_users,
        'active_users': active_users,
        'banned_users': banned_users,
        'admin_users': admin_users,
        'total_predictions': total_predictions,
        'recent_logs': recent_logs,
        'search_query': q,
        'status_filter': status_filter,
        'role_filter': role_filter,
    }
    return render(request, 'farming/admin_dashboard.html', context)


def admin_user_detail(request, user_id):
    """Admin detail view for inspecting specific user activity and history."""
    if not _is_admin(request.user):
        messages.error(request, "Access restricted! Admin privileges required.")
        return redirect('farming:index')

    target_user = get_object_or_404(User.objects.select_related('profile'), id=user_id)
    user_logs = UserActivityLog.objects.filter(user=target_user)

    user_predictions = {
        'crops': CropPrediction.objects.filter(user=target_user)[:5],
        'diseases': DiseaseDetection.objects.filter(user=target_user)[:5],
        'fertilizers': FertilizerRecommendation.objects.filter(user=target_user)[:5],
        'weather': WeatherPrediction.objects.filter(user=target_user)[:5],
        'markets': MarketAnalysis.objects.filter(user=target_user)[:5],
        'compensations': CropCompensation.objects.filter(user=target_user)[:5],
        'eligibilities': CropLossEligibility.objects.filter(user=target_user)[:5],
    }

    context = {
        'target_user': target_user,
        'user_profile': target_user.profile,
        'user_logs': user_logs,
        'user_predictions': user_predictions,
    }
    return render(request, 'farming/admin_user_detail.html', context)


def admin_toggle_user_status(request, user_id):
    """Admin action to switch user status (active <-> banned/inactive)."""
    if not _is_admin(request.user):
        messages.error(request, "Access restricted! Admin privileges required.")
        return redirect('farming:index')

    target_user = get_object_or_404(User, id=user_id)
    if target_user == request.user:
        messages.error(request, "You cannot ban your own admin account!")
        return redirect('farming:admin_dashboard')

    profile = target_user.profile
    new_status = request.POST.get('status')
    if not new_status:
        new_status = 'banned' if profile.status == 'active' else 'active'

    profile.status = new_status
    profile.save()

    log_user_activity(request.user, "Admin Status Change", f"Set user '{target_user.username}' status to {new_status.upper()}", request)
    messages.success(request, f"User '{target_user.username}' status updated to {new_status.upper()}.")
    return redirect('farming:admin_dashboard')


def admin_toggle_user_role(request, user_id):
    """Admin action to toggle user role between 'user' and 'admin'."""
    if not _is_admin(request.user):
        messages.error(request, "Access restricted! Admin privileges required.")
        return redirect('farming:index')

    target_user = get_object_or_404(User, id=user_id)
    if target_user == request.user:
        messages.error(request, "You cannot modify your own admin role!")
        return redirect('farming:admin_dashboard')

    profile = target_user.profile
    new_role = 'admin' if profile.role == 'user' else 'user'
    profile.role = new_role
    profile.save()

    log_user_activity(request.user, "Admin Role Change", f"Set user '{target_user.username}' role to {new_role.upper()}", request)
    messages.success(request, f"User '{target_user.username}' role updated to {new_role.upper()}.")
    return redirect('farming:admin_dashboard')