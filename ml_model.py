import pandas as pd
import numpy as np
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestClassifier
import joblib
import os

MODEL_DIR = os.path.dirname(os.path.abspath(__file__))

def train_crop_model():
    print("Training Crop Model...")
    try:
        df = pd.read_csv(os.path.join(MODEL_DIR, "farming/dataset.csv"))
        df['crop'] = df['crop'].astype('category')
        df['crop_code'] = df['crop'].cat.codes
        
        X = df[['temperature', 'humidity', 'soil_moisture']]
        y = df['crop_code']
        
        X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)
        model = RandomForestClassifier(n_estimators=100, random_state=42)
        model.fit(X_train, y_train)
        
        joblib.dump(model, os.path.join(MODEL_DIR, 'farming/ml_model.pkl'))
        crop_mapping = df[['crop', 'crop_code']].drop_duplicates().set_index('crop_code').to_dict()['crop']
        joblib.dump(crop_mapping, os.path.join(MODEL_DIR, 'farming/crop_mapping.pkl'))
        print("Crop Model trained and saved.")
    except Exception as e:
        print(f"Error training Crop Model: {e}")

def train_fertilizer_model():
    print("Training Fertilizer Model...")
    np.random.seed(42)
    n_samples = 500
    n = np.random.uniform(20, 140, n_samples)
    p = np.random.uniform(10, 80, n_samples)
    k = np.random.uniform(10, 80, n_samples)
    ph = np.random.uniform(5.0, 8.5, n_samples)
    
    fertilizers = ['Urea', 'DAP', 'MOP', 'NPK 10-26-26', 'SSP', 'Zinc Sulphate']
    y = np.random.choice(fertilizers, n_samples)
    
    X = pd.DataFrame({'nitrogen': n, 'phosphorus': p, 'potassium': k, 'soil_ph': ph})
    model = RandomForestClassifier(n_estimators=50, random_state=42)
    model.fit(X, y)
    
    joblib.dump(model, os.path.join(MODEL_DIR, 'farming/fertilizer_model.pkl'))
    print("Fertilizer Model trained and saved.")

def train_weather_model():
    print("Training Weather Model...")
    np.random.seed(42)
    n_samples = 500
    temp = np.random.uniform(10, 45, n_samples)
    hum = np.random.uniform(20, 95, n_samples)
    moist = np.random.uniform(10, 60, n_samples)
    
    conditions = ['Sunny', 'Rainy', 'Cloudy', 'Stormy']
    y = np.random.choice(conditions, n_samples)
    
    X = pd.DataFrame({'temperature': temp, 'humidity': hum, 'soil_moisture': moist})
    model = RandomForestClassifier(n_estimators=50, random_state=42)
    model.fit(X, y)
    
    joblib.dump(model, os.path.join(MODEL_DIR, 'farming/weather_model.pkl'))
    print("Weather Model trained and saved.")

def train_disease_cnn_model():
    print("Training Disease CNN Model...")
    try:
        import tensorflow as tf
        from tensorflow.keras import layers, models
        
        classes = ['Healthy', 'Leaf Blight', 'Leaf Spot', 'Rust', 'Powdery Mildew']
        
        model = models.Sequential([
            layers.Conv2D(16, (3, 3), activation='relu', input_shape=(64, 64, 3)),
            layers.MaxPooling2D((2, 2)),
            layers.Flatten(),
            layers.Dense(32, activation='relu'),
            layers.Dense(len(classes), activation='softmax')
        ])
        
        model.compile(optimizer='adam',
                      loss='sparse_categorical_crossentropy',
                      metrics=['accuracy'])
        
        X_dummy = np.random.rand(10, 64, 64, 3)
        y_dummy = np.random.randint(0, len(classes), 10)
        
        model.fit(X_dummy, y_dummy, epochs=1, verbose=0)
        
        model.save(os.path.join(MODEL_DIR, 'farming/disease_model.h5'))
        joblib.dump(classes, os.path.join(MODEL_DIR, 'farming/disease_classes.pkl'))
        print("Disease CNN Model trained and saved.")
    except ImportError:
        print("Tensorflow not installed. Skipping CNN model.")
    except Exception as e:
        print(f"Error training CNN: {e}")

def train_compensation_model():
    print("Training Compensation Model...")
    np.random.seed(42)
    n_samples = 1000
    
    # Simple encoded mapping for dummy data
    # State (0-28), Crop (0-10), Damage Factor (0=Flood, 1=Drought, 2=Pest/Animals)
    states = np.random.randint(0, 29, n_samples)
    crops = np.random.randint(0, 11, n_samples)
    factors = np.random.randint(0, 3, n_samples)
    
    # Base compensation logic (randomized around some base rules)
    # E.g., Floods pay more, certain states pay more
    base_pay = 15000 + (states * 500) + (crops * 200)
    multiplier = np.where(factors == 0, 1.2, np.where(factors == 1, 1.0, 0.8))
    
    y = base_pay * multiplier + np.random.normal(0, 2000, n_samples)
    y = np.maximum(y, 5000) # Minimum 5000 per acre
    
    X = pd.DataFrame({'state': states, 'crop': crops, 'factor': factors})
    
    from sklearn.ensemble import RandomForestRegressor
    model = RandomForestRegressor(n_estimators=50, random_state=42)
    model.fit(X, y)
    
    joblib.dump(model, os.path.join(MODEL_DIR, 'farming/compensation_model.pkl'))
    
    meta = {
        'states': [
            'Andhra Pradesh', 'Arunachal Pradesh', 'Assam', 'Bihar', 'Chhattisgarh',
            'Goa', 'Gujarat', 'Haryana', 'Himachal Pradesh', 'Jharkhand',
            'Karnataka', 'Kerala', 'Madhya Pradesh', 'Maharashtra', 'Manipur',
            'Meghalaya', 'Mizoram', 'Nagaland', 'Odisha', 'Punjab',
            'Rajasthan', 'Sikkim', 'Tamil Nadu', 'Telangana', 'Tripura',
            'Uttar Pradesh', 'Uttarakhand', 'West Bengal', 'Delhi'
        ],
        'crops': ['Rice', 'Wheat', 'Maize', 'Cotton', 'Sugarcane', 'Coffee', 'Jute', 'Millets', 'Tomato', 'Onion', 'Potato'],
        'factors': ['Flood', 'Drought', 'Pest/Animals', 'Hailstorm', 'Fire']
    }
    joblib.dump(meta, os.path.join(MODEL_DIR, 'farming/compensation_meta.pkl'))
    print("Compensation Model and Meta trained and saved.")

def train_price_prediction_model():
    print("Training Price Prediction Model...")
    np.random.seed(42)
    n_samples = 500
    
    # Features: month (1-12), state_encoded, crop_encoded, past_price
    months = np.random.randint(1, 13, n_samples)
    states = np.random.randint(0, 29, n_samples)
    crops = np.random.randint(0, 11, n_samples)
    past_price = np.random.uniform(20, 200, n_samples)
    
    # Target: next month price
    # Simple trend: adds a seasonal modifier + inflation + noise
    seasonal_mod = np.sin(months * (2 * np.pi / 12)) * 10
    y = past_price * 1.02 + seasonal_mod + np.random.normal(0, 5, n_samples)
    y = np.maximum(y, 10) # Minimum 10 rs
    
    X = pd.DataFrame({'month': months, 'state': states, 'crop': crops, 'past_price': past_price})
    
    from sklearn.ensemble import RandomForestRegressor
    model = RandomForestRegressor(n_estimators=50, random_state=42)
    model.fit(X, y)
    
    joblib.dump(model, os.path.join(MODEL_DIR, 'farming/price_prediction_model.pkl'))
    print("Price Prediction Model trained and saved.")

def train_eligibility_model():
    print("Training Government Crop Loss Benefit Eligibility Model...")
    np.random.seed(42)
    n_samples = 1500
    
    states = np.random.randint(0, 29, n_samples)
    crops = np.random.randint(0, 11, n_samples)
    factors = np.random.randint(0, 6, n_samples) # 0=Flood, 1=Drought, 2=Pest/Animals, 3=Hailstorm, 4=Unseasonal Rain, 5=Fire
    acreage = np.random.uniform(0.5, 25.0, n_samples)
    loss_pct = np.random.uniform(10.0, 100.0, n_samples)
    intimation_hrs = np.random.choice([12, 24, 36, 48, 60, 72, 96, 120], n_samples)
    insured = np.random.choice([0, 1], n_samples, p=[0.35, 0.65])
    farmer_cat = np.random.choice([0, 1, 2, 3], n_samples)
    
    # Eligibility Rule Logic for Synthetic Dataset
    # 1. Minimum 33% crop loss required by Govt (PMFBY / SDRF norms)
    # 2. Intimation within 72 hours window
    # 3. Insured under PMFBY OR Severe Calamity (loss >= 50%) for SDRF state relief
    eligible_mask = (loss_pct >= 33.0) & (intimation_hrs <= 72) & ((insured == 1) | ((loss_pct >= 50.0) & np.isin(factors, [0, 1, 3, 4])))
    
    y = np.where(eligible_mask, 1, 0)
    
    # Inject 3% random noise for realistic ML variance
    noise_indices = np.random.choice(n_samples, size=int(n_samples * 0.03), replace=False)
    y[noise_indices] = 1 - y[noise_indices]
    
    X = pd.DataFrame({
        'state': states,
        'crop': crops,
        'factor': factors,
        'acreage': acreage,
        'loss_percentage': loss_pct,
        'intimation_hours': intimation_hrs,
        'insured_status': insured,
        'farmer_category': farmer_cat
    })
    
    model = RandomForestClassifier(n_estimators=100, random_state=42)
    model.fit(X, y)
    
    joblib.dump(model, os.path.join(MODEL_DIR, 'farming/eligibility_model.pkl'))
    
    # Save metadata lists for matching index encodings
    meta = {
        'states': [
            'Andhra Pradesh', 'Arunachal Pradesh', 'Assam', 'Bihar', 'Chhattisgarh',
            'Goa', 'Gujarat', 'Haryana', 'Himachal Pradesh', 'Jharkhand',
            'Karnataka', 'Kerala', 'Madhya Pradesh', 'Maharashtra', 'Manipur',
            'Meghalaya', 'Mizoram', 'Nagaland', 'Odisha', 'Punjab',
            'Rajasthan', 'Sikkim', 'Tamil Nadu', 'Telangana', 'Tripura',
            'Uttar Pradesh', 'Uttarakhand', 'West Bengal', 'Delhi'
        ],
        'crops': ['Rice', 'Wheat', 'Maize', 'Cotton', 'Sugarcane', 'Coffee', 'Jute', 'Millets', 'Tomato', 'Onion', 'Potato'],
        'factors': ['Flood', 'Drought', 'Pest/Animals', 'Hailstorm', 'Unseasonal Rain', 'Fire'],
        'insured_options': ['No', 'Yes'],
        'farmer_categories': ['Small & Marginal (< 5 Acres)', 'Medium (5 - 10 Acres)', 'Large (> 10 Acres)', 'Tenant / Sharecropper']
    }
    joblib.dump(meta, os.path.join(MODEL_DIR, 'farming/eligibility_meta.pkl'))
    print("Eligibility Model trained and saved successfully.")

if __name__ == "__main__":
    train_crop_model()
    train_fertilizer_model()
    train_weather_model()
    train_disease_cnn_model()
    train_compensation_model()
    train_price_prediction_model()
    train_eligibility_model()

