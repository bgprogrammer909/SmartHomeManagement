package com.example.smarthome.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.smarthome.R
import com.example.smarthome.view.ui.theme.SmartHomeTheme

class SecurityActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartHomeTheme {
                SecurityScreen { finish() }
            }
        }
    }
}

enum class SecurityMode(val title: String, val description: String, val icon: Int) {
    HOME("Home", "Minimal security, you are home", R.drawable.baseline_home_24),
    AWAY("Away", "Full security, you are away", R.drawable.baseline_shield_24),
    NIGHT("Night", "Sleep mode with perimeter guard", R.drawable.baseline_nightlight_24)
}

private fun modeGradient(mode: SecurityMode): List<Color> =
    when (mode) {
        SecurityMode.HOME -> listOf(Color(0xFF2E7D32), Color(0xFF1B5E20))
        SecurityMode.AWAY -> listOf(Color(0xFFFFB74D), Color(0xFFFF9800))
        SecurityMode.NIGHT -> listOf(Color(0xFF5C6BC0), Color(0xFF283593))
    }

private fun modeIconColor(mode: SecurityMode): Color =
    when (mode) {
        SecurityMode.HOME -> Color(0xFF4CAF50)
        SecurityMode.AWAY -> Color(0xFFFF9800)
        SecurityMode.NIGHT -> Color(0xFF7986CB)
    }

@Composable
fun SecurityScreen(onBack: () -> Unit) {
    var activeMode by remember { mutableStateOf(SecurityMode.HOME) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF0A1A2F), Color(0xFF05101F))
                )
            ),
        contentPadding = PaddingValues(20.dp)
    ) {

        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onBack() }
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_arrow_back_24),
                    contentDescription = null,
                    tint = Color(0xFF9DB9D0),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Back", color = Color(0xFF9DB9D0))
            }

            Spacer(Modifier.height(20.dp))

            Text("Security System", color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.Bold)
            Text("Manage your home security", color = Color(0xFF9AB3C8), fontSize = 14.sp)

            Spacer(Modifier.height(20.dp))

            ActiveModeCard(activeMode)

            Spacer(Modifier.height(28.dp))

            Text("Security Modes", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)

            Spacer(Modifier.height(16.dp))

            Row {
                SecurityModeCard(
                    mode = SecurityMode.HOME,
                    selected = activeMode == SecurityMode.HOME,
                    modifier = Modifier.weight(1f)
                ) { activeMode = it }

                Spacer(Modifier.width(16.dp))

                SecurityModeCard(
                    mode = SecurityMode.AWAY,
                    selected = activeMode == SecurityMode.AWAY,
                    modifier = Modifier.weight(1f)
                ) { activeMode = it }
            }

            Spacer(Modifier.height(16.dp))

            Row {
                SecurityModeCard(
                    mode = SecurityMode.NIGHT,
                    selected = activeMode == SecurityMode.NIGHT,
                    modifier = Modifier.weight(1f)
                ) { activeMode = it }

                Spacer(Modifier.width(16.dp))
            }

            Spacer(Modifier.height(28.dp))

            Text("Sensors & Alerts", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)

            Spacer(Modifier.height(16.dp))
        }

        items(
            listOf(
                Triple(R.drawable.baseline_videocam_24, "Motion Detection", "Camera-based monitoring"),
                Triple(R.drawable.baseline_security_24, "Door Sensors", "All entry points"),
                Triple(R.drawable.baseline_notifications_24, "Push Notifications", "Instant alerts")
            )
        ) { sensor ->
            SensorItem(sensor.first, sensor.second, sensor.third)
        }

        item {
            Spacer(Modifier.height(28.dp))

            Text("Recent Activity", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)

            Spacer(Modifier.height(12.dp))

            RecentActivityCard()
        }
    }
}

@Composable
fun ActiveModeCard(mode: SecurityMode) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Brush.linearGradient(modeGradient(mode)))
            .padding(20.dp)
    ) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Column {
                Text("Active Mode", color = Color.White.copy(alpha = 0.7f))
                Text(mode.title, color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(10.dp))
                Text(mode.description, color = Color.White.copy(alpha = 0.7f))
            }

            Icon(
                painter = painterResource(mode.icon),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(36.dp)
            )
        }
    }
}

@Composable
fun SecurityModeCard(
    mode: SecurityMode,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: (SecurityMode) -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .then(
                if (selected)
                    Modifier.background(Brush.linearGradient(modeGradient(mode)))
                else
                    Modifier.background(Color(0xFF101F33))
            )
            .clickable { onClick(mode) }
            .padding(16.dp)
    ) {
        Column {
            Icon(
                painter = painterResource(mode.icon),
                contentDescription = null,
                tint = if (selected) Color.White else modeIconColor(mode),
                modifier = Modifier.size(28.dp)
            )

            Spacer(Modifier.height(12.dp))

            Text(mode.title, color = Color.White, fontWeight = FontWeight.SemiBold)
            Text(mode.description, color = Color(0xFF9AB3C8), fontSize = 13.sp)
        }
    }
}

@Composable
fun SensorItem(icon: Int, title: String, subtitle: String) {
    var enabled by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF0F1E33))
            .padding(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = Color(0xFFFF9800),
                    modifier = Modifier.size(28.dp)
                )

                Spacer(Modifier.width(12.dp))

                Column {
                    Text(title, color = Color.White)
                    Text(subtitle, color = Color(0xFF9AB3C8), fontSize = 13.sp)
                }
            }

            Switch(checked = enabled, onCheckedChange = { enabled = it })
        }
    }

    Spacer(Modifier.height(12.dp))
}

@Composable
fun RecentActivityCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF0F1E33))
            .padding(16.dp)
    ) {
        Column {
            repeat(3) {
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Text("Motion detected – Front door", color = Color(0xFF9AB3C8), fontSize = 13.sp)
                    Text("2 min ago", color = Color(0xFF6B7C93), fontSize = 12.sp)
                }
                Spacer(Modifier.height(6.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SecurityScreenPreview() {
    SmartHomeTheme {
        SecurityScreen(onBack = {})
    }
}
