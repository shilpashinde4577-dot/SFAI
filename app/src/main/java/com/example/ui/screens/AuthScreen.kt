package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.FarmingViewModel
import com.example.ui.theme.CardBorder
import com.example.ui.theme.ForestGreen40
import com.example.ui.theme.ForestGreenContainer
import com.example.ui.theme.SuccessGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    viewModel: FarmingViewModel,
    initialTab: Int = 0,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(initialTab) } // 0 = Login, 1 = Register

    // Login state
    var loginUsername by remember { mutableStateOf("shilpa_farmer") }
    var loginPassword by remember { mutableStateOf("password123") }
    var loginPasswordVisible by remember { mutableStateOf(false) }
    var loginError by remember { mutableStateOf<String?>(null) }
    var loginLoading by remember { mutableStateOf(false) }

    // Register state
    var regFullName by remember { mutableStateOf("") }
    var regUsername by remember { mutableStateOf("") }
    var regEmail by remember { mutableStateOf("") }
    var regPhone by remember { mutableStateOf("") }
    var regState by remember { mutableStateOf("Maharashtra") }
    var regPassword by remember { mutableStateOf("") }
    var regConfirmPassword by remember { mutableStateOf("") }
    var regPasswordVisible by remember { mutableStateOf(false) }
    var regError by remember { mutableStateOf<String?>(null) }
    var regSuccess by remember { mutableStateOf<String?>(null) }
    var regLoading by remember { mutableStateOf(false) }

    val stateList = listOf("Maharashtra", "Punjab", "Haryana", "Karnataka", "Uttar Pradesh", "Gujarat", "Madhya Pradesh", "Tamil Nadu", "Rajasthan", "Andhra Pradesh")

    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item { Spacer(modifier = Modifier.height(28.dp)) }

            // Brand Header
            item {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(ForestGreenContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🌾", fontSize = 36.sp)
                }
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = "SmartFarm AI",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = ForestGreen40
                )
                Text(
                    text = "Agronomic Intelligence & Risk Advisory",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Tab Selector
            item {
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = {
                            selectedTab = 0
                            loginError = null
                        },
                        text = { Text("Log In", fontWeight = FontWeight.SemiBold) },
                        icon = { Icon(Icons.AutoMirrored.Filled.Login, contentDescription = "Log In") },
                        modifier = Modifier.testTag("tab_login")
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = {
                            selectedTab = 1
                            regError = null
                        },
                        text = { Text("New Farmer", fontWeight = FontWeight.SemiBold) },
                        icon = { Icon(Icons.Default.Person, contentDescription = "Register") },
                        modifier = Modifier.testTag("tab_register")
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            // TAB 0: LOGIN
            if (selectedTab == 0) {
                item {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = "Farmer Sign In",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            if (loginError != null) {
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = loginError ?: "",
                                        color = MaterialTheme.colorScheme.onErrorContainer,
                                        style = MaterialTheme.typography.bodySmall,
                                        modifier = Modifier.padding(12.dp)
                                    )
                                }
                            }

                            OutlinedTextField(
                                value = loginUsername,
                                onValueChange = {
                                    loginUsername = it
                                    loginError = null
                                },
                                label = { Text("Username or Email") },
                                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("login_username_input")
                            )

                            OutlinedTextField(
                                value = loginPassword,
                                onValueChange = {
                                    loginPassword = it
                                    loginError = null
                                },
                                label = { Text("Password") },
                                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                                trailingIcon = {
                                    IconButton(onClick = { loginPasswordVisible = !loginPasswordVisible }) {
                                        Icon(
                                            if (loginPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                            contentDescription = "Toggle password visibility"
                                        )
                                    }
                                },
                                visualTransformation = if (loginPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("login_password_input")
                            )

                            Button(
                                onClick = {
                                    if (loginUsername.isBlank() || loginPassword.isBlank()) {
                                        loginError = "Please enter username and password."
                                        return@Button
                                    }
                                    loginLoading = true
                                    viewModel.login(loginUsername, loginPassword) { success, msg ->
                                        loginLoading = false
                                        if (!success) {
                                            loginError = msg
                                        }
                                    }
                                },
                                enabled = !loginLoading,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .testTag("login_submit_btn")
                            ) {
                                if (loginLoading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                } else {
                                    Icon(Icons.Default.Check, contentDescription = null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Sign In & Continue", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                // Quick Demo Login Cards
                item {
                    Spacer(modifier = Modifier.height(18.dp))
                    Text(
                        text = "Or quick-sign in with demo accounts:",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                loginUsername = "shilpa_farmer"
                                loginPassword = "password123"
                                viewModel.login("shilpa_farmer", "password123") { _, _ -> }
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("demo_user_shilpa")
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("👩‍🌾 Shilpa Shinde", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                Text("Admin (MH)", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }

                        OutlinedButton(
                            onClick = {
                                loginUsername = "rajesh_patil"
                                loginPassword = "password123"
                                viewModel.login("rajesh_patil", "password123") { _, _ -> }
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("demo_user_rajesh")
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("👨‍🌾 Rajesh Patil", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                Text("Farmer (KA)", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    TextButton(
                        onClick = { selectedTab = 1 },
                        modifier = Modifier.testTag("switch_to_register_btn")
                    ) {
                        Text("New to SmartFarm? Register a New Farmer Account →")
                    }
                }
            }

            // TAB 1: REGISTER NEW FARMER
            if (selectedTab == 1) {
                item {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Register New Farmer",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                TextButton(
                                    onClick = {
                                        // Quick autofill for instant testing
                                        val randomId = (100..999).random()
                                        regFullName = "Kisan Rao #$randomId"
                                        regUsername = "farmer_$randomId"
                                        regEmail = "kisan$randomId@farm.org"
                                        regPhone = "+91 98${(10000000..99999999).random()}"
                                        regState = "Maharashtra"
                                        regPassword = "password123"
                                        regConfirmPassword = "password123"
                                    },
                                    modifier = Modifier.testTag("autofill_farmer_btn")
                                ) {
                                    Text("⚡ Auto-fill", fontSize = 12.sp)
                                }
                            }

                            if (regError != null) {
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = regError ?: "",
                                        color = MaterialTheme.colorScheme.onErrorContainer,
                                        style = MaterialTheme.typography.bodySmall,
                                        modifier = Modifier.padding(12.dp)
                                    )
                                }
                            }

                            if (regSuccess != null) {
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = ForestGreenContainer),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = regSuccess ?: "",
                                        color = ForestGreen40,
                                        fontWeight = FontWeight.SemiBold,
                                        style = MaterialTheme.typography.bodySmall,
                                        modifier = Modifier.padding(12.dp)
                                    )
                                }
                            }

                            OutlinedTextField(
                                value = regFullName,
                                onValueChange = { regFullName = it; regError = null },
                                label = { Text("Farmer Full Name *") },
                                placeholder = { Text("e.g. Anand Deshmukh") },
                                leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null) },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("reg_fullname_input")
                            )

                            OutlinedTextField(
                                value = regUsername,
                                onValueChange = { regUsername = it; regError = null },
                                label = { Text("Username Handle *") },
                                placeholder = { Text("e.g. anand_farmer") },
                                leadingIcon = { Icon(Icons.Default.AccountCircle, contentDescription = null) },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("reg_username_input")
                            )

                            OutlinedTextField(
                                value = regEmail,
                                onValueChange = { regEmail = it; regError = null },
                                label = { Text("Email Address *") },
                                placeholder = { Text("e.g. anand@farm.in") },
                                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("reg_email_input")
                            )

                            OutlinedTextField(
                                value = regPhone,
                                onValueChange = { regPhone = it; regError = null },
                                label = { Text("Mobile Phone *") },
                                placeholder = { Text("e.g. +91 98765 43210") },
                                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("reg_phone_input")
                            )

                            // State selection chips
                            Column {
                                Text(
                                    text = "Agricultural State: $regState",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    listOf("Maharashtra", "Punjab", "Karnataka").forEach { st ->
                                        FilterChip(
                                            selected = regState == st,
                                            onClick = { regState = st },
                                            label = { Text(st, fontSize = 11.sp) }
                                        )
                                    }
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    listOf("Uttar Pradesh", "Gujarat", "Haryana").forEach { st ->
                                        FilterChip(
                                            selected = regState == st,
                                            onClick = { regState = st },
                                            label = { Text(st, fontSize = 11.sp) }
                                        )
                                    }
                                }
                            }

                            OutlinedTextField(
                                value = regPassword,
                                onValueChange = { regPassword = it; regError = null },
                                label = { Text("Password *") },
                                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                                trailingIcon = {
                                    IconButton(onClick = { regPasswordVisible = !regPasswordVisible }) {
                                        Icon(
                                            if (regPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                            contentDescription = "Toggle password visibility"
                                        )
                                    }
                                },
                                visualTransformation = if (regPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("reg_password_input")
                            )

                            OutlinedTextField(
                                value = regConfirmPassword,
                                onValueChange = { regConfirmPassword = it; regError = null },
                                label = { Text("Confirm Password *") },
                                leadingIcon = { Icon(Icons.Default.LockReset, contentDescription = null) },
                                visualTransformation = PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("reg_confirm_password_input")
                            )

                            Button(
                                onClick = {
                                    if (regFullName.isBlank() || regUsername.isBlank() || regEmail.isBlank() || regPhone.isBlank() || regPassword.isBlank()) {
                                        regError = "Please fill in all required fields."
                                        return@Button
                                    }
                                    if (regPassword.length < 4) {
                                        regError = "Password must be at least 4 characters."
                                        return@Button
                                    }
                                    if (regPassword != regConfirmPassword) {
                                        regError = "Passwords do not match."
                                        return@Button
                                    }

                                    regLoading = true
                                    viewModel.register(
                                        username = regUsername,
                                        fullName = regFullName,
                                        email = regEmail,
                                        phoneNumber = regPhone,
                                        state = regState,
                                        pass = regPassword
                                    ) { success, msg ->
                                        regLoading = false
                                        if (success) {
                                            regSuccess = msg
                                        } else {
                                            regError = msg
                                        }
                                    }
                                },
                                enabled = !regLoading,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .testTag("reg_submit_btn")
                            ) {
                                if (regLoading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                } else {
                                    Icon(Icons.Default.Check, contentDescription = null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Register & Enter SmartFarm", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    TextButton(
                        onClick = { selectedTab = 0 },
                        modifier = Modifier.testTag("switch_to_login_btn")
                    ) {
                        Text("Already registered? Back to Sign In →")
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(30.dp)) }
        }
    }
}
