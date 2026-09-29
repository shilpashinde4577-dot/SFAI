/**
 * SmartFarm AI Assistant - Floating Chatbot Engine
 * Provides interactive system walkthroughs, feature explanations, and quick answers.
 */

document.addEventListener('DOMContentLoaded', () => {
    const toggleBtn = document.getElementById('chatbot-toggle');
    const chatWindow = document.getElementById('chatbot-window');
    const closeBtn = document.getElementById('chatbot-close');
    const sendBtn = document.getElementById('chatbot-send');
    const inputField = document.getElementById('chatbot-input');
    const messageContainer = document.getElementById('chatbot-messages');
    const badge = document.getElementById('chatbot-badge');

    if (!toggleBtn || !chatWindow) return;

    // Toggle Chat Window
    toggleBtn.addEventListener('click', () => {
        const isHidden = chatWindow.style.display === 'none' || !chatWindow.style.display;
        chatWindow.style.display = isHidden ? 'flex' : 'none';
        if (isHidden) {
            if (badge) badge.style.display = 'none';
            if (messageContainer.children.length === 0) {
                sendBotGreeting();
            }
            if (inputField) inputField.focus();
        }
    });

    if (closeBtn) {
        closeBtn.addEventListener('click', () => {
            chatWindow.style.display = 'none';
        });
    }

    // Send on button click or Enter key
    if (sendBtn) {
        sendBtn.addEventListener('click', handleUserMessage);
    }
    if (inputField) {
        inputField.addEventListener('keypress', (e) => {
            if (e.key === 'Enter') {
                handleUserMessage();
            }
        });
    }

    // Quick chip suggestion clicks
    document.addEventListener('click', (e) => {
        if (e.target.classList.contains('chat-chip')) {
            const query = e.target.getAttribute('data-query') || e.target.innerText;
            if (inputField) inputField.value = query;
            handleUserMessage();
        }
    });

    function handleUserMessage() {
        const text = inputField.value.trim();
        if (!text) return;

        appendMessage('user', text);
        inputField.value = '';

        // Show typing indicator
        const typingEl = appendTypingIndicator();

        setTimeout(() => {
            if (typingEl) typingEl.remove();
            const response = generateBotResponse(text);
            appendMessage('bot', response);
        }, 500);
    }

    function appendMessage(sender, htmlContent) {
        const msgDiv = document.createElement('div');
        msgDiv.className = `chat-msg ${sender}-msg`;

        if (sender === 'bot') {
            msgDiv.innerHTML = `
                <div class="bot-avatar">🤖</div>
                <div class="msg-bubble">${htmlContent}</div>
            `;
        } else {
            msgDiv.innerHTML = `
                <div class="msg-bubble">${escapeHtml(htmlContent)}</div>
            `;
        }

        messageContainer.appendChild(msgDiv);
        messageContainer.scrollTop = messageContainer.scrollHeight;
    }

    function appendTypingIndicator() {
        const typingDiv = document.createElement('div');
        typingDiv.className = 'chat-msg bot-msg typing';
        typingDiv.innerHTML = `
            <div class="bot-avatar">🤖</div>
            <div class="msg-bubble"><span class="dot">.</span><span class="dot">.</span><span class="dot">.</span></div>
        `;
        messageContainer.appendChild(typingDiv);
        messageContainer.scrollTop = messageContainer.scrollHeight;
        return typingDiv;
    }

    function sendBotGreeting() {
        const welcomeHTML = `
            👋 <strong>Hello Farmer / Visitor!</strong><br>
            Welcome to <strong>SmartFarmAI</strong>! I am your AI assistant.<br><br>
            Click any button below for a quick guide or ask me a question:
            <div class="chip-container">
                <button class="chat-chip" data-query="System Walkthrough">🚀 System Walkthrough</button>
                <button class="chat-chip" data-query="Crop Recommendation">🌱 Crop Guide</button>
                <button class="chat-chip" data-query="Disease Detection">🔬 Disease Detection</button>
                <button class="chat-chip" data-query="Fertilizer Guide">🧪 Fertilizer Guide</button>
                <button class="chat-chip" data-query="Govt Benefits">🏛️ Govt Crop Benefits</button>
            </div>
        `;
        appendMessage('bot', welcomeHTML);
    }

    function generateBotResponse(input) {
        const q = input.toLowerCase();
        const isAuth = !!window.USER_AUTHENTICATED;

        // 1. System Walkthrough
        if (q.includes('walkthrough') || q.includes('system') || q.includes('how it works') || q.includes('overview') || q.includes('beginner') || q.includes('first time')) {
            return `
                🚀 <strong>SmartFarmAI System Walkthrough:</strong><br><br>
                Here is how our 7 AI modules help farmers:<br><br>
                1. <strong>🌱 Crops:</strong> Recommends the best crop based on temperature, humidity, and soil moisture.<br>
                2. <strong>🔬 Disease:</strong> Upload a leaf photo to diagnose diseases and get organic/fungicide treatments.<br>
                3. <strong>🧪 Fertilizer:</strong> Calculates exact Urea, DAP, and MOP required in kg/acre for your soil.<br>
                4. <strong>🌤️ Weather:</strong> Local sensor-based weather forecasting.<br>
                5. <strong>📊 Market:</strong> Mandi price trend forecasts for the next 3 months.<br>
                6. <strong>🛡️ Compensation:</strong> ML estimation of crop damage payouts.<br>
                7. <strong>🏛️ Govt Benefits:</strong> Checks PMFBY/SDRF scheme eligibility & lists required submission documents.
            `;
        }

        // 2. Crop Recommendation
        if (q.includes('crop') || q.includes('recommend') || q.includes('sow') || q.includes('soil moisture')) {
            if (!isAuth) {
                return `<p>🔐 Please log in or register to use this feature.</p>`;
            }
            return `
                🌱 <strong>Crop Recommendation Module:</strong><br><br>
                Enter your local <strong>Temperature (°C)</strong>, <strong>Humidity (%)</strong>, and <strong>Soil Moisture (%)</strong>.<br>
                Our Random Forest ML model predicts the highest-yielding crop for your soil!<br><br>
                👉 <a href="/crop-recommendation/" class="chat-link">Try Crop Recommendation Now →</a>
            `;
        }

        // 3. Disease Detection
        if (q.includes('disease') || q.includes('leaf') || q.includes('photo') || q.includes('image') || q.includes('spot') || q.includes('blight')) {
            if (!isAuth) {
                return `<p>🔐 Please log in or register to use this feature.</p>`;
            }
            return `
                🔬 <strong>Disease Detection Module:</strong><br><br>
                Simply take a photo of an infected leaf and upload it.<br>
                Our CNN Deep Learning model diagnoses diseases like Leaf Blight, Rust, or Powdery Mildew and gives immediate treatment remedies!<br><br>
                👉 <a href="/disease-detection/" class="chat-link">Upload Leaf Image Now →</a>
            `;
        }

        // 4. Fertilizer Guide
        if (q.includes('fertilizer') || q.includes('urea') || q.includes('dap') || q.includes('mop') || q.includes('nitrogen') || q.includes('npk')) {
            if (!isAuth) {
                return `<p>🔐 Please log in or register to use this feature.</p>`;
            }
            return `
                🧪 <strong>Fertilizer Guide Module:</strong><br><br>
                Input your soil's Nitrogen (N), Phosphorus (P), Potassium (K), and pH levels.<br>
                The system calculates the exact quantity of Urea, DAP, and MOP required per acre!<br><br>
                👉 <a href="/fertilizer-recommendation/" class="chat-link">Calculate Fertilizer Now →</a>
            `;
        }

        // 5. Government Benefits / Documents
        if (q.includes('govt') || q.includes('benefit') || q.includes('pmfby') || q.includes('sdrf') || q.includes('eligib') || q.includes('document') || q.includes('aadhaar') || q.includes('claim')) {
            if (!isAuth) {
                return `<p>🔐 Please log in or register to use this feature.</p>`;
            }
            return `
                🏛️ <strong>Government Crop Loss Benefit Checker:</strong><br><br>
                Checks if your crop damage meets official government norms:<br>
                • Minimum 33% crop loss threshold<br>
                • Reported within 72 hours window<br>
                • PMFBY insurance or SDRF emergency relief<br><br>
                If eligible, it provides a complete printable <strong>Submission Document Checklist</strong> (Aadhaar, 7/12 Land Record, Bank Passbook, Sow Certificate, Geo-photos)!<br><br>
                👉 <a href="/benefit-eligibility/" class="chat-link">Check Govt Eligibility Now →</a>
            `;
        }

        // 6. Market Analysis / Price
        if (q.includes('market') || q.includes('price') || q.includes('mandi') || q.includes('rate') || q.includes('trend')) {
            if (!isAuth) {
                return `<p>🔐 Please log in or register to use this feature.</p>`;
            }
            return `
                📊 <strong>Market Price Forecast Module:</strong><br><br>
                Select your State, Crop, and Current Mandi Price.<br>
                Our ML regressor forecasts price trends for the next 3 months so you know the best time to sell!<br><br>
                👉 <a href="/market-analysis/" class="chat-link">Check Mandi Trends Now →</a>
            `;
        }

        // 7. Compensation
        if (q.includes('compensation') || q.includes('damage') || q.includes('flood') || q.includes('drought') || q.includes('money')) {
            if (!isAuth) {
                return `<p>🔐 Please log in or register to use this feature.</p>`;
            }
            return `
                🛡️ <strong>Crop Loss Compensation Module:</strong><br><br>
                Select your State, Crop, Damage Cause (Flood, Drought, Hailstorm), and Acreage to estimate your financial compensation payout.<br><br>
                👉 <a href="/crop-compensation/" class="chat-link">Calculate Compensation Now →</a>
            `;
        }

        // 8. Greetings
        if (q.includes('hi') || q.includes('hello') || q.includes('hey') || q.includes('namaste') || q.includes('who are you')) {
            return `
                👋 Hello! I am <strong>SmartFarm AI Assistant</strong>.<br>
                How can I assist your farming decisions today? Feel free to ask about crops, fertilizers, disease detection, or government benefits!
            `;
        }

        // Default Fallback
        return `
            🤖 I'm here to help with <strong>SmartFarmAI</strong>!<br><br>
            You can ask me about:<br>
            • <em>"System Walkthrough"</em><br>
            • <em>"Crop Recommendation"</em><br>
            • <em>"Disease Detection"</em><br>
            • <em>"Fertilizer Calculation"</em><br>
            • <em>"Government Scheme Eligibility"</em>
        `;
    }

    function escapeHtml(text) {
        return text
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/"/g, "&quot;")
            .replace(/'/g, "&#039;");
    }
});
