/**
 * SmartFarmAI Multi-Language Translation Engine (i18n)
 * Supports: English (en), Kannada (kn), Hindi (hi), Marathi (mr), Telugu (te), Tamil (ta)
 */

const translations = {
    en: {
        nav_home: "Home",
        nav_crops: "Crops",
        nav_disease: "Disease",
        nav_fertilizer: "Fertilizer",
        nav_weather: "Weather",
        nav_market: "Market",
        nav_compensation: "Compensation",
        nav_govt_benefits: "Govt Benefits",
        nav_help: "Help",
        
        hero_welcome: "Welcome to",
        hero_title: "Smart Farming",
        hero_subtitle: "Using ML",
        hero_desc: "Harness the power of Machine Learning for smarter agriculture. Get crop recommendations, detect plant diseases, receive personalized fertilizer guidance, and check government benefit eligibility.",
        btn_get_started: "Get Started",
        btn_learn_more: "Learn More",
        our_features: "Our Features",
        features_subtitle: "Powered by advanced machine learning models for accurate predictions",
        try_now: "Try Now →",
        
        feature_crop_title: "Crop Recommendation",
        feature_crop_desc: "Enter environmental parameters like temperature, humidity, and soil moisture to get AI-powered crop suggestions.",
        feature_disease_title: "Disease Detection",
        feature_disease_desc: "Upload a leaf image and our ML model will analyze it to detect potential diseases with treatment recommendations.",
        feature_fert_title: "Fertilizer Guide",
        feature_fert_desc: "Input your soil nutrient levels (N, P, K) and pH to receive customized fertilizer recommendations.",
        feature_govt_title: "Govt Crop Loss Benefits",
        feature_govt_desc: "Check if you meet government scheme criteria (PMFBY / SDRF) for crop damage and get a complete document checklist.",
        
        temp_label: "Temperature (°C)",
        humidity_label: "Humidity (%)",
        moisture_label: "Soil Moisture (%)",
        ph_label: "Soil pH",
        crop_label: "Crop Name",
        state_label: "Region / State",
        price_label: "Current Price (₹/kg)",
        factor_label: "Cause of Crop Damage",
        acreage_label: "Cultivated Land Size (in Acres)",
        loss_pct_label: "Estimated Crop Loss Percentage (%)",
        intimation_label: "Intimation / Damage Reporting Time",
        insured_label: "Crop Insurance Enrollment (PMFBY / State)",
        category_label: "Farmer Landholding Category",
        
        btn_recommend: "Recommend Crop →",
        btn_detect: "Detect Disease →",
        btn_calc_fert: "Calculate Fertilizer →",
        btn_predict_weather: "Predict Weather →",
        btn_analyze_market: "Analyze Market Prices →",
        btn_calc_comp: "Calculate Compensation →",
        btn_eval_eligibility: "Evaluate Benefit Eligibility →",
        btn_try_again: "Try Again",
        btn_check_another: "🔄 Check Another Claim",
        btn_print_checklist: "🖨️ Print Checklist",
        
        footer_rights: "SmartFarmAI — ML-Powered Agriculture for Farmers",
        
        // Result Headers
        res_crop_title: "Recommended Crop",
        res_disease_title: "Disease Analysis Result",
        res_fert_title: "Fertilizer Recommendation",
        res_weather_title: "Weather Forecast",
        res_market_title: "Market Price Forecast",
        res_comp_title: "Compensation Estimate",
        res_eligibility_title: "Government Crop Loss Benefit Eligibility",
        
        // Document Checklist
        doc_checklist_title: "📋 Required Submission Documents Checklist",
        doc_checklist_desc: "Please collect and submit the following mandatory documents to the government agriculture authority:",
        office_location_title: "🏢 Office Submission Location:",
    },
    kn: {
        nav_home: "ಮುಖಪುಟ",
        nav_crops: "ಬೆಳೆಗಳು",
        nav_disease: "ರೋಗ ಪತ್ತೆ",
        nav_fertilizer: "ಸೊಪ್ಪು/ಗೊಬ್ಬರ",
        nav_weather: "ಹವಾಮಾನ",
        nav_market: "ಮಾರುಕಟ್ಟೆ",
        nav_compensation: "ಪರಿಹಾರ",
        nav_govt_benefits: "ಸರ್ಕಾರಿ ಸೌಲಭ್ಯಗಳು",
        nav_help: "ಸಹಾಯ",
        
        hero_welcome: "ಸ್ವಾಗತ",
        hero_title: "ಸ್ಮಾರ್ಟ್ ಕೃಷಿ",
        hero_subtitle: "ML ತಂತ್ರಜ್ಞಾನದೊಂದಿಗೆ",
        hero_desc: "ಉತ್ತಮ ಕೃಷಿಗಾಗಿ ಕೃತಕ ಬುದ್ಧಿಮತ್ತೆ ಬಳಸಿ. ಬೆಳೆ ಶಿಫಾರಸು, ರೋಗ ಪತ್ತೆ, ಗೊಬ್ಬರ ಮಾರ್ಗದರ್ಶನ ಮತ್ತು ಸರ್ಕಾರಿ ಪರಿಹಾರ ಅರ್ಹತೆಯನ್ನು ಪರಿಶೀಲಿಸಿ.",
        btn_get_started: "ಪ್ರಾರಂಭಿಸಿ",
        btn_learn_more: "ಇನ್ನಷ್ಟು ತಿಳಿಯಿರಿ",
        our_features: "ನಮ್ಮ ಕೃಷಿ ಸೇವೆಗಳು",
        features_subtitle: "ನಿಖರವಾದ ಮುನ್ಸೂಚನೆಗಳಿಗಾಗಿ ಸುಧಾರಿತ ಯಂತ್ರ ಕಲಿಕೆ (ML) ಮಾದರಿಗಳು",
        try_now: "ಈಗ ಪ್ರಯತ್ನಿಸಿ →",
        
        feature_crop_title: "ಬೆಳೆ ಶಿಫಾರಸು",
        feature_crop_desc: "ತಾಪಮಾನ, ತೇವಾಂಶ ಮತ್ತು ಮಣ್ಣಿನ ತೇವಾಂಶ ನಮೂದಿಸಿ ಸೂಕ್ತ ಬೆಳೆ ಸಲಹೆ ಪಡೆಯಿರಿ.",
        feature_disease_title: "ಸಸ್ಯ ರೋಗ ಪತ್ತೆ",
        feature_disease_desc: "ಎಲೆಯ ಫೋಟೋ ಅಪ್‌ಲೋಡ್ ಮಾಡಿ ರೋಗ ಮತ್ತು ಚಿಕಿತ್ಸೆಯ ಸಲಹೆ ಪಡೆಯಿರಿ.",
        feature_fert_title: "ಗೊಬ್ಬರ ಮಾರ್ಗದರ್ಶಿ",
        feature_fert_desc: "ಮಣ್ಣಿನ ಪೋಷಕಾಂಶ ಮಟ್ಟ ನಮೂದಿಸಿ ನಿಖರ ಗೊಬ್ಬರ ಪ್ರಮಾಣ ಪಡೆಯಿರಿ.",
        feature_govt_title: "ಸರ್ಕಾರಿ ಬೆಳೆ ನಷ್ಟ ಪರಿಹಾರ",
        feature_govt_desc: "PMFBY / SDRF ಯೋಜನೆಗಳ ಅಡಿಯಲ್ಲಿ ಬೆಳೆ ನಷ್ಟ ಪರಿಹಾರ ಅರ್ಹತೆ ಮತ್ತು ದಾಖಲೆಗಳ ಪಟ್ಟಿ ಪಡೆಯಿರಿ.",
        
        temp_label: "ತಾಪಮಾನ (°C)",
        humidity_label: "ಆರ್ದ್ರತೆ (%)",
        moisture_label: "ಮಣ್ಣಿನ ತೇವಾಂಶ (%)",
        ph_label: "ಮಣ್ಣಿನ  pH ಮೌಲ್ಯ",
        crop_label: "ಬೆಳೆಯ ಹೆಸರು",
        state_label: "ರಾಜ್ಯ / ಪ್ರದೇಶ",
        price_label: "ಪ್ರಸ್ತುತ ಬೆಲೆ (₹/ಕೆಜಿ)",
        factor_label: "ಬೆಳೆ ನಷ್ಟದ ಕಾರಣ",
        acreage_label: "ಜಮೀನಿನ ವಿಸ್ತೀರ್ಣ (ಎಕರೆಗಳಲ್ಲಿ)",
        loss_pct_label: "ಅಂದಾಜು ಬೆಳೆ ನಷ್ಟ ಶೇಕಡಾವಾರು (%)",
        intimation_label: "ನಷ್ಟ ವರದಿ ಮಾಡಿದ ಸಮಯ",
        insured_label: "ಬೆಳೆ ವಿಮೆ ನೋಂದಣಿ (PMFBY / ಸರ್ಕಾರಿ)",
        category_label: "ರೈತರ ಭೂಮಿ ವರ್ಗ",
        
        btn_recommend: "ಬೆಳೆ ಶಿಫಾರಸು ಪಡೆಯಿರಿ →",
        btn_detect: "ರೋಗ ಪತ್ತೆ ಮಾಡಿ →",
        btn_calc_fert: "ಗೊಬ್ಬರ ಪ್ರಮಾಣ ಲೆಕ್ಕ ಹಾಕಿ →",
        btn_predict_weather: "ಹವಾಮಾನ ಊಹಿಸಿ →",
        btn_analyze_market: "ಮಾರುಕಟ್ಟೆ ಬೆಲೆ ವಿಶ್ಲೇಷಿಸಿ →",
        btn_calc_comp: "ಪರಿಹಾರ ಲೆಕ್ಕ ಹಾಕಿ →",
        btn_eval_eligibility: "ಸರ್ಕಾರಿ ಅರ್ಹತೆ ಪರಿಶೀಲಿಸಿ →",
        btn_try_again: "ಮತ್ತೆ ಪ್ರಯತ್ನಿಸಿ",
        btn_check_another: "🔄 ಇನ್ನೊಂದು ಅರ್ಜಿ ಪರಿಶೀಲಿಸಿ",
        btn_print_checklist: "🖨️ ಪಟ್ಟಿಯನ್ನು ಪ್ರಿಂಟ್ ಮಾಡಿ",
        
        footer_rights: "ಸ್ಮಾರ್ಟ್‌ಫಾರ್ಮ್ AI — ರೈತರಿಗಾಗಿ ML ಆಧಾರಿತ ಕೃಷಿ ತಂತ್ರಜ್ಞಾನ",
        
        res_crop_title: "ಶಿಫಾರಸು ಮಾಡಿದ ಬೆಳೆ",
        res_disease_title: "ರೋಗ ಪತ್ತೆ ಫಲಿತಾಂಶ",
        res_fert_title: "ಗೊಬ್ಬರ ಶಿಫಾರಸು",
        res_weather_title: "ಹವಾಮಾನ ಮುನ್ಸೂಚನೆ",
        res_market_title: "ಮಾರುಕಟ್ಟೆ ಬೆಲೆ ಮುನ್ಸೂಚನೆ",
        res_comp_title: "ಅಂದಾಜು ಪರಿಹಾರ",
        res_eligibility_title: "ಸರ್ಕಾರಿ ಬೆಳೆ ನಷ್ಟ ಪರಿಹಾರ ಅರ್ಹತೆ",
        
        doc_checklist_title: "📋 ಸಲ್ಲಿಸಬೇಕಾದ ಕಡ್ಡಾಯ ದಾಖಲೆಗಳ ಪಟ್ಟಿ",
        doc_checklist_desc: "ದಯವಿಟ್ಟು ಕೆಳಗಿನ ದಾಖಲೆಗಳನ್ನು ಸಂಗ್ರಹಿಸಿ ಸರ್ಕಾರಿ ಕೃಷಿ ಅಧಿಕಾರಿಗೆ ಸಲ್ಲಿಸಿ:",
        office_location_title: "🏢 ಅರ್ಜಿ ಸಲ್ಲಿಸಬೇಕಾದ ಕಚೇರಿ:",
    },
    hi: {
        nav_home: "मुख्य पृष्ठ",
        nav_crops: "फसलें",
        nav_disease: "रोग पहचान",
        nav_fertilizer: "उर्वरक सलाह",
        nav_weather: "मौसम",
        nav_market: "मंडी भाव",
        nav_compensation: "मुआवजा",
        nav_govt_benefits: "सरकारी लाभ",
        nav_help: "सहायता",
        
        hero_welcome: "स्वागत है",
        hero_title: "स्मार्ट खेती",
        hero_subtitle: "ML तकनीक के साथ",
        hero_desc: "स्मार्ट कृषि के लिए मशीन लर्निंग की शक्ति का उपयोग करें। फसल सिफारिशें, पौधे की बीमारियों का पता लगाना, उर्वरक मार्गदर्शन और सरकारी मुआवजा पात्रता जांचें।",
        btn_get_started: "शुरू करें",
        btn_learn_more: "अधिक जानें",
        our_features: "हमारी सेवाएं",
        features_subtitle: "सटीक अनुमान के लिए उन्नत मशीन लर्निंग मॉडल",
        try_now: "अभी प्रयास करें →",
        
        feature_crop_title: "फसल सिफारिश",
        feature_crop_desc: "तापमान, आर्द्रता और मिट्टी की नमी दर्ज करके उपयुक्त फसल की सिफारिश प्राप्त करें।",
        feature_disease_title: "फसल रोग पहचान",
        feature_disease_desc: "पत्ती की तस्वीर अपलोड करें और उपचार सिफारिशों के साथ बीमारी का पता लगाएं।",
        feature_fert_title: "उर्वरक गाइड",
        feature_fert_desc: "मिट्टी के पोषक तत्वों (N, P, K) और pH स्तर दर्ज करके सही खाद की मात्रा जानें।",
        feature_govt_title: "सरकारी फसल नुकसान लाभ",
        feature_govt_desc: "PMFBY / SDRF योजनाओं के तहत फसल नुकसान मुआवजे की पात्रता और आवश्यक दस्तावेजों की सूची देखें।",
        
        temp_label: "तापमान (°C)",
        humidity_label: "आर्द्रता (%)",
        moisture_label: "मिट्टी की नमी (%)",
        ph_label: "मिट्टी का pH स्तर",
        crop_label: "फसल का नाम",
        state_label: "राज्य / क्षेत्र",
        price_label: "वर्तमान मूल्य (₹/किग्रा)",
        factor_label: "फसल नुकसान का कारण",
        acreage_label: "कृषि भूमि (एकड़ में)",
        loss_pct_label: "अनुमानित फसल नुकसान प्रतिशत (%)",
        intimation_label: "सूचना / रिपोर्टिंग समय",
        insured_label: "फसल बीमा स्थिति (PMFBY / सरकारी)",
        category_label: "किसान भूमि श्रेणी",
        
        btn_recommend: "फसल सिफारिश प्राप्त करें →",
        btn_detect: "रोग पहचानें →",
        btn_calc_fert: "उर्वरक मात्रा की गणना करें →",
        btn_predict_weather: "मौसम का पूर्वानुमान लगाएं →",
        btn_analyze_market: "मंडी भाव विश्लेषण करें →",
        btn_calc_comp: "मुआवजे की गणना करें →",
        btn_eval_eligibility: "सरकारी पात्रता जांचें →",
        btn_try_again: "पुनः प्रयास करें",
        btn_check_another: "🔄 दूसरा आवेदन जांचें",
        btn_print_checklist: "🖨️ चेकलिस्ट प्रिंट करें",
        
        footer_rights: "स्मार्टफार्म AI — किसानों के लिए ML संचालित कृषि तकनीक",
        
        res_crop_title: "अनुशंसित फसल",
        res_disease_title: "रोग पहचान परिणाम",
        res_fert_title: "उर्वरक सिफारिश",
        res_weather_title: "मौसम का पूर्वानुमान",
        res_market_title: "मंडी भाव पूर्वानुमान",
        res_comp_title: "अनुमानित मुआवजा",
        res_eligibility_title: "सरकारी फसल नुकसान लाभ पात्रता",
        
        doc_checklist_title: "📋 जमा किए जाने वाले आवश्यक दस्तावेजों की सूची",
        doc_checklist_desc: "कृपया निम्नलिखित दस्तावेज एकत्र करके सरकारी कृषि अधिकारी के पास जमा करें:",
        office_location_title: "🏢 आवेदन जमा करने का कार्यालय:",
    },
    mr: {
        nav_home: "मुख्यपृष्ठ",
        nav_crops: "पिके",
        nav_disease: "रोग निदान",
        nav_fertilizer: "खत मार्गदर्शक",
        nav_weather: "हवामान",
        nav_market: "बाजारभाव",
        nav_compensation: "भरपाई",
        nav_govt_benefits: "शासकीय योजना",
        nav_help: "मदत",
        
        hero_welcome: "सुस्वागतम",
        hero_title: "स्मार्ट शेती",
        hero_subtitle: "ML तंत्रज्ञानासह",
        hero_desc: "सुधारित शेतीसाठी कृत्रिम बुद्धिमत्तेचा वापर करा. पीक शिफारस, रोग निदान, खत व्यवस्थापन आणि सरकारी पीक नुकसान भरपाई पात्रता तपासा.",
        btn_get_started: "सुरुवात करा",
        btn_learn_more: "अधिक माहिती",
        our_features: "आमच्या सेवा",
        features_subtitle: "अचूक अंदाजासाठी प्रगत मशीन लर्निंग मॉडेल्स",
        try_now: "आत्ताच वापरा →",
        
        feature_crop_title: "पीक शिफारस",
        feature_crop_desc: "तापमान, आर्द्रता आणि जमिनीतील ओलावा नोंदवून योग्य पिकाची शिफारस मिळवा.",
        feature_disease_title: "पीक रोग निदान",
        feature_disease_desc: "पानाचा फोटो अपलोड करा आणि रोगाचे निदान व उपचाराचा सल्ला मिळवा.",
        feature_fert_title: "खत व्यवस्थापन",
        feature_fert_desc: "जमिनीतील पोषक घटक (N, P, K) आणि pH तपासून खताची अचूक मात्रा मिळवा.",
        feature_govt_title: "शासकीय पीक नुकसान भरपाई",
        feature_govt_desc: "PMFBY / SDRF अंतर्गत पीक नुकसानीच्या सरकारी भरपाईची पात्रता आणि कागदपत्रांची यादी पहा.",
        
        temp_label: "तापमान (°C)",
        humidity_label: "आर्द्रता (%)",
        moisture_label: "मातीतील ओलावा (%)",
        ph_label: "मातीचा pH स्तर",
        crop_label: "पिकाचे नाव",
        state_label: "राज्य / क्षेत्र",
        price_label: "सध्याचा भाव (₹/किलो)",
        factor_label: "पीक नुकसानीचे कारण",
        acreage_label: "शेती क्षेत्र (एकरामध्ये)",
        loss_pct_label: "अंदाजित पीक नुकसान टक्केवारी (%)",
        intimation_label: "नुकसान नोंदवण्याचा वेळ",
        insured_label: "पीक विमा नोंदणी (PMFBY / सरकारी)",
        category_label: "शेतकरी जमीन वर्ग",
        
        btn_recommend: "पीक शिफारस मिळवा →",
        btn_detect: "रोग निदान करा →",
        btn_calc_fert: "खताची मात्रा मोजा →",
        btn_predict_weather: "हवामान अंदाज →",
        btn_analyze_market: "बाजारभाव विश्लेषण →",
        btn_calc_comp: "भरपाई मोजा →",
        btn_eval_eligibility: "सरकारी पात्रता तपासा →",
        btn_try_again: "पुन्हा प्रयत्न करा",
        btn_check_another: "🔄 दुसरा अर्ज तपासा",
        btn_print_checklist: "🖨️ यादी प्रिंट करा",
        
        footer_rights: "स्मार्टफार्म AI — शेतकऱ्यांसाठी ML आधारित कृषी तंत्रज्ञान",
        
        res_crop_title: "शिफारस केलेले पीक",
        res_disease_title: "रोग निदान निकाल",
        res_fert_title: "खत शिफारस",
        res_weather_title: "हवामान अंदाज",
        res_market_title: "बाजारभाव अंदाज",
        res_comp_title: "अंदाजित भरपाई",
        res_eligibility_title: "शासकीय पीक नुकसान भरपाई पात्रता",
        
        doc_checklist_title: "📋 सादर करावयाच्या आवश्यक कागदपत्रांची यादी",
        doc_checklist_desc: "कृपया खालील कागदपत्रे जमा करून कृषी अधिकाऱ्याकडे सादर करा:",
        office_location_title: "🏢 अर्ज सादर करण्याचे कार्यालय:",
    },
    te: {
        nav_home: "ముఖపేజీ",
        nav_crops: "పంటలు",
        nav_disease: "వ్యాధి గుర్తింపు",
        nav_fertilizer: "ఎరువుల సలహా",
        nav_weather: "వాతావరణం",
        nav_market: "మార్కెట్ ధరలు",
        nav_compensation: "నష్టపరిహారం",
        nav_govt_benefits: "ప్రభుత్వ పథకాలు",
        nav_help: "సహాయం",
        
        hero_welcome: "స్వాగతం",
        hero_title: "స్మార్ట్ వ్యవసాయం",
        hero_subtitle: "ML సాంకేతికతతో",
        hero_desc: "మెరుగైన వ్యవసాయం కోసం ఆర్టిఫిషియల్ ఇంటెలిజెన్స్ ఉపయోగించండి. పంట సిఫార్సులు, తెగుళ్ళ గుర్తింపు, ఎరువుల మార్గదర్శకత్వం మరియు ప్రభుత్వ సహాయ అర్హతను తనిఖీ చేయండి.",
        btn_get_started: "ప్రారంభించండి",
        btn_learn_more: "మరింత తెలుసుకోండి",
        our_features: "మా సేవలు",
        features_subtitle: "ఖచ్చితమైన అంచనాల కోసం అధునాతన మెషిన్ లెర్నింగ్ నమూనాలు",
        try_now: "ఇప్పుడే ప్రయత్నించండి →",
        
        feature_crop_title: "పంట సిఫార్సు",
        feature_crop_desc: "ఉష్ణోగ్రత, తేమ మరియు నేల తేమ నమోదు చేసి తగిన పంట సూచనలను పొందండి.",
        feature_disease_title: "పంట తెగుళ్ళ గుర్తింపు",
        feature_disease_desc: "ఆకు ఫోటో అప్‌లోడ్ చేసి తెగులు మరియు నివారణ చర్యలు తెలుసుకోండి.",
        feature_fert_title: "ఎరువుల మార్గదర్శి",
        feature_fert_desc: "నేల పోషకాల పరిమాణాన్ని నమోదు చేసి సరైన ఎరువుల మోతాదును పొందండి.",
        feature_govt_title: "ప్రభుత్వ పంట నష్ట సహాయం",
        feature_govt_desc: "PMFBY / SDRF పథకాల కింద పంట నష్టపరిహార అర్హత మరియు అవసరమైన పత్రాల జాబితాను పొందండి.",
        
        temp_label: "ఉష్ణోగ్రత (°C)",
        humidity_label: "తేమ (%)",
        moisture_label: "నేల తేమ (%)",
        ph_label: "నేల pH స్థాయి",
        crop_label: "పంట పేరు",
        state_label: "రాష్ట్రం / ప్రాంతం",
        price_label: "ప్రస్తుత ధర (₹/కిలో)",
        factor_label: "పంట నష్టానికి కారణం",
        acreage_label: "సాగు భూమి వైశాల్యం (ఎకరాల్లో)",
        loss_pct_label: "అంచనా పంట నష్టం శాతం (%)",
        intimation_label: "నష్టం తెలియజేసిన సమయం",
        insured_label: "పంట బీమా నమోదు (PMFBY / ప్రభుత్వం)",
        category_label: "రైతు భూమి వర్గం",
        
        btn_recommend: "పంట సిఫార్సు పొందండి →",
        btn_detect: "తెగులును గుర్తించండి →",
        btn_calc_fert: "ఎరువుల పరిమాణం లెక్కించండి →",
        btn_predict_weather: "వాతావరణ అంచనా →",
        btn_analyze_market: "మార్కెట్ ధరల విశ్లేషణ →",
        btn_calc_comp: "పరిహారం లెక్కించండి →",
        btn_eval_eligibility: "ప్రభుత్వ అర్హత తనిఖీ చేయండి →",
        btn_try_again: "మళ్లీ ప్రయత్నించండి",
        btn_check_another: "🔄 మరొక దరఖాస్తును తనిఖీ చేయండి",
        btn_print_checklist: "🖨️ జాబితాను ప్రింట్ చేయండి",
        
        footer_rights: "స్మార్ట్‌ఫామ్ AI — రైతుల కోసం ML ఆధారిత వ్యవసాయ సాంకేతికత",
        
        res_crop_title: "సిఫార్సు చేసిన పంట",
        res_disease_title: "తెగులు గుర్తింపు ఫలితం",
        res_fert_title: "ఎరువుల సిఫార్సు",
        res_weather_title: "వాతావరణ అంచనా",
        res_market_title: "మార్కెట్ ధరల అంచనా",
        res_comp_title: "అంచనా నష్టపరిహారం",
        res_eligibility_title: "ప్రభుత్వ పంట నష్టపరిహార అర్హత",
        
        doc_checklist_title: "📋 సమర్పించాల్సిన తప్పనిసరి పత్రాల జాబితా",
        doc_checklist_desc: "దయచేసి క్రింది పత్రాలను సేకరించి ప్రభుత్వ వ్యవసాయ అధికారికి సమర్పించండి:",
        office_location_title: "🏢 దరఖాస్తు సమర్పించాల్సిన కార్యాలయం:",
    },
    ta: {
        nav_home: "முகப்பு",
        nav_crops: "பயிர்கள்",
        nav_disease: "நோய் கண்டறிதல்",
        nav_fertilizer: "உர வழிகாட்டி",
        nav_weather: "வானிலை",
        nav_market: "சந்தை விலை",
        nav_compensation: "இழப்பீடு",
        nav_govt_benefits: "அரசு நலத்திட்டங்கள்",
        nav_help: "உதவி",
        
        hero_welcome: "வரவேற்கிறோம்",
        hero_title: "ஸ்மார்ட் விவசாயம்",
        hero_subtitle: "ML தொழில்நுட்பத்துடன்",
        hero_desc: "சிறந்த விவசாயத்திற்கு செயற்கை நுண்ணறிவைப் பயன்படுத்துங்கள். பயிர் பரிந்துரை, நோய் கண்டறிதல், உர வழிகாட்டல் மற்றும் அரசு இழப்பீட்டுத் தகுதியைச் சரிபார்க்கவும்.",
        btn_get_started: "தொடங்கவும்",
        btn_learn_more: "மேலும் அறிய",
        our_features: "எங்கள் சேவைகள்",
        features_subtitle: "துல்லியமான கணிப்புகளுக்கு மேம்பட்ட மெஷின் லேர்னிங் மாதிரிகள்",
        try_now: "இப்போது முயலுங்கள் →",
        
        feature_crop_title: "பயிர் பரிந்துரை",
        feature_crop_desc: "வெப்பநிலை, ஈரப்பதம் மற்றும் மண் ஈரப்பதத்தை உள்ளிட்டு பொருத்தமான பயிர் ஆலோசனையைப் பெறுங்கள்.",
        feature_disease_title: "பயிர் நோய் கண்டறிதல்",
        feature_disease_desc: "இலையின் புகைப்படத்தைப் பதிவேற்றி நோயைக் கண்டறிந்து சிகிச்சை ஆலோசனையைப் பெறுங்கள்.",
        feature_fert_title: "உர வழிகாட்டி",
        feature_fert_desc: "மண் ஊட்டச்சத்து அளவை (N, P, K) உள்ளிட்டு துல்லியமான உர அளவைக் கண்டறியவும்.",
        feature_govt_title: "அரசு பயிர் சேத இழப்பீடு",
        feature_govt_desc: "PMFBY / SDRF திட்டங்களின் கீழ் பயிர் சேத இழப்பீட்டுத் தகுதி மற்றும் ஆவணங்களின் பட்டியலைப் பெறுங்கள்.",
        
        temp_label: "வெப்பநிலை (°C)",
        humidity_label: "ஈரப்பதம் (%)",
        moisture_label: "மண் ஈரப்பதம் (%)",
        ph_label: "மண் pH அளவு",
        crop_label: "பயிரின் பெயர்",
        state_label: "மாநிலம் / பகுதி",
        price_label: "தற்போதைய விலை (₹/கிலோ)",
        factor_label: "பயிர் சேதத்திற்கான காரணம்",
        acreage_label: "நிலப்பரப்பு (ஏக்கரில்)",
        loss_pct_label: "மதிப்பிடப்பட்ட பயிர் சேத சதவீதம் (%)",
        intimation_label: "சேதம் அறிவிக்கப்பட்ட நேரம்",
        insured_label: "பயிர் காப்பீடு பதிவு (PMFBY / அரசு)",
        category_label: "விவசாயி நிலப்பிரிவு",
        
        btn_recommend: "பயிர் பரிந்துரை பெறுக →",
        btn_detect: "நோயைக் கண்டறிக →",
        btn_calc_fert: "உர அளவைக் கணக்கிடுக →",
        btn_predict_weather: "வானிலை கணிப்பு →",
        btn_analyze_market: "சந்தை விலை பகுப்பாய்வு →",
        btn_calc_comp: "இழப்பீட்டைக் கணக்கிடுக →",
        btn_eval_eligibility: "அரசுத் தகுதியைச் சரிபார்க்கவும் →",
        btn_try_again: "மீண்டும் முயற்சிக்கவும்",
        btn_check_another: "🔄 மற்றொரு மனுவைச் சரிபார்க்கவும்",
        btn_print_checklist: "🖨️ பட்டியலை அச்சிடுக",
        
        footer_rights: "ஸ்மார்ட்ஃபார்ம் AI — விவசாயிகளுக்கான ML அடிப்படையிலான விவசாயத் தொழில்நுட்பம்",
        
        res_crop_title: "பரிந்துரைக்கப்பட்ட பயிர்",
        res_disease_title: "நோய் கண்டறிதல் முடிவு",
        res_fert_title: "உரப் பரிந்துரை",
        res_weather_title: "வானிலை முன்னறிவிப்பு",
        res_market_title: "சந்தை விலை முன்னறிவிப்பு",
        res_comp_title: "மதிப்பிடப்பட்ட இழப்பீடு",
        res_eligibility_title: "அரசு பயிர் சேத இழப்பீட்டுத் தகுதி",
        
        doc_checklist_title: "📋 சமர்ப்பிக்க வேண்டிய கட்டாய ஆவணங்களின் பட்டியல்",
        doc_checklist_desc: "தயவுசெய்து பின்வரும் ஆவணங்களைச் சேகரித்து அரசு வேளாண்மை அலுவலரிடம் சமர்ப்பிக்கவும்:",
        office_location_title: "🏢 மனு சமர்ப்பிக்க வேண்டிய அலுவலகம்:",
    }
};

/**
 * Switch language and apply DOM translations
 */
function setLanguage(lang) {
    if (!translations[lang]) lang = 'en';
    localStorage.setItem('smartfarm_lang', lang);
    
    // 1. Update selector UI & enforce clean localized labels
    const selector = document.getElementById('language-select');
    if (selector) {
        const langOptions = [
            { value: 'en', text: 'English' },
            { value: 'kn', text: 'ಕನ್ನಡ (Kannada)' },
            { value: 'hi', text: 'हिंदी (Hindi)' },
            { value: 'mr', text: 'मराठी (Marathi)' },
            { value: 'te', text: 'తెలుగు (Telugu)' },
            { value: 'ta', text: 'தமிழ் (Tamil)' }
        ];
        if (selector.options && selector.options.length === langOptions.length) {
            langOptions.forEach((opt, idx) => {
                if (selector.options[idx]) {
                    selector.options[idx].value = opt.value;
                    selector.options[idx].textContent = opt.text;
                    selector.options[idx].classList.add('notranslate');
                    selector.options[idx].setAttribute('translate', 'no');
                }
            });
        }
        selector.value = lang;
    }
    
    // 2. Apply static data-i18n attributes
    const elements = document.querySelectorAll('[data-i18n]');
    elements.forEach(el => {
        const key = el.getAttribute('data-i18n');
        if (translations[lang] && translations[lang][key]) {
            if (el.tagName === 'INPUT' || el.tagName === 'TEXTAREA') {
                el.placeholder = translations[lang][key];
            } else {
                el.innerText = translations[lang][key];
            }
        }
    });

    // 3. Dynamically translate all form field labels
    const labels = document.querySelectorAll('.form-group label, label.form-label');
    labels.forEach(label => {
        const forId = (label.getAttribute('for') || '').toLowerCase();
        const text = label.textContent.toLowerCase();
        let fieldKey = '';

        if (forId.includes('temp') || text.includes('temperature')) fieldKey = 'temp_label';
        else if (forId.includes('hum') || text.includes('humidity')) fieldKey = 'humidity_label';
        else if (forId.includes('moist') || text.includes('moisture')) fieldKey = 'moisture_label';
        else if (forId.includes('ph') || text.includes('ph')) fieldKey = 'ph_label';
        else if (forId.includes('crop') || text.includes('crop')) fieldKey = 'crop_label';
        else if (forId.includes('state') || text.includes('state') || text.includes('region')) fieldKey = 'state_label';
        else if (forId.includes('price') || text.includes('price')) fieldKey = 'price_label';
        else if (forId.includes('factor') || text.includes('damage') || text.includes('cause')) fieldKey = 'factor_label';
        else if (forId.includes('acreage') || text.includes('acre') || text.includes('land')) fieldKey = 'acreage_label';
        else if (forId.includes('loss') || text.includes('loss percentage')) fieldKey = 'loss_pct_label';
        else if (forId.includes('intimation') || text.includes('reporting time')) fieldKey = 'intimation_label';
        else if (forId.includes('insured') || text.includes('insurance')) fieldKey = 'insured_label';
        else if (forId.includes('category') || text.includes('category')) fieldKey = 'category_label';

        if (fieldKey && translations[lang] && translations[lang][fieldKey]) {
            label.innerText = translations[lang][fieldKey];
        }
    });

    // 4. Update HTML lang attribute
    document.documentElement.lang = lang;
    
    // 5. Update Google Translate bridge cookie silently
    if (lang !== 'en') {
        setGoogleTranslateCookie(lang);
    } else {
        clearGoogleTranslateCookie();
    }
}

/**
 * Google Translate Cookie Bridge
 */
function setGoogleTranslateCookie(lang) {
    document.cookie = `googtrans=/en/${lang}; path=/; domain=${window.location.hostname}`;
    document.cookie = `googtrans=/en/${lang}; path=/;`;
}

function clearGoogleTranslateCookie() {
    document.cookie = `googtrans=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/; domain=${window.location.hostname}`;
    document.cookie = `googtrans=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;`;
}

/**
 * Initialize language on DOM ready
 */
document.addEventListener('DOMContentLoaded', () => {
    const savedLang = localStorage.getItem('smartfarm_lang') || 'en';
    setLanguage(savedLang);
    
    const selector = document.getElementById('language-select');
    if (selector) {
        selector.addEventListener('change', (e) => {
            setLanguage(e.target.value);
        });
    }
});

