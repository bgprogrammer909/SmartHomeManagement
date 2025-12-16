package com.example.smarthome.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import com.example.smarthome.R
import com.example.smarthome.view.ui.theme.SmartHomeTheme
import androidx.compose.ui.tooling.preview.Preview


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
    NIGHT("Night", "Sleep mode with perimeter guard", R.drawable.baseline_nightlight_24),
    CUSTOM("Custom", "Your personalized settings", R.drawable.baseline_warning_24)
}

@Composable
fun SecurityScreen(onBack: () -> Unit) {
    var activeMode by remember { mutableStateOf(SecurityMode.HOME) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF0A1A2F), Color(0xFF05101F))
                )
            )
            .padding(20.dp)
    ) {

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

        Column {
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

                SecurityModeCard(
                    mode = SecurityMode.CUSTOM,
                    selected = activeMode == SecurityMode.CUSTOM,
                    modifier = Modifier.weight(1f)
                ) { activeMode = it }
            }
        }
    }
}

@Composable
fun ActiveModeCard(mode: SecurityMode) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFF143D34), Color(0xFF0F2F2A))
                )
            )
            .padding(20.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Text("Active Mode", color = Color(0xFF9AB3C8))
                Text(mode.title, color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(10.dp))
                Text(mode.description, color = Color(0xFF9AB3C8))
                Spacer(Modifier.height(6.dp))
                Text("• Perimeter monitoring", color = Color(0xFF9AB3C8))
                Text("• Door sensors", color = Color(0xFF9AB3C8))
                Text("• Fire alarm", color = Color(0xFF9AB3C8))
            }

            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0x3328E07B)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(mode.icon),
                    contentDescription = null,
                    tint = Color(0xFF28E07B),
                    modifier = Modifier.size(32.dp)
                )
            }
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
                    Modifier.background(
                        Brush.linearGradient(
                            listOf(Color(0xFF143D34), Color(0xFF0F2F2A))
                        )
                    )
                else
                    Modifier.background(Color(0xFF101F33))
            )
            .clickable { onClick(mode) }
            .padding(16.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        if (selected) Color(0x3328E07B) else Color(0xFF1A2B40)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(mode.icon),
                    contentDescription = null,
                    tint = if (selected) Color(0xFF28E07B) else Color(0xFF6B7C93)
                )
            }

            Spacer(Modifier.height(14.dp))

            Text(mode.title, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            Text(mode.description, color = Color(0xFF9AB3C8), fontSize = 13.sp)
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
