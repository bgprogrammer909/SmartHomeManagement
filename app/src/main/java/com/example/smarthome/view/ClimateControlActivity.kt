package com.example.smarthome.view

import android.os.Bundle
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smarthome.R
import com.example.smarthome.viewmodel.ClimateViewModel
import com.example.smarthome.viewmodel.ClimateViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ClimateControlActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val userId = intent.getStringExtra("USER_ID") ?: return

        setContent {
            val viewModel: ClimateViewModel =
                viewModel(factory = ClimateViewModelFactory(userId))
            ClimateControlScreen(viewModel)
        }
    }
}

@Composable
fun ClimateControlScreen(viewModel: ClimateViewModel) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val activity = context as? ComponentActivity

    val bg = Brush.verticalGradient(
        listOf(Color(0xFF0B132B), Color(0xFF1C1C2E))
    )

    val scope = rememberCoroutineScope()
    // Auto Mode fan cycle every 4 seconds
    LaunchedEffect(state.autoMode, state.powerOn) {
        if (state.autoMode && state.powerOn) {
            while (true) {
                delay(4000)
                val nextSpeed = (state.fanSpeed + 1) % 4
                viewModel.setFanSpeed(nextSpeed)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .padding(20.dp)
            .statusBarsPadding()
    ) {
        // Back button
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(R.drawable.outline_arrow_back_24),
                contentDescription = null,
                tint = Color(0xFF9DB9D0),
                modifier = Modifier
                    .size(20.dp)
                    .clickable { activity?.finish() }
            )
            Spacer(Modifier.width(8.dp))
            Text(
                "Back",
                color = Color(0xFF9DB9D0),
                modifier = Modifier.clickable { activity?.finish() }
            )
        }

        Spacer(Modifier.height(16.dp))

        Text(
            "Fan Control",
            color = Color.White,
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            "Adjust fan settings",
            color = Color(0xFF9AB3C8),
            fontSize = 14.sp
        )

        Spacer(Modifier.height(20.dp))

        // Fan Speed Card
        val fan = state.fanSpeed.toFloat()
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(22.dp))
                .background(
                    Brush.linearGradient(
                        listOf(Color(0xFF113A5B), Color(0xFF0E2B4C))
                    )
                )
                // Dim/fade if power OFF or auto mode ON
                .alpha(if (!state.powerOn || state.autoMode) 0.35f else 1f)
                .padding(18.dp)
        ) {
            Column {
                Text(
                    "Fan Speed",
                    color = if (state.powerOn && !state.autoMode) Color(0xFFBFD9E6)
                    else Color(0xFFBFD9E6).copy(alpha = 0.4f)
                )

                Text(
                    when (fan.toInt()) {
                        0 -> "LOW"
                        1 -> "MEDIUM"
                        2 -> "HIGH"
                        else -> "TURBO"
                    },
                    color = Color.White.copy(alpha = if (state.powerOn && !state.autoMode) 1f else 0.4f),
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(12.dp))

                Slider(
                    value = fan,
                    enabled = state.powerOn && !state.autoMode, // Can't toggle in Auto Mode
                    onValueChange = { value ->
                        if (state.powerOn && !state.autoMode) {
                            viewModel.setFanSpeed(value.toInt())
                        }
                    },
                    valueRange = 0f..3f,
                    steps = 2
                )

                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StyledChip("Low", fan == 0f, state.powerOn && !state.autoMode) {
                        viewModel.setFanSpeed(0)
                    }
                    StyledChip("Medium", fan == 1f, state.powerOn && !state.autoMode) {
                        viewModel.setFanSpeed(1)
                    }
                    StyledChip("High", fan == 2f, state.powerOn && !state.autoMode) {
                        viewModel.setFanSpeed(2)
                    }
                    StyledChip("Turbo", fan == 3f, state.powerOn && !state.autoMode) {
                        viewModel.setFanSpeed(3)
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Power Toggle
        FeatureCard(
            icon = R.drawable.ic_refresh,
            title = "Power",
            subtitle = "System on",
            checked = state.powerOn,
            onCheckedChange = { checked ->
                viewModel.setPower(checked)
                if (!checked && state.autoMode) {
                    viewModel.setAutoMode(false) // Turn off Auto when Power OFF
                }
            }
        )

        Spacer(Modifier.height(16.dp))

        // Auto Mode Toggle
        FeatureCard(
            icon = R.drawable.baseline_thermostat_24,
            title = "Auto Mode",
            subtitle = "Adjust fan automatically",
            checked = state.autoMode,
            onCheckedChange = { checked ->
                if (state.powerOn) {
                    viewModel.setAutoMode(checked)
                }
            },
            disabled = !state.powerOn
        )

        Spacer(Modifier.height(16.dp))

        // Energy Card
        EnergyCard(
            powerOn = state.powerOn,
            fanSpeed = state.fanSpeed,
            autoMode = state.autoMode
        )
    }
}

// ------------------- FEATURE CARD -------------------
@Composable
fun FeatureCard(
    icon: Int,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    disabled: Boolean = false
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0E2433)),
        shape = RoundedCornerShape(18.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .alpha(if (disabled) 0.4f else 1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = Color(0xFF1FB7FF),
                modifier = Modifier.size(22.dp)
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    title,
                    color = Color.White.copy(alpha = if (disabled) 0.4f else 1f)
                )
                Text(
                    subtitle,
                    color = Color(0xFF9AB3C8).copy(alpha = if (disabled) 0.4f else 1f),
                    fontSize = 13.sp
                )
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                enabled = !disabled
            )
        }
    }
}

// ------------------- ENERGY CARD -------------------
@Composable
fun EnergyCard(
    powerOn: Boolean,
    fanSpeed: Int,
    autoMode: Boolean
) {
    val message = when {
        !powerOn -> "You're saving 100% energy."
        autoMode -> "You're saving 50% compared to average usage."
        fanSpeed == 0 -> "You're saving 75% compared to average usage."
        fanSpeed == 1 -> "You're saving 50% compared to average usage."
        fanSpeed == 2 -> "You're saving 25% compared to average usage."
        else -> "You're using full energy."
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
                        "Energy Efficiency",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        if (!powerOn) "Max Saving" else "Active",
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

// ------------------- STYLED CHIP -------------------
@Composable
fun StyledChip(
    text: String,
    active: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (active) Color(0xFF1FB7FF) else Color(0xFF0F2A3D))
            .clickable(enabled = enabled) { onClick() }
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(
            text,
            color = Color.White.copy(alpha = if (enabled) 1f else 0.4f)
        )
    }
}
