package com.example.smarthome.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.foundation.layout.statusBarsPadding
import com.example.smarthome.model.SecurityMode
import com.example.smarthome.viewmodel.SecurityViewModel
import com.example.smarthome.viewmodel.SecurityViewModelFactory
import com.example.smarthome.view.ui.theme.SmartHomeTheme

class SecurityActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userId = intent.getStringExtra("USER_ID") ?: return

        setContent {
            SmartHomeTheme {
                val viewModel: SecurityViewModel = viewModel(
                    factory = SecurityViewModelFactory(userId)
                )
                SecurityScreen(viewModel) { finish() }
            }
        }
    }
}

// Main security screen
@Composable
fun SecurityScreen(viewModel: SecurityViewModel, onBack: () -> Unit) {
    val state by viewModel.state
    val showMotionAlert by viewModel.showMotionAlert

    val activeMode = when (state.activeMode) {
        "AWAY" -> SecurityMode.AWAY
        "NIGHT" -> SecurityMode.NIGHT
        else -> SecurityMode.HOME
    }

    // Show motion alert dialog
    if (showMotionAlert) {
        AlertDialog(
            onDismissRequest = { },
            confirmButton = {
                TextButton(onClick = { viewModel.dismissMotionAlert() }) {
                    Text("OK", color = Color(0xFF1FB7FF))
                }
            },
            title = {
                Text(
                    "Motion Detected",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text("Motion has been detected in your home. Please check your security cameras.")
            },
            containerColor = Color(0xFF1C1C2E),
            titleContentColor = Color.White,
            textContentColor = Color(0xFF9AB3C8)
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF0B132B), Color(0xFF1C1C2E))
                )
            )
            .statusBarsPadding(),
        contentPadding = PaddingValues(20.dp)
    ) {
        item {
            // Back button and header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onBack() }
            ) {
                Text("← Back", color = Color(0xFF9DB9D0))
            }

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

            Spacer(Modifier.height(20.dp))
            ActiveModeCard(activeMode)

            Spacer(Modifier.height(28.dp))
            Text(
                "Security Modes",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(16.dp))
            // Top row of modes
            Row {
                SecurityModeCard(
                    mode = SecurityMode.HOME,
                    selected = activeMode == SecurityMode.HOME,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.setActiveMode("HOME") }
                )
                Spacer(Modifier.width(16.dp))
                SecurityModeCard(
                    mode = SecurityMode.AWAY,
                    selected = activeMode == SecurityMode.AWAY,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.setActiveMode("AWAY") }
                )
            }

            Spacer(Modifier.height(16.dp))
            // Bottom row of modes
            Row {
                SecurityModeCard(
                    mode = SecurityMode.NIGHT,
                    selected = activeMode == SecurityMode.NIGHT,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.setActiveMode("NIGHT") }
                )
                Spacer(Modifier.width(16.dp))
                Spacer(Modifier.weight(1f))
            }
        }
    }
}

// Card showing the active mode
@Composable
fun ActiveModeCard(mode: SecurityMode) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Brush.linearGradient(modeGradient(mode)))
            .padding(20.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Text("Active Mode", color = Color.White.copy(alpha = 0.7f))
                Text(
                    mode.title,
                    color = Color.White,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    mode.description,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
            Text(
                when (mode) {
                    SecurityMode.HOME -> "🏠"
                    SecurityMode.AWAY -> "🛡️"
                    SecurityMode.NIGHT -> "🌙"
                },
                fontSize = 36.sp
            )
        }
    }
}

// Individual security mode card
@Composable
fun SecurityModeCard(
    mode: SecurityMode,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .background(
                brush = if (selected)
                    Brush.linearGradient(modeGradient(mode))
                else
                    Brush.linearGradient(
                        listOf(Color(0xFF101F33), Color(0xFF101F33))
                    )
            )
            .padding(16.dp)
    ) {
        Column {
            Text(
                when (mode) {
                    SecurityMode.HOME -> "🏠"
                    SecurityMode.AWAY -> "🛡️"
                    SecurityMode.NIGHT -> "🌙"
                },
                fontSize = 28.sp
            )
            Spacer(Modifier.height(12.dp))
            Text(mode.title, color = Color.White, fontWeight = FontWeight.SemiBold)
            Text(
                mode.description,
                color = Color(0xFF9AB3C8),
                fontSize = 13.sp
            )
        }
    }
}

// Returns gradient for given security mode
fun modeGradient(mode: SecurityMode): List<Color> =
    when (mode) {
        SecurityMode.HOME -> listOf(Color(0xFF2E7D32), Color(0xFF1B5E20))
        SecurityMode.AWAY -> listOf(Color(0xFFFFB74D), Color(0xFFFF9800))
        SecurityMode.NIGHT -> listOf(Color(0xFF5C6BC0), Color(0xFF283593))
    }
