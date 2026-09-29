# 🌾 SmartFarmAI: Intelligent Farming Solution

SmartFarmAI is a comprehensive Django-based platform that leverages Machine Learning to empower farmers with data-driven insights. It provides crop recommendations, disease detection, fertilizer guides, weather forecasting, and market analysis.

---

## 📂 Project Structure & Main Folders

### 1. `myProject/` (The Configuration Folder)
This is the **Project Management Folder**. It serves as the central brain of the application.
*   `settings.py`: Contains all project configurations, database settings, and registered apps.
*   `urls.py`: The master routing file that connects web addresses to specific views.

### 2. `farming/` (The Application Folder)
This is the **Main Application Folder** where the core logic resides.
*   `models.py`: Defines the database structure.
*   `views.py`: Contains the logic for processing user inputs and ML predictions.
*   `templates/`: Contains the HTML files for the user interface.
*   `*.pkl` files: The pre-trained Machine Learning models used for real-time inference.

### 3. `manage.py` (The Main Execution Entry Point)
This is the **Main File** for the entire project. It is located in the root directory. You use it to:
*   Start the server: `python manage.py runserver`
*   Sync the database: `python manage.py migrate`

---

## 🗄️ Why do we use a Database (`db.sqlite3`)?

We use a database to ensure that the application is **Persistent** and **Data-Driven**:
1.  **History Tracking**: It stores every prediction made (Crops, Diseases, etc.) so you can review past data in the Admin panel.
2.  **Market Analytics**: It stores regional price data to generate trends over time.
3.  **Data Integrity**: Using a database is more professional and secure than storing data in temporary files or variables, which would be lost if the server restarts.

---

## 🧠 Machine Learning Modules
*   **Crop Recommendation**: Random Forest model trained on soil and climate data.
*   **Disease Detection**: CNN (Convolutional Neural Network) trained on 2000+ leaf images (85% accuracy).
*   **Fertilizer Recommendation**: Multi-class classification for nutrient optimization.
*   **Market Prediction**: Time-series analysis for Indian Mandi price forecasting.

---

## 🚀 How to Run
1.  Navigate to the project root.
2.  Activate the virtual environment: `.\venv\Scripts\activate`
3.  Run the server: `python manage.py runserver`
4.  Open `http://127.0.0.1:8000` in your browser.
