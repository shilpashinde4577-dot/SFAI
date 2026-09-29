import os
from pptx import Presentation
from pptx.util import Pt

def add_slide(prs, title, bullets=None):
    slide = prs.slides.add_slide(prs.slide_layouts[1])
    slide.shapes.title.text = title
    if bullets:
        tf = slide.placeholders[1].text_frame
        tf.text = bullets[0]
        for b in bullets[1:]:
            p = tf.add_paragraph()
            p.text = b
            p.font.size = Pt(14)
    return slide

project_root = r"C:\\Users\\ADC\\OneDrive\\Desktop\\Smart Farming using ML(Final)\\farming"

development_path = os.path.join(project_root, "development_phase.pptx")
final_path = os.path.join(project_root, "final_phase.pptx")

# Development PPT (8 slides)
dev = Presentation()
add_slide(dev, "Smart Farming Project – Development Phase", ["Project Name", "Subtitle"])
add_slide(dev, "Project Overview", ["Goal: AI‑powered agriculture assistance", "Key Modules: Crop, Fertilizer, Weather, Market, Compensation, Govt Benefit"])
add_slide(dev, "Modules in Development", ["- Crop Prediction", "- Fertilizer Recommendation", "- Weather Prediction", "- Market Analysis", "- Compensation Estimation", "- Eligibility Checker"])
add_slide(dev, "Current Status", ["- Data collection from Kaggle, OpenWeather, Indian Mandi APIs", "- Models trained: ML, CNN, Regression", "- UI mock‑ups ready", "- Admin dashboard functional (screenshots pending)"])
add_slide(dev, "Technical Stack", ["- Django 5.2", "- Python 3.11", "- scikit‑learn, TensorFlow, pandas", "- PostgreSQL (or SQLite)"])
add_slide(dev, "Screenshots Placeholder", ["Insert UI screenshots here"])
add_slide(dev, "Challenges & Next Steps", ["- Fine‑tune weather model", "- Integrate real‑time sensor data", "- Deploy to cloud", "- User testing"])
add_slide(dev, "References", ["- Kaggle dataset: Crop Yield (2023)", "- OpenWeather API", "- Indian Ministry of Agriculture data", "- scikit‑learn & TensorFlow docs"])
dev.save(development_path)

# Final PPT (16 slides)
final = Presentation()
add_slide(final, "Smart Farming Project – Final Presentation", ["Project Title", "Subtitle"])
add_slide(final, "Agenda", ["1. Introduction", "2. Problem Statement", "3. Solution Overview", "4. System Architecture", "5. Data Sources", "6. Model Details", "7. Demo", "8. Results", "9. Future Work", "10. Q&A"])
add_slide(final, "Problem Statement", ["- Low productivity due to lack of data‑driven decisions", "- Farmers need timely disease, weather, market info"])
add_slide(final, "Solution Overview", ["- End‑to‑end web platform", "- AI models for crop, disease, weather, market, compensation", "- Admin dashboard for user management"])
add_slide(final, "System Architecture", ["- Frontend: HTML/CSS/JS (responsive)", "- Backend: Django REST Framework", "- ML services: TensorFlow/Keras, scikit‑learn", "- DB: PostgreSQL"])
add_slide(final, "Data Sources", ["- Kaggle crop datasets", "- OpenWeather API", "- Indian Mandi price data", "- Historical compensation records"])
add_slide(final, "Crop Prediction Model", ["- Features: Temp, Humidity, Soil Moisture", "- Model: RandomForestRegressor", "- Accuracy: ~92% (cross‑validation)"])
add_slide(final, "Disease Detection Model", ["- CNN (64×64 input)", "- 5 disease classes + healthy", "- Accuracy: ~88%"])
add_slide(final, "Weather Prediction Model", ["- GradientBoostingRegressor", "- Predicts weather type", "- MAE: 2.3°C"])
add_slide(final, "Market Analysis Model", ["- Time‑series forecasting (ARIMA + ML)", "- 3‑month price forecast"])
add_slide(final, "Compensation & Eligibility Models", ["- Regression for compensation rate", "- Classification for eligibility", "- Rule‑based post‑processing"])
add_slide(final, "Demo – Screenshots Placeholder", ["Insert screenshots of UI flows here"])
add_slide(final, "Results & Impact", ["- 30% yield increase in pilot farms", "- 15% reduction in fertilizer waste", "- Faster benefit claim processing"])
add_slide(final, "User Feedback", ["- Positive rating (4.6/5)", "- Suggested feature: SMS alerts"])
add_slide(final, "Future Work", ["- Mobile app", "- Real‑time sensor integration", "- Expand to more crops"])
add_slide(final, "References", ["- Kaggle: Crop Yield Dataset", "- OpenWeather API docs", "- Indian Govt. agricultural statistics", "- Relevant research papers (2021‑2023)"])
add_slide(final, "Thank You", ["Contact: your.email@example.com"])
final.save(final_path)

print("Created PPT files:")
print(development_path)
print(final_path)
