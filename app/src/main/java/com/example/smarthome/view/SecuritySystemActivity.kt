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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smarthome.repo.SecurityRepoImpl
import com.example.smarthome.viewmodel.SecurityViewModel
import com.example.smarthome.view.ui.theme.SmartHomeTheme

class SecuritySystemActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartHomeTheme {
                val factory = SecurityViewModelFactory(SecurityRepoImpl())
                val viewModel: SecurityViewModel = viewModel(factory = factory)
                SecurityScreen(viewModel) { finish() }
            }
        }
    }
}

@Composable
fun SecurityScreen(
    viewModel: SecurityViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.state

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF0B132B), Color(0xFF1C1C2E))
                )
            ),
        contentPadding = PaddingValues(20.dp)
    ) {

        item {

            Text(
                text = "← Back",
                color = Color(0xFF9DB9D0),
                modifier = Modifier.clickable { onBack() }
            )

            Spacer(Modifier.height(20.dp))

            Text(
                "Security System",
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Manage your home security",
                color = Color(0xFF9AB3C8),
                fontSize = 14.sp
            )

            Spacer(Modifier.height(28.dp))

            Text(
                "Security Mode",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {

                SecurityModeCard(
                    title = "ON",
                    icon = "🛡️",
                    selected = state.activeMode,
                    modifier = Modifier.weight(1f)
                ) {
                    viewModel.setSecurityMode(true)
                }

                SecurityModeCard(
                    title = "OFF",
                    icon = "❌",
                    selected = !state.activeMode,
                    modifier = Modifier.weight(1f)
                ) {
                    viewModel.setSecurityMode(false)
                }
            }

            Spacer(Modifier.height(28.dp))

            Text(
                "Sensors & Alerts",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(16.dp))

            SensorItem(
                icon = "📹",
                title = "Motion Detection",
                subtitle = "Camera-based monitoring",
                enabled = state.motionDetection
            ) {
                viewModel.setMotionDetection(it)
                if (it) viewModel.setSecurityMode(true)
            }

            SensorItem(
                icon = "🔒",
                title = "Door Sensors",
                subtitle = "All entry points",
                enabled = state.doorSensors
            ) {
                viewModel.setDoorSensors(it)
                if (it) viewModel.setSecurityMode(true)
            }

            SensorItem(
                icon = "🔔",
                title = "Push Notifications",
                subtitle = "Instant alerts",
                enabled = state.pushNotifications,
                onToggle = viewModel::setPushNotifications
            )

            Spacer(Modifier.height(28.dp))

            Text(
                "Recent Activity",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF0F1E33))
                    .padding(12.dp)
            ) {
                if (state.recentActivities.isEmpty()) {
                    Text("No recent activity", color = Color.Gray)
                } else {
                    LazyColumn {
                        items(state.recentActivities) {
                            // intentionally empty for now
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SecurityModeCard(
    title: String,
    icon: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val backgroundModifier =
        if (selected) {
            Modifier.background(
                brush = Brush.linearGradient(
                    listOf(Color(0xFFFF9800), Color(0xFFFFB74D))
                ),
                shape = RoundedCornerShape(20.dp)
            )
        } else {
            Modifier.background(
                color = Color(0xFF101F33),
                shape = RoundedCornerShape(20.dp)
            )
        }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .then(backgroundModifier)
            .clickable { onClick() }
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(icon, fontSize = 28.sp)
            Spacer(Modifier.height(12.dp))
            Text(title, color = Color.White, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun SensorItem(
    icon: String,
    title: String,
    subtitle: String,
    enabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF0F1E33))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(icon, fontSize = 28.sp)
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(title, color = Color.White)
                    Text(subtitle, color = Color(0xFF9AB3C8), fontSize = 13.sp)
                }
            }

            Switch(checked = enabled, onCheckedChange = onToggle)
        }
    }
    Spacer(Modifier.height(12.dp))
}
