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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smarthome.R   // <-- FIXED R IMPORT

class ClimateControlActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ClimateControlScreen()
        }
    }
}

@Composable
fun ClimateControlScreen() {

    var fan by remember { mutableFloatStateOf(0f) }
    var powerOn by remember { mutableStateOf(true) }
    var autoMode by remember { mutableStateOf(false) }

    val bg = Brush.verticalGradient(listOf(Color(0xFF05060A), Color(0xFF051225)))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .padding(20.dp)
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.outline_arrow_back_24),
                contentDescription = null,
                tint = Color(0xFF9DB9D0),
                modifier = Modifier
                    .size(20.dp)
                    .clickable { }
            )
            Spacer(Modifier.width(8.dp))
            Text("Back", color = Color(0xFF9DB9D0), fontSize = 16.sp)
        }

        Spacer(Modifier.height(16.dp))

        Text("Climate Control", color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.ExtraBold)
        Text("Adjust temperature and fan settings", color = Color(0xFF9AB3C8), fontSize = 14.sp)

        Spacer(Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(
                    Brush.linearGradient(
                        listOf(Color(0xFF113A5B), Color(0xFF0E2B4C), Color(0xFF112B4C))
                    )
                )
                .padding(18.dp)
        ) {

            Column(modifier = Modifier.fillMaxSize()) {

                Row(modifier = Modifier.fillMaxWidth()) {

                    Column(modifier = Modifier.weight(1f)) {
                        Text("Fan Speed", color = Color(0xFFBFD9E6), fontSize = 16.sp)
                        Text(
                            text = when (fan.toInt()) {
                                0 -> "Low"
                                1 -> "Medium"
                                2 -> "High"
                                else -> "Turbo"
                            },
                            color = Color.White,
                            fontSize = 34.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text("Current", color = Color(0xFF9AB3C8), fontSize = 12.sp)
                        Spacer(Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_thermostat_24),
                                contentDescription = null,
                                tint = Color(0xFFFF9800),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text("28°C", color = Color(0xFFFF9800), fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))

                Slider(
                    value = fan,
                    onValueChange = { fan = it },
                    valueRange = 0f..3f,
                    steps = 2,
                    modifier = Modifier.fillMaxWidth(),
                    colors = SliderDefaults.colors(
                        thumbColor = Color.White,
                        activeTrackColor = Color.White.copy(alpha = 0.95f),
                        inactiveTrackColor = Color.White.copy(alpha = 0.12f)
                    )
                )

                Spacer(Modifier.height(12.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    StyledChip("Low", fan == 0f) { fan = 0f }
                    StyledChip("Medium", fan == 1f) { fan = 1f }
                    StyledChip("High", fan == 2f) { fan = 2f }
                    StyledChip("Turbo", fan == 3f) { fan = 3f }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(84.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0E2433))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_air_24),
                    contentDescription = null,
                    tint = Color(0xFF00C1FF),
                    modifier = Modifier.size(28.dp)
                )
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("Power", color = Color.White, fontSize = 16.sp)
                    Text(
                        if (powerOn) "System on" else "System off",
                        color = Color(0xFF9AB3C8),
                        fontSize = 12.sp
                    )
                }
                Spacer(Modifier.weight(1f))
                Switch(checked = powerOn, onCheckedChange = { powerOn = it })
            }
        }

        Spacer(Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(84.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0E2433))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_thermostat_24),
                    contentDescription = null,
                    tint = Color(0xFF6AA6FF),
                    modifier = Modifier.size(28.dp)
                )
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("Auto Mode", color = Color.White, fontSize = 16.sp)
                    Text(
                        "Adjust temp automatically",
                        color = Color(0xFF9AB3C8),
                        fontSize = 12.sp
                    )
                }
                Spacer(Modifier.weight(1f))
                Switch(checked = autoMode, onCheckedChange = { autoMode = it })
            }
        }

        Spacer(Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF0B3B25), Color(0xFF063026))
                        )
                    )
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(modifier = Modifier.weight(1f)) {
                        Text("Energy Efficiency", color = Color(0xFFB8E9D0), fontSize = 16.sp, fontWeight = FontWeight.Medium)
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "Current settings are energy efficient. You're saving 15% compared to average usage.",
                            color = Color(0xFFBFDCD0),
                            fontSize = 13.sp
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF0EA96F).copy(alpha = 0.12f))
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Text("Optimal", color = Color(0xFF3CE387), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(6.dp))
    }
}

@Composable
fun StyledChip(text: String, active: Boolean, onClick: () -> Unit) {
    val bg = if (active) {
        Brush.linearGradient(listOf(Color(0xFF1FB7FF), Color(0xFF00C7D9)))
    } else {
        Brush.linearGradient(listOf(Color(0xFF0F2A3D), Color(0xFF0E2636)))
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bg)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(text, color = if (active) Color.White else Color(0xFF8AA2B5))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ClimatePreview() {
    ClimateControlScreen()
}
