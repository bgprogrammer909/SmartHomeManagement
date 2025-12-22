package com.example.smarthome.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.smarthome.R
import com.example.smarthome.repo.LightRepoImpl
import com.example.smarthome.viewmodel.LightsViewModel

// Main Activity for controlling lights
class LightActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Fullscreen layout

        setContent {
            val vm = LightsViewModel(LightRepoImpl())       // ViewModel
            val state by vm.lights.collectAsState()         // Observe state
            val ctx = LocalContext.current                  // Context for Toasts

            // Background gradient
            val bgGradient = Brush.verticalGradient(
                colors = listOf(Color(0xFF0D1B2A), Color(0xFF0A1320))
            )

            Scaffold(
                modifier = Modifier.fillMaxSize()
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(bgGradient)
                        .padding(paddingValues)
                        .padding(16.dp)
                ) {
                    Column {

                        // Top navigation row
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null,
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Back", color = Color.White)
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Title and subtitle
                        Text(
                            "Lights Control",
                            color = Color.White,
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text("Manage all your smart lights", color = Color.White.copy(alpha = 0.7f))

                        Spacer(modifier = Modifier.height(20.dp))

                        // Top status card (total lights + master switch)
                        TopStatusCard(
                            activeCount = listOf(state.light1On, state.light2On).count { it },
                            masterSwitch = state.light1On && state.light2On,
                            onToggleAll = {
                                if (state.light1On && state.light2On) {
                                    vm.turnOffAll()
                                    Toast.makeText(ctx, "Turned All Off", Toast.LENGTH_SHORT).show()
                                } else {
                                    vm.turnOnAll()
                                    Toast.makeText(ctx, "Turned All On", Toast.LENGTH_SHORT).show()
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(25.dp))

                        // Light 1 control
                        LightControlCard(
                            label = "Light 1",
                            lightStatus = state.light1On,
                            brightness = state.light1Brightness,
                            onSwitchToggle = { vm.toggleLight(1, it) },
                            onBrightnessChange = { vm.changeBrightness(1, it) }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Light 2 control
                        LightControlCard(
                            label = "Light 2",
                            lightStatus = state.light2On,
                            brightness = state.light2Brightness,
                            onSwitchToggle = { vm.toggleLight(2, it) },
                            onBrightnessChange = { vm.changeBrightness(2, it) }
                        )
                    }
                }
            }
        }
    }
}

// -----------------------------
// Top Status Card Composable
// -----------------------------
@Composable
fun TopStatusCard(activeCount: Int, masterSwitch: Boolean, onToggleAll: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(Color(0xFF1D233A)),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Row: Total lights + icon
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text("Total Lights", color = Color.White.copy(alpha = 0.6f))
                    Text(
                        "$activeCount On",
                        color = Color.White,
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x33FBC02D)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_lightbulb_24),
                        contentDescription = null,
                        tint = Color(0xFFFBC02D)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Master switch button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Brush.horizontalGradient(listOf(Color(0xFFFFC107), Color(0xFFFFA000))))
                    .clickable { onToggleAll() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    if (masterSwitch) "Turn All Off" else "Turn All On",
                    color = Color.Black,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

// -----------------------------
// Individual Light Control Card Composable
// -----------------------------
@Composable
fun LightControlCard(
    label: String,
    lightStatus: Boolean,
    brightness: Float,
    onSwitchToggle: (Boolean) -> Unit,
    onBrightnessChange: (Float) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(Color(0xFF1A2036)),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {

            // Row: Light icon + label + switch
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x33FBC02D)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_lightbulb_24),
                        contentDescription = null,
                        tint = Color(0xFFFBC02D)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(label, color = Color.White, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                    Text("${brightness.toInt()}%", color = Color.White.copy(alpha = 0.6f))
                }

                Spacer(modifier = Modifier.weight(1f))

                Switch(checked = lightStatus, onCheckedChange = onSwitchToggle)
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Row: Brightness slider + icon + text check
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.outline_brightness_5_24),
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.5f),
                    modifier = Modifier.size(20.dp)
                )

                Slider(
                    value = brightness,
                    onValueChange = onBrightnessChange,
                    valueRange = 0f..100f,
                    steps = 98,
                    modifier = Modifier.weight(1f),
                    enabled = lightStatus
                )

                Text("${brightness.toInt()}%", color = Color.White.copy(alpha = 0.7f))
            }
        }
    }
}
