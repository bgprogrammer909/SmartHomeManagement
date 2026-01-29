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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smarthome.R
import com.example.smarthome.repo.PLightRepoImpl
import com.example.smarthome.viewmodel.PLightsViewModel
import com.example.smarthome.viewmodel.PLightsViewModelFactory

class PLightActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val userId = intent.getStringExtra("USER_ID")
        if (userId.isNullOrEmpty()) {
            finish()
            return
        }

        setContent {
            val ctx = LocalContext.current

            val vm: PLightsViewModel = viewModel(
                factory = PLightsViewModelFactory(
                    repo = PLightRepoImpl(),
                    userId = userId
                )
            )

            val state by vm.lights.collectAsState()

            val bgGradient = Brush.verticalGradient(
                colors = listOf(Color(0xFF0D1B2A), Color(0xFF0A1320))
            )

            Scaffold(containerColor = Color.Transparent) { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(bgGradient)
                        .padding(padding)
                        .padding(16.dp)
                ) {
                    // Back button and top bar
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { finish() }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.outline_arrow_back_24),
                            contentDescription = "Back",
                            tint = Color(0xFF9DB9D0)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Back", color = Color(0xFF9DB9D0))
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Header text
                    Text(
                        "My Lights",
                        color = Color.White,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        "Control your smart lights",
                        color = Color(0xFF9AB3C8),
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Summary card with total active lights and master switch
                    TopStatusCard(
                        activeCount = state.lightsOnCount,
                        masterSwitch = state.light1On && state.light2On,
                        onToggleAll = {
                            if (state.light1On && state.light2On) {
                                vm.turnOffAll()
                                Toast.makeText(ctx, "All lights OFF", Toast.LENGTH_SHORT).show()
                            } else {
                                vm.turnOnAll()
                                Toast.makeText(ctx, "All lights ON", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Individual light controls
                    LightControlCard(
                        label = "Light 1",
                        lightStatus = state.light1On,
                        brightness = state.light1Brightness,
                        onSwitchToggle = { vm.toggleLight(1, it) },
                        onBrightnessChange = { vm.changeBrightness(1, it) }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    LightControlCard(
                        label = "Light 2",
                        lightStatus = state.light2On,
                        brightness = state.light2Brightness,
                        onSwitchToggle = { vm.toggleLight(2, it) },
                        onBrightnessChange = { vm.changeBrightness(2, it) }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Energy usage and efficiency
                    LightEnergyCard(
                        light1On = state.light1On,
                        light1Brightness = state.light1Brightness,
                        light2On = state.light2On,
                        light2Brightness = state.light2Brightness
                    )
                }
            }
        }
    }
}

// Card showing total lights on and master toggle
@Composable
fun TopStatusCard(activeCount: Int, masterSwitch: Boolean, onToggleAll: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(Color(0xFF1D233A)),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text("Total Lights", color = Color.White.copy(alpha = 0.6f), fontSize = 14.sp)
                    Text(
                        "$activeCount On",
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
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

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("masterlightButton")
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFFFFC107), Color(0xFFFFA000))
                        )
                    )
                    .clickable { onToggleAll() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    if (masterSwitch) "Turn All Off" else "Turn All On",
                    color = Color(0xFF1A1A1A),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// Card to control individual light and brightness
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (lightStatus) Color(0x55FBC02D) else Color(0x22FBC02D)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_lightbulb_24),
                        contentDescription = null,
                        tint = if (lightStatus) Color(0xFFFBC02D) else Color(0xFF7D7D7D)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        label,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        if (lightStatus) "${brightness.toInt()}%" else "Off",
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Switch(
                    checked = lightStatus,
                    onCheckedChange = onSwitchToggle,
                    modifier = Modifier.testTag("$label - switch")
                )
            }

            if (lightStatus) {
                Spacer(modifier = Modifier.height(14.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_lightbulb_24),
                        contentDescription = null,
                        tint = Color(0xFF9AB3C8),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    Slider(
                        value = brightness,
                        onValueChange = onBrightnessChange,
                        valueRange = 0f..100f,
                        steps = 98,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("$label-brightness")
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        "${brightness.toInt()}%",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp,
                        modifier = Modifier.width(40.dp)
                    )
                }
            }
        }
    }
}

// Card to display energy usage and efficiency
@Composable
fun LightEnergyCard(
    light1On: Boolean,
    light1Brightness: Float,
    light2On: Boolean,
    light2Brightness: Float
) {
    val light1Usage = if (light1On) light1Brightness / 100f else 0f
    val light2Usage = if (light2On) light2Brightness / 100f else 0f
    val totalUsage = (light1Usage + light2Usage) / 2f
    val savingPercent = ((1f - totalUsage) * 100f).coerceIn(0f, 100f)

    val message = when {
        totalUsage == 0f -> "You're saving 100% energy."
        totalUsage < 0.5f -> "You're saving ${savingPercent.toInt()}% energy."
        totalUsage >= 1f -> "You're consuming 100% energy."
        else -> "You're consuming ${(totalUsage * 100).toInt()}% energy."
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp)
    ) {
        Row(
            modifier = Modifier
                .background(
                    Brush.linearGradient(listOf(Color(0xFF0F3D2E), Color(0xFF0A2A22)))
                )
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Light Energy",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        when {
                            totalUsage == 0f -> "Max Saving"
                            totalUsage >= 1f -> "Full Usage"
                            else -> "Active"
                        },
                        color = Color(0xFF2EFFA3),
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(Modifier.height(6.dp))
                Text(
                    message,
                    color = Color(0xFFB7E8D8),
                    fontSize = 13.sp
                )
            }
        }
    }
}
