# 📖 Comprehensive Project Information: SmartFarmAI

This document provides a detailed technical breakdown of the SmartFarmAI project, covering architecture, technology stack, machine learning components, and library dependencies.

---

## 🏗️ 1. Architecture & Technology Stack

### **Backend (The Logic Layer)**
*   **Framework**: Django (Python Web Framework)
*   **Main Logic**: `farming/views.py` processes all requests and interfaces with ML models.
*   **Database**: SQLite (`db.sqlite3`). Used for persistent storage of prediction history and market data.
*   **API Layer**: Django REST Framework (used for internal AJAX data fetching).

### **Frontend (The User Interface)**
*   **Language**: HTML5, Vanilla JavaScript.
*   **Styling**: Custom CSS3 (located in `farming/static/farming/css/style.css`).
*   **Visualizations**: Chart.js (used in Market Analysis for dynamic price trends).
*   **Dynamic UI**: AJAX/Fetch API (used for real-time compensation rate fetching).

---

## 🧠 2. Machine Learning Modules & Algorithms

| Module | Algorithm / Model Type | Purpose | Dataset Size | Dataset Source |
| :--- | :--- | :--- | :--- | :--- |
| **Crop Recommendation** | Random Forest Classifier | Recommends crops based on Temp, Humidity, and Moisture. | 2200+ samples | Kaggle / UCI Repository |
| **Disease Detection** | CNN (Convolutional Neural Network) | Classifies leaf images into 5 health categories. | 2000+ Leaf Images | PlantVillage Dataset |
| **Fertilizer Guide** | Multi-Class Random Forest | Predicts the general fertilizer type needed. | 500 Samples | Indian Agriculture Data |
| **Fertilizer Logic** | Mathematical Agronomy Engine | Calculates exact Urea/DAP/MOP quantities in kg/acre. | Rule-based | Agronomy Field Guides |
| **Weather Prediction** | Random Forest Classifier | Predicts 4 weather states based on environmental sensors. | 500 Samples | NOAA / Local Sensor Logs |
| **Market Analysis** | Random Forest Regressor | Predicts future price trends (3-month forecast). | 500 Samples | Govt. Mandi Portals (Agmarknet) |
| **Compensation** | Random Forest Regressor | Predicts fair compensation rates based on state/crop/damage. | 1000 Samples | PM Fasal Bima Yojana Data |

---

## 📚 3. Key Python Libraries Used

### **Core Data & ML**
*   **Python Version**: Optimized for **Python 3.12+** (Tested on 3.14).
*   `scikit-learn`: Primary library for Random Forest training and inference.
*   `tensorflow/keras`: Used for CNN-based Disease Detection (Requires Python ≤ 3.12 for full support).
*   `pandas`: Used for data manipulation and feature engineering.
*   `numpy`: Used for numerical operations and array handling.
*   `joblib`: Used for loading pre-trained `.pkl` model files.

### **Web & External Data**
*   `django`: The core web framework.
*   `yfinance`: Used to fetch global commodity price data (converted to Indian Mandi rates).
*   `django-cors-headers`: Handles Cross-Origin Resource Sharing for secure API calls.
*   `pillow`: Used for image processing during disease detection uploads.

---

## 📂 4. Detailed Folder & File Guide

### **Root Directory**
*   `manage.py`: The main script to run the project.
*   `requirements.txt`: List of all necessary Python packages.
*   `ml_model.py`: The script used to train and generate the models.
*   `demo_guide.md`: Quick-start guide for demonstration values.

### **`myProject/` Folder (Configuration)**
*   `settings.py`: Master project settings (DB, Apps, Middleware).
*   `urls.py`: URL patterns for the entire website.

### **`farming/` Folder (Application)**
*   `models.py`: Database schema for storing prediction records.
*   `views.py`: The "Brain" containing ML integration and page logic.
*   `forms.py`: Django forms for user inputs (validation logic).
*   `templates/`: All HTML pages (Crop, Disease, Fertilizer, Market, etc.).
*   `static/`: CSS and JavaScript files.
*   `ml_model.pkl`, `disease_model.h5`, etc.: The actual trained brain of the AI.

---

## 🛠️ 5. Technical Highlights
*   **Indian Mandi Simulation**: Unlike basic projects that use international stocks, this project simulates realistic Indian regional market prices (₹/kg) across different states.
*   **Exact Fertilizer Quantities**: The project calculates actual fertilizer requirements (e.g., Urea in kg per acre) rather than just general suggestions.
*   **Scalable Architecture**: The use of Django's Model-View-Template (MVT) architecture allows for easy addition of more AI features in the future.
