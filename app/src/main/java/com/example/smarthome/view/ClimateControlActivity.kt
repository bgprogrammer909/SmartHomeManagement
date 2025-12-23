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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smarthome.R
import com.example.smarthome.viewmodel.ClimateViewModel

class ClimateControlActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: ClimateViewModel = viewModel()
            ClimateControlScreen(viewModel)
        }
    }
}

@Composable
fun ClimateControlScreen(viewModel: ClimateViewModel) {

    val state by viewModel.state
    val fan = state.fanSpeed.toFloat()

    val bg = Brush.verticalGradient(
        listOf(Color(0xFF05060A), Color(0xFF051225))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .padding(20.dp)
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(R.drawable.outline_arrow_back_24),
                contentDescription = null,
                tint = Color(0xFF9DB9D0),
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text("Back", color = Color(0xFF9DB9D0))
        }

        Spacer(Modifier.height(16.dp))

        Text(
            "Climate Control",
            color = Color.White,
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Text(
            "Adjust temperature and fan settings",
            color = Color(0xFF9AB3C8),
            fontSize = 14.sp
        )

        Spacer(Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(22.dp))
                .background(
                    Brush.linearGradient(
                        listOf(Color(0xFF113A5B), Color(0xFF0E2B4C))
                    )
                )
                .padding(18.dp)
        ) {

            Column {

                Row(modifier = Modifier.fillMaxWidth()) {

                    Column(modifier = Modifier.weight(1f)) {
                        Text("Fan Speed", color = Color(0xFFBFD9E6))
                        Text(
                            when (fan.toInt()) {
                                0 -> "LOW"
                                1 -> "MEDIUM"
                                2 -> "HIGH"
                                else -> "TURBO"
                            },
                            color = Color.White,
                            fontSize = 34.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_thermostat_24),
                            contentDescription = null,
                            tint = Color(0xFFFF9800),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            "${state.temperature}°C",
                            color = Color(0xFFFF9800),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                Slider(
                    value = fan,
                    onValueChange = { viewModel.setFanSpeed(it.toInt()) },
                    valueRange = 0f..3f,
                    steps = 2
                )

                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StyledChip("Low", fan == 0f) { viewModel.setFanSpeed(0) }
                    StyledChip("Medium", fan == 1f) { viewModel.setFanSpeed(1) }
                    StyledChip("High", fan == 2f) { viewModel.setFanSpeed(2) }
                    StyledChip("Turbo", fan == 3f) { viewModel.setFanSpeed(3) }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        FeatureCard(
            icon = R.drawable.baseline_air_24,
            title = "Power",
            subtitle = "System on",
            checked = state.powerOn,
            onCheckedChange = { viewModel.setPower(it) }
        )

        Spacer(Modifier.height(16.dp))

        FeatureCard(
            icon = R.drawable.baseline_thermostat_24,
            title = "Auto Mode",
            subtitle = "Adjust temp automatically",
            checked = state.autoMode,
            onCheckedChange = { viewModel.setAutoMode(it) }
        )

        Spacer(Modifier.height(16.dp))

        EnergyCard()
    }
}

@Composable
fun FeatureCard(
    icon: Int,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0E2433)),
        shape = RoundedCornerShape(18.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
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
                Text(title, color = Color.White)
                Text(subtitle, color = Color(0xFF9AB3C8), fontSize = 13.sp)
            }
            Switch(checked = checked, onCheckedChange = onCheckedChange)
        }
    }
}

@Composable
fun EnergyCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        listOf(Color(0xFF0F3D2E), Color(0xFF0A2A22))
                    )
                )
                .padding(16.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {

                    Spacer(Modifier.width(1.dp))
                    Text(
                        "Energy Efficiency",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.weight(1f))
                    Text("Optimal", color = Color(0xFF2EFFA3))
                }

                Spacer(Modifier.height(6.dp))

                Text(
                    "Current settings are energy efficient. You're saving 15% compared to average usage.",
                    color = Color(0xFFB7E8D8),
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
fun StyledChip(text: String, active: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (active) Color(0xFF1FB7FF) else Color(0xFF0F2A3D))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(text, color = Color.White)
    }
}
